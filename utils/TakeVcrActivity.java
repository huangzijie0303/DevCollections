package com.xinhuamm.sdk.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinhuamm.sdk.R;
import com.xinhuamm.sdk.app.AppConfig;
import com.xinhuamm.sdk.app.base.BaseActivity;
import com.xinhuamm.sdk.util.DateUtils;
import com.xinhuamm.sdk.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 拍摄录像的Activity
 * Created by Xinyi on 2016/10/19/019.
 */

public class TakeVcrActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView vcrSurface;
    private ImageButton ibtn_backVcr;//返回
    private ImageButton ibtn_changeCamVcr;//切换前后像头
    private TextView tv_vcrDuration;//摄像时间
    private ImageButton ibtn_recordVcr;//拍摄录像
    private LinearLayout ll_showRemake;//显示重录菜单
    private Button bt_done;//完成

    private boolean mStartedFlg = false;
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private long duration = 0;//单位秒
    private int mCammeraIndex;//摄像头方向,前置,后置
    private Camera camera;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            duration++;
            tv_vcrDuration.setText(DateUtils.parseLength(duration * 1000));
            timerHandler.postDelayed(timerRunnable, 1000);
            //限制时长为2分钟
            if (duration >= 2 * 60) {
                Toast.makeText(TakeVcrActivity.this, "考虑到您的流量费用,录像限制时长为2分钟!", Toast.LENGTH_LONG).show();
                stopRecord();
                ll_showRemake.setVisibility(View.VISIBLE);
                ibtn_recordVcr.setVisibility(View.GONE);
                ibtn_changeCamVcr.setVisibility(View.VISIBLE);
            }
        }
    };
    private int cameraPosition = 1;
    private String filenamePath;

    public static void show(Context context){

        File file = new File(AppConfig.DEFAULT_SAVE_VIDEO_PATH);//录像存在sd卡
        if (!file.exists() ) {
           if(!file.mkdirs()){
               ToastUtils.showShort(context,"请检查SD卡");
               return;
           }
        }
        Intent intent = new Intent(context, TakeVcrActivity.class);
        context.startActivity(intent);
    }

    public static void show(Context context,int requestCode){
        File file = new File(AppConfig.DEFAULT_SAVE_VIDEO_PATH);//录像存在sd卡
        if (!file.exists() ) {
            if(!file.mkdirs()){
                ToastUtils.showShort(context,"请检查SD卡");
                return;
            }
        }
        Intent intent = new Intent(context, TakeVcrActivity.class);
        ((Activity)context).startActivityForResult(intent,requestCode);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        vcrSurface = (SurfaceView) findViewById(R.id.vcr_surfaceView);

        ibtn_backVcr = (ImageButton) findViewById(R.id.ibtn_back_vcr);
        ibtn_backVcr.setOnClickListener(this);

        ibtn_changeCamVcr = (ImageButton) findViewById(R.id.ibtn_change_camera_vcr);
        ibtn_changeCamVcr.setOnClickListener(this);

        tv_vcrDuration = (TextView) findViewById(R.id.tv_duration_vcr);

        ibtn_recordVcr = (ImageButton) findViewById(R.id.ibtn_record_vcr);
        ibtn_recordVcr.setOnClickListener(this);

        ll_showRemake = (LinearLayout) findViewById(R.id.ll_show_remake);

        bt_done = (Button) findViewById(R.id.bt_done_vcr);
        bt_done.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        if (Build.VERSION.SDK_INT >= 23) {
            permissionCheck();
        }

        mCammeraIndex = findBackCamera();//默认后置像头
        mSurfaceHolder = vcrSurface.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.setKeepScreenOn(true);
    }

    public void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibtn_back_vcr) {
            showStopDialog();
        } else if (v.getId() == R.id.ibtn_change_camera_vcr) {
            changeCamera();
        } else if (v.getId() == R.id.ibtn_record_vcr) {
            if (!mStartedFlg) {
                startRecorder();
                ibtn_changeCamVcr.setVisibility(View.GONE);
            } else {
                if (duration<3){
                    ToastUtils.showShort(TakeVcrActivity.this,"请至少录3秒钟！");
                    return;
                }
                stopRecord();
                ll_showRemake.setVisibility(View.VISIBLE);
                ibtn_recordVcr.setVisibility(View.GONE);
                ibtn_changeCamVcr.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.bt_done_vcr) {
            Intent intent = new Intent();
            intent.putExtra("fileName", filenamePath);
            setResult(RESULT_OK, intent);
            finish();
        }

    }


    private void changeCamera() {
        if (!mStartedFlg) {
            //切换前后摄像头
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

            for (int i = 0; i < cameraCount; i++) {
                Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                if (cameraPosition == 1) {
                    //现在是后置，变更为前置
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                        camera.stopPreview();//停掉原来摄像头的预览
                        camera.release();//释放资源
                        camera = null;//取消原来摄像头
                        camera = Camera.open(i);//打开当前选中的摄像头
                        camera.setDisplayOrientation(90);
                        try {
                            camera.setPreviewDisplay(mSurfaceHolder);//通过surfaceview显示取景画面
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        camera.startPreview();//开始预览
                        cameraPosition = 0;
                        mCammeraIndex = findFrontCamera();
                        break;
                    }
                } else {
                    //现在是前置， 变更为后置
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                        camera.stopPreview();//停掉原来摄像头的预览
                        camera.release();//释放资源
                        camera = null;//取消原来摄像头
                        camera = Camera.open(i);//打开当前选中的摄像头
                        camera.setDisplayOrientation(90);
                        try {
                            camera.setPreviewDisplay(mSurfaceHolder);//通过surfaceview显示取景画面
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        camera.startPreview();//开始预览
                        cameraPosition = 1;
                        mCammeraIndex = findBackCamera();
                        break;
                    }
                }

            }
        }
    }

    private void showStopDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("是否停止录像?" + '\n' + "退出后将无法保存")
                .setPositiveButton("取消", null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopRecord();
                        timerHandler.removeCallbacks(timerRunnable);
                        finish();
                    }
                }).create();
        alertDialog.show();
    }

    //开始录像
    public void startRecorder() {
        if (!mStartedFlg) {
            try {
                // 关闭预览并释放资源
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }
                mRecorder = new MediaRecorder();
                // 声明视频文件对象
                if (!TextUtils.isEmpty(AppConfig.DEFAULT_SAVE_VIDEO_PATH)) {
                    filenamePath = AppConfig.DEFAULT_SAVE_VIDEO_PATH + "/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
                    mRecorder.reset();
                    camera = Camera.open(mCammeraIndex);
                    setCameraParams();//设置相机属性,自动对焦

                    // 设置摄像头预览顺时针旋转90度，才能使预览图像显示为正确的，而不是逆时针旋转90度的。
                    int result;
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        result = (info.orientation + 90) % 360;
                        result = (360 - result) % 360;   // compensate the mirror
                    } else {
                        // back-facing
                        result = 90;
                    }
                    camera.setDisplayOrientation(result);
                    camera.unlock();
                    mRecorder.setCamera(camera); //设置摄像头为相机
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
                    mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P)); //设置视频和声音的编码为系统自带的格式480*720
                    mRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);//设置视频振频率,降低视频质量,减小视频大小

                    mRecorder.setOutputFile(filenamePath);
                    mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface()); // 预览
