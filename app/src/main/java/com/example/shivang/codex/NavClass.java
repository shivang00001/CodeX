package com.example.shivang.codex;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class NavClass extends AppCompatActivity {
    private String[] members;
    private DrawerLayout drawerLayout;
    private ListView listView;

    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_layout1);

        members=getResources().getStringArray(R.array.members);
        drawerLayout=(DrawerLayout)findViewById(R.id.darwerlayout);
        listView=(ListView)findViewById(R.id.left_drawer);

        listView.setAdapter(new ArrayAdapter<>(this,R.layout.content_nav_drar, members));

        listView.setOnItemClickListener(new DrawerItemClickListener());

    }

}
