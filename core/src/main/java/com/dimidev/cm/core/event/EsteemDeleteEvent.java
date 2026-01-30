package com.dimidev.cm.core.event;

public class EsteemDeleteEvent {

    private Long likerId;
    private Long likedId;

    public EsteemDeleteEvent() {
    }

    public EsteemDeleteEvent(Long likerId, Long likedId) {
        this.likerId = likerId;
        this.likedId = likedId;
    }

    public Long getLikerId() {
        return likerId;
    }

    public void setLikerId(Long likerId) {
        this.likerId = likerId;
    }

    public Long getLikedId() {
        return likedId;
    }

    public void setLikedId(Long likedId) {
        this.likedId = likedId;
    }
}
