package com.example.kakaoonboarding.dto;

public class DistanceResponse {
    private String origin;
    private String destination;
    private Coordinates originCoordinates;
    private Coordinates destinationCoordinates;
    private int distanceInMeters;
    private int durationInSeconds;

    public DistanceResponse() {
    }

    public DistanceResponse(String origin, String destination, Coordinates originCoordinates,
                           Coordinates destinationCoordinates, int distanceInMeters, int durationInSeconds) {
        this.origin = origin;
        this.destination = destination;
        this.originCoordinates = originCoordinates;
        this.destinationCoordinates = destinationCoordinates;
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Coordinates getOriginCoordinates() {
        return originCoordinates;
    }

    public void setOriginCoordinates(Coordinates originCoordinates) {
        this.originCoordinates = originCoordinates;
    }

    public Coordinates getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public void setDestinationCoordinates(Coordinates destinationCoordinates) {
        this.destinationCoordinates = destinationCoordinates;
    }

    public int getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(int distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
}
