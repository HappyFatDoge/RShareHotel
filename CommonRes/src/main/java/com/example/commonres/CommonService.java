package com.example.commonres;

import com.example.commonres.beans.FaceRegisterBean;
import com.example.commonres.beans.FaceVerificationBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 */
public interface CommonService {

    /**
     * 人脸注册
     * @param accessToken
     * @param image
     * @param imageType
     * @param groupId
     * @param userId
     * @param userInfo
     * @param qualityControl
     * @param livenessControl
     * @return
     */
    @FormUrlEncoded
    @POST("faceset/user/add")
    @Headers("Content-Type: application/json")
    Flowable<FaceRegisterBean> registerFace(@Query("access_token")String accessToken,
                                            @Field("image")String image,
                                            @Field("image_type")String imageType,
                                            @Field("group_id")String groupId,
                                            @Field("user_id")String userId,
                                            @Field("user_info")String userInfo,
                                            @Field("quality_control")String qualityControl,
                                            @Field("liveness_control")String livenessControl);


    /**
     * 人脸搜索
     * @param accessToken
     * @param image
     * @param imageType
     * @param groupList
     * @param qualityControl
     * @param livenessControl
     * @param userId
     * @param maxUserNum
     * @return
     */
    @FormUrlEncoded
    @POST("search")
    @Headers("Content-Type: application/json")
    Flowable<FaceVerificationBean> verification(@Query("access_token")String accessToken,
                                                @Field("image")String image,
                                                @Field("image_type")String imageType,
                                                @Field("group_id_list")String groupList,
                                                @Field("quality_control")String qualityControl,
                                                @Field("liveness_control")String livenessControl,
                                                @Field("user_id")String userId,
                                                @Field("max_user_num")int maxUserNum);

}
