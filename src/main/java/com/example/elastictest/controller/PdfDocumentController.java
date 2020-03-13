package com.example.elastictest.controller;

import com.example.elastictest.repos.PdfDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfDocumentController {

    @Autowired
    PdfDocumentRepository pdfDocumentRepository;

    @PostMapping("/add")
    public void addDocument() {

    }
}
