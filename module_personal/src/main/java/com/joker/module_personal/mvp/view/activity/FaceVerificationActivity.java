package com.joker.module_personal.mvp.view.activity;

import android.content.Context;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonres.beans.Order;
import com.example.commonres.utils.FaceAPIUtils;
import com.example.commonres.utils.ToastUtil;
import com.example.commonres.utils.WindowSizeUtil;
import com.example.commonsdk.core.RouterHub;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.joker.module_personal.R2;
import com.joker.module_personal.di.component.DaggerFaceVerificationComponent;
import com.joker.module_personal.mvp.contract.FaceVerificationContract;
import com.joker.module_personal.mvp.presenter.FaceVerificationPresenter;

import com.joker.module_personal.R;


import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 16:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

/**
 * 人脸验证
 */
@Route(path = RouterHub.PERSONAL_FACEVERIFICATIONACTIVITY)
public class FaceVerificationActivity extends BaseActivity<FaceVerificationPresenter>
        implements FaceVerificationContract.View {

    @BindView(R2.id.face_verification)
    Button verification;
    @BindView(R2.id.surfaceView)
    SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private String accessToken;
    private String userId;
    private Order mOrder;
    private String lockAddress;
    private boolean checkOut;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFaceVerificationComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_face_verification;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                accessToken = FaceAPIUtils.getAuth();
            }
        }).start();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        mOrder = (Order) intent.getSerializableExtra("order");
        lockAddress = intent.getStringExtra("lockAddress");
        checkOut = intent.getBooleanExtra("checkOut",false);
    }


    private void initView(){
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolderCallback());

        int surfaceWidth = WindowSizeUtil.getWidth(this) - 400;
        int surfaceHeight = (int) (surfaceWidth * WindowSizeUtil.getProportion(this));
        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams(surfaceWidth,surfaceHeight);
        int left = (WindowSizeUtil.getWidth(this ) - surfaceWidth) / 2;
        int top = (WindowSizeUtil.getHeight(this) - surfaceHeight) / 2 - 150;
        layoutParams.setMargins(left , top , 0 , 0);
        mSurfaceView.setLayoutParams(layoutParams);
    }

    /**
     * 人脸验证返回结果
     * @param result
     */
    @Override
    public void verificationResult(Boolean result) {
        //若人脸识别失败，重新进行识别
        if (result) {
            //若为退房操作，直接进行判断人脸识别结果，否则需要判断
            //蓝牙门锁是否正确连接
            if (!checkOut) {
                //判断连接的门锁是否正确
                if (!mOrder.getHotel().getLockAddress().equals(lockAddress)){
                    Intent intent = new Intent();
                    intent.putExtra("verification",false);
                    setResult(1,intent);
                    ToastUtil.makeText(getViewContext(),"识别失败，连接的门锁错误，请重新连接");
                    finish();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("verification", true);
                    intent.putExtra("order", mOrder);
                    setResult(1, intent);
                    this.finish();
                }
            } else
                mPresenter.checkOut(mOrder);
        }else{
            ToastUtil.makeText(this,"验证失败，请重新验证");
            //开启相机预览
            previceCamera();
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }


    /**
     * 退房操作结果
     * @param result
     * @param tips
     */
    @Override
    public void checkOutResult(Boolean result, String tips) {
        ToastUtil.makeText(getViewContext(),tips);
        finish();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("verification",false);
        setResult(1,intent);
        ToastUtil.makeText(this,"退出识别，取消操作");
        this.finish();
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



    @OnClick({R2.id.surfaceView,R2.id.face_verification,R2.id.back})
    public void onClicked(View view){
        int viewId = view.getId();
        if (viewId == R.id.surfaceView) {
            if (mCamera != null)
                mCamera.autoFocus(null);
        }else if (viewId == R.id.face_verification)
            //进行人脸识别
            startTakePhoto();
        else if (viewId == R.id.back) {
            Intent intent = new Intent();
            intent.putExtra("verification", false);
            setResult(1, intent);
            ToastUtil.makeText(this,"退出识别，取消操作");
            this.finish();
        }
    }


    /**
     * 人脸识别
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
        //拍照进行人脸验证
        mCamera.takePicture(null,null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                mPresenter.verification(accessToken,bytes,"BASE64",
                        "0","LOW","NORMAL",userId,1);

            }
        });
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
