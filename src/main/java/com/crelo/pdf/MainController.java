package com.crelo.pdf;

import com.crelo.pdf.entities.pojos.LLO;
import com.crelo.pdf.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@Controller
public class MainController {

    @Autowired
    PDFService pdfService;

    @Value("${pdfLoc}")
    String pdfloc;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] print(
            @PathVariable String id,
            @RequestParam boolean leerling,
            @RequestParam boolean leraar,
            @RequestParam boolean ouder
    ) {
        try {
            //Create LLO (which entities to export?)
            LLO llo = LLO.builder()
                .leerling(leerling)
                .leeraar(leraar)
                .ouder(ouder)
                .build();
            //Produce a PDF
            pdfService.produce(id, llo);

            //Stream the file
            File f = new File(String.format(pdfloc + "%s.pdf", id));
            InputStream inputStream = new FileInputStream(f);

            //Return this stream, aka download the file
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @GetMapping(value = "/health")
    public @ResponseBody ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("so good");
    }

}
