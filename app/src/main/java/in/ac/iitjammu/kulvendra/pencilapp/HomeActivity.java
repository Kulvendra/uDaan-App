package in.ac.iitjammu.kulvendra.pencilapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    String kullu;
    String city;
    String imageURL;


    final String[] nameChild = new String[1];
    final String[] toemail = new String[1];
    final String[] number = new String[1];

    String toEmails = "2016ucs0029@iitjammu.ac.in";
    String emailBody = "Discription about mail..!!";

    Button getButton;

    int count=0;
    String Name, Email, PhotoURL, Number,phoneNumber;
    NavigationView navigationView;
    ImageView imageView;
    Button chooseButton, uploadButton, lockButton;
    String complaint, complaintNumber;
    private Uri mImageUri;
    String phn;

    TextView progressTextView;
    ProgressBar progressBar2;

    TextView nameTextView, addressTextView, address2TextView, cityTextView, stateTextView, zipTextView, descriptionTextView;

    String ChildName, Address, Address2, City, State, Zip, Description;

    private StorageReference mStorageRef;
    FirebaseFirestore db;

    Button resetButton;
    FirebaseDatabase database;


    public static final int PICK_Image_Request = 1;


    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


//    */***********************************************************Location*****************************************************************************************

        TextView hintTextView;
//    Button locationButton;
    TextView locationAddressTextView;
    double latAddress,longAddress;

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.location_result)
    TextView txtLocationResult;

    @BindView(R.id.updated_on)
    TextView txtUpdatedOn;

    @BindView(R.id.btn_start_location_updates)
    Button btnStartUpdates;

    @BindView(R.id.btn_stop_location_updates)
    Button btnStopUpdates;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;


    //**********************************************************************************************************************************************************
    Context ctx;

    private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
    private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    String ph_no;
    String str;

//    ****************************************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ctx = this;
        resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        getButton = (Button)findViewById(R.id.getButton);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDatabase();
            }
        });

        //    */***********************************************************Phone Activity*****************************************************************************************
        ButterKnife.bind(this);

        locationAddressTextView = (TextView)findViewById(R.id.locationAddressTextView);
        hintTextView = (TextView)findViewById(R.id.hintTextView);

        // initialize the necessary libraries
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
//        locationButton = (Button)findViewById(R.id.locationButton);



//**********************************************************************************************************************************************************








         database = FirebaseDatabase.getInstance();


        nameTextView = (TextView)findViewById(R.id.nameTextView);
        addressTextView  = (TextView)findViewById(R.id.addressTextView);
        address2TextView  = (TextView)findViewById(R.id.address2TextView);
        cityTextView  = (TextView)findViewById(R.id.cityTextView);
        stateTextView  = (TextView)findViewById(R.id.stateTextView);
        zipTextView =  (TextView)findViewById(R.id.zipTextView);
        descriptionTextView = (TextView)findViewById(R.id.descriptionTextView);



        lockButton = (Button)findViewById(R.id.lockButton);

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getData();


//                if(nameTextView.getText().toString()!="" && addressTextView.getText().toString()!="" && cityTextView.getText().toString()!="" && stateTextView.getText().toString()!="" && zipTextView.getText().toString()!="" ){
                if(count==0) {
                    if (!address2TextView.getText().toString().isEmpty() && !cityTextView.getText().toString().isEmpty()
                            && !stateTextView.getText().toString().isEmpty() && !zipTextView.getText().toString().isEmpty()
                            && !descriptionTextView.getText().toString().isEmpty()) {

                        chooseButton.setVisibility(View.VISIBLE);

                    nameTextView.setEnabled(false);
                        addressTextView.setEnabled(false);
                        address2TextView.setEnabled(false);
                        cityTextView.setEnabled(false);
                        stateTextView.setEnabled(false);
                        zipTextView.setEnabled(false);
                        descriptionTextView.setEnabled(false);


                    }else
                        Toast.makeText(HomeActivity.this, "Complete the form", Toast.LENGTH_LONG).show();





                }else{

                    if (!descriptionTextView.getText().toString().isEmpty()) {

                        chooseButton.setVisibility(View.VISIBLE);

                        nameTextView.setEnabled(false);

                        descriptionTextView.setEnabled(false);


                    }else {

                        Toast.makeText(HomeActivity.this, "Please fill the address part..!!", Toast.LENGTH_SHORT).show();

                    }



                }


