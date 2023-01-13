package com.tei.tenis.point.reporting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private String gameId;
    private String userId;
    private String winner;
    private List<Set> sets;

}
