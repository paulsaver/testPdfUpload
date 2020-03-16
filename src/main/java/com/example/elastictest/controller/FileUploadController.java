package com.example.elastictest.controller;

import com.example.elastictest.commons.FileResponse;
import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.repos.PdfDocumentRepository;
import com.example.elastictest.storage.StorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private StorageService storageService;
    private ElasticsearchOperations elasticsearchOperations;
    private PdfDocumentRepository pdfDocumentRepository;

    public FileUploadController(StorageService storageService, ElasticsearchOperations elasticsearchOperations, PdfDocumentRepository pdfDocumentRepository) {
        this.storageService = storageService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.pdfDocumentRepository = pdfDocumentRepository;
    }

    @GetMapping("/")
    public String listAllFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList()));

        return "listFiles";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        String text = null;
        String documentId = null;
        try {
            PDDocument document = PDDocument.load(storageService.load(name).toFile());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
            document.close();

            PdfDocument pdfDocument = new PdfDocument(name, text);
            pdfDocumentRepository.save(pdfDocument);

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(pdfDocument.getId().toString())
                    .withObject(pdfDocument)
                    .build();
            documentId = elasticsearchOperations.index(indexQuery);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return new FileResponse(name, uri, file.getContentType(), documentId,  text, file.getSize());
    }

    @PostMapping("/upload-multiple-files")
    @ResponseBody
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

}