//                Toast.makeText(HomeActivity.this,nameTextView.getText().toString() , Toast.LENGTH_SHORT).show();

                inputData();

//                inputData();


            }
        });

//        inputData();


        mAuth = FirebaseAuth.getInstance();
        imageView = (ImageView)findViewById(R.id.imageView);
        chooseButton = (Button) findViewById(R.id.chooseButton);
        uploadButton = (Button)findViewById(R.id.uploadButton);

        progressTextView = (TextView)findViewById(R.id.progressTextView);
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);

        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("complaint/");

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();
            }
        });




        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Toast.makeText(HomeActivity.this, "Uploading starts...", Toast.LENGTH_SHORT).show();
                uploadFile();
//                Toast.makeText(HomeActivity.this, "Uploading ends", Toast.LENGTH_SHORT).show();
            }
        });
//
        db.collection("complaintNumber")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                complaint =document.getData().get("Value").toString();
//                                Toast.makeText(HomeActivity.this,complaint , Toast.LENGTH_LONG).show();

                                complaintNumber=String.valueOf(Integer.parseInt(complaint)+1);

//                                Toast.makeText(HomeActivity.this, String.valueOf(Integer.parseInt(complaint)+1).toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(HomeActivity.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                int temp = Integer.parseInt(complaint)+1;
//        Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            Name = extras.getString("Name");
//            Email = extras.getString("Email");
////            PhotoURL = extras.getString("URL");
//
//
//        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            Name = user.getDisplayName();

            Email = user.getEmail();

            PhotoURL = mAuth.getCurrentUser().getPhotoUrl().toString();

            phn = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

//            Toast.makeText(this, "Phone Number:"+phn, Toast.LENGTH_SHORT).show();



//            findNumber();
        }


        Picasso.with(this).load(PhotoURL).into(imageView);




//        inputData();
//



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mAuthListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()== null)
                {
                    startActivity(new Intent(HomeActivity.this,MainActivity.class));
                }else {
                    TextView emailHeader=navigationView.getHeaderView(0).findViewById(R.id.emailHeader);
                    TextView nameHeader =navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
                    ImageView imageHeader=navigationView.getHeaderView(0).findViewById(R.id.imageHeader);
                    TextView phntextview = navigationView.getHeaderView(0).findViewById(R.id.phoneHeader);


//                    if(phn != null){
                        phntextview.setText(phn);
//                    }

                    emailHeader.setText(Email);
                    nameHeader.setText(Name);

                    Picasso.with(HomeActivity.this).load(PhotoURL).into(imageHeader);

                }
            }
        };



        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);



