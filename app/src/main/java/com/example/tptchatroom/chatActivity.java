package com.example.tptchatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class chatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String email=auth.getCurrentUser().getEmail();


        //......................................................
        getSupportActionBar().setTitle("Chit Chat");
        //create loader.....................................................



//bind fragmentaStateAdapter to
        ViewPager2 view=findViewById(R.id.viewpager);
        ChatFragmentAdapter adapter=new ChatFragmentAdapter(getSupportFragmentManager(),getLifecycle());
        view.setAdapter(adapter);

        TabLayout tab=findViewById(R.id.chattab);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       view.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               super.onPageScrolled(position, positionOffset, positionOffsetPixels);
               tab.getTabAt(position).select();
           }
       });
    }
    @Override
    protected void onStart() {
//        ProgressDialog pr=new ProgressDialog(this);
//        pr.setIcon(R.drawable.whatsapp);
//        pr.show();
        super.onStart();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null)
        {
//            pr.hide();
            Intent intent=new Intent(this,SignIn.class);
            startActivity(intent);
        }
    }


    //lets bind option menu in this activity

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_setting:
                break;
            case R.id.menu_logout:
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent=new Intent(this,SignIn.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}