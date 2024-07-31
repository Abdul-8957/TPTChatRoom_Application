package com.example.tptchatroom.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.tptchatroom.R;
import com.example.tptchatroom.chatActivity;
import com.example.tptchatroom.modal.statusmodal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ViewHolder>{

    Context context;
    ArrayList<statusmodal> statuslist;

    public StatusListAdapter(Context context, ArrayList<statusmodal> statuslist) {
        this.context = context;
        this.statuslist = statuslist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.statussampledesign,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.statususername.setText(statuslist.get(position).username);
        holder.statususertime.setText(statuslist.get(position).statusdate);
        holder.circularStatus.setPortionsCount(statuslist.size());
        int statussize=statuslist.get(position).status.size();
        Picasso.get().load(statuslist.get(position).status.get(statussize-1)).placeholder(R.drawable.defaultusericon).into(holder.statususerimg);

        //show storis on click of user status icon
        holder.statususerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<MyStory> myStories = new ArrayList<>();

                for(String story: statuslist.get(position).status){
                    myStories.add(new MyStory(
                            story
                    ));
                }

                new StoryView.Builder(((chatActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
//                        .setTitleText("Hamza Al-Omari") // Default is Hidden
                        .setTitleText(statuslist.get(position).username) // Default is Hidden
//                        .setSubtitleText("Damascus") // Default is Hidden
                        .setSubtitleText(statuslist.get(position).statusdate) // Default is Hidden
//                        .setTitleLogoUrl("some-link") // Default is Hidden
                        .setTitleLogoUrl(statuslist.get(position).status.get(statussize-1)) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return statuslist.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView statususername,statususertime;
        CircularStatusView circularStatus;
        ImageView statususerimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statususername=itemView.findViewById(R.id.statususername);
            statususertime=itemView.findViewById(R.id.statususertime);
            circularStatus=itemView.findViewById(R.id.circular_status_view);
            statususerimg=itemView.findViewById(R.id.statususerimg);
        }
    }
}
