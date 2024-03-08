package com.crelo.pdf.service.pages;

import com.crelo.pdf.entities.LijstInvulling;
import com.crelo.pdf.entities.pojos.LLO;
import com.spire.doc.Document;
import com.spire.doc.collections.SectionCollection;
import com.spire.doc.documents.TextSelection;
import com.spire.doc.formatting.CharacterFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class VoorbladService {

    @Value("${pdfLoc}")
    String pdfloc;

    String VOORBLAD_LOCATION =  "templates/voorblad.docx";

    public SectionCollection getVoorbladen(LijstInvulling lijstInvulling, LLO llo) {

        Document document = new Document(pdfloc + VOORBLAD_LOCATION);

        TextSelection textSelection = document.findString("{{leerlingnaam}}", true, true);
        if (textSelection != null) {
            CharacterFormat charFormat = textSelection.getAsOneRange().getCharacterFormat();
            String customFontName = charFormat.getFontName();
            float customFontSize = charFormat.getFontSize();
            textSelection.getAsOneRange().setText(lijstInvulling.getLeerlingResolved().getNaam());
            charFormat.setFontName(customFontName);
            charFormat.setFontSize(customFontSize);
        }

        textSelection = document.findString("{{klas}}", true, true);
        if (textSelection != null){
            CharacterFormat charFormat = textSelection.getAsOneRange().getCharacterFormat();
            String customFontName = charFormat.getFontName();
            float customFontSize = charFormat.getFontSize();
            textSelection.getAsOneRange().setText(lijstInvulling.getLeerlingResolved().getKlasResolved().getNaam());
            charFormat.setFontName(customFontName);
            charFormat.setFontSize(customFontSize);
        }

        textSelection = document.findString("{{schoolnaam}}", true, true);
        if (textSelection != null){
            CharacterFormat charFormat = textSelection.getAsOneRange().getCharacterFormat();
            String customFontName = charFormat.getFontName();
            float customFontSize = charFormat.getFontSize();
            textSelection.getAsOneRange().setText(lijstInvulling.getLeerlingResolved().getSchoolResolved().getNaam());
            charFormat.setFontName(customFontName);
            charFormat.setFontSize(customFontSize);
        }

        textSelection = document.findString("{{versie}}", true, true);
        if (textSelection != null){
            CharacterFormat charFormat = textSelection.getAsOneRange().getCharacterFormat();
            String customFontName = charFormat.getFontName();
            float customFontSize = charFormat.getFontSize();
            textSelection.getAsOneRange().setText(llo.generateVersie());
            charFormat.setFontName(customFontName);
            charFormat.setFontSize(customFontSize);
        }

        return document.getSections();
    }

}