//                mRecorder.setMaxFileSize(10 * 1024 * 1024); //设置视频文件的最大值为10M,单位B //
//                    mRecorder.setMaxDuration(3 * 60 * 1000);//设置视频的最大时长3分钟，单位毫秒 //
                    mRecorder.setOrientationHint(cameraPosition == 0 ? 270 : 90);//视频旋转90度，
                    mRecorder.prepare(); // 准备录像
                    mRecorder.start(); // 开始录像
                    ibtn_recordVcr.setImageResource(R.mipmap.stop_record);
                    timerHandler.postDelayed(timerRunnable, 1000);
                    mStartedFlg = true; // 改变录制状态为正在录制
                }
            } catch (IOException e1) {
                releaseMediaRecorder();
                timerHandler.removeCallbacks(timerRunnable);
                mStartedFlg = false;
            } catch (IllegalStateException e) {
                releaseMediaRecorder();
                timerHandler.removeCallbacks(timerRunnable);
                mStartedFlg = false;
            }
        }
    }

    //设置自动对焦
    public void setCameraParams() {
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            List<String> list = params.getSupportedFocusModes();
            if (list.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            params.set("orientation", "portrait");
            camera.setParameters(params);
        }
    }


    /**
     * 停止录像
     */
    private void stopRecord() {
        // stop
        if (mStartedFlg && mRecorder != null) {
            try {
                releaseMediaRecorder();
                ibtn_recordVcr.setImageResource(R.mipmap.start_record);
                timerHandler.removeCallbacks(timerRunnable);
                tv_vcrDuration.setText("00:00");
                duration = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mStartedFlg = false; // Set button status flag
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        if (camera == null) {
            int CammeraIndex = findFrontCamera();
            if (CammeraIndex == -1) {
                ibtn_changeCamVcr.setVisibility(View.GONE);
            } else {
                ibtn_changeCamVcr.setVisibility(View.VISIBLE);
            }
            camera = Camera.open(mCammeraIndex);//设置前后摄像头,默认后置
            try {
                camera.setPreviewDisplay(mSurfaceHolder);//录像预览
                camera.setDisplayOrientation(90);
            } catch (IOException e) {
                e.printStackTrace();
                camera.release();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        camera.startPreview();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        vcrSurface = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;
        }
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    //判断前置摄像头是否存在
    private int findFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) { // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    //判断后置摄像头是否存在
    private int findBackCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) { // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    //释放recorder资源
    private void releaseMediaRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showStopDialog();
        }
        return true;
    }

    private void permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissionManifest) {
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_vcr;
    }
}
