package com.example.evcharging.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Connector {
    private String id;
    private ConnectorType type;
    private ConnectorStatus status;

    @Builder
    public Connector(String id, ConnectorType type) {
        this.id = id;
        this.type = type;
        this.status = ConnectorStatus.AVAILABLE;
    }

}
