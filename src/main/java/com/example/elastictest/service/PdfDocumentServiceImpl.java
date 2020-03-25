package com.example.elastictest.service;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.repos.PdfDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PdfDocumentServiceImpl implements PdfDocumentService {

    private PdfDocumentRepository pdfDocumentRepository;

    public PdfDocumentServiceImpl(PdfDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }

    @Override
    public List<PdfDocument> getAll() {
        List<PdfDocument> pdfDocuments = new ArrayList<>();
        for (PdfDocument pdfDocument : pdfDocumentRepository.findAll()) {
            pdfDocuments.add(pdfDocument);
        }
        return pdfDocuments;
    }

    @Override
    public PdfDocument getById(String id) {
        return pdfDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(String id) {
        pdfDocumentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        pdfDocumentRepository.deleteAll();
    }

    @Override
    public PdfDocument save(PdfDocument pdfDocument) {
        return pdfDocumentRepository.findById(pdfDocument.getId())
                .map(doc -> {
                    doc.setName(pdfDocument.getName());
                    doc.setText(pdfDocument.getText());
                    doc.setCustomer(pdfDocument.getCustomer());
                    doc.setDate(pdfDocument.getDate());
                    return pdfDocumentRepository.save(doc);
                })
                .orElseGet(() -> pdfDocumentRepository.save(pdfDocument));
    }
}
