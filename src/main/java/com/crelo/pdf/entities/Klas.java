package com.crelo.pdf.entities;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Klas {

    String naam;
    String leraarNaam;

}
