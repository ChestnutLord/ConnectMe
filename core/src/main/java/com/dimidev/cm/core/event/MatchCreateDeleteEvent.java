package com.dimidev.cm.core.event;

public class MatchCreateDeleteEvent {

    private Long matchId;
    private Long user1Id;
    private Long user2Id;

    public MatchCreateDeleteEvent() {
    }

    public MatchCreateDeleteEvent(Long matchId, Long user1Id, Long user2Id) {
        this.matchId = matchId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }
}
