package com.example.shivang.codex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



/**
 * Created by shivang on 14-03-2017.
 */

public class DrawerItemClickListener extends AppCompatActivity implements ListView.OnItemClickListener  {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);

    }
    public void selectItem(int position){
        Intent intent;
        switch (position){
            case 0:
                 intent=new Intent(getApplicationContext() ,PlacePickerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case 1:
                intent=new Intent(getApplicationContext(),GroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case 2:
                intent=new Intent(getApplicationContext(),AboutUs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;


        }
    }


}
