package com.tangl.tanglapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScanLabelActivity extends AppCompatActivity{
    private static final String TAG = "TANGL_LABEL_SCAN";
    private FloatingActionButton mSnapPhotoButton;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private ProgressBar mProgressBar;
    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }
    private TextureView mCameraPreview;
    private String mCameraID;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder previewCaptureRequest;
    private ImageReader mImageReader;
    private Image mImage;
    private static final Integer REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundCameraThread;
    private Size mPreviewSize;
    private CameraManager mCameraManager;
    private Integer REQUEST_CODE = 3489;
    private Integer mSensorOrientation;
    private CameraCharacteristics mCameraCharacteristics;
    private IngredientsListManager mIngredientsListManager;
    private int mOrientationDegrees; // to find out the orientation to give the firebasevision function
    private Boolean mPreviewPhotoMode = false; // if true when user presses back goes to preview mode instead of the previous activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);
        mCameraPreview = findViewById(R.id.camera_preview);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        assert mCameraPreview != null;
        mCameraPreview.setSurfaceTextureListener(mCameraPreviewListener);
        mSnapPhotoButton = findViewById(R.id.snap_photo_button);
        assert mSnapPhotoButton != null;
        mSnapPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                takePicture();
            }
        });

        startBackgroundThread();

        OrientationEventListener mOrientationEventListener = new OrientationEventListener(getApplicationContext()) {
            @Override
            public void onOrientationChanged(int i) {
                mOrientationDegrees = i;
            }
        };

        if(mOrientationEventListener.canDetectOrientation()){
            mOrientationEventListener.enable();
        }
        mIngredientsListManager = new IngredientsListManager("HAIR");
    }


    @Override
    public void onResume(){
        super.onResume();

        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
        }
        if (mCameraPreview.isAvailable()) {
            openCamera();
        } else {
            mCameraPreview.setSurfaceTextureListener(
                    mCameraPreviewListener);
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        closeCamera();
        //stopBackgroundThread();
    }

    @Override
    public void onBackPressed(){
        if(!mPreviewPhotoMode){
            super.onBackPressed();
        }
        else{
            startPreview(mCameraCaptureSession);
        }

    }

    private TextureView.SurfaceTextureListener mCameraPreviewListener = new TextureView.SurfaceTextureListener(){

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height){
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height){
            //transform image captured sie

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface){
            closeCamera();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface){
        }
    };


    private void openCamera() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.i(TAG, "opening camera");
        try {
            mCameraID = mCameraManager.getCameraIdList()[0];
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraID);
            StreamConfigurationMap mScalerStreamConfigMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            assert mScalerStreamConfigMap != null;
            assert mSensorOrientation != null;
            mPreviewSize = mScalerStreamConfigMap.getOutputSizes(ImageFormat.YUV_420_888)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ScanLabelActivity.this, new String[]{
                                Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
            }
            mCameraManager.openCamera(mCameraID, cameraDeviceCallbacks, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "opened Camera");
    }


    private final CameraDevice.StateCallback cameraDeviceCallbacks = new CameraDevice.StateCallback(){

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            //create the initial surfaces to go into the cameraSession
            mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.YUV_420_888, 1);
            Surface imageReaderSurface = mImageReader.getSurface();

            SurfaceTexture cameraPreviewSurfaceTexture = mCameraPreview.getSurfaceTexture();
            cameraPreviewSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(),mPreviewSize.getHeight());
            Surface cameraPreviewSurface = new Surface(cameraPreviewSurfaceTexture);

            List<Surface> surfacesList = new ArrayList<Surface>();
            surfacesList.add(imageReaderSurface);
            surfacesList.add(cameraPreviewSurface);

            try{
                previewCaptureRequest = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewCaptureRequest.addTarget(cameraPreviewSurface);
            }catch(CameraAccessException e){
                e.printStackTrace();
            }

            try{
                 mCameraDevice.createCaptureSession(surfacesList, captureSessionCallback,mBackgroundHandler); //add another surface
            }catch(CameraAccessException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int i) {

        }
    };


    private final CameraCaptureSession.StateCallback captureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
           startPreview(cameraCaptureSession);
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

        }
    };

    protected void startPreview(CameraCaptureSession session) {
        mPreviewPhotoMode = false;
        mCameraCaptureSession = session;
        previewCaptureRequest.set(CaptureRequest.CONTROL_MODE,CameraMetadata.CONTROL_MODE_AUTO);

        try{
            mCameraCaptureSession.setRepeatingRequest(previewCaptureRequest.build(),null,mBackgroundHandler);
        }
        catch(CameraAccessException e){
            e.printStackTrace();
        }
    }


    private final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            //startPreview(session); //preview the photo for testing but use this to call the firebase shit
            Log.i(TAG,"capture callback");
        }
    };


    private void startBackgroundThread() {
        mBackgroundCameraThread = new HandlerThread("camera_background_thread");
        mBackgroundCameraThread.start();
        mBackgroundHandler = new Handler(mBackgroundCameraThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundCameraThread.quitSafely();
        try {
            mBackgroundCameraThread.join();
            mBackgroundCameraThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPreviewPhotoMode = true;
        //float photoAzimuth = mAzimuth;
        if(null == mCameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }

        try {
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            SurfaceTexture cameraPreviewSurfaceTexture = mCameraPreview.getSurfaceTexture();
            if(cameraPreviewSurfaceTexture == null ){
                return;
            }

            cameraPreviewSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(),mPreviewSize.getHeight());
            Surface cameraPreviewSurface = new Surface(cameraPreviewSurfaceTexture);
            captureBuilder.addTarget(cameraPreviewSurface);
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            ImageReader.OnImageAvailableListener imageListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Log.i(TAG, "image is available, process here");
                    mImage = reader.acquireNextImage();
                    processImage(mImage);
                    mImage.close();
                }

            };

            mImageReader.setOnImageAvailableListener(imageListener, mBackgroundHandler);

            mCameraCaptureSession.stopRepeating();
            mCameraCaptureSession.capture(captureBuilder.build(),captureListener,mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void processImage(Image image){
        FirebaseVisionImage fbImage = FirebaseVisionImage.fromMediaImage(mImage,determineRotation());
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result = textRecognizer.processImage(fbImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                Log.i(TAG, "we did it");
                //String resultText = firebaseVisionText.getTextBlocks();
                String resultText = firebaseVisionText.getText();
                mProgressBar.setVisibility(View.GONE);
                if(resultText.length()> 0){
                    try {
                        List<String> badList = mIngredientsListManager.checkCapturedText(resultText);
                        Intent scanResultsIntent = new Intent(getApplicationContext(),ScanResults.class);
                        scanResultsIntent.putStringArrayListExtra("badIngredients", (ArrayList<String>) badList);
                        startActivity(scanResultsIntent);
                        System.out.println("this is the bad list "+ badList);
                    }
                    catch(Exception e){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.scan_again),Toast.LENGTH_LONG).show();
                    }
                    finally {
                        mPreviewPhotoMode = false;
                        startPreview(mCameraCaptureSession);
                    }

                }
                else{
                    startPreview(mCameraCaptureSession);
                    //popup to say scan again
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "nigga you failed");
            }
        });
    }


    private int determineRotation(){
        //rotation returns values between 0 and 3
        //int rotation = ((WindowManager) ScanLabelActivity.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        Log.i(TAG,"rotation is "+mOrientationDegrees);
        Log.i(TAG, "mSensorOrientation is "+ mSensorOrientation);
        Log.i(TAG,"division value "+(mSensorOrientation  + mOrientationDegrees)/90);
        Log.i(TAG,"output value is "+ (Math.round((float)(mSensorOrientation  + mOrientationDegrees)/90))%4);
        return (Math.round((float)(mSensorOrientation  + mOrientationDegrees)/90))%4;//rotation to match sensor orientatio
    }

    final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

            //int orientation
            //processImage(mImage,orientation);
        }
    };


    protected void updatePreview() {
        if(null == mCameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        previewCaptureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            mCameraCaptureSession.setRepeatingRequest(previewCaptureRequest.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }
    }




    static class CompareSizesByArea implements Comparator<Size>{
        @Override
        public int compare(Size lhs, Size rhs){
            return Long.signum((long) lhs.getWidth()*lhs.getHeight() - (long) rhs.getHeight()*lhs.getWidth());
        }
    }
    }
