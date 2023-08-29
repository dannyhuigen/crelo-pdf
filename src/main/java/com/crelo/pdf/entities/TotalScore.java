package com.crelo.pdf.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalScore {

    double score;
    double maxScore;

    public double getPercentage() {
        return (score / maxScore) * 100;
    }
}
