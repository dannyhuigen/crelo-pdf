package com.crelo.pdf.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalScore {

    String eigenschap;
    double score;
    double maxScore;
    double percentage;

    public void setScore(double score) {
        this.score = score;
        this.calcPercentage();
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
        this.calcPercentage();
    }

    public void calcPercentage() {
        try {
            double initialPercentage = (score / maxScore) * 100;
            this.percentage = 5 + (((double) 95 / 100) * initialPercentage);
        } catch (Exception e){
            this.percentage = 5;
        }
    }
}
