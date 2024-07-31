package com.example.tptchatroom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tptchatroom.R;
import com.example.tptchatroom.modal.messagemodal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<messagemodal> msglist;
    String senderreciever;

    public MessageListAdapter(Context context, ArrayList<messagemodal> msglist,String senderreciever) {
        this.context = context;
        this.msglist = msglist;
        this.senderreciever=senderreciever;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1) {
            View v = LayoutInflater.from(context).inflate(R.layout.sendermsgsampledesign, parent, false);
            return new SenderViewHolder(v);
        }
        else
        {
            View v=LayoutInflater.from(context).inflate(R.layout.recievermsgsampledesign,parent,false);
            return  new RecieverViewHolder(v);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder) holder).sendermsg.setText(msglist.get(position).msg);
            ((SenderViewHolder) holder).sendermsgtime.setText(msglist.get(position).msgtime);
        }
        else
        {
            ((RecieverViewHolder) holder).recievermsg.setText(msglist.get(position).msg);
            ((RecieverViewHolder) holder).recievermsgtime.setText(msglist.get(position).msgtime);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               // Toast.makeText(context, msglist.get(position).messagekey+"", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Delete Message");
                alert.setMessage("Do you really want to delete message");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("message").child(senderreciever)
                                .child(msglist.get(holder.getAdapterPosition()).messagekey).setValue(null);
                        Toast.makeText(context, "Message delete", Toast.LENGTH_LONG).show();

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(msglist.get(position).senderid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return 1;
        else
            return 2;

    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView sendermsg,sendermsgtime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermsg=itemView.findViewById(R.id.sendermsg);
            sendermsgtime=itemView.findViewById(R.id.sendermsgtime);
        }
    }
    class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        TextView recievermsg,recievermsgtime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recievermsg=itemView.findViewById(R.id.recievermsg);
            recievermsgtime=itemView.findViewById(R.id.recievermsgtime);
        }
    }
}
