package com.ampersandor.cotopia.event;

import org.springframework.context.ApplicationEvent;

public class LikeUpdateEvent extends ApplicationEvent {
    private final Long foodId;
    private final int likeCount;

    public LikeUpdateEvent(Object source, Long foodId, int likeCount) {
        super(source);
        this.foodId = foodId;
        this.likeCount = likeCount;
    }

    public Long getFoodId() {
        return foodId;
    }

    public int getLikeCount() {
        return likeCount;
    }
} 