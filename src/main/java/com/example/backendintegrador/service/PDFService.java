package com.example.backendintegrador.service;

import com.example.backendintegrador.exception.PDFGenerationException;
import com.example.backendintegrador.persistence.entity.Asiento;
import com.example.backendintegrador.persistence.entity.Pasaje;
import com.example.backendintegrador.persistence.entity.Usuario;
import com.example.backendintegrador.persistence.entity.UsuarioAsiento;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class PDFService {

    @Value("${app.pdf.directory}")
    private String pdfDirectory;

    public String generateTicket(Pasaje pasaje, List<UsuarioAsiento> usuarioAsientos) {
        try {
            // Crear directorio si no existe
            File dir = new File(pdfDirectory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Nombre del archivo
            String fileName = "ticket_" + pasaje.getIdPasaje() + "_" +
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
            String filePath = pdfDirectory + File.separator + fileName;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("BOLETO DE VIAJE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Fuente para texto normal
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Datos generales del pasaje
            document.add(new Paragraph("Número de boleto: " + pasaje.getIdPasaje(), infoFont));
            document.add(new Paragraph("Fecha de emisión: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), infoFont));
            document.add(new Paragraph("Ruta: " + pasaje.getRuta().getNombre(), infoFont));
            document.add(new Paragraph("Fecha de viaje: " + pasaje.getViaje().getFechaSalida(), infoFont));
            document.add(new Paragraph("Hora de salida: " + pasaje.getViaje().getHoraSalida(), infoFont));
            document.add(new Paragraph("Total pagado: S/ " + String.format("%.2f", pasaje.getTotalPagar()), infoFont));
            document.add(new Paragraph(" ", infoFont));

            // Pasajeros
            Paragraph pasajeroTitle = new Paragraph("Pasajeros:", infoFont);
            pasajeroTitle.setSpacingBefore(10);
            pasajeroTitle.setSpacingAfter(5);
            document.add(pasajeroTitle);

            Set<Usuario> pasajeros = new HashSet<>();
            for (UsuarioAsiento ua : usuarioAsientos) {
                pasajeros.add(ua.getUsuario());
            }

            for (Usuario u : pasajeros) {
                document.add(new Paragraph("- " + u.getNombres() + " " + u.getApellidos() + " (DNI: " + u.getDni() + ")", infoFont));
            }

            document.add(new Paragraph(" ", infoFont));

            // Tabla de asientos
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            // Cabeceras
            Stream.of("Asiento", "Piso", "Precio").forEach(header -> {
                PdfPCell cell = new PdfPCell(new Phrase(header, infoFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            });

            for (UsuarioAsiento ua : usuarioAsientos) {
                Asiento a = ua.getAsiento();
                table.addCell(a.getColumna() + a.getFila());
                table.addCell(a.getPiso().toString());
            }

            document.add(table);

            // Código de boleto
            Paragraph barcode = new Paragraph("Código de boleto: " + pasaje.getIdPasaje(), infoFont);
            barcode.setAlignment(Element.ALIGN_CENTER);
            document.add(barcode);

            document.close();
            return filePath;

        } catch (DocumentException | IOException e) {
            throw new PDFGenerationException("Error al generar el PDF del boleto: " + e.getMessage());
        }
    }
}
