package com.siguasystem.awstextextract.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertPDFPagesToImages {
	
	private Logger logg=LoggerFactory.getLogger(getClass().getName());
	private final static String pdf_dir="pdfs/";
	
	 @Autowired
	 private PdfTextractService pdfTextractService;
	 private static SimpleDateFormat formatterFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	 public void convertirPdfToImg(String archivopdf) {
			try {
	            String sourceDir = pdf_dir+archivopdf;//"0038557743.pdf"; // Pdf files are read from this folder
	            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here

	            File sourceFile = new File(sourceDir);
	            File destinationFile = new File(destinationDir+sourceFile.getName());
	            if (!destinationFile.exists()) {
	                destinationFile.mkdir();
	                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
	            }
	            if (sourceFile.exists()) {
	                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
	                PDDocument document = PDDocument.load(sourceFile);
	                PDFRenderer pdfRenderer = new PDFRenderer(document);
	                
	                int numberOfPages = document.getNumberOfPages();
	                System.out.println("Total files to be converting -> "+ numberOfPages);

	                String fileName = sourceFile.getName().replace(".pdf", "");             
	                String fileExtension= "jpg";//El formato PNG es mas pesado para utilizar en la nube
	                /*
	                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
	                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
	                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
	                 */
	                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
	                
	                for (int i = 0; i < numberOfPages; ++i) {
	                    File outPutFile = new File(destinationDir + fileName +"_"+ (i+1) +"."+ fileExtension);
	                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
	                    ImageIO.write(bImage, fileExtension, outPutFile);
	                }
	                
	                document.close();
	                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	            } else {
	                System.err.println(sourceFile.getName() +" File not exists");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	 
	 public List<String> convertirPdfToImgListFile(String archivopdf) {
		 List<String> larchivos=new ArrayList<String>();
			try {
	            String sourceDir = pdf_dir+archivopdf;//"0038557743.pdf"; // Pdf files are read from this folder
	            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here

	            File sourceFile = new File(sourceDir);
	            File destinationFile = new File(destinationDir+sourceFile.getName());
	            if (!destinationFile.exists()) {
	                destinationFile.mkdir();
	                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
	            }
	            if (sourceFile.exists()) {
	                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
	                PDDocument document = PDDocument.load(sourceFile);
	                PDFRenderer pdfRenderer = new PDFRenderer(document);
	                
	                int numberOfPages = document.getNumberOfPages();
	                System.out.println("Total files to be converting -> "+ numberOfPages);

	                String fileName = sourceFile.getName().replace(".pdf", "");             
	                String fileExtension= "jpg";//El formato PNG es mas pesado para utilizar en la nube
	                /*
	                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
	                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
	                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
	                 */
	                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
	                
	                for (int i = 0; i < numberOfPages; ++i) {
	                    File outPutFile = new File(destinationDir + fileName +"_"+ (i+1) +"."+ fileExtension);
	                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
	                    ImageIO.write(bImage, fileExtension, outPutFile);
	                    larchivos.add(outPutFile.getAbsolutePath());
	                }
	                
	                document.close();
	                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	                return larchivos;
	            } else {
	                System.err.println(sourceFile.getName() +" File not exists");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return larchivos;
		}
	 
	 public List<String> convertirPdfToImgListFilev2(String rutaarchivopdf) {
		 List<String> larchivos=new ArrayList<String>();
			try {
	            String sourceDir = rutaarchivopdf;//"0038557743.pdf"; // Pdf files are read from this folder
	            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here

	            File sourceFile = new File(sourceDir);
	            File destinationFile = new File(destinationDir+sourceFile.getName().substring(0, sourceFile.getName().indexOf(".")));
	            destinationDir+=sourceFile.getName().substring(0, sourceFile.getName().indexOf("."));
	            if (!destinationFile.exists()) {
	                destinationFile.mkdir();
	                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
	            }
	            if (sourceFile.exists()) {
	                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
	                PDDocument document = PDDocument.load(sourceFile);
	                PDFRenderer pdfRenderer = new PDFRenderer(document);
	                
	                int numberOfPages = document.getNumberOfPages();
	                System.out.println("Total files to be converting -> "+ numberOfPages);

	                String fileName = sourceFile.getName().replace(".pdf", "");             
	                String fileExtension= "jpg";//El formato PNG es mas pesado para utilizar en la nube
	                /*
	                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
	                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
	                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
	                 */
	                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
	                
	                for (int i = 0; i < numberOfPages; ++i) {
	                    File outPutFile = new File(destinationDir +"/"+ fileName +"_"+ (i+1) +"."+ fileExtension);
	                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
	                    ImageIO.write(bImage, fileExtension, outPutFile);
	                    larchivos.add(outPutFile.getAbsolutePath());
	                }
	                
	                document.close();
	                //Path folderPath = Paths.get(destinationDir);
	                //Files.delete(folderPath);
	                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	                return larchivos;
	            } else {
	                System.err.println(sourceFile.getName() +" File not exists");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return larchivos;
		}
	
	 public List<String> convertirPdfSelPagToImgS3(String archivopdf,List<Integer> pagsel) {
		 List<String> larchivos=new ArrayList<String>();
			try {
				
	            String sourceDir = pdf_dir+"0038557743.pdf"; // Pdf files are read from this folder
	            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here
	            //List<Integer> selpaginas=Arrays.asList(1,2);
	            File sourceFile = new File(sourceDir);
	            File destinationFile = new File(destinationDir+sourceFile.getName());
	            if (!destinationFile.exists()) {
	                destinationFile.mkdir();
	                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
	            }
	            if (sourceFile.exists()) {
	                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
	                PDDocument document = PDDocument.load(sourceFile);
	                PDFRenderer pdfRenderer = new PDFRenderer(document);
	              
	                int numberOfPages = document.getNumberOfPages();
	                System.out.println("Total files to be converting -> "+ numberOfPages);
	                
	                String fileName = sourceFile.getName().replace(".pdf", "");             
	                String fileExtension= "jpg";//El formato PNG es pesado para la nube
	                //Subiendo PDF original
	                String folderS3Key = "pdfs/"+fileName+"F-"+formatterFecha.format(new Date())+"/" ;
	                pdfTextractService.uploadPdfToS3(sourceFile.getAbsolutePath(), folderS3Key+sourceFile.getName());
	                /*
	                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
	                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
	                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
	                 */
	                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
	                	for (Integer pag : pagsel) {
	                		for (int i = 0; i < numberOfPages; ++i) {
	                		if (pag==i) {
	                			 File outPutFile = new File(destinationDir + fileName +"_"+ pag +"."+ fileExtension);
	                             BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
	                             ImageIO.write(bImage, fileExtension, outPutFile);
	                             String s3Key = folderS3Key +outPutFile.getName();
	                             pdfTextractService.uploadPdfToS3(outPutFile.getAbsolutePath(), s3Key);	
	                             larchivos.add(outPutFile.getAbsolutePath());
							}
						}
	                }
	                
	                document.close();
	                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	                return larchivos;
	            } else {
	                System.err.println(sourceFile.getName() +" File not exists");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return larchivos;
		}

	 
	public void convertirPdfToImgTest() {
		try {
            String sourceDir = pdf_dir+"0038557743.pdf"; // Pdf files are read from this folder
            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here

            File sourceFile = new File(sourceDir);
            File destinationFile = new File(destinationDir+sourceFile.getName());
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                
                int numberOfPages = document.getNumberOfPages();
                System.out.println("Total files to be converting -> "+ numberOfPages);

                String fileName = sourceFile.getName().replace(".pdf", "");             
                String fileExtension= "jpg";//El formato PNG es mas pesado para utilizar en la nube
                /*
                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                 */
                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
                
                for (int i = 0; i < numberOfPages; ++i) {
                    File outPutFile = new File(destinationDir + fileName +"_"+ (i+1) +"."+ fileExtension);
                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                    ImageIO.write(bImage, fileExtension, outPutFile);
                }
                
                document.close();
                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void convertirPdfToImgS3Test() {
		try {
            String sourceDir = pdf_dir+"0038557743.pdf"; // Pdf files are read from this folder
            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here

            File sourceFile = new File(sourceDir);
            File destinationFile = new File(destinationDir+sourceFile.getName());
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                
                int numberOfPages = document.getNumberOfPages();
                System.out.println("Total files to be converting -> "+ numberOfPages);

                String fileName = sourceFile.getName().replace(".pdf", "");             
                String fileExtension= "jpg";//El formato PNG es pesado para la nube
                /*
                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                 */
                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
                
                for (int i = 0; i < numberOfPages; ++i) {
                    File outPutFile = new File(destinationDir + fileName +"_"+ (i+1) +"."+ fileExtension);
                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                    ImageIO.write(bImage, fileExtension, outPutFile);
                    String s3Key = "pdfs/" +outPutFile.getName();
                    pdfTextractService.uploadPdfToS3(outPutFile.getAbsolutePath(), s3Key);
                }
                
                document.close();
                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void convertirPdfSelPagToImgS3Test( List<Integer> pagsel) {
		try {
            String sourceDir = pdf_dir+"0038557743.pdf"; // Pdf files are read from this folder
            String destinationDir = pdf_dir+""; // converted images from pdf document are saved here
            //List<Integer> selpaginas=Arrays.asList(1,2);
            File sourceFile = new File(sourceDir);
            File destinationFile = new File(destinationDir+sourceFile.getName());
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());             
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
              
                int numberOfPages = document.getNumberOfPages();
                System.out.println("Total files to be converting -> "+ numberOfPages);
                
                String fileName = sourceFile.getName().replace(".pdf", "");             
                String fileExtension= "jpg";//El formato PNG es pesado para la nube
                //Subiendo PDF original
                String folderS3Key = "pdfs/"+fileName+"F-"+formatterFecha.format(new Date())+"/" ;
                pdfTextractService.uploadPdfToS3(sourceFile.getAbsolutePath(), folderS3Key+sourceFile.getName());
                /*
                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                 */
                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi 
                	for (Integer pag : pagsel) {
                		for (int i = 0; i < numberOfPages; ++i) {
                		if (pag==i) {
                			 File outPutFile = new File(destinationDir + fileName +"_"+ pag +"."+ fileExtension);
                             BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                             ImageIO.write(bImage, fileExtension, outPutFile);
                             String s3Key = folderS3Key +outPutFile.getName();
                             pdfTextractService.uploadPdfToS3(outPutFile.getAbsolutePath(), s3Key);	
						}
					}
                }
                
                document.close();
                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public byte[] convertTxtToPdf(String text) throws IOException {
	    try (PDDocument document = new PDDocument()) {
	        String[] lines = text.split("\n");
	        int lineHeight = 15;
	        int yPosition = 650;
	        PDPage page = new PDPage();
	        document.addPage(page);
	        PDPageContentStream contentStream = new PDPageContentStream(document, page);

	        contentStream.setFont(PDType1Font.HELVETICA, 8);
	        contentStream.beginText();
	        contentStream.newLineAtOffset(35, yPosition);

	        for (String line : lines) {
	            if (yPosition <= 40) { // Si llega al final de la página
	                contentStream.endText();
	                contentStream.close();
	                page = new PDPage();
	                document.addPage(page);
	                contentStream = new PDPageContentStream(document, page);
	                // Configurar fuente para la nueva página
	                contentStream.setFont(PDType1Font.HELVETICA, 8); 
	                contentStream.beginText();
	                contentStream.newLineAtOffset(40, 650);
	                yPosition = 700;
	            }
	            contentStream.showText(line);
	            contentStream.newLineAtOffset(0, -lineHeight);
	            yPosition -= lineHeight;
	        }

	        contentStream.endText();
	        contentStream.close();

	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        document.save(byteArrayOutputStream);
	        return byteArrayOutputStream.toByteArray();
	    }
	}
	
	public Path getRuta(String filename) {
		return Paths.get(pdf_dir).resolve(filename).toAbsolutePath();
	}
	
	public void crearCarpetaPdf(String carpeta) {
		try {
                if (Files.notExists(Paths.get(pdf_dir+"/"+carpeta))) {
                    Files.createDirectory(Paths.get(pdf_dir+"/"+carpeta));
                    logg.info("Carpeta pdf Creada!");
                }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
