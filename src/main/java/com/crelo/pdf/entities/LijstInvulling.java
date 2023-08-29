package com.crelo.pdf.entities;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LijstInvulling {

    private ArrayList<DocumentReference> leerlingInvulling;
    private ArrayList<DocumentReference> leerkrachtInvulling;
    private ArrayList<DocumentReference> ouderInvulling;
    private ArrayList<DocumentReference> allInvullingen = new ArrayList<>();

    private ArrayList<VraagInvulling> allInvullingenResolved = new ArrayList<>();

    // Method to resolve the references to actual objects
    public void resolveReferences(Firestore firestore) throws ExecutionException, InterruptedException {

        if (leerlingInvulling != null)  allInvullingen.addAll(leerlingInvulling);
        if (leerkrachtInvulling != null) allInvullingen.addAll(leerkrachtInvulling);
        if (ouderInvulling != null) allInvullingen.addAll(ouderInvulling);

        if (allInvullingen != null) {
            for (DocumentReference reference : allInvullingen) {
                DocumentSnapshot snapshot = reference.get().get();
                if (snapshot.exists()) {
                    VraagInvulling vraagInvulling = snapshot.toObject(VraagInvulling.class);
                    allInvullingenResolved.add(vraagInvulling);
                }
            }
        }
    }

    public HashMap<String, TotalScore> getScores() {
        HashMap<String, TotalScore> scores = new HashMap<>();
        for (VraagInvulling vraagInvulling: allInvullingenResolved) {

            TotalScore score = new TotalScore();
            if (scores.containsKey(vraagInvulling.ingevuldDoor)) {
                score = scores.get(vraagInvulling.ingevuldDoor);
                score.setScore(score.getScore() + vraagInvulling.getInvulling());
                score.setMaxScore(score.getMaxScore() + 4);
            }
            else {
                score.setMaxScore(4);
                score.setScore(vraagInvulling.invulling);
                scores.put(vraagInvulling.ingevuldDoor, score);
            }
        }
        return scores;
    }
}
