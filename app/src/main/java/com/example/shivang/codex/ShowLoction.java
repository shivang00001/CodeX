package com.example.shivang.codex;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.maps.OnMapReadyCallback;




public class ShowLoction extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG ="ShowLoction";
    GoogleMap googleMap;
    private static int timer=3000;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_loction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras=getIntent().getExtras();
        name=extras.getString("name");

        /*MapView mapView=(MapView)findViewById(R.id.map_view);
         mapView.getMapAsync(this);
        Log.e(TAG,"point1");*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






        /*MapController mapController = mapView.getController();

        double lat = lat1 * 1e6;
        double lon = lng1 * 1e6;

        Barcode.GeoPoint startpoint = new Barcode.GeoPoint((int) lat, (int) lon);
        mapController.animateTo(startpoint);*/


            }


    public void closeShowLoc(View view){
        Intent mIntent=new Intent(this,FuncActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ServiceLocationFetch service2=new ServiceLocationFetch();
        service2.stopSelf();
        startActivity(mIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(getApplicationContext(),"You did not gave me permission",Toast.LENGTH_SHORT).show();
        }
        /*googleMap.setMyLocationEnabled(true);
        String latitude=Constant.LATITUDE;
        Double lat1=Double.parseDouble(latitude);
        String longitude=Constant.LONGITUDE;
        Double lng1=Double.parseDouble(longitude);
        final LatLng latLng = new LatLng(26.8467, 80.9462);
        final MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.bookameet));
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14f).tilt(70).build();
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);*/
        //googleMap.addMarker()

       // buildGoogleApiClient();

        //mGoogleApiClient.connect();


        LatLng sydney = new LatLng(Double.parseDouble(Constant.LATITUDE),Double.parseDouble(Constant.LONGITUDE) );
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(name));
        googleMap.clear();

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}


