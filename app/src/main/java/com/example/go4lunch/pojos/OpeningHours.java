package com.example.go4lunch.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public Boolean getOpenNow() {
        return openNow;
    }
}
