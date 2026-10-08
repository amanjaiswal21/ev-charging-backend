package com.example.evcharging.controller;

import lombok.RequiredArgsConstructor;

import com.example.evcharging.dto.ChargingSessionResponse;
import com.example.evcharging.dto.EndSessionRequest;
import com.example.evcharging.dto.StartSessionRequest;
import com.example.evcharging.mapper.ApiDtoMapper;
import com.example.evcharging.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;
    private final ApiDtoMapper mapper;

    @PostMapping("/start")
    public ChargingSessionResponse start(@Valid @RequestBody StartSessionRequest request) {
        return mapper.toResponse(sessionService.start(mapper.toCommand(request)));
    }

    @PostMapping("/{sessionId}/end")
    public ChargingSessionResponse end(@PathVariable String sessionId,
                                       @Valid @RequestBody EndSessionRequest request) {
        return mapper.toResponse(sessionService.end(sessionId, mapper.toCommand(request)));
    }
}
