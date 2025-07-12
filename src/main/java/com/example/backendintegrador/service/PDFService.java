package com.example.backendintegrador.service;

import com.example.backendintegrador.exception.PDFGenerationException;
import com.example.backendintegrador.persistence.entity.Asiento;
import com.example.backendintegrador.persistence.entity.Pasaje;
import com.example.backendintegrador.persistence.entity.Usuario;
import com.example.backendintegrador.persistence.entity.UsuarioAsiento;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PDFService {

    @Value("${app.pdf.temp-directory}") // Propiedad para el directorio temporal donde se guardarán los PDFs
    private String tempPdfDirectory;

    /**
     * Generates the travel ticket in PDF format and saves it to a temporary file.
     * @param pasaje The Pasaje object with purchase details.
     * @param usuarioAsientos The list of UsuarioAsiento relationships to get passenger and seat details.
     * @return The absolute path of the generated PDF file.
     * @throws PDFGenerationException if an error occurs during PDF generation.
     */
    public String generateTicket(Pasaje pasaje, List<UsuarioAsiento> usuarioAsientos) {
        PDDocument document = null; // Initialize to null for finally block
        PDPageContentStream contentStream = null; // Initialize to null for finally block
        String filePath = null; // To store the path of the temporary file

        try {
            // Ensure the temporary directory exists
            File dir = new File(tempPdfDirectory);
            if (!dir.exists()) {
                dir.mkdirs(); // Creates the directory and any necessary parent directories
            }

            // Generate a unique file name using trip ID and timestamp
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String pasajeId = (pasaje != null && pasaje.getIdPasaje() != null) ? pasaje.getIdPasaje().toString() : "temp";
            String fileName = "boleto_" + pasajeId + "_" + timestamp + ".pdf";
            filePath = tempPdfDirectory + File.separator + fileName;

            document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float leading = 18; // Line spacing

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("BOLETO DE VIAJE");
            contentStream.endText();
            yPosition -= leading * 2;

            // Font for normal text
            PDType1Font infoFont = PDType1Font.HELVETICA;
            float fontSize = 12;

            // General ticket data
            contentStream.beginText();
            contentStream.setFont(infoFont, fontSize);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Número de boleto: " + (pasaje != null && pasaje.getIdPasaje() != null ? pasaje.getIdPasaje() : "N/A"));
            yPosition -= leading;
            contentStream.newLineAtOffset(0, -leading);
            contentStream.showText("Fecha de emisión: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            yPosition -= leading;
            contentStream.newLineAtOffset(0, -leading);
            contentStream.showText("Ruta: " + (pasaje != null && pasaje.getRuta() != null && pasaje.getRuta().getNombre() != null ? pasaje.getRuta().getNombre() : "N/A"));
            yPosition -= leading;
            contentStream.newLineAtOffset(0, -leading);
            contentStream.showText("Fecha de viaje: " + (pasaje != null && pasaje.getViaje() != null && pasaje.getViaje().getFechaSalida() != null ? pasaje.getViaje().getFechaSalida() : "N/A"));
            yPosition -= leading;
            contentStream.newLineAtOffset(0, -leading);
            contentStream.showText("Hora de salida: " + (pasaje != null && pasaje.getViaje() != null && pasaje.getViaje().getHoraSalida() != null ? pasaje.getViaje().getHoraSalida() : "N/A"));
            yPosition -= leading;
            contentStream.newLineAtOffset(0, -leading);
            contentStream.showText("Total pagado: S/ " + (pasaje != null && pasaje.getTotalPagar() != null ? String.format("%.2f", pasaje.getTotalPagar()) : "0.00"));
            contentStream.endText();
            yPosition -= leading * 2;

            // Passengers
            contentStream.beginText();
            contentStream.setFont(infoFont, fontSize);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Pasajeros:");
            contentStream.endText();
            yPosition -= leading;

            Set<Usuario> pasajeros = new HashSet<>();
            if (usuarioAsientos != null) {
                for (UsuarioAsiento ua : usuarioAsientos) {
                    if (ua != null && ua.getUsuario() != null) {
                        pasajeros.add(ua.getUsuario());
                    }
                }
            }

            for (Usuario u : pasajeros) {
                contentStream.beginText();
                contentStream.setFont(infoFont, fontSize);
                contentStream.newLineAtOffset(margin + 10, yPosition);
                contentStream.showText("- " + (u.getNombres() != null ? u.getNombres() : "") + " " + (u.getApellidos() != null ? u.getApellidos() : "") + " (DNI: " + (u.getDni() != null ? u.getDni() : "N/A") + ")");
                contentStream.endText();
                yPosition -= leading;
            }
            yPosition -= leading;

            // Seat table (manual drawing for simplicity)
            float tableY = yPosition;
            float colWidth = tableWidth / 3; // 3 columns: Seat, Floor, Price
            float rowHeight = 20;

            // Table headers
            contentStream.setLineWidth(0.5f);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, tableY);
            contentStream.showText("Asiento");
            contentStream.newLineAtOffset(colWidth, 0);
            contentStream.showText("Piso");
            contentStream.newLineAtOffset(colWidth, 0);
            contentStream.showText("Precio");
            contentStream.endText();
            tableY -= rowHeight;
            contentStream.moveTo(margin, tableY + rowHeight / 2); // Line below headers
            contentStream.lineTo(margin + tableWidth, tableY + rowHeight / 2);
            contentStream.stroke();


            contentStream.setFont(infoFont, fontSize);
            if (usuarioAsientos != null) {
                for (UsuarioAsiento ua : usuarioAsientos) {
                    if (ua != null && ua.getAsiento() != null) {
                        Asiento a = ua.getAsiento();
                        String asientoDesc = a.getDescripcion() != null ? a.getDescripcion() : "N/A";
                        String pisoStr = a.getPiso() != null ? a.getPiso().toString() : "N/A";
                        String precioStr = (pasaje != null && pasaje.getViaje() != null && pasaje.getViaje().getCosto() != null) ? "S/ " + String.format("%.2f", pasaje.getViaje().getCosto()) : "S/ 0.00";

                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, tableY);
                        contentStream.showText(asientoDesc);
                        contentStream.newLineAtOffset(colWidth, 0);
                        contentStream.showText(pisoStr);
                        contentStream.newLineAtOffset(colWidth, 0);
                        contentStream.showText(precioStr);
                        contentStream.endText();
                        tableY -= rowHeight;
                    }
                }
            }
            yPosition = tableY; // Update yPosition after table

            // Ticket code
            yPosition -= leading * 2;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Código de boleto: " + (pasaje != null && pasaje.getIdPasaje() != null ? pasaje.getIdPasaje() : "N/A"));
            contentStream.endText();

            contentStream.close(); // Close the content stream before saving the document

            document.save(filePath); // ⭐ Save the document to the specified file path ⭐
            System.out.println("PDF generated and saved to: " + filePath); // Log for debugging

        } catch (IOException e) {
            System.err.println("IO Error generating PDF ticket: " + e.getMessage());
            e.printStackTrace();
            throw new PDFGenerationException("IO Error generating PDF ticket: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error generating PDF ticket: " + e.getMessage());
            e.printStackTrace();
            throw new PDFGenerationException("Unexpected error generating PDF ticket: " + e.getMessage());
        } finally {
            try {
                if (contentStream != null) {
                    contentStream.close(); // Ensure contentStream is closed
                }
                if (document != null) {
                    document.close(); // Ensure document is closed
                }
            } catch (IOException e) {
                System.err.println("Error closing PDF resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return filePath; // ⭐ Return the file path ⭐
    }
}
