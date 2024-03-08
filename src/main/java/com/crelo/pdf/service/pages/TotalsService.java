package com.crelo.pdf.service.pages;

import com.crelo.pdf.entities.LijstInvulling;
import com.crelo.pdf.entities.TotalScore;
import com.crelo.pdf.service.PictureService;
import com.spire.doc.Document;
import com.spire.doc.collections.SectionCollection;
import com.spire.doc.documents.BorderStyle;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ShapeLineStyle;
import com.spire.doc.documents.TextSelection;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.fields.TextRange;
import com.spire.doc.formatting.CharacterFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;

@Service
public class TotalsService {

    @Value("${pdfLoc}")
    String pdfloc;

    @Autowired
    PictureService pictureService;

    public SectionCollection createTotals(LijstInvulling lijstInvulling) {
        // Load a Word document
        Document document = new Document(pdfloc + "templates/totals2.docx");

        // Replace a specific text
        for (Map.Entry<String, TotalScore> set : lijstInvulling.getScoresWithDoubleLeerkrachtWeight().entrySet()) {
            try {
                TextSelection textSelection = document.findString("{{bar(" + set.getKey().toLowerCase().replace(" ", "_") + ")}}", false, true);
                CharacterFormat charFormat = textSelection.getAsOneRange().getCharacterFormat();

                // Load an image
                DocPicture pic = new DocPicture(document);
                pic.loadImage(pictureService.generateLoadingBarImage(1000, 30,
                        (int) lijstInvulling.getScores().get(set.getKey()).getPercentage()).get());
                pic.setWidth(150f);
                pic.setHeight(4f);
                pic.setStrokeColor(Color.WHITE);
                TextRange range = textSelection.getAsOneRange();
                int index = range.getOwnerParagraph().getChildObjects().indexOf(range);

                // Insert the image to the position
                range.getOwnerParagraph().getChildObjects().insert(index, pic);

                // Remove the text
                range.getOwnerParagraph().getChildObjects().remove(range);

                // Set the font for the replaced text
                //charFormat.setFontName("");
                charFormat.setFontSize(12);

            } catch (Exception e) {
                System.out.println("EXCEPTION FOR: " + set.getKey());
            }
        }
        return document.getSections();
    }
}
