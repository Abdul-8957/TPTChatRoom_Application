package com.example.tptchatroom.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tptchatroom.MessageDetail;
import com.example.tptchatroom.R;
import com.example.tptchatroom.modal.usermodal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.viewHolder> {
    Context con;
    ArrayList<usermodal> list;
    ImageView userprofilepic;
    TextView username, userlastmsg;
    
    public UserListAdapter(Context context,ArrayList<usermodal> list) {
       this.con=context;
        this.list=list;
    }

    public UserListAdapter(Context context) {
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(con).inflate(R.layout.sampleuserdesign,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        usermodal user=list.get(position);
        username.setText(user.name);
        userlastmsg.setText(user.email);
        Picasso.get().load(user.profilepic).placeholder(R.drawable.defaultusericon).into(userprofilepic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(con, MessageDetail.class);
                intent.putExtra("name",user.name);
                intent.putExtra("uid",user.uid);
                intent.putExtra("profilepic",user.profilepic);
                con.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class viewHolder extends RecyclerView.ViewHolder
    {

        public viewHolder(@NonNull View itemView){ super(itemView);
            userprofilepic=itemView.findViewById(R.id.userprofilepic);
            username=itemView.findViewById(R.id.username);
            userlastmsg=itemView.findViewById(R.id.userlastmsg);

        }

    }
}