package com.example.leng.myapplication2.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.leng.myapplication2.R;

import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.kingleng.baselibrary.face.CameraMatrix;
import com.kingleng.baselibrary.face.ConUtil;
import com.kingleng.baselibrary.face.DialogUtil;
import com.kingleng.baselibrary.face.ICamera;
import com.kingleng.baselibrary.face.OpenGLUtil;
import com.kingleng.baselibrary.face.Screen;
import com.kingleng.baselibrary.face.bean.FaceActionInfo;

import static android.os.Build.VERSION_CODES.M;

public class CameraActivity extends AppCompatActivity implements Camera.PreviewCallback, GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{

    private int printTime = 31;
    private GLSurfaceView mGlSurfaceView;
    private ICamera mICamera;
    private Camera mCamera;

    private boolean isBackCamera = false;

    private FaceActionInfo faceActionInfo;

    private int screenWidth;
    private int screenHeight;

    long startTime;
    private HashMap<String, Integer> resolutionMap;

    private DialogUtil mDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screen.initialize(this);
        setContentView(R.layout.activity_camera);

        init();

        ConUtil.toggleHideyBar(this);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;


    }

    private void requestCameraPerm() {
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else
                resume();
        } else
            resume();
    }

    public static final int EXTERNAL_STORAGE_REQ_CAMERA_CODE = 10;
    public static final int EXTERNAL_STORAGE_REQ_AUDIO_CODE = 11;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CAMERA_CODE)
            resume();
        if ((requestCode == EXTERNAL_STORAGE_REQ_AUDIO_CODE) && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            resume();
    }


    private void init() {
        if (android.os.Build.MODEL.equals("PLK-AL10"))
            printTime = 50;

        faceActionInfo = (FaceActionInfo) getIntent().getSerializableExtra("FaceAction");

//        isStartRecorder = faceActionInfo.isStartRecorder;
//        is3DPose = faceActionInfo.is3DPose;
//        isDebug = faceActionInfo.isdebug;
//        isROIDetect = faceActionInfo.isROIDetect;
//        is106Points = faceActionInfo.is106Points;
//        isBackCamera = faceActionInfo.isBackCamera;
//        isFaceProperty = faceActionInfo.isFaceProperty;
//        isOneFaceTrackig = faceActionInfo.isOneFaceTrackig;
//        isFaceCompare = faceActionInfo.isFaceCompare;
//        trackModel = faceActionInfo.trackModel;
//
//        min_face_size = faceActionInfo.faceSize;
//        detection_interval = faceActionInfo.interval;
//        resolutionMap = faceActionInfo.resolutionMap;
//
//        facepp = new Facepp();
//
//        sensorUtil = new SensorEventUtil(this);
//
//        mHandlerThread.start();
//        mHandler = new Handler(mHandlerThread.getLooper());

        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.opengl_layout_surfaceview);
        mGlSurfaceView.setEGLContextClientVersion(2);// 创建一个OpenGL ES 2.0
        // context
        mGlSurfaceView.setRenderer(this);// 设置渲染器进入gl
        // RENDERMODE_CONTINUOUSLY不停渲染
        // RENDERMODE_WHEN_DIRTY懒惰渲染，需要手动调用 glSurfaceView.requestRender() 才会进行更新
        mGlSurfaceView.setRenderMode(mGlSurfaceView.RENDERMODE_WHEN_DIRTY);// 设置渲染器模式
        mGlSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFocus();
            }
        });

        mICamera = new ICamera();
        mDialogUtil = new DialogUtil(this);

    }

    private void autoFocus() {
        if (mCamera != null && isBackCamera) {
            mCamera.cancelAutoFocus();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            mCamera.autoFocus(null);
        }
    }

    private int Angle;

    private void resume(){
        ConUtil.acquireWakeLock(this);
        startTime = System.currentTimeMillis();
        mCamera = mICamera.openCamera(isBackCamera, this, resolutionMap);
        if (mCamera != null) {
            Angle = 360 - mICamera.Angle;
            if (isBackCamera)
                Angle = mICamera.Angle;

            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam();
            mGlSurfaceView.setLayoutParams(layout_params);

//            int width = mICamera.cameraWidth;
//            int height = mICamera.cameraHeight;
//
//            int left = 0;
//            int top = 0;
//            int right = width;
//            int bottom = height;
//            if (isROIDetect) {
//                float line = height * roi_ratio;
//                left = (int) ((width - line) / 2.0f);
//                top = (int) ((height - line) / 2.0f);
//                right = width - left;
//                bottom = height - top;
//            }
//
//            String errorCode = facepp.init(this, ConUtil.getFileContent(this, R.raw.megviifacepp_0_5_2_model), isOneFaceTrackig ? 1 : 0);
//
//            //sdk内部其他api已经处理好，可以不判断
//            if (errorCode!=null){
//                Intent intent=new Intent();
//                intent.putExtra("errorcode",errorCode);
//                setResult(101,intent);
//                finish();
//                return;
//            }

//            Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
//            faceppConfig.interval = detection_interval;
//            faceppConfig.minFaceSize = min_face_size;
//            faceppConfig.roi_left = left;
//            faceppConfig.roi_top = top;
//            faceppConfig.roi_right = right;
//            faceppConfig.roi_bottom = bottom;
//            String[] array = getResources().getStringArray(R.array.trackig_mode_array);
//            if (trackModel.equals(array[0]))
//                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_FAST;
//            else if (trackModel.equals(array[1]))
//                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_ROBUST;
//            else if (trackModel.equals(array[2])) {
//                faceppConfig.detectionMode = Facepp.FaceppConfig.MG_FPP_DETECTIONMODE_TRACK_RECT;
//                isShowFaceRect = true;
//            }
//
//
//            facepp.setFaceppConfig(faceppConfig);
//
//            String version = facepp.getVersion();
//            Log.d("ceshi", "onResume:version:" + version);
        } else {
            mDialogUtil.showDialog(getResources().getString(R.string.camera_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCameraPerm();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ConUtil.releaseWakeLock();

        mICamera.closeCamera();
        mCamera = null;



        finish();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mGlSurfaceView.requestRender();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        // 黑色背景
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        surfaceInit();
    }

    private int mTextureID = -1;
    private SurfaceTexture mSurface;
    private CameraMatrix mCameraMatrix;

    private boolean flip = true;

    private void surfaceInit() {
        mTextureID = OpenGLUtil.createTextureID();

        mSurface = new SurfaceTexture(mTextureID);

        // 这个接口就干了这么一件事，当有数据上来后会进到onFrameAvailable方法
        mSurface.setOnFrameAvailableListener(this);// 设置照相机有数据时进入
        mCameraMatrix = new CameraMatrix(mTextureID);
        mICamera.startPreview(mSurface);// 设置预览容器
        mICamera.actionDetect(this);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        // 设置画面的大小
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        ratio = 1; // 这样OpenGL就可以按照屏幕框来画了，不是一个正方形了

//        // this projection matrix is applied to object coordinates
//        // in the onDrawFrame() method
//        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
//        // Matrix.perspectiveM(mProjMatrix, 0, 0.382f, ratio, 3, 700);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        final long actionTime = System.currentTimeMillis();
//		Log.w("ceshi", "onDrawFrame===");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);// 清除屏幕和深度缓存
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);
        mCameraMatrix.draw(mtx);
//        // Set the camera position (View matrix)
////        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1f, 0f);
//        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, -1f, 0f, 0f);
//
//        // Calculate the projection and view transformation
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
//
//        mPointsMatrix.draw(mMVPMatrix);


        mSurface.updateTexImage();// 更新image，会调用onFrameAvailable方法

    }
}
