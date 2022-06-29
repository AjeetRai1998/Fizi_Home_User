package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticButton;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityOtpBinding;
import digi.coders.thecapsico.helper.AppSignatureHashHelper;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.GPSTracker;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SMSReceiver;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity implements  SMSReceiver.OTPReceiveListener{
    private MaterialButton otpVerify;
    String location;
    protected Context context;
    Double latitude = null, longitude = null;
    String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    ActivityOtpBinding binding;
    private SingleTask singleTask;
    private String mobileno;
    private ProgressDialog progressDialog;
//    SmsVerifyCatcher smsVerifyCatcher;


    public static final String TAG = OtpActivity.class.getSimpleName();

    private SMSReceiver smsReceiver;

    public static Activity OtpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OtpActivity=this;
        singleTask = (SingleTask) getApplication();
        mobileno = getIntent().getStringExtra("mobile");
        initView();

//        new OtpReceiver().setotpView(binding.otpView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadCurrentLocation();
                String otp = binding.otpView.getText().toString();

                verifyOTP(otp);
            }
        });
        binding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                Call<JsonArray> call = myApi.resendOtp(mobileno,LoginActivity.appToken);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String res = jsonObject.getString("res");
                                String msg = jsonObject.getString("message");
                                if (res.equals("success")) {
                                    progressDialog.hide();
                                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();


                                } else {
                                    progressDialog.hide();
                                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        progressDialog.hide();
                        Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
//                String[] otp=message.split("Your OTP Verification Code is ");
//                String[] finalOTP=otp[1].split(". Do not share it with anyone.");
//           ShowAlertDialoge(finalOTP[0],OtpActivity.this);
////                Toast.makeText(getApplicationContext(),finalOTP[0],Toast.LENGTH_LONG).show();
//            }
//        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        startSMSListener();

    }
    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    showToast("API Started");

                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    showToast("API Failed");

                }
            });
        } catch (Exception e) {
            showToast(e.getMessage().toString());
        }
    }


    @Override
    public void onOTPReceived(String otp) {
//        showToast("OTP Received: " + otp);

//        ShowAlertDialoge( otp.split(" ")[0],OtpActivity.this);
        binding.otpView.setText(otp.split(" ")[0]);
        verifyOTP(otp.split(" ")[0]);
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    public void onOTPTimeOut() {
//        showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void ShowAlertDialoge(String text, Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.msg_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final AlertDialog alert2 = alertDialogBuilder.create();

        ElasticButton ok=promptView.findViewById(R.id.ok);
        ElasticButton deny =promptView.findViewById(R.id.deny);
        TextView txt=promptView.findViewById(R.id.txt);

        txt.setText("Your OTP Verification Code is "+text+". Use this code to login into your Capsico account.");
        deny.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.otpView.setText(text);
                        verifyOTP(text);
                        alert2.dismiss();
                    }
                }
        );


        alert2.show();
    }
    void verifyOTP(String otp){
        if (otp.isEmpty()) {
            Toast.makeText(OtpActivity.this, "Please Enter  Otp", Toast.LENGTH_SHORT).show();
        } else if (otp.length() < 6) {

            Toast.makeText(OtpActivity.this, "Please Enter valid 6 digit  Otp", Toast.LENGTH_SHORT).show();

        } else {
            progressDialog.show();
            MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
            Call<JsonArray> call = myApi.verifyOtp(mobileno, binding.otpView.getText().toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String res = jsonObject.getString("res");
                            String msg = jsonObject.getString("message");
                            if (res.equals("success")) {
                                progressDialog.hide();
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                                singleTask.addValue("user", jsonObject1);
                                //startActivity(new Intent(OtpActivity.this,OtpActivity.class));
                                if (ActivityCompat.checkSelfPermission(OtpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OtpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    Intent in = new Intent(OtpActivity.this, GetPermissionActivity.class);
                                    startActivity(in);
                                    finishAffinity();
                                }else {
                                    Intent in = new Intent(OtpActivity.this, DashboardActivity.class);
                                    startActivity(in);
                                    finishAffinity();
                                }
                            } else {
                                progressDialog.hide();
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                    progressDialog.hide();
                    Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void initView() {
        otpVerify = findViewById(R.id.otp_verify);
        binding.mobile.setText(mobileno);
    }

    public void goBack(View view) {
        finish();
    }

    private void loadCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(OtpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OtpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OtpActivity.this, permission, 500);
        } else {
            List<Address> addresses;
            latitude = new GPSTracker(OtpActivity.this).getLatitude();
            longitude = new GPSTracker(OtpActivity.this).getLongitude();
            Log.i("aaadad11", latitude + "  " + longitude);
            try {
                Geocoder geocoder = new Geocoder(OtpActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                Log.i("jmhnbvc", addresses + "");
                String add = addresses.get(0).getAddressLine(0);
                //Constraint.Loc = add;
                //address.setText(add);
                //startActivity(new Intent(OtpActivity.this, MainActivity.class));
                //finish();
                       /* currentLocation.setText(add);
                        location=(add);*/

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("asdaf", e.getMessage());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}