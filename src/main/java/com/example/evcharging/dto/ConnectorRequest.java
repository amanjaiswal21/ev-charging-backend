package com.example.evcharging.dto;

import com.example.evcharging.model.ConnectorType;
import jakarta.validation.constraints.NotNull;

public record ConnectorRequest(@NotNull ConnectorType type) {}
