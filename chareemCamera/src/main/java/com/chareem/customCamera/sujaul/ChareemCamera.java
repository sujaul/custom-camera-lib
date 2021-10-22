package com.chareem.customCamera.sujaul;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.chareem.customCamera.sujaul.configuration.CameraConfiguration;
import com.chareem.customCamera.sujaul.ui.camera.Camera1Activity;
import com.chareem.customCamera.sujaul.ui.camera2.Camera2Activity;
import com.chareem.customCamera.sujaul.ui.view.CameraSwitchView;
import com.chareem.customCamera.sujaul.utils.CameraHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Sandrios Camera Builder Class
 * Created by Arpit Gandhi on 7/6/16.
 */
public class ChareemCamera {

    public static int RESULT_CODE = 956;
    public static String MEDIA = "media";
    private static ChareemCamera mInstance = null;
    private int mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
    private boolean showPicker = true;
    private boolean showSetting = true;
    private boolean autoRecord = false;
    private boolean isUseTimestamp = false;
    private boolean isUseMockDetection = false;
    private boolean enableImageCrop = false;
    private String lat = "";
    private String lon = "";
    private long videoSize = -1;
    public static String filePath = Environment.getExternalStorageDirectory().getPath()+"/ChareemCamera/";
    public static String fileNames = null;
    public @CameraSwitchView.CameraType int cameraType = CameraSwitchView.CAMERA_TYPE_REAR;

    public static ChareemCamera with() {
        /*if (mInstance == null) {
            mInstance = new CharemCamera();
        }*/
        mInstance = new ChareemCamera();
        filePath = Environment.getExternalStorageDirectory().getPath()+"/ChareemCamera/";
        fileNames = null;
        return mInstance;
    }

    public ChareemCamera setFileDirectory(String captureDirectory) {
        if (!captureDirectory.equals("") && captureDirectory != null)
            filePath = Environment.getExternalStorageDirectory().getPath()+"/"+captureDirectory+"/";
        return mInstance;
    }

    public ChareemCamera setFileName(String fileName) {
        fileNames = fileName;
        return mInstance;
    }

    public ChareemCamera setShowPicker(boolean showPicker) {
        this.showPicker = showPicker;
        return mInstance;
    }

    public ChareemCamera setShowSettings(boolean showSetting) {
        this.showSetting = showSetting;
        return mInstance;
    }

    public ChareemCamera setTimeStamp(boolean isUseTimestamp) {
        this.isUseTimestamp = isUseTimestamp;
        return mInstance;
    }

    public ChareemCamera setMockDetection(boolean isUseMockDetection) {
        this.isUseMockDetection = isUseMockDetection;
        return mInstance;
    }

    public ChareemCamera setDevaultPosition(String latitude, String longitude) {
        if (latitude != null){
            this.lat = latitude;
        }
        if (longitude != null){
            this.lon = longitude;
        }
        return mInstance;
    }

    public ChareemCamera setCameraFacing(@CameraSwitchView.CameraType int cameraType) {
        this.cameraType = cameraType;
        return mInstance;
    }

    public ChareemCamera setMediaAction(int mediaAction) {
        this.mediaAction = mediaAction;
        return mInstance;
    }

    public ChareemCamera enableImageCropping(boolean enableImageCrop) {
        this.enableImageCrop = enableImageCrop;
        return mInstance;
    }

    @SuppressWarnings("SameParameterValue")
    public ChareemCamera setVideoFileSize(int fileSize) {
        this.videoSize = fileSize;
        return mInstance;
    }

    /**
     * Only works if Media Action is set to Video
     */
    public ChareemCamera setAutoRecord() {
        autoRecord = true;
        return mInstance;
    }

    public void launchCamera(final Activity activity) {
        if (CameraHelper.hasCamera(activity)) {
            ArrayList<String> permissions = new ArrayList<>();
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (mediaAction != CameraConfiguration.MEDIA_ACTION_PHOTO) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (isUseTimestamp){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            Dexter.withActivity(activity)
                    .withPermissions(permissions)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            Intent cameraIntent;
                            if (CameraHelper.hasCamera2(activity)) {
                                cameraIntent = new Intent(activity, Camera2Activity.class);
                            } else {
                                cameraIntent = new Intent(activity, Camera1Activity.class);
                            }
                            cameraIntent.putExtra(CameraConfiguration.Arguments.SHOW_PICKER, showPicker);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.SHOW_SETTING, showSetting);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.MEDIA_ACTION, mediaAction);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.ENABLE_CROP, enableImageCrop);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.AUTO_RECORD, autoRecord);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.SHOW_LOCATION, isUseTimestamp);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.MOCK_LOCATION, isUseMockDetection);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.DEVAULT_LAT, lat);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.DEVAULT_LON, lon);
                            cameraIntent.putExtra(CameraConfiguration.Arguments.IS_PERMISSIONS_GRANTED, report.areAllPermissionsGranted());
                            cameraIntent.putExtra(CameraConfiguration.Arguments.CAMERA_FACING_TYPE, cameraType);

                            if (videoSize > 0) {
                                cameraIntent.putExtra(CameraConfiguration.Arguments.VIDEO_FILE_SIZE, videoSize * 1024 * 1024);
                            }
                            activity.startActivityForResult(cameraIntent, RESULT_CODE);
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    Toast.makeText(activity, "Error permission occurred! ", Toast.LENGTH_SHORT).show();
                }
            }).onSameThread().check();
        }
    }

    public class MediaType {
        public static final int PHOTO = 0;
        public static final int VIDEO = 1;
    }
}
