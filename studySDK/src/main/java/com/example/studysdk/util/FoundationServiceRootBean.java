package com.example.studysdk.util;

import com.google.gson.annotations.SerializedName;

public class FoundationServiceRootBean {
    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private String data;

    public FoundationServiceRootBean() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
