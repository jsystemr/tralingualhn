package com.siguasystem.awstextextract.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentRequest;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentResponse;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.FeatureType;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

@Service
public class AwsTextTranslateService {
	 @Autowired
	    private TextractClient textractClient;

	    @Autowired
	    private TranslateClient translateClient;

	    public String extractTextFromPdf(MultipartFile file) throws IOException {
	        SdkBytes bytes = SdkBytes.fromInputStream(file.getInputStream());

	        AnalyzeDocumentRequest request = AnalyzeDocumentRequest.builder()
	                .document(Document.builder().bytes(bytes).build())
	                .featureTypes(FeatureType.TABLES, FeatureType.FORMS)
	                .build();

	        AnalyzeDocumentResponse response = textractClient.analyzeDocument(request);
	        
	        // Extraer texto de los bloques
	        StringBuilder text = new StringBuilder();
	        for (Block block : response.blocks()) {
	            if (block.blockType().equals(BlockType.LINE)) {
	                text.append(block.text()).append("\n");
	            }
	        }
	        return text.toString();
	    }

	    public String translateText(String text, String sourceLang, String targetLang) {
	    	
	        TranslateTextRequest request = TranslateTextRequest.builder()
	                .sourceLanguageCode(sourceLang) // Ej: "auto" para detectar
	                .targetLanguageCode(targetLang) // Ej: "es" (español)
	                .text(text)
	                .build();

	        TranslateTextResponse response = translateClient.translateText(request);
	        return response.translatedText();
	    }
}
