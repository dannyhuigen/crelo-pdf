package com.crelo.pdf.service;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class PDFService {

    @Autowired FirebaseService firebaseService;

    @PostConstruct
    public void test() throws IOException, ExecutionException, InterruptedException {

        PdfReader reader = new PdfReader("src/main/resources/pdf_templates/template.pdf");
        PdfWriter writer = new PdfWriter("src/main/resources/pdf_templates/template-modified.pdf");
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        addContentToDocument(pdfDocument);
        pdfDocument.close();

        generateFromLijstInvulling("IrkF2rsoRHR8gYAjy8nS");
    }

    public void generateFromLijstInvulling(String lijstInvullingId) throws ExecutionException, InterruptedException, IOException {
        firebaseService.get(lijstInvullingId);
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
