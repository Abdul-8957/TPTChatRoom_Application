package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //....................................................
        getSupportActionBar().setTitle("Sign In");
        //..............................................

        //create loader
        ProgressDialog pr=new ProgressDialog(this);
        pr.setTitle("Please Wait");
        pr.setMessage("Featching Your Data");

        //get reference of all element
        TextInputEditText txtemail=findViewById(R.id.txtemail);
        TextInputEditText txtpassword=findViewById(R.id.txtpassword);
        Button btnSignIn=findViewById(R.id.btnSignIn);
        //onClickListener of button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loader show
                pr.show();
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(txtemail.getText().toString(),txtpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //loader hide
                        pr.dismiss();
                        if(task.isSuccessful())

                        {
                            Toast.makeText(SignIn.this, "Welcome to TPT Chat", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(SignIn.this,chatActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(SignIn.this,task.getException().getMessage()+"", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null) {
            Intent intent = new Intent(this, chatActivity.class);
            startActivity(intent);
        }
    }

    public void OpenSignUp(View v){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}