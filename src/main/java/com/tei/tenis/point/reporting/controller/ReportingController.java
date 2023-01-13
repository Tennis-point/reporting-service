package com.tei.tenis.point.reporting.controller;

import com.tei.tenis.point.reporting.model.Game;
import com.tei.tenis.point.reporting.model.User;
import com.tei.tenis.point.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping(value = "/report/{userId}/")
    public String getReport(@PathVariable String userId, HttpServletRequest request) {
        log.info("trying to report for : " + userId);
        String token = request.getHeader("Authorization");
        User user = reportingService.getUser(userId, token);
        log.info(String.valueOf(user));
        Game game = reportingService.getGame(user.getUserId(), token);
        log.info(String.valueOf(game));
        return reportingService.getReport(List.of(game), user);
    }

}
