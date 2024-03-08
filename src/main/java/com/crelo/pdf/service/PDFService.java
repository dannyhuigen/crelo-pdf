package com.crelo.pdf.service;


import com.crelo.pdf.entities.LijstInvulling;
import com.crelo.pdf.entities.TotalScore;
import com.crelo.pdf.entities.pojos.LLO;
import com.crelo.pdf.service.pages.GoodBadService;
import com.crelo.pdf.service.pages.TotalsService;
import com.crelo.pdf.service.pages.VoorbladService;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.collections.SectionCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Service responsible for generating the pdf itself
 */
@Service
public class PDFService {

    @Value("${pdfLoc}")
    String pdfloc;

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    PictureService pictureService;

    @Autowired
    GoodBadService goodBadService;

    @Autowired
    VoorbladService voorbladService;

    @Autowired
    TotalsService totalsService;

    public void produce(String id, LLO llo) throws IOException, ExecutionException, InterruptedException {
        LijstInvulling lijstInvulling = firebaseService.get(id, llo);
        Document document = new Document();
        addSections(voorbladService.getVoorbladen(lijstInvulling, llo), document);
        addSections(goodBadService.getGood(lijstInvulling, llo), document);
        addSections(goodBadService.getBad(lijstInvulling, llo), document);
        addSections(totalsService.createTotals(lijstInvulling), document);
        document.saveToFile(String.format(pdfloc + "%s.docx", id), FileFormat.Docx);
        convertToPdf(String.format(pdfloc + "%s.docx", id));
    }

    public Document addSections(SectionCollection sectionCollection, Document document) {
        for (int i = 0; i < sectionCollection.getCount(); i++) {
            document.getSections().add(sectionCollection.get(i).deepClone());
        }
        return document;
    }

    public void convertToPdf(String docxFile) throws IOException {
        String outputDir = pdfloc;
        try {
            ArrayList<String> command = new ArrayList<>();
            command.add("libreoffice");
            command.add("--headless");
            command.add("--convert-to");
            command.add("pdf");
            command.add(docxFile);
            command.add("--outdir");
            command.add(outputDir);

            // Create a process builder and start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Wait for the process to finish
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Conversion completed successfully.");
            } else {
                System.err.println("Conversion failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
