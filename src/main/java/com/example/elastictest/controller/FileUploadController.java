package com.example.elastictest.controller;

import com.example.elastictest.model.PdfDocument;
import com.example.elastictest.model.Tag;
import com.example.elastictest.repos.PdfDocumentRepository;
import com.example.elastictest.repos.TagRepository;
import com.example.elastictest.storage.StorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private StorageService storageService;
    private ElasticsearchOperations elasticsearchOperations;
    private PdfDocumentRepository pdfDocumentRepository;
    private TagRepository tagRepository;

    public FileUploadController(StorageService storageService,
                                ElasticsearchOperations elasticsearchOperations,
                                PdfDocumentRepository pdfDocumentRepository,
                                TagRepository tagRepository) {
        this.storageService = storageService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.pdfDocumentRepository = pdfDocumentRepository;
        this.tagRepository = tagRepository;
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
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("customer") String customer,
                             @RequestParam("doc_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date doc_date,
                             HttpServletResponse httpResponse) {
        String name = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        String text;
        try {
            PDDocument document = PDDocument.load(storageService.load(name).toFile());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
            document.close();

            String[] tags = customer.split(",");
            for (String tag : tags) {
                if (!tagRepository.existsByValue(tag)) {
                    tagRepository.save(new Tag(tag));
                }
            }

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(doc_date);

            PdfDocument pdfDocument = new PdfDocument(name, text, tags, date);
            pdfDocumentRepository.save(pdfDocument);

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(pdfDocument.getId())
                    .withObject(pdfDocument)
                    .build();
            elasticsearchOperations.index(indexQuery);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            httpResponse.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
