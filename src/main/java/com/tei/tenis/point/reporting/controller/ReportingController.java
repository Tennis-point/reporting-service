package com.tei.tenis.point.reporting.controller;

import com.tei.tenis.point.reporting.google.GoogleAuthorizeUtil;
import com.tei.tenis.point.reporting.google.GoogleSheet;
import com.tei.tenis.point.reporting.model.Game;
import com.tei.tenis.point.reporting.model.User;
import com.tei.tenis.point.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping(value = "/report/{userId}/game/{gameId}/")
    public String getReport(@PathVariable String userId, @PathVariable String gameId, HttpServletRequest request) {
        log.info("REPORTING GET getReport: " + request.getRequestURI());
        String token = request.getHeader("Authorization");
        User user = reportingService.getUser(userId, token);
        Game game = reportingService.getGame(user.getUserId(), gameId, token);
        return reportingService.getReport(List.of(game), user);
    }

    @GetMapping("/url")
    public String authorize() throws GeneralSecurityException, IOException {
        GoogleSheet.cred = null;
        return GoogleAuthorizeUtil.getUrl();
    }


}
