package digi.coders.thecapsico.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityAddAddressBinding;
import digi.coders.thecapsico.databinding.ActivityMapBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.FunctionClass;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.AddressResponse.PredictionsItem;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, Refresh {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE =2 ;
    ImageButton back;
    private GoogleMap mMap;
    FunctionClass functionClass;
    LocationManager locationManager;
    LocationListener locationListener;
    FloatingActionButton fab;
    Location main_location;
    TextView Location_txt;
    MaterialButton continueAdd;
    private SingleTask singleTask;
    private boolean isZooming = false;
    private int key;
    private static String address = "";
    private static String state = "";
    private static String city = "";
    private static String userPostalCode = "";
    private static String country = "";
    private static String zip = "";
    private static String userAddressline1 = "";
    private static String latitude;
    private static String longitude;
    ActivityMapBinding binding;
    public static Refresh refresh;
    public static Activity MapActivity;
    public static int status=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MapActivity=this;
        // ask for permissions

        refresh=this;
        checkLocationPermission();

        functionClass = new FunctionClass();
        Location_txt = findViewById(R.id.location);
        continueAdd = findViewById(R.id.continueAdd);
        singleTask = (SingleTask) getApplication();
        key = getIntent().getIntExtra("key", 0);
        continueAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key != 0) {
//                    if (key == 1) {
//                        showAlert();
//                    } else if (key == 2) {
//                        showAlert();
//                    } else if (key == 3) {
           /*             Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        //intent.putExtra("city",city);
                        startActivity(intent);
                        finish();*/
                        showAlert();
//                    }

                }


            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(status==0){
            binding.lineSearch.setVisibility(View.GONE);
        }else{
            binding.lineSearch.setVisibility(View.VISIBLE);
        }
        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MapActivity.this,AddressSearchActivity.class));
