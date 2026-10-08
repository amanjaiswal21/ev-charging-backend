package com.example.evcharging.controller;

import com.example.evcharging.dto.EndSessionRequest;
import com.example.evcharging.dto.StartSessionRequest;
import com.example.evcharging.model.ChargingSession;
import com.example.evcharging.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/start")
    public ChargingSession start(@Valid @RequestBody StartSessionRequest request) {
        return sessionService.start(request);
    }

    @PostMapping("/{sessionId}/end")
    public ChargingSession end(@PathVariable String sessionId,
                               @Valid @RequestBody EndSessionRequest request) {
        return sessionService.end(sessionId, request);
    }
}
