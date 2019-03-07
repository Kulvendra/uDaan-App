package in.ac.iitjammu.kulvendra.pencilapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    TextView phoneTextView;
    Button sendButton, verifyButton;
    ProgressBar progressBar, codeProgressBar;
    TextView codeText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseUser mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String Name, Email, PhotoURl;

    TextView verifyTextView;


    String phoneNumber;

    String SignInCheck,phoneCount;
    Button passButton,tempButton;

    FirebaseDatabase database;

        FirebaseAuth firebaseAuth;

        TextView errorTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        errorTextView = (TextView)findViewById(R.id.errorTextView);

        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        sendButton = (Button) findViewById(R.id.sendButton);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        codeProgressBar = (ProgressBar) findViewById(R.id.codeProgressBar);
        codeText = (TextView) findViewById(R.id.codeText);

        verifyTextView = (TextView)findViewById(R.id.verifyTextVIew);
        passButton = (Button)findViewById(R.id.passButton);
        tempButton = (Button)findViewById(R.id.tempButton);

        database = FirebaseDatabase.getInstance();


        mAuth = FirebaseAuth.getInstance().getCurrentUser();

            firebaseAuth = FirebaseAuth.getInstance();



        FirebaseUser checkNumber = FirebaseAuth.getInstance().getCurrentUser();
        if(checkNumber.getPhoneNumber()!=null){

            Intent intent = new Intent(PhoneActivity.this,HomeActivity.class);
            startActivity(intent);

        }




//
//        String temp = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
//
//        Toast.makeText(this, "Current Phone"+temp + temp.length(), Toast.LENGTH_SHORT).show();











        // No user is signed in


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (phoneTextView.getText().toString().isEmpty()) {

                    Toast.makeText(PhoneActivity.this, "Please fill the details required", Toast.LENGTH_SHORT).show();
                } else {


                    phoneTextView.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    sendButton.setEnabled(false);


                    phoneNumber = "+91"+ phoneTextView.getText().toString();


                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneActivity.this,               // Activity (for callback binding)
                            mCallbacks
                    );


                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePhoneNumber(phoneAuthCredential).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Status", "signInWithCredential:success");

                            Snackbar.make(findViewById(android.R.id.content), "Mobile Verified Successfully.",
                                    Snackbar.LENGTH_SHORT).show();

                        } else {
                            Log.w("Status", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //mVerificationField.setError("Invalid code.");
                                Snackbar.make(findViewById(android.R.id.content), "Invalid Code.",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PhoneActivity.this,"signInWithCredential:failure"+task.getException(),
                                        Snackbar.LENGTH_LONG).show();
                                errorTextView.setText("signInWithCredential:failure"+task.getException());

                            }
                        }
                    }
                });

                passButton.setVisibility(View.VISIBLE);
                phoneTextView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                codeProgressBar.setVisibility(View.INVISIBLE);
                codeText.setVisibility(View.INVISIBLE);
                verifyButton.setVisibility(View.INVISIBLE);
                verifyTextView.setVisibility(View.VISIBLE);

                phoneCount="1111";

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(PhoneActivity.this, e + "Code is wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                progressBar.setVisibility(View.INVISIBLE);
                codeText.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.INVISIBLE);

                // ...
            }
        };


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codeProgressBar.setVisibility(View.VISIBLE);

                String verificationCode = codeText.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);

//                mAuth.updatePhoneNumber(credential)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(PhoneActivity.this, "User mobile updated.(manual)", Toast.LENGTH_LONG).show();
//                                    String mailID =  Email.substring(0, Email.lastIndexOf("@"));
//
//                                    Toast.makeText(PhoneActivity.this, mailID, Toast.LENGTH_LONG).show();
//
//
//
//
//
//                                    DatabaseReference myRef = database.getReference(mailID).child("Number");
//                                    myRef.setValue(phoneTextView.getText().toString());
//
//                                    DatabaseReference myRef2 = database.getReference(mailID).child("Verify");
//                                    myRef2.setValue("yes");
//                                } else {
//                                    Toast.makeText(PhoneActivity.this, "User mobile not updated.(manual)", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
                firebaseAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Status", "signInWithCredential:success");

                            Snackbar.make(findViewById(android.R.id.content), "Mobile Verified Successfully.",
                                    Snackbar.LENGTH_SHORT).show();

                        } else {
                            Log.w("Status", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //mVerificationField.setError("Invalid code.");
                                Snackbar.make(findViewById(android.R.id.content), "Invalid Code.",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PhoneActivity.this,"signInWithCredential:failure"+task.getException(),
                                        Snackbar.LENGTH_LONG).show();

                            }
                        }
                    }
                });

//                signInWithPhoneAuthCredential(credential);
                passButton.setVisibility(View.VISIBLE);
                phoneTextView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                codeProgressBar.setVisibility(View.INVISIBLE);
                codeText.setVisibility(View.INVISIBLE);
                verifyButton.setVisibility(View.INVISIBLE);
                verifyTextView.setVisibility(View.VISIBLE);

                Toast.makeText(PhoneActivity.this, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();

            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            Name = user.getDisplayName();
            Email = user.getEmail();

            boolean emailVerified = user.isEmailVerified();


            String uid = user.getUid();

//           Toast.makeText(PhoneActivity.this, Email + " "+ Name, Toast.LENGTH_SHORT).show();


        }

//        Picasso.with(PhoneActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).into(imageView);
//        PhotoURl = mAuth.getCurrentUser().getPhotoUrl().toString();



        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent id = new Intent(PhoneActivity.this,HomeActivity.class);

//                id.putExtra("Name",Name);
//                id.putExtra("Email",Email);
//                id.putExtra("URL",PhotoURl);
                startActivity(id);
//                finish();




            }
        });


        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent id2 = new Intent(PhoneActivity.this,HomeActivity.class);//
                startActivity(id2);

            }
        });

    }







    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {



    }
}