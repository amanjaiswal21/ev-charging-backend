package com.example.evcharging.dto;

import com.example.evcharging.model.ConnectorStatus;
import com.example.evcharging.model.ConnectorType;

public record ConnectorResponse(String id, ConnectorType type, ConnectorStatus status) {}