//        String id =  Email.substring(0, Email.lastIndexOf("@"));
//        DatabaseReference check = database.getReference(id);
//
//        check.child("Verify").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                String kullu  =dataSnapshot.getValue(String.class);
//                if(kullu=="yes"){
//
//                    Intent i = new Intent(HomeActivity.this,HomeActivity.class);
//                    startActivity(i);
//                }else {
//
//                    Intent i = new Intent(HomeActivity.this,PhoneActivity.class);
//                    startActivity(i);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });





    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.history) {

//             Intent intent = new Intent(HomeActivity.this,EmailActivity.class);
//             startActivity(intent);

        } else if (id == R.id.progress) {

            Intent i = new Intent(HomeActivity.this,scannerActivity.class);
            startActivity(i);

        } else if (id == R.id.aboutus) {

        } else if (id == R.id.donate) {

        } else if (id == R.id.signOut) {


            mAuth.signOut();
            mAuth.getInstance().signOut();




        } else if(id== R.id.verifyNumber){

//             Toast.makeText(this, "KULLU", Toast.LENGTH_SHORT).show();

             Intent i =new Intent(HomeActivity.this,PhoneActivity.class);
             startActivity(i);
         }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    void openFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_Image_Request);

        uploadButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PICK_Image_Request && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(imageView);

            imageView.setVisibility(View.VISIBLE);
        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private String getFileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    private void uploadFile(){

        if(mImageUri!=null){

                final StorageReference fileReference = mStorageRef.child(complaintNumber);
                fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                        Map<String, Object> user = new HashMap<>();
                        user.put("Value", complaintNumber);

                        Map<String, Object> details = new HashMap<>();
                        details.put("Name",ChildName);



                        if(count==1) {

                            details.put("Address", locationAddressTextView.getText().toString());


                        }
                            else
                                details.put("Address",Address+","+Address2+","+City+","+State+","+Zip);
                        details.put("Description",Description);
                        details.put("ReportName",Name);
                        details.put("ReportEmail",Email);
                        details.put("City",city);

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageURL= uri.toString();
                                kullu=imageURL;

//                                Toast.makeText(HomeActivity.this, "Image URl"+imageURL, Toast.LENGTH_SHORT).show();
                            }
                        });


                        details.put("ReportNumber",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());


                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        DocumentReference docRef = db.collection("database").document("tamil nadu").collection("district").document(city.toLowerCase());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {

                                        nameChild[0] = document.getString("Name");
                                        toEmails = document.getString("Email");
                                        number[0] = document.getString("Number");

                                        Log.i("LOGGER","First "+ nameChild[0]);
                                        Log.i("LOGGER","Last "+ toemail[0]);
                                        Log.i("LOGGER","Born "+ number[0]);
                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }
                        });

//
//                        new SendMailTask(HomeActivity.this).execute("kulvendra6749@gmail.com",
//                                "kulvendra4508", toemail[0], "Child Labour Complaint",Description);
//
//





                        db.collection("complaintNumber").document("sequence")
                                .set(user);

                        db.collection("complaintList").document(complaintNumber).set(details);






                        Toast.makeText(HomeActivity.this, "Complaint Registered Successfully", Toast.LENGTH_SHORT).show();




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(HomeActivity.this, "Error while Uploading Pic"+e, Toast.LENGTH_SHORT).show();
                        //display error message
                    }
                })
            .addOnProgressListener(this, new
                    OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
//                            progressBar.setProgress((int) progress);

                            progressBar2.setVisibility(View.VISIBLE);
                            progressTextView.setVisibility(View.VISIBLE);
                            progressBar2.setProgress((int)progress);


//                            progressTextView.setText(progress+"%");

                            if(progress==100){


                                progressBar2.setVisibility(View.INVISIBLE);
                                progressTextView.setText("Done");

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
//                                        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
//                                        startActivity(intent);

                                        sendAMail();
                                    }
                                }, 1000);
                            }



                        }
                    });

//            Toast.makeText(this, "Done ulpdong", Toast.LENGTH_SHORT).show();


        }else {

            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();

        }



    }




    private void inputData(){





            ChildName = nameTextView.getText().toString();
            Address = addressTextView.getText().toString();
            Address2 = address2TextView.getText().toString();
            City = cityTextView.getText().toString();
            State = stateTextView.getText().toString();
            Zip = zipTextView.getText().toString();
            Description = descriptionTextView.getText().toString();
//
//            findNumber();
//
//        String temp =FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

//        Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();









    }


    private void findNumber(){
        String mailID =  Email.substring(0, Email.lastIndexOf("@"));

        DatabaseReference myRef = database.getReference(mailID);

        myRef.child("Number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Number =dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        Toast.makeText(this,Number , Toast.LENGTH_SHORT).show();
    }


//    ************************************************************************Location**********************************************


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }


    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            latAddress = mCurrentLocation.getLatitude();
            longAddress =mCurrentLocation.getLongitude();
            txtLocationResult.setText(
                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
                            "Lng: " + mCurrentLocation.getLongitude()
            );

            getCompleteAddressString(latAddress,longAddress);





            // giving a blink animation on TextView
            txtLocationResult.setAlpha(0);
            txtLocationResult.animate().alpha(1).setDuration(300);

            // location last updated time
            txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
        }

        toggleButtons();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

                city =addresses.get(0).getLocality();

                getDatabase(city);





