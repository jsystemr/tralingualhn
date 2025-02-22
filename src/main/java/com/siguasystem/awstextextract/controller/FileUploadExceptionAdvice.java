package com.siguasystem.awstextextract.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {
@ExceptionHandler(MaxUploadSizeExceededException.class)
public String handleMaxSizeException(MaxUploadSizeExceededException ex, Model model) {
    model.addAttribute("errorMessage", "El archivo excede el tamaño máximo permitido (10MB)");
    return "upload-form"; // Redirige a la vista del formulario de subida
}
}
