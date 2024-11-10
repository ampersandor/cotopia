package com.ampersandor.leettrack.dto;

public class LikeRequest {
    private Long likes;

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "" + likes;
    }
}