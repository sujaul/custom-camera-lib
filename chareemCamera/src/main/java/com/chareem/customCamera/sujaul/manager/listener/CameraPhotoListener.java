package com.chareem.customCamera.sujaul.manager.listener;


import java.io.File;

/**
 * Created by Arpit Gandhi on 8/14/16.
 */
public interface CameraPhotoListener {
    void onPhotoTaken(File photoFile);

    void onPhotoTakeError();
}
