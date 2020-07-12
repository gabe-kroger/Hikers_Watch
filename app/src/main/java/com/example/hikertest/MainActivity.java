package com.example.hikertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void updateLocationInfo(Location location) {
        Log.i("Location information", location.toString());
        TextView latTextView = (TextView) findViewById(R.id.textView2);
        TextView lonTextView = (TextView) findViewById(R.id.textView3);
        TextView accTextView = (TextView) findViewById(R.id.textView4);
        TextView altTextView = (TextView) findViewById(R.id.textView5);
        latTextView.setText("Latitude: " + location.getLatitude());
        lonTextView.setText("Longitude: " + location.getLongitude());
        altTextView.setText("Latitude: " + location.getAltitude());
        accTextView.setText("Latitude: " + location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                Log.i("place info", listAddresses.get(0).toString());
                address = "Address: \n";
                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + ", ";
                }
                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + ", ";
                }
                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + ", ";
                }
                if (listAddresses.get(0).getCountryName() != null) {
                    address += listAddresses.get(0).getCountryName();
                }
            }
            TextView addressTextView = (TextView) findViewById(R.id.textView6);
            addressTextView.setText(address);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

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
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //we have permission
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                updateLocationInfo(location);
            }

        }
    }
}