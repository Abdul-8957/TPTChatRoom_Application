package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //....................................................
        getSupportActionBar().setTitle("Sign Up");
        //..............................................


        try {
            //create loader
            ProgressDialog pr = new ProgressDialog(this);
           // pr.setIcon(R.drawable.ic_baseline_person_24);
             pr.setTitle("Please Wait....");
            pr.setMessage("We are Creating Your Account");

            //get reference of all element..................................................
            TextInputEditText txtname = findViewById(R.id.txtname);
            TextInputEditText txtmobile = findViewById(R.id.txtmobile);
          //  EditText txtdob = findViewById(R.id.txtdob);
            TextInputEditText txtemail = findViewById(R.id.txtemail);
            TextInputEditText txtpassword = findViewById(R.id.txtpassword);
            Button btn = findViewById(R.id.btnregister);
            //cilck event of button register..............................................
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //loader show
                    pr.show();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(txtemail.getText().toString(), txtpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //loader hide
                                pr.dismiss();
                                String id = task.getResult().getUser().getUid();
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Name", txtname.getText().toString());
                                hashMap.put("Mobile", "+91"+txtmobile.getText().toString());
                               // hashMap.put("DOB", txtdob.getText().toString());
                                hashMap.put("Email", txtemail.getText().toString());
                                hashMap.put("Password", txtpassword.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("user").child(id).setValue(hashMap);

                                Toast.makeText(MainActivity.this, "Account created", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, chatActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage() + "", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Field your entry", Toast.LENGTH_SHORT).show();
        }
    }
    public void OpenSignIn(View v){
        Intent intent=new Intent(this,SignIn.class);
        startActivity(intent);
    }


}