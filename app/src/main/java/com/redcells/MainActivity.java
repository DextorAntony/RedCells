package com.redcells;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Button newb;
    private ImageButton u;
    private TextView b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference();
        final EditText name = (EditText) findViewById(R.id.editText);
        final EditText mobile = (EditText) findViewById(R.id.editText3);
        newb = (Button) findViewById(R.id.button2);
        final Spinner blood = (Spinner) findViewById(R.id.spinner);


         //b = findViewById(R.id.b);
         b = findViewById(R.id.textView18);

        newb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String location = b.getText().toString();


                String name1 = name.getText().toString();
                String mobile1 = mobile.getText().toString();
                String blood1 = blood.getSelectedItem().toString();
                String combo = name1 + "," + mobile1 + "," + location;
                //  usr newuser = new usr(name1, blood1, panch1);
                if (blood1.equals("") || mobile1.equals("") || location.equals("Touch to detect")) {
                    Toast.makeText(MainActivity.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
                } else {
                    mFirebaseDatabase.child("users").child(blood1).child(mobile1).setValue(combo);

                    Toast.makeText(MainActivity.this, "Donor Added", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(MainActivity.this, Home.class);
                    startActivity(home);
                    finish();
                }
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    assert locationManager != null;
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        assert location != null;
                        String city = hereLocation(location.getLatitude(), location.getLongitude());
                        b.setText(city);
                        Toast.makeText(MainActivity.this, "Awesome you're in " + city, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "NOT FOUND", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    assert locationManager != null;
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        assert location != null;
                        String city = hereLocation(location.getLatitude(),location.getLongitude());
                        b.setText(city);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"NOT FOUND",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast t =  Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT);
                    t.show();
                }
                break;
            }
        }
    }

    private String hereLocation(double lat, double lon)
    {
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat,lon,10);
            if (addresses.size() > 0){
                for(Address adr: addresses){
                    if (adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }
}
