package com.example.go4lunch.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Detail {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;

    @SerializedName("result")
    @Expose
    private DetailsResult result;

    @SerializedName("status")
    @Expose
    private String status;

    public DetailsResult getResult() {
        return result;
    }

    public void setResult(DetailsResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
