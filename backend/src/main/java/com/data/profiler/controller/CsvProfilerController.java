package com.data.profiler.controller;
import com.data.profiler.model.ProfileResult;
import com.data.profiler.service.CsvProfilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CsvProfilerController {

    @Autowired
    private CsvProfilerService csvProfilerService;

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping("/profile")
    public ResponseEntity<?> profileCsv(@RequestParam("file") MultipartFile file) {
        try {
            // Créer le répertoire de téléchargement s'il n'existe pas
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Sauvegarder le fichier temporairement
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // Effectuer le profilage
            List<ProfileResult> results = csvProfilerService.profileCsv(filePath.toString());

            // Nettoyer le fichier temporaire
            Files.deleteIfExists(filePath);

            return ResponseEntity.ok(results);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du téléchargement du fichier: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du profilage: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service de profilage CSV opérationnel");
    }
}