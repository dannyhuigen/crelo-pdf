package com.crelo.pdf.service.pages;

import com.crelo.pdf.entities.LijstInvulling;
import com.crelo.pdf.entities.TotalScore;
import com.crelo.pdf.entities.pojos.LLO;
import com.crelo.pdf.service.PictureService;
import com.spire.doc.Document;
import com.spire.doc.collections.SectionCollection;
import com.spire.doc.documents.TextSelection;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.fields.TextRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Comparator;

@Service
public class GoodBadService {

    @Value("${pdfLoc}")
    String pdfloc;

    @Autowired
    PictureService pictureService;

    public SectionCollection getGood(LijstInvulling lijstInvulling, LLO llo) {
        TotalScore[] best = get2Best(lijstInvulling);
        SectionCollection sectionCollection = new SectionCollection();

        for (TotalScore totalScore: best) {
            Document document = new Document(String.format(pdfloc + "templates/good/%s.docx", totalScore.getEigenschap().toLowerCase().replace(" ", "_")));
            if (llo.isLeerling()) {
                replaceWithBar(document, "<leerling>", 1000, 70,
                        (int) lijstInvulling.getScore("leerling", totalScore.getEigenschap().toLowerCase()).getPercentage());
            } else {
                removeText(document, "Leerling  <leerling>");
            }

            if (llo.isLeeraar()) {
                replaceWithBar(document, "<leerkracht>", 1000, 70,
                        (int) lijstInvulling.getScore("leerkracht", totalScore.getEigenschap().toLowerCase()).getPercentage());
            } else {
                removeText(document, "Leerkracht  <leerkracht>");
            }

            if (llo.isOuder()) {
                replaceWithBar(document, "<ouder>", 1000, 70,
                        (int) lijstInvulling.getScore("ouder", totalScore.getEigenschap().toLowerCase()).getPercentage());
            } else {
                removeText(document, "Ouder  <ouder>");
            }

            for (int i = 0; i < document.getSections().getCount(); i++) {
                sectionCollection.add(document.getSections().get(i));
            }
        }
        return sectionCollection;
    }

    public SectionCollection getBad(LijstInvulling lijstInvulling, LLO llo) {
        TotalScore[] worst = get2Worst(lijstInvulling);
        SectionCollection sectionCollection = new SectionCollection();

        for (TotalScore totalScore: worst) {
            Document document = new Document(String.format(pdfloc + "templates/bad/slecht_%s.docx", totalScore.getEigenschap().toLowerCase().replace(" ", "_")));

            if (llo.isLeerling()) {
                replaceWithBar(document, "<leerling>", 1000, 70,
                        (int) lijstInvulling.getScore("leerling", totalScore.getEigenschap().toLowerCase()).getPercentage());
            } else {
                removeText(document, "Leerling  <leerling>");
            }

            if (llo.isLeeraar()) {
                replaceWithBar(document, "<leerkracht>", 1000, 70,
                        (int) lijstInvulling.getScore("leerkracht", totalScore.getEigenschap().toLowerCase()).getPercentage());
            } else {
                removeText(document, "Leerkracht  <leerkracht>");
            }

            if (llo.isOuder()) {
                replaceWithBar(document, "<ouder>", 1000, 70,
                        (int) lijstInvulling.getScore("ouder", totalScore.getEigenschap().toLowerCase()).getPercentage());
            } else {
                removeText(document, "Ouder  <ouder>");
            }

            for (int i = 0; i < document.getSections().getCount(); i++) {
                sectionCollection.add(document.getSections().get(i));
            }
        }
        return sectionCollection;
    }

    public void removeText(Document doc, String replace) {
        try {
            TextSelection textSelection = doc.findString(replace, false, true);
            TextRange range = textSelection.getAsOneRange();
            range.getOwnerParagraph().getChildObjects().remove(range);
        }catch (Exception e) {
            System.out.println("ERROR ON REPLACE " + replace);
            e.printStackTrace();
        }
    }

    public void replaceWithBar(Document doc, String replace, int width, int height, int percentage) {
        try {
            TextSelection textSelection = doc.findString(replace, false, true);
            DocPicture pic = new DocPicture(doc);
            pic.loadImage(pictureService.generateLoadingBarImage(width, height, percentage).get());
            pic.setWidth(100f);
            pic.setHeight(7f);
            pic.setStrokeWeight(0);
            pic.setStrokeColor(Color.WHITE);
            TextRange range = textSelection.getAsOneRange();
            int index = range.getOwnerParagraph().getChildObjects().indexOf(range);
            range.getOwnerParagraph().getChildObjects().insert(index,pic);
            range.getOwnerParagraph().getChildObjects().remove(range);
        }catch (Exception e) {
            System.out.println("ERROR ON REPLACE " + replace);
            e.printStackTrace();
        }
    }

    public TotalScore[] get2Best(LijstInvulling lijstInvulling) {
        return lijstInvulling.getScores().values()
                .stream()
                .sorted(Comparator.comparingDouble(TotalScore::getPercentage).reversed())
                .limit(2)
                .toArray(TotalScore[]::new);
    }

    public TotalScore[] get2Worst(LijstInvulling lijstInvulling) {
        return lijstInvulling.getScores().values()
                .stream()
                .sorted(Comparator.comparingDouble(TotalScore::getPercentage))
                .limit(2)
                .toArray(TotalScore[]::new);
    }
}
