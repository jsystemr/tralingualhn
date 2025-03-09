package com.siguasystem.awstextextract.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.siguasystem.awstextextract.service.AwsTextTranslateService;
import com.siguasystem.awstextextract.service.ConvertPDFPagesToImages;
import com.siguasystem.awstextextract.service.PdfTextractService;
import com.siguasystem.awstextextract.service.TextractService;

@RestController
@RequestMapping("/api-tra")
public class FileTaductorController {

	@Autowired
    private AwsTextTranslateService service;
	@Autowired
    private TextractService textractService;
	@Autowired
    private ConvertPDFPagesToImages pdfImgService;
	
	@Autowired
    private PdfTextractService pdfExtract;
	  
	  @PostMapping("/traducir-a-ingles")
	 public ResponseEntity<ByteArrayResource> translatePdf(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {

	        try {
	            // Guardar el archivo temporalmente
	            File tempFile = File.createTempFile("temp-", ".jpg");
	            file.transferTo(tempFile);
	            // Extraer texto
	            String extractedText = textractService.extractTextFromImage(tempFile.getAbsolutePath());
	            tempFile.delete();
	            // Traducir
	            String translatedText = service.translateText(extractedText, sourceLang, targetLang);
	            
	            // Convertir a TXT
	            byte[] txtBytes = translatedText.getBytes();
	            
	            // Respuesta como archivo descargable
	            return ResponseEntity.ok()
	                    .contentType(MediaType.TEXT_PLAIN)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"translated.txt\"")
	                    .body(new ByteArrayResource(txtBytes));
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error procesando el archivo", e);
	        }
	    }
	  
	  @PostMapping("/traducir-imagen-a-ingles")
	    public ResponseEntity<ByteArrayResource> translateImagen(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {

	        try {
	            // Guardar el archivo temporalmente
	            File tempFile = File.createTempFile("temp-", ".jpg");
	            file.transferTo(tempFile);
	            // Extraer texto
	            String extractedText = textractService.extractTextFromImage(tempFile.getAbsolutePath());
	            tempFile.delete();
	            // Traducir
	            String translatedText = service.translateText(extractedText, sourceLang, targetLang);
	            
	            // Convertir a TXT
	            byte[] txtBytes = translatedText.getBytes();
	            
	            // Respuesta como archivo descargable
	            return ResponseEntity.ok()
	                    .contentType(MediaType.TEXT_PLAIN)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"translated.txt\"")
	                    .body(new ByteArrayResource(txtBytes));
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error procesando el archivo", e);
	        }
	    }
	  
	  @PostMapping("/traducir-pdf-imagen-a-ingles")
	    public ResponseEntity<ByteArrayResource> translatePdfImagen(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {

	        try {
	        	 // Guardar el archivo temporalmente
	            File tempFile = File.createTempFile("temp-", ".pdf");
	            file.transferTo(tempFile);
	            // Extraer texto
	            List<String> lisTextractedText = pdfImgService.convertirPdfToImgListFilev2(tempFile.getAbsolutePath());
	            tempFile.delete();
	            String carpetaFile=lisTextractedText.get(0).substring(0, lisTextractedText.get(0).lastIndexOf("\\"));
	            StringBuilder txtunido=new StringBuilder();
	            byte[] txtBytes=new byte[10];
	            Integer x=0;
	            for (String rutaimg : lisTextractedText) {
	            	x++;
	            	 File tempImgFile = new File(rutaimg);	
	 	            // Extraer texto
	            	 txtunido.append("\n-----------------------Pag."+(x)+"---------------------------\n");
	            	 txtunido.append(textractService.extractTextFromImage(tempImgFile.getAbsolutePath()));
	            	 txtunido.append("\n----------------------------------------------------------------\n");
		 	           try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Espera 5 segundos (ajusta según necesidad)
		 	          tempImgFile.delete();
				}	            
	            String translatedText =service.translateText(txtunido.toString(), sourceLang, targetLang);
	            // Convertir a TXT
	            txtBytes = translatedText.getBytes();
	            // 3. Eliminar Carpeta temporal
	            Path folderPath = Paths.get(carpetaFile);
                Files.delete(folderPath);
	            // Respuesta como archivo descargable
	            return ResponseEntity.ok()
	                    .contentType(MediaType.TEXT_PLAIN)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"translated.txt\"")
	                    .body(new ByteArrayResource(txtBytes));
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error procesando el archivo", e);
	        }
	    }
	  
