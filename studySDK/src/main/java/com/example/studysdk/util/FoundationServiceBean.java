package com.example.studysdk.util;

import com.google.gson.annotations.SerializedName;

/**
 * 基础服务Bean
 * */
public class FoundationServiceBean {
    @SerializedName("functionName")
    private String functionName;
    @SerializedName("functionCode")
    private String functionCode;
    @SerializedName("expireTime")
    private String expireTime;
    @SerializedName("isActivate")
    private Integer isActivate;
    @SerializedName("expiredFlag")
    private Integer expiredFlag;
    @SerializedName("remarkCN")
    private String remarkCN;
    @SerializedName("remarkEN")
    private String remarkEN;
    @SerializedName("presetClassName")
    private String presetClassName;

    public FoundationServiceBean(){

    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(Integer isActivate) {
        this.isActivate = isActivate;
    }

    public Integer getExpiredFlag() {
        return expiredFlag;
    }

    public void setExpiredFlag(Integer expiredFlag) {
        this.expiredFlag = expiredFlag;
    }

    public String getRemarkCN() {
        return remarkCN;
    }

    public void setRemarkCN(String remarkCN) {
        this.remarkCN = remarkCN;
    }

    public String getRemarkEN() {
        return remarkEN;
    }

    public void setRemarkEN(String remarkEN) {
        this.remarkEN = remarkEN;
    }

    public String getPresetClassName() {
        return presetClassName;
    }

    public void setPresetClassName(String presetClassName) {
        this.presetClassName = presetClassName;
    }
}
