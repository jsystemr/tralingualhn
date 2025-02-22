package com.siguasystem.awstextextract.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siguasystem.awstextextract.service.TextractService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api")
public class TextractController {
	private static SimpleDateFormat formatterFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	@Autowired
    private TextractService textractService;

    @PostMapping("/extract-text")
    public String extractText(@RequestParam("file") MultipartFile file) {
        try {
            // Guardar el archivo temporalmente
            File tempFile = File.createTempFile("temp-", ".jpg");
            file.transferTo(tempFile);

            // Procesar el archivo
            String extractedText = textractService.extractTextFromImage(tempFile.getAbsolutePath());

            // Eliminar el archivo temporal
            tempFile.delete();

            return extractedText;
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/extracttext-to-file")
    public ResponseEntity<byte[]> stringToTextFile(@RequestParam("file") MultipartFile file,HttpServletResponse response) {
        try {
			 String fcreacion=formatterFecha.format(new Date());
			  String nameFile=fcreacion+".txt";
			  // Guardar el archivo temporalmente
            File tempFile = File.createTempFile("temp-", ".jpg");
            file.transferTo(tempFile);
        	// Procesar el archivo
            String extractedText = textractService.extractTextFromImage(tempFile.getAbsolutePath());
            response.setHeader(
	      	          HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +file.getName()+fcreacion+".txt"+ ";");
	      	      response.setContentType("application/octet-stream");
	      	      System.out.println("Report successfully generated  Done!");
			return new ResponseEntity<>(textractService.creartextfile(extractedText).toByteArray(),HttpStatus.OK); 
		
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}