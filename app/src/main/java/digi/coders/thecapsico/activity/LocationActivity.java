package digi.coders.thecapsico.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.maps.model.AddressComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.AddressAdapter;
import digi.coders.thecapsico.databinding.ActivityLocationBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.GPSTracker;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity  implements OnMapReadyCallback, LocationListener{
    private static final int ADDRESS_PICKER_REQUEST =105 ;
    private RecyclerView addressList;
    private LinearLayout currentLocation;
    protected LocationManager locationManager;
    Double latitude=null,longitude=null;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    LocationListener locationListener;
    GoogleMap mMap;
    String location;
    private List<UserAddress> addressLis;
    protected Context context;
    String[] permission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    private ProgressDialog progressDialog;
    private EditText address;
    ActivityLocationBinding binding;
    private SingleTask singleTask;
    public static Activity LocationActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocationActivity=this;
        initView();
        singleTask=(SingleTask)getApplication();
        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        loadSavedAddress();
        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(LocationActivity.this,MapActivity.class);
                in.putExtra("key",3);
                startActivity(in);
                finish();
            }
        });
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(LocationActivity.this,MapActivity.class);
                in.putExtra("key",3);
                startActivity(in);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress =data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */
                    address.setText(new StringBuilder().append("addressline2: ").append
                            (completeAddress.getString("addressline2")).append("\ncity: ").append
                            (completeAddress.getString("city")).append("\npostalcode: ").append
                            (completeAddress.getString("postalcode")).append("\nstate: ").append
                            (completeAddress.getString("state")).toString());


                    /*txtLatLong.setText(new StringBuilder().append("Lat:").append(currentLatitude).append
                            ("  Long:").append(currentLongitude).toString());*/

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this,permission,500);
        }else {
            List<Address> addresses;
            latitude = new GPSTracker(LocationActivity.this).getLatitude();
            longitude = new GPSTracker(LocationActivity.this).getLongitude();
            Log.i("aaadad11",latitude+"  "+longitude);
            try {
                Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                Log.i("jmhnbvc",addresses+"");
                progressDialog.dismiss();
                String add = addresses.get(0).getAddressLine(0);
                String featur=addresses.get(0).getFeatureName();
                address.setText(add);
                Constraint c = new Constraint(add, String.valueOf(latitude), String.valueOf(longitude),featur);
                SharedPrefManagerLocation.getInstance(LocationActivity.this).userLocation(c);
                //startActivity(new Intent(LocationActivity.this, DashboardActivity.class));
                //finish();


            }catch (Exception e){
                e.printStackTrace();
                Log.i("asdaf",e.getMessage());
            }
        }


    }



    @Override
    public void onLocationChanged(android.location.Location location) {
        //txtLat = (TextView) findViewById(R.id.location);
        //getLocation(location.getLatitude(),location.getLongitude());
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        getLocation();

       /* Constraint.Latitude=String.valueOf(location.getLatitude());
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
        //Constraint.Loc=location;


//        return ;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.484,80.89938), 17.5f));

    }


    public  String getLocation() {

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

        return address + " , " + city + " , " + state + " , " + zip + " , " + country;

    }


    private void initView() {
        addressList=findViewById(R.id.address_list);
        currentLocation=findViewById(R.id.current_location);
        address=findViewById(R.id.address);
    }

    private void loadSavedAddress() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllAddress(user.getId(),"");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        if(res.equals("success"))
                        {
                            Toast.makeText(LocationActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                                        addressLis=new ArrayList<>();
                                        for(int i=0;i<jsonArray1.length();i++)
                                        {
                                            UserAddress address=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),UserAddress.class);
                                            addressLis.add(address);

                                        }
                                        addressList.setLayoutManager(new LinearLayoutManager(LocationActivity.this,LinearLayoutManager.VERTICAL,false));
                                        AddressAdapter adapter=new AddressAdapter(1,addressLis,singleTask);
                                        addressList.setAdapter(adapter);
                                        adapter.findPosition(new AddressAdapter.GetPosition() {
                                            @Override
                                            public void position(View v, int position,UserAddress address) {
                                                Constraint constraint=new Constraint(address.getAddress(),address.getLatitude(),address.getLongitude(),address.getCity());
                                                SharedPrefManagerLocation.getInstance(LocationActivity.this).userLocation(constraint);
                                                startActivity(new Intent(LocationActivity.this,DashboardActivity.class));
                                                finish();

                                            }
                                        });
                        }
                        else
                        {

                            Toast.makeText(LocationActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Toast.makeText(LocationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void goBack(View view) {
        finish();
    }
}