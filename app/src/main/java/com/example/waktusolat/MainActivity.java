package com.example.waktusolat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    TextView mLocationTv;
    Geocoder geocoder;
    List<Address> addresses;
    String city;

    String url;
    TextView mFajrTv, mDhuhrTv, mAsrTv, mMaghribTv, mIsyaTv, mDateTv;
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    private static final String TAG = "tag";
    String longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationTv = findViewById(R.id.locationTv);
        mFajrTv = findViewById(R.id.fajrTv);
        mDhuhrTv = findViewById(R.id.dhuhrTv);
        mAsrTv = findViewById(R.id.asrTv);
        mMaghribTv = findViewById(R.id.MagrbTv);
        mIsyaTv = findViewById(R.id.IsyaTv);
        mDateTv = findViewById(R.id.dateTv);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        getLocation();


        /*pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get date
                            String date = response.getJSONArray("items").getJSONObject(0).get("date_for").toString();

                            //get timming
                            String txtSubuh = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                            String txtShuroq = response.getJSONArray("items").getJSONObject(0).get("shurooq").toString();
                            String txtDzuhur = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                            String txtAshar = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                            String txtMaghrib = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                            String txtIsya = response.getJSONArray("items").getJSONObject(0).get("isha").toString();
                            mFajrTv.setText(txtSubuh);
                            mShuroqTv.setText(txtShuroq);
                            mDhuhrTv.setText(txtDzuhur);
                            mAsrTv.setText(txtAshar);
                            mMaghribTv.setText(txtMaghrib);
                            mIsyaTv.setText(txtIsya);
                            mDateTv.setText(date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: "+error.getMessage());
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
*/
    }


    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMesssageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if ((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    geocoder = new Geocoder(this);
                    double latt = location.getLatitude();
                    double longi = location.getLongitude();
                    latitude = String.valueOf(latt);
                    longitude = String.valueOf(longi);

                    try {
                        addresses = geocoder.getFromLocation(latt, longi, 1);
                        city = addresses.get(0).getLocality();
                        mLocationTv.setText(String.valueOf(city));
                        url = "https://muslimsalat.com/" + city + ".json?key=5fe91155ab8498eb43bd3aae6755c72c";
                        searchLoc();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
                        private void searchLoc() {
                            pDialog = new ProgressDialog(this);
                            pDialog.setMessage("Loading");
                            pDialog.show();

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                    url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                //get date
                                                String date = response.getJSONArray("items").getJSONObject(0).get("date_for").toString();

                                                //get timming
                                                String txtSubuh = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                                                String txtDzuhur = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                                                String txtAshar = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                                                String txtMaghrib = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                                                String txtIsya = response.getJSONArray("items").getJSONObject(0).get("isha").toString();
                                                mFajrTv.setText(txtSubuh);
                                                mDhuhrTv.setText(txtDzuhur);
                                                mAsrTv.setText(txtAshar);
                                                mMaghribTv.setText(txtMaghrib);
                                                mIsyaTv.setText(txtIsya);
                                                mDateTv.setText(date);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            pDialog.hide();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    pDialog.hide();
                                }
                            });
                            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
                        }

                    private void buildAlertMesssageNoGps() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Please Turn ON your GPS Connection")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

}