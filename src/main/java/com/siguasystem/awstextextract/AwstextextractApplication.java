package com.siguasystem.awstextextract;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.plaf.synth.Region;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.DocumentMetadata;
import software.amazon.awssdk.services.textract.model.TextractException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;*/

@SpringBootApplication
public class AwstextextractApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwstextextractApplication.class, args);
		/*String currentDir = System.getProperty("user.dir");
		Path filePath = Paths.get(currentDir+"/src/main/resources/static/", "prueba-carta.JPG");
		final String usage = """

                Usage:
                    <sourceDoc>\s

                Where:
                    sourceDoc - The path where the document is located (must be an image, for example, C:/AWS/book.png).\s
                """;

        if (filePath== null) {
            System.out.println(usage);
            System.exit(1);
        }

        String sourceDoc = filePath.toString();
        software.amazon.awssdk.regions.Region region = software.amazon.awssdk.regions.Region.US_EAST_2;
        TextractClient textractClient = TextractClient.builder()
                .region(region)
                .build();

        detectDocText(textractClient, sourceDoc);
        textractClient.close();
	}
	
	 public static void detectDocText(TextractClient textractClient, String sourceDoc) {
	        try {
	            InputStream sourceStream = new FileInputStream(new File(sourceDoc));
	            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

	            // Get the input Document object as bytes.
	            Document myDoc = Document.builder()
	                    .bytes(sourceBytes)
	                    .build();

	            DetectDocumentTextRequest detectDocumentTextRequest = DetectDocumentTextRequest.builder()
	                    .document(myDoc)
	                    .build();

	            // Invoke the Detect operation.
	            DetectDocumentTextResponse textResponse = textractClient.detectDocumentText(detectDocumentTextRequest);
	            List<Block> docInfo = textResponse.blocks();
	            for (Block block : docInfo) {
	                System.out.println("The block type is " + block.blockType().toString());
	            }

	            DocumentMetadata documentMetadata = textResponse.documentMetadata();
	            System.out.println("The number of pages in the document is " + documentMetadata.pages());

	        } catch (TextractException | FileNotFoundException e) {

	            System.err.println(e.getMessage());
	            System.exit(1);
	        }*/
	    }

}
