package com.crelo.pdf.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VraagInvulling {

    String eigenschap;
    String ingevuldDoor;
    int invulling;

    public int getInvulling() {
        return invulling + 1;
    }
}
