package com.tei.tenis.point.reporting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tei.tenis.point.reporting.google.GoogleSheet;
import com.tei.tenis.point.reporting.model.Game;
import com.tei.tenis.point.reporting.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ReportingService {

    @Value("${auth}")
    public String AUTH_SERVICE;
    @Value("${tennis}")
    public String TENNIS_SERVICE;

    public User getUser(String userId, String token) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(AUTH_SERVICE + userId))
            .GET()
            .header("Authorization", token).build();
        HttpResponse<String> response = null;
        try {
            log.info("REPORTING -> AUTH :" + request.uri());
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
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(TENNIS_SERVICE + "game/" + userId + "/"))
            .GET()
            .header("Authorization", token).build();

        HttpResponse<String> response = null;

        try {
            log.info("REPORTING -> TENNIS :" + request.uri());
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
            return GoogleSheet.createReport(games, user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Something went wrong.");
        }
    }
}