//                if (!Places.isInitialized()) {
//                    Places.initialize(MapActivity.this.getApplicationContext(),getResources().getString(R.string.google_maps_key));
//                }
//
//                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG,Place.Field.PHOTO_METADATAS);
//                // Start the autocomplete intent.
//                Intent intent = new Autocomplete.IntentBuilder(
//                        AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(MapActivity.this);
//                MapActivity.this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                startActivity(new Intent(getApplicationContext(),SearchAddressActivity.class));
            }
        });




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 100000, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 100000, locationListener);

                            try {
                                moveCamera(new LatLng(main_location.getLatitude(), main_location.getLongitude()));
                                binding.imgSearch.setText("Search Other address Here");
                            } catch (Exception e) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }

                        } else {
                            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }

                    }
                }
        );
        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number", 0) == 0) {
            // Zoom into users location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    /*googleMap.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(),location.getLongitude()))
                            .radius(10000)
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE));*/
                    List<Address> addresses = functionClass.getAddresses(location.getLatitude(), location.getLongitude(), MapActivity.this);
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    zip = addresses.get(0).getPostalCode();
                    country = addresses.get(0).getCountryName();
                    latitude = String.valueOf(addresses.get(0).getLatitude());
                    longitude = String.valueOf(addresses.get(0).getLongitude());

                    String featur = addresses.get(0).getFeatureName();
                    Location_txt.setText(address);
                    Constraint c =new Constraint(address, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), featur);
                    SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                    main_location = location;
                    centreMapOnLocation(location, "Your Location");
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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 100000, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 100000, locationListener);

                Location lastKnownLocation1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                main_location = lastKnownLocation1;
                centreMapOnLocation(lastKnownLocation1, "Your Location");
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                if (cameraPosition != null) {
                    List<Address> list = functionClass.getAddresses(cameraPosition.target.latitude, cameraPosition.target.longitude, getApplicationContext());
                   if(list!=null) {
                       if (list.size() > 0) {
                           String feature = list.get(0).getFeatureName();
                           Location_txt.setText(list.get(0).getAddressLine(0));
                           //Log.e("hello", list.toString());
                           address = list.get(0).getAddressLine(0);
                           city = list.get(0).getLocality();
                           state = list.get(0).getAdminArea();
                           zip = list.get(0).getPostalCode();
                           country = list.get(0).getCountryName();
                           latitude = String.valueOf(list.get(0).getLatitude());
                           longitude = String.valueOf(list.get(0).getLongitude());
                           Constraint c = new Constraint(address, String.valueOf(list.get(0).getLatitude()), String.valueOf(list.get(0).getLongitude()), feature);
                           SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                           SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                           Location_txt.setText(address);
                       }
                   }
                }

                //Constraint.lo=Location_txt.getText().toString();
                /*Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                LatLng userLocation = new LatLng(cameraPosition.target.latitude, cameraPosition.target.latitude);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                    //Log.i("address", latitude+"");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String zip = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();
                */

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("requesecodea", requestCode + "");

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1000, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centreMapOnLocation(lastKnownLocation, "Your Location");

            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(MapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    public void centreMapOnLocation(Location location, String title) {

        if (location != null) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                //Log.i("address", latitude+"");

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("dsd", addresses + "");
            if(addresses!=null) {
                if(addresses.size()>0) {
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    this.city = addresses.get(0).getSubLocality();
                    state = addresses.get(0).getAdminArea();
                    zip = addresses.get(0).getPostalCode();
                    country = addresses.get(0).getCountryName();
                    latitude = String.valueOf(addresses.get(0).getLatitude());
                    longitude = String.valueOf(addresses.get(0).getLongitude());
                    String featur = addresses.get(0).getFeatureName();
                    Constraint c = new Constraint(address, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), featur);
                    SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                    SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                    Location_txt.setText(address);
                    //Constraint.Loc=Location_txt.getText().toString();
                    addMarker(Double.parseDouble(latitude), Double.parseDouble(longitude));
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_LONG).show();

            if(!checkLocationPermission()) {

                // ask for permission
                Intent a = new Intent(MapActivity.this, LocationOffActivity.class);
                startActivity(a);
            }
            //finish();

        }


    }


    private void addMarker(double latitude,double longitude)
    {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("You Are Here");
        // Changing marker icon
        int height = 60;
        int width = 60;
        //@SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.location_new_ico);
        //Bitmap b=bitmapdraw.getBitmap();
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.map_marker);
        Bitmap smallMarker = Bitmap.createScaledBitmap(icon, width, height, false);
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(latitude, longitude));
        circleOptions.radius(700);
        circleOptions.fillColor(Color.TRANSPARENT);
        circleOptions.strokeWidth(6);
        mMap.addCircle(circleOptions);
        //geocoder = new Geocoder(MapsActivity.this, getDefault());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        mMap.addMarker(markerOptions.title(String.valueOf("your location")));
        mMap.clear();
        LatLng userLocation = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17.5f));
        moveCamera(userLocation);
        mMap.addMarker(marker);


    }
    public void moveCamera(LatLng latLng) {
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17.5f).bearing(0).tilt(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos), null);
    }

    private String flatNo, typeOfAddress, pincode, fullAddress, landmark;

    private boolean valid(ActivityAddAddressBinding binding) {
        flatNo = binding.addressline1.getText().toString();
        pincode = binding.citydetails.getText().toString();
        fullAddress = binding.addressline2.getText().toString();
        typeOfAddress = binding.addressType.getText().toString();
        landmark = binding.landmark.getText().toString();

        if (TextUtils.isEmpty(typeOfAddress)) {
            binding.addressType.setError("Please Enter Address Type");
            binding.addressType.requestFocus();

            return false;

        } else if (TextUtils.isEmpty(landmark)) {
            binding.landmark.setError("Please Enter Landmark");
            binding.landmark.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(flatNo)) {
            binding.addressline1.setError("Please Enter Flat No");
            binding.addressline1.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(fullAddress)) {
            binding.addressline2.setError("Please Enter Full address");
            binding.addressline2.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(pincode)) {
            binding.citydetails.setError("Please Enter Pincode");
            binding.citydetails.requestFocus();
            return false;

        } else {
            return true;
        }


    }

    private void showAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
        View view = LayoutInflater.from(MapActivity.this).inflate(R.layout.activity_add_address, null);
        ActivityAddAddressBinding binding = ActivityAddAddressBinding.bind(view);
        binding.addressline1.setText("");
        binding.addressType.setText("");
        binding.landmark.setText("");
        binding.addressline2.setText(address);
        binding.citydetails.setText(city);
        AlertDialog dialog = alert.create();
        binding.fabSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid(binding)) {
                    ShowProgress.getShowProgress(MapActivity.this).show();
                    String js = singleTask.getValue("user");
                    User user = new Gson().fromJson(js, User.class);
                    MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                    Log.e("sdsd",latitude+"sdsds"+longitude);
                    if(zip==null){
                        zip="273164";
                    }
                    Call<JsonArray> call = myApi.addAddress(user.getId(), latitude, longitude, typeOfAddress, fullAddress, flatNo, landmark, zip);
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String res = jsonObject.getString("res");
                                    String msg = jsonObject.getString("message");
                                    ShowProgress.getShowProgress(MapActivity.this).hide();
                                    if (res.equals("success")) {

                                        Toast.makeText(MapActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        if(key==3)
                                        {
                                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                            //intent.putExtra("city",city);
                                            startActivity(intent);
                                            finish();

                                        }
                                        else {
                                            Intent in=new Intent(MapActivity.this, DeliveryLocationActivity.class);
                                            in.putExtra("key",key);
                                            startActivity(in);
                                            finish();
                                        }
                                       /* JSONArray jsonArray1=jsonObject.getJSONArray("data");
                                        JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                                        singleTask.addValue("user",jsonObject1);
                                        //startActivity(new Intent(OtpActivity.this,OtpActivity.class));
                                        startActivity(new Intent(EditAccountActivity.this, PickAddressActivity.class));*/
                                    } else {

                                        Toast.makeText(MapActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {

                            ShowProgress.getShowProgress(MapActivity.this).hide();
                            Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
        alert.setView(view);


        alert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                String userAddress = place.getAddress();
                Log.e("dsds", place.getAttributions() + "");
                //  addressdetails=place.getAddressComponents();
                binding.imgSearch.setText("" + userAddress);
                Location_txt.setText(userAddress);
                address = place.getAddress();
                LatLng loc = place.getLatLng();
                latitude = String.valueOf(loc.latitude);
                longitude = String.valueOf(loc.longitude);
                //centreMapOnLocation();
                //onMapReady(mMap);
                moveCamera(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                //addMarker(Double.parseDouble(latitude),Double.parseDouble(longitude));
                Constraint c = new Constraint(address, String.valueOf(loc.latitude), String.valueOf(loc.longitude), "");
                if (!SharedPrefManagerLocation.getInstance(MapActivity.this).locationModel(Constraint.LOCATION).isEmpty()) {
                    SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                    SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                }


                //Constraint c = new Constraint(address, String.valueOf(list.get(0).getLatitude()), String.valueOf(list.get(0).getLongitude()), feature);
                //SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                Location_txt.setText(address);
                //mLatitude = place.getLatLng().latitude;
                //mLongitude = place.getLatLng().longitude;
                //place_id = place.getId();
                // place_url = String.valueOf(place.getWebsiteUri());bb
                //addMarker();
            }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        if(seLocation!=null) {
            binding.imgSearch.setText("" + predictionsItem.getDescription());
            Location_txt.setText(predictionsItem.getDescription());
            address = predictionsItem.getDescription();
            LatLng loc = new LatLng(seLocation.getLat(), seLocation.getLng());
            latitude = String.valueOf(loc.latitude);
            longitude = String.valueOf(loc.longitude);
            //centreMapOnLocation();
            //onMapReady(mMap);
            moveCamera(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
            //addMarker(Double.parseDouble(latitude),Double.parseDouble(longitude));
            Constraint c = new Constraint(address, String.valueOf(loc.latitude), String.valueOf(loc.longitude), "");
            if (!SharedPrefManagerLocation.getInstance(MapActivity.this).locationModel(Constraint.LOCATION).isEmpty()) {
                SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
            }


            //Constraint c = new Constraint(address, String.valueOf(list.get(0).getLatitude()), String.valueOf(list.get(0).getLongitude()), feature);
            //SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
            Location_txt.setText(address);
            //mLatitude = place.getLatLng().latitude;
            //mLongitude = place.getLatLng().longitude;
            //place_id = place.getId();
            // place_url = String.valueOf(place.getWebsiteUri());bb
            //addMarker();
        }
    }

    public static PredictionsItem predictionsItem;
    public static digi.coders.thecapsico.model.LatLongRespons.Location seLocation;
    /*private void addMarker() {
        CameraUpdate cameraUpdate;
        String SPACE = " , ";
        LatLng coordinate = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        if (mMap != null) {
            MarkerOptions markerOptions;
            try {
                mMap.clear();
                //imgSearch.setText("" + userAddress);
                //markerOptions = new MarkerOptions().position(coordinate).title(userAddress).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_pointer",100,100)));
                if(isZooming) {
                    //  camera will not Update
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, mMap.getCameraPosition().zoom);
                }else {
                    // camera will Update zoom
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, 18);


                }

                mMap.animateCamera(cameraUpdate);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                Marker marker = mMap.addMarker(markerOptions);
                //marker.showInfoWindow();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try{
            //userAddressline2 = userAddressline2.substring(0, userAddressline2.indexOf(userCity));
            // userAddressline.replace(userCity,"");
            //  userAddressline.replace(userPostalCode,"");
            //   userAddressline.replace(userState,"");
            //  userAddressline.replace(userCountry,"");
        }catch (Exception ex){ Log.d("TAG","address error "+ex);}

        try {
            *//*if(useAddress!=null)
            {


            }
            else {
                addressline2.setText(userAddressline2);
                //citydetail.setText(userCity+SPACE+userPostalCode+SPACE+userState+SPACE+userCountry);
                citydetail.setText(userPostalCode);
            }*//*

        }catch (Exception ex){ ex.printStackTrace();}

    }
*/
}