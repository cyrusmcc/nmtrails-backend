package com.nmtrails.appcontest.payload.requests;


import javax.validation.constraints.NotNull;

public class AddTrailToHikeListRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long trailId;

    @NotNull
    private String listType;

    private int userRating;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTrailId() {
        return trailId;
    }

    public void setTrailId(Long trailId) {
        this.trailId = trailId;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }
}
