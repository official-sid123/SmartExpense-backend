package com.example.demo.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OcrService {

    public String extractText(File imageFile) {
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("src/main/resources/tessdata");  // path correct
            tesseract.setLanguage("eng");
            return tesseract.doOCR(imageFile);

        } catch (Exception e) {
            return "OCR_ERROR: " + e.getMessage();
        }
    }
}
