package com.highfive.artary.domain;

public enum StickerType {
    HAPPY("https://cdn-icons-png.flaticon.com/512/4746/4746825.png"),
    SAD("https://cdn-icons-png.flaticon.com/512/4746/4746825.png"),
    LOVE("https://cdn-icons-png.flaticon.com/512/4746/4746825.png"),
    ANGRY("https://cdn-icons-png.flaticon.com/512/4746/4746825.png");

    private final String url;

    StickerType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
