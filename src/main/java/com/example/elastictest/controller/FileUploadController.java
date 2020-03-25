package com.example.elastictest.controller;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.service.IndexService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
public class FileUploadController {

    private IndexService indexService;

    public FileUploadController(IndexService indexService) {
        this.indexService = indexService;
    }

    @PostMapping("/upload")
    public PdfDocument uploadFile(@RequestParam("file") MultipartFile file,
                                  @RequestParam("customer") String customer,
                                  @RequestParam("doc_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date doc_date) {

        return indexService.indexDocument(file, customer, doc_date);
    }

}
