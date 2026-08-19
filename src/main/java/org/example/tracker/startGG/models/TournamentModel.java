package org.example.tracker.startGG.models;

public class TournamentModel {

    private String id;
    private String name;
    private String countryCode;
    private String startAt;
    private String url;
    private String owner;
    private String game;

    public TournamentModel(String id, String name, String countryCode, String startAt, String url) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.startAt = startAt;
        this.url = url;
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
        return startAt;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
