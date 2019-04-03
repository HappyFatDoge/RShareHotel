package com.joker.module_personal.mvp.view.activity;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.User;
import com.example.commonres.utils.FaceAPIUtils;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.WindowSizeUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerFaceRegisterComponent;
import com.joker.module_personal.di.module.FaceRegisterModule;
import com.joker.module_personal.mvp.contract.FaceRegisterContract;
import com.joker.module_personal.mvp.presenter.FaceRegisterPresenter;

import com.joker.module_personal.R;


import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
/**
 * 个人中心Fragment -> 登录 -> 新用户注册 -> 人脸注册
 */
@Route(path = RouterHub.PERSONAL_FACEREGISTERACTIVITY)
public class FaceRegisterActivity extends BaseActivity<FaceRegisterPresenter>
        implements FaceRegisterContract.View {

    @BindView(R2.id.face_register)
    Button faceRegisterButton;
    @BindView(R2.id.surfaceView)
    SurfaceView surfaceView;

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private String token;
    private User mUser;
    private String account;
    private String name;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceRegisterComponent
            .builder()
            .appComponent(appComponent)
            .faceRegisterModule(new FaceRegisterModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_register;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                token = FaceAPIUtils.getAuth();
            }
        }).start();
        mUser = LoginUtil.getInstance().getUser();
        account = mUser.getAccount();
        name = mUser.getName();
    }

    //初始化控件
    private void initView(){
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolderCallback());

        int surfaceWidth = WindowSizeUtil.getWidth(this) - 400;
        int surfaceHeight = (int) (surfaceWidth * WindowSizeUtil.getProportion(this));
        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams(surfaceWidth,surfaceHeight);
        int left = (WindowSizeUtil.getWidth(this ) - surfaceWidth) / 2;
        int top = (WindowSizeUtil.getHeight(this) - surfaceHeight) / 2 - 150;
        layoutParams.setMargins(left , top , 0 , 0);
        surfaceView.setLayoutParams(layoutParams);
    }


    @OnClick({R2.id.surfaceView,R2.id.face_register,R2.id.back})
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.back){
            Toast.makeText(this,"退出人脸注册，人脸验证时需重新注册",Toast.LENGTH_SHORT).show();
            killMyself();
        }else if (viewId == R.id.surfaceView){
            if (mCamera != null)
                mCamera.autoFocus(null);
        }else if (viewId == R.id.face_register)//进行人脸注册
            startTakePhoto();
    }


    /**
     * 人脸注册
     */
    private void startTakePhoto(){
        //获取到相机参数
        Camera.Parameters parameters = mCamera.getParameters();
        //设置图片保存格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //设置图片大小
        parameters.setPreviewSize(480, (int) (480 * WindowSizeUtil.getProportion(this)));
        //设置对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        //拍照进行人脸注册
        mCamera.takePicture(null,null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        mPresenter.registerFace(token, bytes, "BASE64", "0",
                                account, name, "LOW", "NORMAL");
                    }
                }
        );
    }

    /**
     * 人脸注册结果
     * @param result
     */
    @Override
    public void faceRegisterResult(Boolean result) {

    }

    class SurfaceHolderCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            previceCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            mCamera.stopPreview();
            previceCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (mCamera != null){
                mCamera.setPreviewCallback(null);
                //停止预览
                mCamera.stopPreview();
                //释放相机资源
                mCamera.release();
                mCamera = null;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null){
            mCamera = Camera.open(getCameraId());

            if (mSurfaceHolder != null)
                //开启相机预览
                previceCamera();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
            //释放相机资源
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //返回到主页面
        Toast.makeText(this,"退出人脸注册，人脸验证时需重新注册",Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取摄像头开启方向，优先前置
     * @return
     */
    private int getCameraId(){
        int numberOfCamera = Camera.getNumberOfCameras();
        int faceBackId = -1 , faceFrontId = -1;
        for (int i = 0 ; i < numberOfCamera ; ++ i){
            final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            Camera.getCameraInfo(i,cameraInfo);
            //前置摄像头
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                faceFrontId = i;
                //后置摄像头
            else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                faceBackId = i;
        }

        return faceFrontId != -1 ? faceFrontId : faceBackId;
    }


    /**
     * 开启相机预览
     */
    private void previceCamera(){
        try {
            //摄像头设置SurfaceHolder对象，把摄像头与SurfaceHolder进行绑定
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //调整系统相机拍照角度
            mCamera.setDisplayOrientation(90);
            //调用相机预览功能
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
