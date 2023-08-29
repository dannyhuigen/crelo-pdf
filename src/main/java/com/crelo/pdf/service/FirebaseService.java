package com.crelo.pdf.service;


import com.crelo.pdf.config.ApplicationConfig;
import com.crelo.pdf.entities.LijstInvulling;
import com.google.api.core.ApiFuture;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    @Autowired
    private ApplicationConfig applicationConfig;

    private Set<DocumentReference> visitedReferences = new HashSet<>();

    public Object get(String id) throws ExecutionException, InterruptedException, IOException {
        Credentials credentials = GoogleCredentials.fromStream(
                Objects.requireNonNull(FirebaseService.class.getResourceAsStream("/key.json"))
        );

        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("en-meer")
                .build();

        Firestore dbFirestore = firestoreOptions.getService();
        DocumentReference documentReference =
                dbFirestore.collection("lijstInvulling").document(id);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {

                LijstInvulling lijstInvulling = document.toObject(LijstInvulling.class);
                assert lijstInvulling != null;
                lijstInvulling.resolveReferences(dbFirestore);
                return lijstInvulling;
            } else {
                return null; // Document not found
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


}