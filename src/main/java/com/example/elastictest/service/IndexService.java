package com.example.elastictest.service;

import com.example.elastictest.model.PdfDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface IndexService {
    PdfDocument indexDocument(MultipartFile multipartFile, String customer, Date docDate);
}
