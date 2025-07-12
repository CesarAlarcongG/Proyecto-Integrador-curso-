package com.example.backendintegrador.controller;

import com.example.backendintegrador.dto.PasajeDTO;
import com.example.backendintegrador.service.PasajeService;
import com.example.backendintegrador.service.PDFService;
import com.example.backendintegrador.persistence.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.io.File; // Importar File
import java.io.IOException;
import java.nio.file.Files; // Importar Files
import java.nio.file.Path; // Importar Path
import java.nio.file.Paths; // Importar Paths
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/pasajes")
public class PasajeController {

    @Autowired
    private PasajeService pasajeService;

    @Autowired
    private PDFService pdfService;

    @PostMapping
    public ResponseEntity<PasajeDTO> createPasaje(@Valid @RequestBody PasajeDTO pasajeDTO) { // Restaurado @Valid
        PasajeDTO savedPasaje = pasajeService.savePasaje(pasajeDTO);
        return ResponseEntity.ok(savedPasaje);
    }

    @GetMapping
    public ResponseEntity<List<PasajeDTO>> getAllPasajes() {
        List<PasajeDTO> pasajes = pasajeService.getAllPasajes();
        return ResponseEntity.ok(pasajes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeDTO> getPasajeById(@PathVariable Integer id) {
        PasajeDTO pasaje = pasajeService.getPasajeById(id);
        return ResponseEntity.ok(pasaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasaje(@PathVariable Integer id) {
        pasajeService.deletePasaje(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PasajeDTO>> getPasajesByUsuarioId(@PathVariable Integer usuarioId) {
        List<PasajeDTO> pasajes = pasajeService.getPasajesByUsuarioId(usuarioId);
        return ResponseEntity.ok(pasajes);
    }

    /**
     * Endpoint para descargar el boleto de un pasaje en formato PDF.
     * Genera el PDF, lo guarda temporalmente en el servidor y luego lo sirve.
     * @param id El ID del pasaje.
     * @return ResponseEntity con el archivo PDF como un array de bytes.
     */
    @GetMapping("/{id}/ticket/download")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Integer id) {
        String filePath = null; // Para almacenar la ruta del archivo temporal
        try {
            // 1. Obtener datos
            Pasaje pasaje = pasajeService.getPasajeEntityById(id);
            List<UsuarioAsiento> usuarioAsientos = pasajeService.getUsuarioAsientosByPasajeId(id);

            // 2. Generar PDF y guardarlo en un archivo temporal
            // ⭐ AHORA PDFService.generateTicket DEVUELVE LA RUTA DEL ARCHIVO ⭐
            filePath = pdfService.generateTicket(pasaje, usuarioAsientos);
            Path pdfPath = Paths.get(filePath);
            byte[] pdfBytes = Files.readAllBytes(pdfPath); // Leer los bytes del archivo

            // 3. Verificar integridad del PDF (opcional, pero buena práctica)
            if (pdfBytes == null || pdfBytes.length < 100) { // Un PDF válido debe tener al menos 100 bytes
                throw new RuntimeException("El PDF generado está vacío o corrupto en el disco.");
            }

            // 4. Configurar respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // ⭐ Generar nombre de archivo con timestamp para evitar caché ⭐
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String filename = "boleto_viaje_" + id + "_" + timestamp + ".pdf";
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(filename)
                            .build());
            headers.setContentLength(pdfBytes.length);

            // 5. Deshabilitar caché del navegador para asegurar descarga fresca
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error al generar o descargar el boleto PDF para el pasaje " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al generar el boleto: " + e.getMessage() + ". Por favor, intente de nuevo."
            );
        } finally {
            // ⭐ Asegurarse de eliminar el archivo temporal ⭐
            if (filePath != null) {
                try {
                    Files.deleteIfExists(Paths.get(filePath));
                    System.out.println("Archivo temporal eliminado: " + filePath);
                } catch (IOException ex) {
                    System.err.println("Error al eliminar el archivo temporal " + filePath + ": " + ex.getMessage());
                }
            }
        }
    }
}
