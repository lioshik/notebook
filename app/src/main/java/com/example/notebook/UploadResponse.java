package com.example.notebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    @Expose
    public String result;
}
