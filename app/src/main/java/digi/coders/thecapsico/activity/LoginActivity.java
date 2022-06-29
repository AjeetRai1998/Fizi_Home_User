package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityLoginBinding;
import digi.coders.thecapsico.helper.AppSignatureHashHelper;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ProgressDisplay;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements TextWatcher,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private MaterialButton login;
    private EditText mobileno;
    private SingleTask singleTask;
    String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    ActivityLoginBinding binding;
//    private ProgressDialog progressDialog;

    ProgressDisplay progressDisplay;
    String btnLoginStatus="",user_id="";
    public static Activity LoginActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoginActivity=this;
        initView();
        progressDisplay=new ProgressDisplay(this);

        singleTask=(SingleTask)getApplication();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .enableAutoManage(this,this)
                .build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        Glide.with(this)
                .load(R.raw.img_gif_driver)
                .into(binding.imgGif);

        HintRequest hintRequest = new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
                PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
                try {
                    startIntentSenderForResult(intent.getIntentSender(), 1008, null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("", "Could not start hint picker Intent", e);
                }


        mobileno.addTextChangedListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText().toString().equalsIgnoreCase("PROCEED")) {
                    loginUser();
                }else  if(login.getText().toString().equalsIgnoreCase("SEND OTP")) {
                    if(binding.name.getText().toString().length()>0){
                        if(binding.email.getText().toString().length()>0){
                            if(binding.email.getText().toString().contains("@")&&binding.email.getText().toString().contains(".")){
                                uploadDetails(user_id);
                            }else{
                                Toast.makeText(getApplicationContext(), "Please Enter Valid Email ID", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Enter Email ID", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        binding.terms.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourcapsico.com/Home/TermsConditions"));
                        startActivity(intent);
                    }
                }
        );
        binding.policy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourcapsico.com/Home/PrivacyPolicies"));
                        startActivity(intent);
                    }
                }
        );

        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        // This code requires one time to get Hash keys do comment and share key
        Log.i("LOGINACTIVITY", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        appToken= appSignatureHashHelper.getAppSignatures().get(0);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, permission, 500);
        }
    }


    public static String appToken="";
    private void loginUser() {
        ProgressDialog pd=ProgressDialog.show(LoginActivity.this,"","Connecting...");
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.userLogin(binding.mobileno.getText().toString(),appToken);
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
                        String status=jsonObject.getString("status");
                        if(res.equals("success"))
                        {

//                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                          if(status.equalsIgnoreCase("new")){
                              btnLoginStatus="new";
                              user_id=jsonObject.getJSONArray("data").getJSONObject(0).getString("id");
                              binding.cardEmail.setVisibility(View.VISIBLE);
                              binding.cardName.setVisibility(View.VISIBLE);
                              binding.loginButton.setText("SEND OTP");
                          }else{
                              Intent intent=new Intent(LoginActivity.this,OtpActivity.class);
                              intent.putExtra("mobile",binding.mobileno.getText().toString());
                              startActivity(intent);
                              finish();
                              Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                          }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

               pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                pd.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        login=findViewById(R.id.login_button);
        mobileno=findViewById(R.id.mobileno);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length()==10)
        {
            loginUser();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1008:
                if (resultCode == RESULT_OK) {
                    Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
//                    cred.getId====: ====+919*******
                    Log.e("cred.getId", cred.getId());
                    String phoneno=cred.getId();
                    mobileno.setText(phoneno.substring(3));


                } else {
                    // Sim Card not found!
                    Log.e("cred.getId", "1008 else");

                    return;
                }


                break;
        }
    }

    public void uploadDetails(String id){
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        progressDisplay.showProgress();
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.updateProfile(binding.name.getText().toString(),id,binding.email.getText().toString(),"login","",appToken);
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

//                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,OtpActivity.class);
                            intent.putExtra("mobile",binding.mobileno.getText().toString());
                            startActivity(intent);
                            finish();

                        }
                        else
                        {

                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDisplay.hideProgress();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
             progressDisplay.hideProgress();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}