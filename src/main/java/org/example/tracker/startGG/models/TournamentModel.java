package org.example.tracker.startGG.models;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TournamentModel {

    private String id;
    private String name;
    private String countryCode;
    private String startAt;
    private String url;
    private String city;

    public TournamentModel(String id, String name, String countryCode, String startAt, String url, String city) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.startAt = startAt;
        this.url = url;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStartAt() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(startAt)),
                ZoneId.of("Europe/Zurich"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
