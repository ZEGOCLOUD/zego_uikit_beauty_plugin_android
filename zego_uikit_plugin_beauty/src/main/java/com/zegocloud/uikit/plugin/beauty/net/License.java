package com.zegocloud.uikit.plugin.beauty.net;

import com.google.gson.annotations.SerializedName;

public class License {


    @SerializedName("License")
    private String license;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
