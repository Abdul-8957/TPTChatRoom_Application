package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tptchatroom.adapter.MessageListAdapter;
import com.example.tptchatroom.modal.messagemodal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        getSupportActionBar().hide();

        Intent intent=getIntent();
        TextView username=findViewById(R.id.username);
        username.setText(intent.getStringExtra("name"));
        ImageView userprofile=findViewById(R.id.userprofile);
        Picasso.get().load(intent.getStringExtra("profilepic")).placeholder(R.drawable.defaultusericon).into(userprofile);
        String receiveruid=intent.getStringExtra("uid");
        String senderuid= FirebaseAuth.getInstance().getCurrentUser().getUid();

       // Toast.makeText(this, receiveruid+"  "+senderuid+"  ", Toast.LENGTH_SHORT).show();

        //lets create back button click event
        ImageView back=findViewById(R.id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MessageDetail.this,chatActivity.class);
                startActivity(in);
            }
        });

        //lets save message in firebase database on click of send button

        ImageView sendbtn=findViewById(R.id.sendmsg);
        EditText message=findViewById(R.id.usermsg);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!message.getText().toString().trim().isEmpty())
                {
                    //colection : hashmap : Message , time , sender
                    messagemodal md=new messagemodal();
                    md.msg=message.getText().toString();
                    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy hh:mm aa");
                    Date date=new Date();
                    md.msgtime=String.valueOf(format.format(date));
                    md.senderid=senderuid;
                    FirebaseDatabase.getInstance().getReference().child("message").child(senderuid+receiveruid).push().
                            setValue(md).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            FirebaseDatabase.getInstance().getReference().child("message").child(receiveruid+senderuid).push()
                                    .setValue(md);

                            message.setText("");
                        }
                    });


                }
                else
                {
                    Toast.makeText(MessageDetail.this, "Enter Message", Toast.LENGTH_LONG).show();
                }
            }
        });


        ArrayList<messagemodal> msglist=new ArrayList<>();
        //lets bind messagelistadapter to the recylerview
        RecyclerView recycle=findViewById(R.id.recyclemsg);
        MessageListAdapter adapter=new MessageListAdapter(this,msglist,senderuid+receiveruid);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));



        //lets select all message of sender and reciever from database
        FirebaseDatabase.getInstance().getReference().child("message").child(senderuid+receiveruid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msglist.clear();

                for(DataSnapshot ds:snapshot.getChildren())
                {

                    messagemodal m=new messagemodal();
                    m.msg=ds.child("msg").getValue(String.class);
                    m.msgtime=ds.child("msgtime").getValue(String.class);
                    m.senderid=ds.child("senderid").getValue(String.class);
                    m.messagekey=ds.getKey();
                    msglist.add(m);
                }
                adapter.notifyDataSetChanged();
                if(msglist.size()>2)
                recycle.smoothScrollToPosition(msglist.size()-1);
               // Log.v("data",snapshot+" ");
               // Toast.makeText(MessageDetail.this, snapshot+" ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}