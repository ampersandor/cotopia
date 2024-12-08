package com.ampersandor.cotopia.dto;

public class LikeRequest {
    private Integer likes;

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "" + likes;
    }
}