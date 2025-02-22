package com.siguasystem.awstextextract.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siguasystem.awstextextract.service.PdfTextractService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api-pdf")
public class PdfTextractController {
	
	  private  String fcreacion="";
	  private String resultado="";
	  private String extractedText="";
	  private static SimpleDateFormat formatterFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	 @Autowired
	    private PdfTextractService pdfTextractService;

	    @PostMapping("/process-pdf")
	    public String processPdf(@RequestParam("file") MultipartFile file) {
	        try {
	            // 1. Guardar el PDF temporalmente
	            File tempFile = File.createTempFile("temp-", ".pdf");
	            file.transferTo(tempFile);

	            // 2. Subir el PDF a S3 (clave única para evitar colisiones)
	            String s3Key = "pdfs/" + UUID.randomUUID() + ".pdf";
	            pdfTextractService.uploadPdfToS3(tempFile.getAbsolutePath(), s3Key);

	            // 3. Iniciar el análisis de Textract
	            String jobId = pdfTextractService.startPdfAnalysis(s3Key);

	            // 4. Obtener resultados (en producción, usa un sistema asíncrono con SNS)
	            //    Aquí es una espera básica (no recomendado para producción)
	            Thread.sleep(5000); // Espera 5 segundos (ajusta según necesidad)
	            extractedText = pdfTextractService.getPdfAnalysisResult(jobId);

	            // 5. Eliminar archivo temporal
	            tempFile.delete();

	            return extractedText;

	        } catch (IOException | InterruptedException e) {
	            return "Error: " + e.getMessage();
	        }
	    }
	    
	    
	    @PostMapping("/process-pdf-to-text")
	    public ResponseEntity<byte[]> processPdfToText(@RequestParam("file") MultipartFile file,HttpServletResponse response) {
	        try {
	        	fcreacion=formatterFecha.format(new Date());
	            // 1. Guardar el PDF temporalmente
	            File tempFile = File.createTempFile("temp-", ".pdf");
	            file.transferTo(tempFile);

	            // 2. Subir el PDF a S3 (clave única para evitar colisiones)
	            String s3Key = "pdfs/" + UUID.randomUUID() + ".pdf";
	            pdfTextractService.uploadPdfToS3(tempFile.getAbsolutePath(), s3Key);

	            // 3. Iniciar el análisis de Textract
	            String jobId = pdfTextractService.startPdfAnalysis(s3Key);

	            // 4. Obtener resultados (en producción, usa un sistema asíncrono con SNS)
	            //    Aquí es una espera básica (no recomendado para producción)
	            Thread.sleep(5000); // Espera 5 segundos (ajusta según necesidad)
	            extractedText = pdfTextractService.getPdfAnalysisResult(jobId);
	            resultado=pdfTextractService.extractTextFromPdf(tempFile.getAbsolutePath());
	            // 5. Eliminar archivo temporal
	            tempFile.delete();

	            response.setHeader(
		      	          HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +file.getName()+fcreacion+".txt"+ ";");
		      	      response.setContentType("application/octet-stream");
		      	      System.out.println("Report successfully generated  Done!");
				return new ResponseEntity<>(pdfTextractService.creartextfile(extractedText).toByteArray(),HttpStatus.OK); 

	        } catch (IOException | InterruptedException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	        return null;
	    }
	    
	    @PostMapping("/process-pdf-to-img")
	    public ResponseEntity<?> processPdfToImg(@RequestParam("file") MultipartFile file,HttpServletResponse response) {
	        try {
	        	fcreacion=formatterFecha.format(new Date());
	            // 1. Guardar el PDF temporalmente
	            File tempFile = File.createTempFile(file.getName()+"-", ".pdf");
	            file.transferTo(tempFile);

	            // 2. Subir el PDF a S3 (clave única para evitar colisiones)
	            String s3Key = "pdfs/" + UUID.randomUUID() + ".pdf";
	            pdfTextractService.uploadPdfToS3(tempFile.getAbsolutePath(), s3Key);

	            // 3. Iniciar el análisis de Textract
	            String jobId = pdfTextractService.startPdfAnalysis(s3Key);

	            // 4. Obtener resultados (en producción, usa un sistema asíncrono con SNS)
	            //    Aquí es una espera básica (no recomendado para producción)
	            Thread.sleep(5000); // Espera 5 segundos (ajusta según necesidad)
	            extractedText = pdfTextractService.getPdfAnalysisResult(jobId);
	            resultado=pdfTextractService.extractTextFromPdf(tempFile.getAbsolutePath());
	            // 5. Eliminar archivo temporal
	            tempFile.delete();

				return new ResponseEntity<>("PDF covertido con exito!",HttpStatus.OK); 

	        } catch (IOException | InterruptedException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	        return new ResponseEntity<>("Error al convertir PDF!",HttpStatus.NO_CONTENT);
	    }
	

}
