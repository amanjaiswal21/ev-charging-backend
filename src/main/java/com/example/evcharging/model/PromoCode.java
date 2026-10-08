package com.example.evcharging.model;

public class PromoCode {
    private String code;
    private PromoType type;
    private double value;
    private boolean active;

    public PromoCode() {}

    public PromoCode(String code, PromoType type, double value, boolean active) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.active = active;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public PromoType getType() { return type; }
    public void setType(PromoType type) { this.type = type; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
