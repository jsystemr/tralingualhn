package com.siguasystem.awstextextract.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController {
	
	@RequestMapping(value = {"/","index"})
	public String index() {
		
		return "index";
	}
	
	@RequestMapping(path = {"pdf"})
	public String pdfextract() {
		
		return "pdfextract";
	}
	
	@RequestMapping(path = {"pdfocr"})
	public String pdfocr() {
		
		return "pdfocr";
	}
	
	@RequestMapping(path = {"ocrimagentraducir"})
	public String ocrimagentraducir() {
		return "ocrimagentraducir";
	}

	@RequestMapping(path = {"pdftoimgtotraductor"})
	public String pdftoimgtotraductor() {
		return "pdftoimgtotraductor";
	}
	
	@RequestMapping(path = {"pdftoimgtotraductors3"})
	public String pdftoimgtotraductors3() {
		return "pdftoimgtotraductors3";
	}
	
	@RequestMapping(path = {"pdftoimgtotraductorv1"})
	public String pdftoimgtotraductorv1() {
		return "pdftoimgtotraductorv1";
	}
	
	@RequestMapping(path = {"pdftoimgtotraductorv2"})
	public String pdftoimgtotraductorv2() {
		return "pdftoimgtotraductorv2";
	}
}
