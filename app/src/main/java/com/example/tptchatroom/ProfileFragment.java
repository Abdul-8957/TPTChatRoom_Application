package com.example.tptchatroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tptchatroom.modal.messagemodal;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor

    }


ImageView usericon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        usericon=v.findViewById(R.id.usericon);
        ImageView addusericon=v.findViewById(R.id.addusericon);
        //....................................................................
//        ImageView editname=v.findViewById(R.id.editname);
//        TextView profilename=v.findViewById(R.id.profilename);
//        editname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        //.........................................................................
        addusericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,1);

            }
        });

        //lets get all profile of current user and dispaly
        Button btn=v.findViewById(R.id.adduserabout);
        EditText userabout=v.findViewById(R.id.userabout);
        //.....................................................................

        //.........................................................................
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!userabout.getText().toString().isEmpty())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("about",userabout.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(hashMap);

                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Write something is about", Toast.LENGTH_LONG).show();
                }
            }
        });

        //lets get all profile of current user
        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView usernameprofile=v.findViewById(R.id.usernameprofile);
                TextView txtaboutprofile=v.findViewById(R.id.txtaboutprofile);
                TextView txtnameprofile=v.findViewById(R.id.txtnameprofile);
                TextView txtemailprofile=v.findViewById(R.id.txtemailprofile);
                ImageView usericon=v.findViewById(R.id.usericon);
                TextView txtmobileprofile=v.findViewById(R.id.txtmobileprofile);
                Picasso.get().load(snapshot.child("profilepic").getValue(String.class))
                        .placeholder(R.drawable.defaultusericon).into(usericon);
                usernameprofile.setText(snapshot.child("Name").getValue(String.class));
                txtnameprofile.setText(snapshot.child("Name").getValue(String.class));
                txtemailprofile.setText(snapshot.child("Email").getValue(String.class));
                txtaboutprofile.setText(snapshot.child("about").getValue(String.class));
                txtmobileprofile.setText(snapshot.child("Mobile").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
           Uri userimg= data.getData();
           usericon.setImageURI(userimg);
           StorageReference storage= FirebaseStorage.getInstance().getReference().child("profilepic")
                   .child(FirebaseAuth.getInstance().getUid());
            storage.putFile(userimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           Toast.makeText(getContext(), "Profile uploaded successfuly", Toast.LENGTH_LONG).show();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("profilepic",uri.toString());
                            FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance()
                                    .getUid()).updateChildren(hashMap);

                        }
                    });
                }
            });
        }
    }
}