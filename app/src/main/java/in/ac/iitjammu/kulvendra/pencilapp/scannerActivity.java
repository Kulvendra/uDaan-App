package in.ac.iitjammu.kulvendra.pencilapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class scannerActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView output;
    BarcodeDetector barcodeDetector;


    String dataID;


    private StorageReference mStorageRef;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        surfaceView = (SurfaceView) findViewById(R.id.cameraPreview);
        output = (TextView) findViewById(R.id.outputTextView);
        db = FirebaseFirestore.getInstance();


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                    return;
                }
                try{
                    cameraSource.start(holder);

                }catch (IOException e){
                    e.printStackTrace();
                }

             }

             @Override
             public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

             }

             @Override
             public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();

             }
         });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if(qrCode.size()!=0){

                    output.post(new Runnable() {
                        @Override
                        public void run() {

                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            dataID =qrCode.valueAt(0).displayValue;

                            String[] parts = dataID.split(" ");


                            output.setText(parts[1]);

//                            Toast.makeText(scannerActivity.this, Toast.LENGTH_LONG, dataID[1]).show();

//                            StorageReference fileReference = mStorageRef.child(complaintNumber);



                                    Map<String, Object> user = new HashMap<>();
                                    user.put("WeeklyStatusChecked", "true");
//
//








                                     db.collection(parts[0]).document(parts[1])
                                            .update(user);






                        }
                    });
                }
            }
        });






    }
}
