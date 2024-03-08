package com.crelo.pdf.entities.pojos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LLO {

    boolean leeraar;
    boolean leerling;
    boolean ouder;

    public String generateVersie() {
        String versie = "";

        if (leeraar) versie += "leraar, ";
        if (leerling) versie += "leerling, ";
        if (ouder) versie += "ouder  ";

        if (versie.endsWith(", ")) {
            versie = versie.substring(0, versie.length() - 2);
        }
        return versie;
    }
}
