package com.example.commonres.beans;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class FaceVerificationBean {

    private int error_code;

    private String error_msg;

    private long log_id;

    private long timestamp;

    private int cached;

    private FaceVerificationResult result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public FaceVerificationResult getResult() {
        return result;
    }

    public void setResult(FaceVerificationResult result) {
        this.result = result;
    }
}
