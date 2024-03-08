package com.crelo.pdf.entities;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.*;

import java.util.concurrent.ExecutionException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Leerling {

    String naam;
    private DocumentReference klas;
    private Klas klasResolved;

    DocumentReference school;
    School schoolResolved;

    public void resolveReferences(Firestore firestore) throws ExecutionException, InterruptedException {
        DocumentSnapshot klasSnapshot = klas.get().get();
        if (klasSnapshot.exists()) {
            klasResolved = klasSnapshot.toObject(Klas.class);
        }

        DocumentSnapshot snapshot = school.get().get();
        if (snapshot.exists()) {
            schoolResolved = snapshot.toObject(School.class);
        }

    }
}
