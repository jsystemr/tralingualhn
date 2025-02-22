package com.siguasystem.awstextextract;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.siguasystem.awstextextract.service.ConvertPDFPagesToImages;

@SpringBootTest
class AwstextextractApplicationTests {
	
	@Autowired
	ConvertPDFPagesToImages cimg;

	@Test
	void contextLoads() {
		//cimg.convertirPdfToImgTest();
		//cimg.convertirPdfToImgS3Test();
        List<Integer> selpaginas=Arrays.asList(0,1);
		cimg.convertirPdfSelPagToImgS3Test(selpaginas);
	}

}
