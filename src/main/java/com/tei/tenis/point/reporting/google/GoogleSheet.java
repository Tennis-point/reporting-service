package com.tei.tenis.point.reporting.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.tei.tenis.point.reporting.model.Game;
import com.tei.tenis.point.reporting.model.Set;
import com.tei.tenis.point.reporting.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleSheet {

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static Credential cred;

    public static String createReport(List<Game> games, User user) throws GeneralSecurityException, IOException {
        if (cred == null) {
            cred = GoogleAuthorizeUtil.authorize();
        }

        Sheets service = getService(GoogleNetHttpTransport.newTrustedTransport());
        Spreadsheet spreadsheet = new Spreadsheet()
            .setProperties(new SpreadsheetProperties().setTitle(
                "Games: " + user.getUsername() + " - " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        Spreadsheet created = create(service, spreadsheet);
        String id = created.getSpreadsheetId();

        Game game = games.get(0);
        List<Object> gameRow = Arrays.asList("GAME", game.getGameId());

        List<Set> sets = game.getSets();
        List<List<String>> setRows = sets.stream().map(s -> Arrays.asList("SET " + (sets.indexOf(s) + 1), s.getSetId())).toList();

        List<List<Object>> values = getValuesToParse(sets, gameRow, setRows);

        ValueRange body = new ValueRange()
            .setValues(
                values
            );

        service.spreadsheets().values().update(id, "A2", body)
            .setValueInputOption("RAW")
            .execute();

        return "https://docs.google.com/spreadsheets/d/" + id;
    }

    private static List<List<Object>> getValuesToParse(List<Set> sets, List<Object> gameRow, List<List<String>> setRows) {
        // game id | empty
        // set x | empty
        // gem x | empty
        // 40 | 15
        List<List<Object>> values = new ArrayList<>(Arrays.asList(
            gameRow
        ));

        for (int i = 0; i < setRows.size(); i++) {
            List<Object> e = Collections.singletonList(setRows.get(i));
            values.add((List<Object>) e.get(0));
            sets.get(i).getGems().stream().map(
                g -> Arrays.asList(g.getP1Points(), g.getP2Points())
            ).forEach(
                row -> {
                    List<Object> e1 = Collections.singletonList(row);
                    values.add((List<Object>) e1.get(0));
                }
            );
        }
        return values;
    }

    private static Sheets getService(NetHttpTransport HTTP_TRANSPORT) throws IOException, GeneralSecurityException {
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }

    private static Spreadsheet create(Sheets service, Spreadsheet spreadsheet) throws IOException {
        return service.spreadsheets().create(spreadsheet).execute();
    }
}
