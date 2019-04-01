package com.example.commonres.beans;

import java.util.List;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public class FaceVerificationResult {

    private String face_token;

    private List<FaceUser> user_list;

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public List<FaceUser> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<FaceUser> user_list) {
        this.user_list = user_list;
    }

}
