package com.crelo.pdf.service;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFService {

    @PostConstruct
    public void test() throws IOException {

        PdfReader reader = new PdfReader("src/main/resources/template.pdf");
        PdfWriter writer = new PdfWriter("src/main/resources/template-modified.pdf");
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        addContentToDocument(pdfDocument);
        pdfDocument.close();

    }

    public void addContentToDocument(PdfDocument pdfDocument) throws IOException {
        PdfFont font = PdfFontFactory.createFont();

        PdfPage lastPage = pdfDocument.getLastPage();
        PdfCanvas canvas = new PdfCanvas(lastPage);

        canvas.beginText()
                .setFontAndSize(font, 12)
                .moveText(100, 100) // Adjust the coordinates as needed
                .showText("Hello, World!")
                .endText();

        canvas.release();
    }
}
