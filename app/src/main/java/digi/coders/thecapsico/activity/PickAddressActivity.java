package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityPickAddressBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.GPSTracker;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import dmax.dialog.SpotsDialog;

public class PickAddressActivity extends AppCompatActivity {

    ActivityPickAddressBinding binding;
    Double latitude = null, longitude = null;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    LocationListener locationListener;
    GoogleMap mMap;
    String location;
    private MaterialButton currentLocation;
    private ProgressDialog progressDialog;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    protected Context context;
    String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static Activity PickAddressActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPickAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PickAddressActivity=this;

        // manually address
        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(PickAddressActivity.this, MapActivity.class);
                in.putExtra("key", 3);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

//                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();

//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    //request for permission
//                    ActivityCompat.requestPermissions(PickAddressActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                }
//                else {
//                    Intent in = new Intent(PickAddressActivity.this, MapActivity.class);
//                    in.putExtra("key", 3);
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(in);
//
//                }

            }
        });
        //getCurrent Location
        binding.getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (ActivityCompat.checkSelfPermission(PickAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PickAddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PickAddressActivity.this,permission,500);
                }else {
                    ProgressDialog progressDialog=new ProgressDialog(PickAddressActivity.this);
                    progressDialog.setMessage("Loading");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                     List<Address> addresses;
                     latitude = new GPSTracker(PickAddressActivity.this).getLatitude();
                    longitude = new GPSTracker(PickAddressActivity.this).getLongitude();
                    Log.i("aaadad11", latitude + "  " + longitude);
                    try {
                        Geocoder geocoder = new Geocoder(PickAddressActivity.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                      //  progressDialog.dismiss();
                        String add= addresses.get(0).getAddressLine(0);
                        if(!add.isEmpty()) {
                            progressDialog.dismiss();
                            Log.i("jmhnbvc", add + "");
                            Constraint c = new Constraint(add, String.valueOf(latitude), String.valueOf(longitude));
                            SharedPrefManagerLocation.getInstance(PickAddressActivity.this).userLocation(c);
                            startActivity(new Intent(PickAddressActivity.this, DashboardActivity.class));
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "We are unable to find your location", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }*/
                Intent in = new Intent(PickAddressActivity.this, MapActivity.class);
                in.putExtra("key", 3);
                startActivity(in);
                finish();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}

