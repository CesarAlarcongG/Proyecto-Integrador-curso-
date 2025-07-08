package com.example.backendintegrador.service;

import com.example.backendintegrador.exception.PDFGenerationException;
import com.example.backendintegrador.persistence.entity.Pasaje;
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
import java.util.List;

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

            // Información del pasaje
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            Paragraph p1 = new Paragraph();
            p1.add(new Chunk("Número de boleto: ", infoFont));
            p1.add(new Chunk(pasaje.getIdPasaje().toString(), infoFont));
            p1.setSpacingAfter(10);
            document.add(p1);

            Paragraph p2 = new Paragraph();
            p2.add(new Chunk("Fecha de emisión: ", infoFont));
            p2.add(new Chunk(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), infoFont));
            p2.setSpacingAfter(10);
            document.add(p2);

            Paragraph p3 = new Paragraph();
            p3.add(new Chunk("Ruta: ", infoFont));
            p3.add(new Chunk(pasaje.getRuta().getNombre(), infoFont));
            p3.setSpacingAfter(10);
            document.add(p3);

            Paragraph p4 = new Paragraph();
            p4.add(new Chunk("Fecha de viaje: ", infoFont));
            p4.add(new Chunk(pasaje.getViaje().getFechaSalida().toString(), infoFont));
            p4.setSpacingAfter(10);
            document.add(p4);

            Paragraph p5 = new Paragraph();
            p5.add(new Chunk("Hora de salida: ", infoFont));
            p5.add(new Chunk(pasaje.getViaje().getHoraSalida().toString(), infoFont));
            p5.setSpacingAfter(10);
            document.add(p5);

            Paragraph p6 = new Paragraph();
            p6.add(new Chunk("Pasajero: ", infoFont));
            p6.add(new Chunk(pasaje.getUsuario().getNombres() + " " + pasaje.getUsuario().getApellidos(), infoFont));
            p6.setSpacingAfter(10);
            document.add(p6);

            Paragraph p7 = new Paragraph();
            p7.add(new Chunk("DNI: ", infoFont));
            p7.add(new Chunk(pasaje.getUsuario().getDni(), infoFont));
            p7.setSpacingAfter(10);
            document.add(p7);

            Paragraph p8 = new Paragraph();
            p8.add(new Chunk("Total pagado: S/ ", infoFont));
            p8.add(new Chunk(String.format("%.2f", pasaje.getTotalPagar()), infoFont));
            p8.setSpacingAfter(20);
            document.add(p8);

            // Tabla de asientos
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            // Encabezados de la tabla
            PdfPCell cell1 = new PdfPCell(new Phrase("Asiento", infoFont));
            PdfPCell cell2 = new PdfPCell(new Phrase("Piso", infoFont));
            PdfPCell cell3 = new PdfPCell(new Phrase("Precio", infoFont));

            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            // Datos de la tabla
            for (UsuarioAsiento ua : usuarioAsientos) {
                table.addCell(ua.getAsiento().getAsiento());
                table.addCell(ua.getAsiento().getPiso().toString());
                table.addCell("S/ " + String.format("%.2f", ua.getAsiento().getPrecio()));
            }

            document.add(table);

            // Código de barras (simulado)
            Paragraph barcode = new Paragraph("Código de boleto: " + pasaje.getIdPasaje() + "-" +
                    pasaje.getUsuario().getDni(), infoFont);
            barcode.setAlignment(Element.ALIGN_CENTER);
            document.add(barcode);

            document.close();

            return filePath;
        } catch (DocumentException | IOException e) {
            throw new PDFGenerationException("Error al generar el PDF del boleto: " + e.getMessage());
        }
    }
}