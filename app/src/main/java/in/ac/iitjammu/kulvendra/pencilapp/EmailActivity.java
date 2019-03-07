package in.ac.iitjammu.kulvendra.pencilapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class EmailActivity extends AppCompatActivity {


    EditText senderEmail,emailSubject,emailDescription;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);


//        sendEmail = (Button) findViewById(R.id.send_Mail_Button);
//        senderEmail = (EditText)findViewById(R.id.email_Text_View);
//        emailSubject = (EditText)findViewById(R.id.subject_Text_View);
//        emailDescription = (EditText)findViewById(R.id.description_Text_View);


        final Button send = (Button) this.findViewById(R.id.send_Mail_Button);

        final Button getDataButton = (Button)this.findViewById(R.id.getdataButton);

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                sendAMail();

            }
        });






        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {









            }
        });









    }

    public void getDatabase(String city,String description){

        final String[] nameChild = new String[1];
        final String[] toemail = new String[1];
        final String[] number = new String[1];

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

//
    }


    public void sendAMail(){


        Log.i("SendMailActivity", "Send Button Clicked.");

        String fromEmail = "kulvendra6749@gmail.com";
        String fromPassword = "kulvendra4508";
        String toEmails = "2016ucs0029@iitjammu.ac.in";
        List toEmailList = Arrays.asList(toEmails
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);
        String emailSubject = "Child Labour Complaint";
        String emailBody = "Discription about mail..!!";


        new SendMailTask(EmailActivity.this).execute(fromEmail,
                fromPassword, toEmailList, emailSubject, emailBody);

    }
}
