package com.yalantis.ucrop.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chareem.customCamera.sujaul.ChareemCamera;
import com.chareem.customCamera.sujaul.configuration.CameraConfiguration;
import com.chareem.customCamera.sujaul.ui.model.Media;
import com.chareem.customCamera.sujaul.ui.view.CameraSwitchView;

/**
 * Sample for Sandrios Camera library
 * Created by Arpit Gandhi on 11/8/16.
 */

public class MainActivity extends AppCompatActivity {

    private AppCompatActivity activity;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.withPicker:
                    ChareemCamera
                            .with()
                            .setShowPicker(true)
                            .setVideoFileSize(20)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                            .enableImageCropping(true)
                            .launchCamera(activity);
                    break;
                case R.id.withoutPicker:
                    ChareemCamera
                            .with()
                            .setShowPicker(false)
                            .setShowSettings(false)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                            .enableImageCropping(false)
                            .setTimeStamp(true)
                            .setMockDetection(true)
                            .setCameraFacing(CameraSwitchView.CAMERA_TYPE_FRONT)
                            .launchCamera(activity);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        activity = this;

        findViewById(R.id.withPicker).setOnClickListener(onClickListener);
        findViewById(R.id.withoutPicker).setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK
                && requestCode == ChareemCamera.RESULT_CODE
                && data != null) {
            if (data.getSerializableExtra(ChareemCamera.MEDIA) instanceof Media) {
                Media media = (Media) data.getSerializableExtra(ChareemCamera.MEDIA);

                Log.e("File", "" + media.getPath());
                Log.e("Type", "" + media.getType());
                Toast.makeText(getApplicationContext(), "Media captured.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
