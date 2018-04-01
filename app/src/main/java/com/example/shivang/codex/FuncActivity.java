package com.example.shivang.codex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.jar.*;
import java.util.jar.Manifest;

import static com.example.shivang.codex.AppThemeColor.backgroundColor;
import static com.example.shivang.codex.AppThemeColor.buttonColor;
import static com.example.shivang.codex.GroupsHelper.MEETING_TABLE_NAME;
import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

public class FuncActivity extends AppCompatActivity implements LocationListener,NavigationView.OnNavigationItemSelectedListener{
    LocationManager mLocationManager;
    LocationListener mLocationListener;
    private Location location;
    private DrawerLayout drawerLayout;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


     int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_new_layout);
        checkPermissionForApp();

        String randomColor=backgroundColor[(int)(Math.random()*backgroundColor.length)];
        for(i=0;i<backgroundColor.length;i++){
            if(backgroundColor[i].equals(randomColor)){
                break;
            }
        }
        MeetDataBaseHelper meetDataBaseHelper=new MeetDataBaseHelper(this);
        meetDataBaseHelper.print("MY_MEETS");
        SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("backgroundColor", randomColor);
        editor.putString("buttonColor", buttonColor[i]);
        editor.commit();
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.funcLinLayout);
        Button b1=(Button)findViewById(R.id.newFuncB1);
        Button b2=(Button)findViewById(R.id.newFuncB2);
        Button b3=(Button)findViewById(R.id.newFuncB3);
        Button b4=(Button)findViewById(R.id.NavDrarButton);


        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        b1.setBackgroundColor(Color.parseColor(buttonColor[i]));
        b1.setAlpha((float) 0.75);
        b2.setBackgroundColor(Color.parseColor(buttonColor[i]));
        b2.setAlpha((float) 0.75);
        b3.setBackgroundColor(Color.parseColor(buttonColor[i]));
        b3.setAlpha((float) 0.75);
        b4.setBackgroundColor(Color.parseColor(buttonColor[i]));
        b4.setAlpha((float) 0.75);


        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(Color.parseColor(randomColor));


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, this);
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
        catch (Exception e){
            Log.e("FuncActivity",e.toString());
        }

       /* Constant.aSyncFlag=false;
        if(Constant.aSyncFlag==false){
            UpdateContact updateContact=new UpdateContact(this);
            updateContact.execute();
            if(updateContact.isCancelled()){
                updateContact.execute();
            }
            updateContact.onPostExecute(null);

        }*/
    }

    private void checkPermissionForApp() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_CONTACTS)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_CONTACTS)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(getApplicationContext(),"permission needed",Toast.LENGTH_SHORT).show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.READ_CONTACTS,
                                android.Manifest.permission.WRITE_CONTACTS,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                       1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                
                if (hasGrantedAll(grantResults)) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Constant.permissionFlag=true;
                    Intent intent = new Intent(this, MyService.class);
                    startService(intent);
                    Intent mIntent=new Intent(getApplicationContext(),AppSetupActiviy.class);
                    startActivity(mIntent);


                } else {
                    Toast.makeText(getApplicationContext(),"You have not granted Permission",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean hasGrantedAll(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void onMyLoc(View view) {

       /* if (location != null)
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + location.getLatitude() + " Lng: "
                            + location.getLongitude(), Toast.LENGTH_SHORT).show();*/

    Intent mIntent=new Intent(this,SelectPersonLoc.class);
        startActivity(mIntent);

    }

    public void onMeetBook(View view) {
        Intent intent = new Intent(this, PlacePickerActivity.class);
        startActivity(intent);

    }

    public void onContactImp(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(
                getBaseContext(),
                "Location : Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onNavDrarButton(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.upcomingMeets:
                try {

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    Cursor cursor;int c=0;
                    String dom, tom;
                    MeetDataBaseHelper obj = new MeetDataBaseHelper(getBaseContext());
                    SQLiteDatabase db = obj.getWritableDatabase();
                    String selectQuery = "SELECT  DATE,TIME FROM " + MEETING_TABLE_NAME;
                    cursor = db.rawQuery(selectQuery, null);
                    Log.e("checkError","point1");
                    if (cursor.moveToFirst()) {
                        Log.e("checkError","point2");
                        do {
                        dom = cursor.getString(cursor.getColumnIndex("DATE"));
                        tom = cursor.getString(cursor.getColumnIndex("TIME"));
                            Log.e("checkError","point3");
                        if(!dom.isEmpty()&&!tom.isEmpty()) {
                            Log.e("Func", date);
                            Log.e("Func", dom + ", " + tom);
                            Date end = new SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.ENGLISH).parse(dom + ", " + tom);
                            Date start = new SimpleDateFormat("yyyy-MM-dd, HH:mm", Locale.ENGLISH).parse(date);
                            Log.e("checkError","point4");

                            Log.e("Func", start.toString());
                            Log.e("Func", end.toString());

                            if (end.compareTo(start) > 0) {
                                Log.e("checkError","point5");
                                c++;
                            }
                        }
                        }while (cursor.moveToNext());
                    }

                    Log.e("valOfC",c+"");
                    if(c>0){
                        Intent myIntent=new Intent(FuncActivity.this,UpcomingMeetsActivity.class);
                        startActivity(myIntent);
                    }
                    else {Toast.makeText(getApplicationContext(),"No Meetings",Toast.LENGTH_SHORT).show();}

                }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.d("Func activity","line 184"+e.toString());
                        //Intent myIntent=new Intent(FuncActivity.this,FuncActivity.class);
                        //startActivity(myIntent);
                        Intent myIntent=new Intent(FuncActivity.this,UpcomingMeetsActivity.class);
                        startActivity(myIntent);
                    }

                break;
            case R.id.myGroup:
                Intent myIntent=new Intent(getApplicationContext(),GroupActivity.class);
                startActivity(myIntent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            /*case R.id.permission:
                Intent permIntent=new Intent(this,GetPermissionActivity.class);
                startActivity(permIntent);
                break;*/

            case R.id.aboutUs:
                Intent mIntent=new Intent(getApplicationContext(),AboutUs.class);
                startActivity(mIntent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.SignOut:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Yahoo once rejected to buy GOOGLE\nDo yo really want to leave?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor=preferences.edit();
                                        editor.putBoolean("hasLoggedIn", false);
                                        editor.commit();
                                        MyService myService=new MyService();
                                        myService.onDestroy();

                                        Intent myIntent=new Intent(FuncActivity.this,SignINActivity.class);
                                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(myIntent);
                                        drawerLayout.closeDrawer(GravityCompat.START);

                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

        }
       return true;
    }


}
