package com.example.commonres.beans;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class FaceRegisterResult {

    private String face_token;

    private FaceLocation location;

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public FaceLocation getLocation() {
        return location;
    }

    public void setLocation(FaceLocation location) {
        this.location = location;
    }
}
