package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.GPSTracker;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    protected LocationManager locationManager;
    Double latitude=null,longitude=null;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    LocationListener locationListener;
    GoogleMap mMap;
    String location;
    private MaterialButton currentLocation;
    private ProgressDialog progressDialog;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    protected Context context;
    public static Activity ChooseLocationActivity;
    String[] permission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        ChooseLocationActivity=this;
        currentLocation=findViewById(R.id.current_location);
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        //loadLocation();
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog.show();
                progressDialog.show();
                if (ActivityCompat.checkSelfPermission(ChooseLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChooseLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChooseLocationActivity.this,permission,500);
                }else {
                    List<Address> addresses;
                    latitude = new GPSTracker(ChooseLocationActivity.this).getLatitude();
                    longitude = new GPSTracker(ChooseLocationActivity.this).getLongitude();
                    Log.i("aaadad11",latitude+"  "+longitude);
                    try {
                        Geocoder geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        Log.i("jmhnbvc",addresses+"");
                        progressDialog.dismiss();
                        String add = addresses.get(0).getAddressLine(0);
                        /*Constraint.Loc=add;
                        startActivity(new Intent(ChooseLocationActivity.this,MainActivity.class));
                        finish();*/
                       /* currentLocation.setText(add);
                        location=(add);*/

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.i("asdaf",e.getMessage());
                    }
                }


            //                      getLocation();

            }
        });
        /*currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                *//*try {
                    if (ActivityCompat.checkSelfPermission(ChooseLocationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChooseLocationActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1001);

                    }
                    else {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ChooseLocationActivity.this);
                        getLocation(latitude, longitude);
                        Constraint.Loc = location;
                        Log.e("sdsd", location + "");
                        int key = getIntent().getIntExtra("key", 0);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("akey", key);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception e){
                    Log.e("sd",e.getMessage());
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                }*//*


            }

        });*/




    }

    private void loadLocation() {
        int fine = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (fine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(ChooseLocationActivity.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(ChooseLocationActivity.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                // Toast.makeText(getApplicationContext(),"hello ",Toast.LENGTH_SHORT).show();
                try {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    //getLocation(latitude,longitude);
                    getLocation();
                    Log.e("sdsd",latitude+longitude+"");
                    // latLng=new LatLng(latitude,longitude);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        try {
            if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                // Toast.makeText(getApplicationContext(),"hii ",Toast.LENGTH_SHORT).show();
            }
            else if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 0, locationListener);

                //Toast.makeText(getApplicationContext(),"hii ",Toast.LENGTH_SHORT).show();
            }
        }
        catch (SecurityException e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public void onLocationChanged(android.location.Location location) {
        //txtLat = (TextView) findViewById(R.id.location);
        //getLocation(location.getLatitude(),location.getLongitude());
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        /*Constraint.Latitude=String.valueOf(location.getLatitude());
        Constraint.Longitude=String.valueOf(location.getLongitude());*/
        //txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude","status");

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude","enable");

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude","disable");

    }


    public  void getLocation(double latitude,double longitude) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.i("address", latitude+"");

        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();

        // Log.i("address", address + " , " + city + " , " + state + " , " + zip + " , " + country);
        //Location.setText(address + " , " + city + " , " + state + " , " + zip + " , " + country);
        location=(address + " , " + city + " , " + state + " , " + zip + " , " + country);
        progressDialog.dismiss();
        /*Constraint.Loc=location;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();*/

//        return ;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.484,80.89938), 17.5f));

    }


    public  void getLocation() {


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();
        Log.d("address", address + " , " + city + " , " + state + " , " + zip + " , " + country);
        // Toast.makeText(getApplicationContext(), address + " , " + city+" , "+state + " , " + zip+" , "+country, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        //Constraint.Loc=location;
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        //finish();

    }



    public void goBack(View view) {
        finish();
    }

    public void searchOtherLocation(View view) {
        startActivity(new Intent(ChooseLocationActivity.this, LocationActivity.class));
    }
}