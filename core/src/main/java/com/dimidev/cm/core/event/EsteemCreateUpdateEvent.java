package com.dimidev.cm.core.event;

public class EsteemCreateUpdateEvent {

    private Long likerId;
    private Long likedId;
    private boolean esteem;

    public EsteemCreateUpdateEvent() {
    }

    public EsteemCreateUpdateEvent(Long likerId, Long likedId, boolean esteem) {
        this.likerId = likerId;
        this.likedId = likedId;
        this.esteem = esteem;
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

    public boolean isEsteem() {
        return esteem;
    }

    public void setEsteem(boolean esteem) {
        this.esteem = esteem;
    }
}