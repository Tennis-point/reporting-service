package com.tei.tenis.point.reporting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gem {
    private String gemId;
    private String p1Points;
    private String p2Points;
    private String p1Tie;
    private String p2Tie;
    private String winner;
}
