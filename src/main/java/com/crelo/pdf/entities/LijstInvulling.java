package com.crelo.pdf.entities;

import com.crelo.pdf.entities.pojos.LLO;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
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
    private DocumentReference leerling;
    private Leerling leerlingResolved;
    private ArrayList<VraagInvulling> allInvullingenResolved = new ArrayList<>();

    // Method to resolve the references to actual objects
    public void resolveReferences(Firestore firestore, LLO llo) throws ExecutionException, InterruptedException {
        if (leerlingInvulling != null && llo.isLeerling())  allInvullingen.addAll(leerlingInvulling);
        if (leerkrachtInvulling != null && llo.isLeeraar()) allInvullingen.addAll(leerkrachtInvulling);
        if (ouderInvulling != null && llo.isOuder()) allInvullingen.addAll(ouderInvulling);

        DocumentSnapshot leerlingSnapshot = leerling.get().get();
        if (leerlingSnapshot.exists()) {
            leerlingResolved = leerlingSnapshot.toObject(Leerling.class);
            leerlingResolved.resolveReferences(firestore);
        }

        if (allInvullingen != null) {
            List<ApiFuture<DocumentSnapshot>> futures = new ArrayList<>();
            for (DocumentReference reference : allInvullingen) {
                futures.add(reference.get());
            }
            List<DocumentSnapshot> snapshots = ApiFutures.allAsList(futures).get();

            for (DocumentSnapshot snapshot : snapshots) {
                if (snapshot.exists()) {
                    VraagInvulling vraagInvulling = snapshot.toObject(VraagInvulling.class);
                    allInvullingenResolved.add(vraagInvulling);
                }
            }
        }
    }

    public TotalScore getScore(String invuller, String eigenschap) {
        TotalScore totalScore = new TotalScore();
        for (VraagInvulling vraagInvulling: allInvullingenResolved) {
            if (vraagInvulling.ingevuldDoor.equalsIgnoreCase(invuller)) {
                if (vraagInvulling.eigenschap.equalsIgnoreCase(eigenschap)) {
                    totalScore.setEigenschap(eigenschap);
                    totalScore.setMaxScore(totalScore.getMaxScore() + 4);
                    totalScore.setScore(totalScore.getScore() + vraagInvulling.getInvulling());
                }
            }
        }
        return totalScore;
    }

    //Used for totals
    public HashMap<String, TotalScore> getScores() {
        HashMap<String, TotalScore> scores = new HashMap<>();
        for (VraagInvulling vraagInvulling: allInvullingenResolved) {

            TotalScore score = new TotalScore();
            if (scores.containsKey(vraagInvulling.eigenschap)) {
                score = scores.get(vraagInvulling.eigenschap);
                score.setScore(score.getScore() + vraagInvulling.getInvulling());
                score.setMaxScore(score.getMaxScore() + 4);
                score.setEigenschap(vraagInvulling.eigenschap);
            }
            else {
                score.setMaxScore(4);
                score.setScore(vraagInvulling.invulling);
                score.setEigenschap(vraagInvulling.eigenschap);
                scores.put(vraagInvulling.eigenschap, score);
            }
        }

        //Sort scores
        return scores;
    }

    public HashMap<String, TotalScore> getScoresWithDoubleLeerkrachtWeight() {
        HashMap<String, TotalScore> scores = new HashMap<>();
        for (VraagInvulling vraagInvulling: allInvullingenResolved) {

            //Leerkracht equals to 1/2 of the total score (1/4 leerling and 1/4 ouder totalling 1.0)
            //Looping over leerkracht twice makes the splits 1/4 instead of 1/3
            int loopamount = (vraagInvulling.ingevuldDoor.equals("leerkracht")) ? 2 : 1;
            for (int i = 0; i < loopamount; i++) {
                TotalScore score = new TotalScore();
                if (scores.containsKey(vraagInvulling.eigenschap)) {
                    score = scores.get(vraagInvulling.eigenschap);
                    score.setScore(score.getScore() + vraagInvulling.getInvulling());
                    score.setMaxScore(score.getMaxScore() + 4);
                    score.setEigenschap(vraagInvulling.eigenschap);
                }
                else {
                    score.setMaxScore(4);
                    score.setScore(vraagInvulling.invulling);
                    score.setEigenschap(vraagInvulling.eigenschap);
                    scores.put(vraagInvulling.eigenschap, score);
                }
            }
        }

        //Sort scores
        return scores;
    }
}
