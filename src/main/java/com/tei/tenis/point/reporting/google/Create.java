package com.tei.tenis.point.reporting.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

import java.io.IOException;
import java.security.GeneralSecurityException;

/* Class to demonstrate the use of Spreadsheet Create API */
public class Create {
    /**
     * Create a new spreadsheet.
     *
     * @param title - the name of the sheet to be created.
     * @return newly created spreadsheet id
     * @throws IOException - if credentials file not found.
     */
    public static String createSpreadsheet(String title) throws IOException, GeneralSecurityException {
        // Create the sheets API client
        Sheets service = new Sheets.Builder(new NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            SheetsQuickstart.getCredentials(GoogleNetHttpTransport.newTrustedTransport()))
            .setApplicationName("Sheets samples")
            .build();

        // Create new spreadsheet with a title
        Spreadsheet spreadsheet = new Spreadsheet()
            .setProperties(new SpreadsheetProperties()
                .setTitle(title));
        service.spreadsheets().get("1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms");
        spreadsheet = service.spreadsheets().create(spreadsheet)
            .setFields("spreadsheetId")
            .execute();
        // Prints the new spreadsheet id
        System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
        return spreadsheet.getSpreadsheetId();
    }
}