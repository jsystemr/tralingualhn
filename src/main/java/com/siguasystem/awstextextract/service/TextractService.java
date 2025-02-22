package com.siguasystem.awstextextract.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;

@Service
public class TextractService {

	@Autowired
    private TextractClient textractClient;
	 
	private static SimpleDateFormat formatterFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		/*------------------------------------------------*/

    public String extractTextFromImage(String imagePath) throws IOException {
        // Leer el archivo JPG como bytes
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        SdkBytes bytes = SdkBytes.fromByteArray(imageBytes);

        // Crear la solicitud para Textract
        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                .document(Document.builder()
                        .bytes(bytes)
                        .build())
                .build();

        // Enviar la solicitud y obtener la respuesta
        DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

        // Extraer y concatenar el texto detectado
        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(DetectDocumentTextResponse response) {
        return response.blocks().stream()
                .filter(block -> block.blockType().equals(BlockType.LINE))
                .map(Block::text)
                .collect(Collectors.joining("\n"));
    }

    public ByteArrayOutputStream  creartextfile(String txtcontenido) throws IOException {
		  String fcreacion=formatterFecha.format(new Date());
		  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		  try (
				  BufferedWriter writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(outputStream));
				  PrintWriter fileText = new PrintWriter(outputStream);
		        ) {
			  		fileText.append(txtcontenido);
			  		fileText.flush();         
		            System.out.println("Finalizo la Generacion del txt...");
		            return outputStream;
		        } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        return outputStream;
	  }
}