	  @PostMapping("/traducir-pdf-imagen-a-ingles-pdf")
	    public ResponseEntity<?>  translatePdfImagenPdf(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {
	        HttpHeaders headers = new HttpHeaders();
	        try {
	        	
	        	 // Guardar el archivo temporalmente
	            File tempFile = File.createTempFile("temp-", ".pdf");
	           
	            file.transferTo(tempFile);
	            // Extraer texto
	            List<String> lisTextractedText = pdfImgService.convertirPdfToImgListFilev2(tempFile.getAbsolutePath());
	            String carpetaFile=lisTextractedText.get(0).substring(0, lisTextractedText.get(0).lastIndexOf("\\"));
	            tempFile.delete();
	            StringBuilder txtunido=new StringBuilder();
	            byte[] txtBytes=new byte[10];
	            Integer x=0;
	            for (String rutaimg : lisTextractedText) {
	            	x++;
	            	 File tempImgFile = new File(rutaimg);	
	 	            // Extraer texto
	            	 txtunido.append("\n-----------------------Pag."+(x)+"---------------------------\n");
	            	 txtunido.append(textractService.extractTextFromImage(tempImgFile.getAbsolutePath()));
	            	 txtunido.append("\n----------------------------------------------------------------\n");
		 	           try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Espera 5 segundos (ajusta según necesidad)
		 	          tempImgFile.delete();
				}	            
	            String translatedText =service.translateText(txtunido.toString(), sourceLang, targetLang);
	         // 2. Generar el PDF
	            byte[] pdfBytes = pdfImgService.convertTxtToPdf(translatedText);
	            // 3. Eliminar Carpete Temporal
	            Path folderPath = Paths.get(carpetaFile);
                Files.delete(folderPath);
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDispositionFormData("attachment", "traduccion"+".pdf");

	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .body(pdfBytes);
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error procesando el archivo", e);
	        }
	    }

		@PostMapping("/traducir-pdf-imagens3-a-ingles-pdf")
	    public ResponseEntity<?>  translatePdfImagenS3Pdf(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {
	        HttpHeaders headers = new HttpHeaders();
	        try {
	        	
	        	 // Guardar el archivo temporalmente
	            File tempFile = File.createTempFile("temp-", ".pdf");
	           
	            file.transferTo(tempFile);
	            // Extraer texto
	            List<String> lisTextractedText = pdfImgService.convertirPdfToImgS3(tempFile,file);//Envia nombre archivo y archivo adjunto
	            String carpetaFile=lisTextractedText.get(0).substring(0, lisTextractedText.get(0).lastIndexOf("/"));
	            tempFile.delete();
	            StringBuilder txtunido=new StringBuilder();
	            byte[] txtBytes=new byte[10];
	            Integer x=0;
	            for (String rutaimg : lisTextractedText) {
	            	x++;
	 	            // Extraer texto
	            	 txtunido.append("\n-----------------------Pag."+(x)+"---------------------------\n");
	            	 //txtunido.append(textractService.extractTextFromImageS3(pdfExtract.downloadFileS3(rutaimg)));
	            	 txtunido.append(textractService.extractTextBlqueFromImageS3(pdfExtract.downloadFileS3(rutaimg)));
	            	 txtunido.append("\n----------------------------------------------------------------\n");
		 	           try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Espera 5 segundos (ajusta según necesidad)
				}	            
	            String translatedText =service.translateText(txtunido.toString(), sourceLang, targetLang);
	        // 2. Generar el PDF Traducido
			 	String nombreArchivoOcr=file.getOriginalFilename().replace(".","-")+"-ocr.txt";
				 String nombreArchivoResultado=file.getOriginalFilename().replace(".","-")+"-traduccion.pdf";
				 //Generar Txt Extraido
				byte[] textoBytes = translatedText.getBytes();
				pdfExtract.uploadFileTxtS3(textoBytes, carpetaFile+"/"+nombreArchivoOcr);
	            byte[] pdfBytes = pdfImgService.convertTxtToPdfS3(translatedText,carpetaFile+"/"+nombreArchivoResultado);
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDispositionFormData("attachment", nombreArchivoResultado);

	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .body(pdfBytes);
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error procesando el archivo", e);
	        }
	    }
		
		@PostMapping("/traducir-pdf-text-a-ingles-pdf")
	    public ResponseEntity<?>  translatePdfToTextPdf(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {
	        HttpHeaders headers = new HttpHeaders();
	        try {
	        	
	        	 // Guardar el archivo temporalmente
	            File tempFile = File.createTempFile("temp-", ".pdf");
	           
	            file.transferTo(tempFile);
	            // Extraer texto
	            String lisTextractedText = pdfImgService.convertirPdfToOnlyTxt(tempFile,file);//Envia nombre archivo y archivo adjunto
	            tempFile.delete();
	            StringBuilder txtunido=new StringBuilder();
	            byte[] txtBytes=new byte[10];
            
	            String translatedText =service.translateText(lisTextractedText, sourceLang, targetLang);
	        // 2. Generar el PDF Traducido
			 	String nombreArchivoOcr=file.getOriginalFilename().replace(".","-")+"-ocr.txt";
				 String nombreArchivoResultado=file.getOriginalFilename().replace(".","-")+"-traduccion.pdf";
				 //Generar Txt Extraido
				byte[] textoBytes = translatedText.getBytes();
				pdfExtract.uploadFileTxtS3(textoBytes, nombreArchivoOcr);
	            byte[] pdfBytes = pdfImgService.convertTxtToPdfS3(translatedText,nombreArchivoResultado);
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDispositionFormData("attachment", nombreArchivoResultado);

	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .body(pdfBytes);
	            
	        } catch (IOException e) {
	            throw new RuntimeException("Error procesando el archivo", e);
	        }
	    }
		
		@PostMapping("/traducir-pdf-textparrafo-a-ingles-pdf")
	    public ResponseEntity<?>  translatePdfToTextParrafoPdf(
	            @RequestParam("file") MultipartFile file,
	            @RequestParam(defaultValue = "auto") String sourceLang,
	            @RequestParam(defaultValue = "en") String targetLang) {
			 HttpHeaders headers = new HttpHeaders();
		        try {
		        	
		        	 // Guardar el archivo temporalmente
		            File tempFile = File.createTempFile("temp-", ".pdf");
		           
		            file.transferTo(tempFile);
		            // Extraer texto
		            List<String> lisTextractedText = pdfImgService.convertirPdfToImgS3(tempFile,file);//Envia nombre archivo y archivo adjunto
		            String carpetaFile=lisTextractedText.get(0).substring(0, lisTextractedText.get(0).lastIndexOf("/"));
		            tempFile.delete();
		            StringBuilder txtunido=new StringBuilder();
		            byte[] txtBytes=new byte[10];
		            Integer x=0;
		            for (String rutaimg : lisTextractedText) {
		            	x++;
		 	            // Extraer texto
		            	 txtunido.append("\n---------------------------------------------------------Pag"+(x)+"------------------------------------------------------\n");
		            	 //txtunido.append(textractService.extractTextFromImageS3(pdfExtract.downloadFileS3(rutaimg)));
		            	 txtunido.append(textractService.extractTextClearFromImageS3(pdfExtract.downloadFileS3(rutaimg)));//quita los saltos de linea
		            	 txtunido.append("\n---------------------------------------------------------------------------------------------------------------------\n");
			 	           try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // Espera 5 segundos (ajusta según necesidad)
					}	            
		            String translatedText =service.translateText(txtunido.toString(), sourceLang, targetLang);
		        // 2. Generar el PDF Traducido
				 	String nombreArchivoOcr=file.getOriginalFilename().replace(".","-")+"-ocr.txt";
					 String nombreArchivoResultado=file.getOriginalFilename().replace(".","-")+"-traduccion.pdf";
					 //Generar Txt Extraido
					byte[] textoBytes = translatedText.getBytes();
					pdfExtract.uploadFileTxtS3(textoBytes, carpetaFile+"/"+nombreArchivoOcr);
		            byte[] pdfBytes = pdfImgService.convertTxtToPdfS3Parrafo(translatedText,carpetaFile+"/"+nombreArchivoResultado);
		            headers.setContentType(MediaType.APPLICATION_PDF);
		            headers.setContentDispositionFormData("attachment", nombreArchivoResultado);

		            return ResponseEntity.ok()
		                    .headers(headers)
		                    .body(pdfBytes);
		            
		        } catch (IOException e) {
		            throw new RuntimeException("Error procesando el archivo", e);
		        }
	    }
	
}