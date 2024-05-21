package com.yumeinaruu.iis.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class FileController {
    private final Path ROOT_FILE_PATH = Paths.get("data");

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPERADMIN')")
    public ResponseEntity<HttpStatus> upload(@RequestParam("file") MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), ROOT_FILE_PATH.resolve(file.getOriginalFilename()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            log.warn(e + "");
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/{filename}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN', 'SUPERADMIN')")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        Path path = ROOT_FILE_PATH.resolve(filename);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            log.warn(e + "");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN', 'SUPERADMIN')")
    public ResponseEntity<ArrayList<String>> getFiles() {
        try {
            ArrayList<String> filenames = (ArrayList<String>) Files.walk(this.ROOT_FILE_PATH, 1).filter(path -> !path.equals(this.ROOT_FILE_PATH)).map(Path::toString).collect(Collectors.toList());
            return new ResponseEntity<>(filenames, HttpStatus.OK);
        } catch (IOException e) {
            log.warn(e + "");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } finally {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{filename}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPERADMIN')")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable String filename) {
        Path path = ROOT_FILE_PATH.resolve(filename);

        File file = new File(path.toString());
        if (file.delete()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
