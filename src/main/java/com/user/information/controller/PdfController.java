package com.user.information.controller;

import com.user.information.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','DEVELOPER')")
    public ResponseEntity<byte[]> downloadUsersPdf() {

        byte[] pdf = pdfService.generateUsersPdf();

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=users.pdf")

                .contentType(MediaType.APPLICATION_PDF)

                .body(pdf);
    }

    @GetMapping("/devices")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','DEVELOPER')")
    public ResponseEntity<byte[]> downloadDevicesPdf() {

        byte[] pdf = pdfService.generateDevicesPdf();

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=devices.pdf")

                .contentType(MediaType.APPLICATION_PDF)

                .body(pdf);
    }
}
