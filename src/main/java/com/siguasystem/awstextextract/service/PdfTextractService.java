package com.siguasystem.awstextextract.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.DocumentLocation;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionRequest;
import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;
import software.amazon.awssdk.services.textract.model.S3Object;
import software.amazon.awssdk.services.textract.model.StartDocumentTextDetectionRequest;
import software.amazon.awssdk.services.textract.model.StartDocumentTextDetectionResponse;

@Service
public class PdfTextractService {
	
	 private final TextractClient textractClient;
	private final S3Client s3Client;
    
	/*Variables AWS se reemplazaraon por variables de session*/
	private String accessKey=System.getenv("textractAccessKey");
    private String secretKey=System.getenv("textractSecretKey");
    private String region="us-east-2";
    private String bucketName="tralingual-ocr-pdf-hn";
	/*---------------------------------------------------------*/
	private static SimpleDateFormat formatterFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        
    public String getAccessKey() {
                return accessKey;
        }

        public void setAccessKey(String accessKey) {
                this.accessKey = accessKey;
        }

        public String getSecretKey() {
                return secretKey;
        }

        public void setSecretKey(String secretKey) {
                this.secretKey = secretKey;
        }

        public String getRegion() {
                return region;
        }

        public void setRegion(String region) {
                this.region = region;
        }

        public String getBucketName() {
                return bucketName;
        }

        public void setBucketName(String bucketName) {
                this.bucketName = bucketName;
        }

public PdfTextractService() {
        /*Variables AWS se reemplazaraon por variables de session*/
        setAccessKey(System.getenv("textractAccessKey"));
        setSecretKey(System.getenv("textractSecretKey"));
        setRegion("us-east-2");
        setBucketName("tralingual-ocr-pdf-hn");
        this.textractClient = TextractClient.builder()
                .region(Region.of(getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(getAccessKey(), getSecretKey())
                ))
                .build();

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(getAccessKey(), getSecretKey())
                ))
                .build();

        //this.bucketName = bucketName;
    }

    // Sube el PDF a S3 (requerido para Textract)
    public void uploadPdfToS3(String localFilePath, String s3Key) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(getBucketName())
                        .key(s3Key)
                        .build(),
                Paths.get(localFilePath));
    }
    
 // Subir archivo como bytes
    public void uploadFileS3(MultipartFile file, String objectKey) throws IOException {
        
        // Obtener bytes y tipo de contenido
        byte[] bytes = file.getBytes();
        String contentType = file.getContentType();

        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .contentType(contentType) // Conserva el tipo de archivo
            .build();
            s3Client.putObject(request, RequestBody.fromBytes(bytes));
    }

    public void uploadFileS3(File file, String objectKey) throws IOException {
        
        // Obtener bytes y tipo de contenido
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String contentType = "pdf";

        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(getBucketName())
            .key(objectKey)
            .contentType(contentType) // Conserva el tipo de archivo
            .build();
            s3Client.putObject(request, RequestBody.fromBytes(bytes));
    }
 
 public void uploadFileS3Byte(byte[] bytes, String objectKey) throws IOException {
     
     // Obtener bytes y tipo de contenido
     String contentType = "pdf";

     PutObjectRequest request = PutObjectRequest.builder()
         .bucket(getBucketName())
         .key(objectKey)
         .contentType(contentType) // Conserva el tipo de archivo
         .build();
         s3Client.putObject(request, RequestBody.fromBytes(bytes));
 }
    // Descargar un archivo
    public byte[] downloadFile(String objectKey) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(getBucketName())
            .key(objectKey)
            .build();

        ResponseBytes<GetObjectResponse> response =s3Client.getObjectAsBytes(request);
        return response.asByteArray();
    }
       // Descargar un archivo
       public byte[] downloadFileS3(String objectKey) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(getBucketName())
            .key(objectKey)
            .build();

        ResponseBytes<GetObjectResponse> response =s3Client.getObjectAsBytes(request);
        return response.asByteArray();
    }

    // Inicia el análisis del PDF y obtiene el JobId
    public String startPdfAnalysis(String s3Key) {
        StartDocumentTextDetectionRequest request = StartDocumentTextDetectionRequest.builder()
                .documentLocation(DocumentLocation.builder()
                        .s3Object(S3Object.builder()
                                .bucket(getBucketName())
                                .name(s3Key)
                                .build())
                        .build())
                .build();

        StartDocumentTextDetectionResponse response = textractClient.startDocumentTextDetection(request);
        return response.jobId();
    }

    // Obtiene el resultado del análisis usando el JobId
    public String getPdfAnalysisResult(String jobId) {
        GetDocumentTextDetectionRequest request = GetDocumentTextDetectionRequest.builder()
                .jobId(jobId)
                .build();

        // Espera hasta que el análisis esté completo (puedes implementar retries)
        GetDocumentTextDetectionResponse response = textractClient.getDocumentTextDetection(request);
        return extractTextFromResponse(response);
    }
    
    public String extractTextFromPdf(String pdfPath) throws IOException {
        // Leer el archivo PDF como bytes
        byte[] pdfBytes = Files.readAllBytes(Paths.get(pdfPath));
        SdkBytes bytes = SdkBytes.fromByteArray(pdfBytes);

        // Crear la solicitud para Textract
        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                .document(Document.builder()
                        .bytes(bytes)
                        .build())
                .build();

        // Enviar la solicitud y obtener la respuesta
        DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

        // Extraer y concatenar el texto detectado
        return extractTextPdfFromResponse(response);
    }
    
    private String extractTextPdfFromResponse(DetectDocumentTextResponse response) {
        return response.blocks().stream()
                .filter(block -> block.blockType().equals(BlockType.LINE))
                .map(Block::text)
                .collect(Collectors.joining("\n"));
    }

    private String extractTextFromResponse(GetDocumentTextDetectionResponse response) {
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
