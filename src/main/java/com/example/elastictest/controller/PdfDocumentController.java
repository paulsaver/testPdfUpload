package com.example.elastictest.controller;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.service.PdfDocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document")
public class PdfDocumentController {

    private PdfDocumentService pdfDocumentService;

    public PdfDocumentController(PdfDocumentService pdfDocumentService) {
        this.pdfDocumentService = pdfDocumentService;
    }

    @GetMapping("/all")
    public List<PdfDocument> getAllDocuments() {
        return pdfDocumentService.getAll();
    }

    @DeleteMapping("/all")
    public void deleteAllDocuments() {
        pdfDocumentService.deleteAll();
    }

    @GetMapping("/{id}")
    public PdfDocument getDocumentById(@PathVariable String id){
        return pdfDocumentService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDocumentById(@PathVariable String id) {
        pdfDocumentService.deleteById(id);
    }

    @PutMapping("/{id}")
    public PdfDocument updateDocument(@RequestBody PdfDocument newPdfDocument, @PathVariable String id) {
        newPdfDocument.setId(id);
        return pdfDocumentService.save(newPdfDocument);
    }
}