//            Toast.makeText(this, "City :"+city, Toast.LENGTH_SHORT).show();


            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("address", strReturnedAddress.toString());
//                locationAddressTextView.append(strReturnedAddress.toString());
                locationAddressTextView.setText(strReturnedAddress.toString() + "["+LATITUDE + " , "+LONGITUDE+"]");
            } else {
                Log.w("address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    private void toggleButtons() {
        if (mRequestingLocationUpdates) {
            btnStartUpdates.setVisibility(View.GONE);

            addressTextView.setVisibility(View.GONE);
            address2TextView.setVisibility(View.GONE);
            cityTextView.setVisibility(View.GONE);
            stateTextView.setVisibility(View.GONE);
            zipTextView.setVisibility(View.GONE);

            count =1;

            hintTextView.setVisibility(View.VISIBLE);
            locationAddressTextView.setVisibility(View.VISIBLE);

            btnStopUpdates.setVisibility(View.VISIBLE);

        } else {

            btnStartUpdates.setVisibility(View.VISIBLE);
            btnStopUpdates.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

//                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    @OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @OnClick(R.id.btn_stop_location_updates)
    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        toggleButtons();
                    }
                });
    }

    @OnClick(R.id.btn_get_last_location)
    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.e(TAG, "User agreed to make required location settings changes.");
//                        // Nothing to do. startLocationupdates() gets called in onResume again.
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.e(TAG, "User chose not to make required location settings changes.");
//                        mRequestingLocationUpdates = false;
//                        break;
//                }
//                break;
//        }
//    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }



    public void getDatabase(String city){



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("database").document("tamil nadu").collection("district").document(city);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {

                        nameChild[0] = document.getString("Name");
                        toemail[0] = document.getString("Email");
                        number[0] = document.getString("Number");

                        Log.i("LOGGER","First "+ nameChild[0]);
                        Log.i("LOGGER","Last "+ toemail[0]);
                        Log.i("LOGGER","Born "+ number[0]);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

//        new SendMailTask(EmailActivity.this).execute("kulvendra6749@gmail.com",
//                "kulvendra4508", toemail[0], "Child Labour Complaint", description);
    }




    public void sendAMail(){


        Log.i("SendMailActivity", "Send Button Clicked.");

        String fromEmail = "kulvendra6749@gmail.com";
        String fromPassword = "kulvendra4508";


//        String toEmails = "2016ucs0029@iitjammu.ac.in";
//        String emailBody = "Discription about mail..!!";

//        Toast.makeText(this, "Image URL"+kullu, Toast.LENGTH_SHORT).show();



        if(count==1){

            emailBody =Description + ". Address :=> " +locationAddressTextView.getText().toString()  + "Google Map : http://www.google.com/maps/place/"+Double.toString(latAddress)+","+Double.toString(longAddress)+ " . Image URL :" + imageURL;

        }
        else {
            emailBody = Description+ ". Address" + Address + "," + Address2 + "," + City + "," + State + "," + Zip  + ". Image URL :" + imageURL;

        }



        List toEmailList = Arrays.asList(toEmails
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);

        String emailSubject = "Child Labour Complaint";





//




        new SendMailTask(HomeActivity.this).execute(fromEmail,
                fromPassword, toEmailList, emailSubject, emailBody);






        resetButton.setVisibility(View.VISIBLE);
    }







}



