package com.tei.tenis.point.reporting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tei.tenis.point.reporting.google.SheetsQuickstart;
import com.tei.tenis.point.reporting.model.Game;
import com.tei.tenis.point.reporting.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ReportingService {

    public static final String AUTH_SERVICE = "http://localhost:8081";
    public static final String GAME_SERVICE = "http://localhost:4000";

    public User getUser(String userId, String token) {
        log.info(token);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(AUTH_SERVICE + "/api/v1/user/" + userId))
            .GET()
            .header("Authorization", token).build();

        log.info(request.toString());

        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(response.body(), User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Game getGame(String userId, String gameId, String token) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GAME_SERVICE + "/game/" + userId + "/"))
            .GET()
            .header("Authorization", token).build();

        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                log.info(response.body());
                return Arrays.stream(objectMapper.readValue(response.body(), Game[].class)).filter(g -> g.getGameId().equals(gameId)).findFirst().get();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public String getReport(List<Game> games, User user) {
        try {
            return SheetsQuickstart.createReport(games, user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Something went wrong.");
        }
    }
}
