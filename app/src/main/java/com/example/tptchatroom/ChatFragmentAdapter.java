package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ChatFragmentAdapter extends FragmentStateAdapter {
    public ChatFragmentAdapter(@NonNull FragmentManager fragmentManager,@NonNull Lifecycle lifecycle) {
        super(fragmentManager,lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0)
       return new ChatFragment();
       else if(position==1)
            return new StatuskFragment();
        else
            return new ProfileFragment();
        
    }
    //.........................................................................................
    public CharSequence getPageTitle(int position)
    {
        if(position==0)
            return "CHAT";
        else if(position==1)
            return "STATUS";
        else
            return "PROFILE";
    }
    //..........................................................................................

    @Override
    public int getItemCount() {
        return 3;
    }
}