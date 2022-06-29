package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityEditAccountBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {
    TextView change_password;
    ActivityEditAccountBinding binding;
    private String name,email,mobile,dob;
    private ProgressDialog progressDialog;
    private SingleTask singleTask;
    private String image;

    SimpleDateFormat sdf,sdf1;
    DatePickerDialog datePickerDialog;
    TextInputEditText ddob;
    Calendar myCalendar;
    public static Activity EditAccountActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EditAccountActivity=this;
        change_password=findViewById(R.id.change_password);
//        back_icon=findViewById(R.id.back_icon);
        singleTask=(SingleTask)getApplication();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
            }
        });
        //handle back

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                 dob=myCalendar.getTimeInMillis()+"";
//                 myCalendar.getTimeInMillis();
//                dob=new Date(sdf1.format(myCalendar.getTime()));
                String day="",month="";
                if(dayOfMonth<10){
                    day="0"+dayOfMonth;
                }else{
                    day=dayOfMonth+"";
                }
                if((monthOfYear+1)<10){
                    month="0"+(monthOfYear+1);
                }else{
                    month=(monthOfYear+1)+"";
                }
                binding.dob.setText(month+"/"+day+"/"+year);
//                  binding.dobEdittext.setText(sdf1.format(myCalendar.getTime()));
            }
        };

        binding.dob.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(EditAccountActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
        );

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadProfileDetils();
        binding.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),101);
                }
            }
        });
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid())
                {
                    String js=singleTask.getValue("user");
                    User user=new Gson().fromJson(js,User.class);
                    progressDialog.show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.updateProfile(name,user.getId(),email,"update",binding.dob.getText().toString(),"");
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
                                        progressDialog.hide();
                                        Toast.makeText(EditAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        finish();
                                        loadProfileDetils();
                                        /*JSONArray jsonArray1=jsonObject.getJSONArray("data");
                                        JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                                        singleTask.addValue("user",jsonObject1);
                                        //startActivity(new Intent(OtpActivity.this,OtpActivity.class));
                                        startActivity(new Intent(EditAccountActivity.this, PickAddressActivity.class));*/
                                    }
                                    else
                                    {
                                        progressDialog.hide();
                                        Toast.makeText(EditAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            progressDialog.hide();
                            Toast.makeText(EditAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                }
        });
        binding.updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!image.isEmpty())
                {
                    String js=singleTask.getValue("user");
                    User user=new Gson().fromJson(js,User.class);
                    progressDialog.show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.updatePhoto(image,user.getName(),user.getId());
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
                                        progressDialog.hide();
                                        Toast.makeText(EditAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        finish();
                                        /*JSONArray jsonArray1=jsonObject.getJSONArray("data");
                                        JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                                        singleTask.addValue("user",jsonObject1);
                                        //startActivity(new Intent(OtpActivity.this,OtpActivity.class));
                                        startActivity(new Intent(EditAccountActivity.this, PickAddressActivity.class));*/
                                    }
                                    else
                                    {
                                        progressDialog.hide();
                                        Toast.makeText(EditAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {

                            progressDialog.hide();
                            Toast.makeText(EditAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }

        });

        //setUserData();
    }

    private void loadProfileDetils() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getUserProfile(user.getId());
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
                            progressDialog.hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            User user=new Gson().fromJson(jsonObject1.toString(),User.class);
                            singleTask.addValue("user", jsonObject1);
                            setUserData(user);

                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(EditAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                progressDialog.hide();
                Toast.makeText(EditAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserData(User user) {
        binding.umobile.getEditText().setText(user.getMobile());
        binding.uemail.getEditText().setText(user.getEmail());
        binding.uname.getEditText().setText(user.getName());

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(user.getDob());
        binding.dob.setText(user.getDob());


        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.USER+user.getIcon()).into(binding.image);
    }


    private boolean valid() {
        name=binding.uname.getEditText().getText().toString();
        email=binding.uemail.getEditText().getText().toString();
        mobile=binding.umobile.getEditText().getText().toString();
//        dob=binding.dob.getEditText().getText().toString();
        if(TextUtils.isEmpty(name))
        {
            binding.uname.setError("Please Enter your name");
            binding.uname.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(email))
        {
            binding.uemail.setError("Please Enter Email Id");
            binding.uemail.requestFocus();
            return false;
        }

        else if(!email.contains("@")&&!email.endsWith(".com"))
        {
            binding.uemail.setError("Please Enter Valid Email Id");
            binding.uemail.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(mobile))
        {

            binding.umobile.setError("Please Enter Mobile no");
            binding.umobile.requestFocus();
            return false;
        }
        else if(mobile.length()<10)
        {
            binding.umobile.setError("Please Enter 10 digit Mobile no");
            binding.umobile.requestFocus();
            return false;
        }
        else
        {
            return true;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),101);

        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            if(resultCode==RESULT_OK && data!=null)
            {
                Uri uri=data.getData();
                binding.image.setImageURI(uri);
                try {
                    InputStream iStream=getContentResolver().openInputStream(uri);
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        image= Base64.getEncoder().encodeToString(bb);
                        binding.updateProfilePic.setVisibility(View.VISIBLE);

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}