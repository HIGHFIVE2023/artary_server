package com.highfive.artary.domain;

public enum StickerType {
    goodJob("https://i.ibb.co/LYfYjXd/IMG-0308.png"),
    cheerUp("https://i.ibb.co/jMgLXkg/cheerUp.png"),
    goodLuck("https://i.ibb.co/kMFyCnT/goodLuck.png"),
    perfect("https://i.ibb.co/k0FPwg6/perfect.png");

    private final String url;

    StickerType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
