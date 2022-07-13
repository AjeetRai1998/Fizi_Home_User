package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityProfileBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ProgressDisplay;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {


    ActivityProfileBinding binding;
    private SingleTask singleTask;
    public static Activity ProfileActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ProfileActivity=this;

        singleTask=(SingleTask)getApplication();
        //handle back

        binding.returnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,ReturnOrder.class));

            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardActivity.clickStatus=0;
                startActivity(new Intent(ProfileActivity.this,DashboardActivity.class));
                finish();
            }
        });
        //handle setting

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,AccountSettingActivity.class));
            }
        });


        //orders
        binding.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MyOrdersActivity.class));
            }
        });

        //about

        binding.aboutCapsico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this,AboutUsActivity.class));
//                Intent in=new Intent(ProfileActivity.this,WebActivity.class);
//                in.putExtra("key",2);
//                startActivity(in);
            }
        });

        //my wallet
        binding.myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,CapsicoMoneyActivity.class));
            }
        });

        //help
        binding.needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,ChatActivity.class));
            }
        });

        binding.mywishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,WishlistActivity.class));
            }
        });

        binding.lineInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermission()) {
                    new convert().execute();
                }
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                  sendIntent.setType("text/plain");
//                Intent shareIntent = Intent.createChooser(sendIntent, null);
//                startActivity(shareIntent);
            }
        });


        binding.lineRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            showRateDialoge1(ProfileActivity.this);
            }
        });

        binding.lineBecomeDeliveryPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=digi.coders.capsicodeliverypartner"));
                startActivity(intent);
            }
        });

        binding.lineBecomeRestaurantPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=digi.coders.capsicostorepartner&hl=en"));
                startActivity(intent);
            }
        });


        setUserData();

    }

    private void setUserData() {
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
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String res=jsonObject1.getString("res");
                        if(res.equals("success"))
                        {
                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            JSONObject jsonObject2=jsonArray1.getJSONObject(0);
                            singleTask.addValue("user",jsonObject2);
                            User user=new Gson().fromJson(jsonObject2.toString(),User.class);
                            setData(user);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }

    private void setData(User user) {
        if(user!=null)
        {
            if(!user.getName().isEmpty())
            {
                binding.name.setText(user.getName());
            }
            binding.mobileno.setText(user.getMobile());
            if(!user.getEmail().isEmpty())
            {
                binding.email.setText(user.getEmail());
            }
            binding.walletMoney.setText("Wallet : \u20b9"+user.getWallet());
        }
    }

    /*@Override
    public void onBackPressed() {
        *//*startActivity(new Intent(ProfileActivity.this,DashboardActivity.class));
        finish();*//*
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DashboardActivity.clickStatus=0;
    }


    public class convert extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

            progressDialog=ProgressDialog.show(ProfileActivity.this,"","Loading...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Bitmap imgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_image);
                String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), imgBitmap, "title", null);
                Uri imgBitmapUri = Uri.parse(imgBitmapPath);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                shareIntent.setType("image/png");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "*Hey, I just discovered a food delivery app in our city!*\nCheck out Capsico, Our city's most loved food delivery app.\n\nDownload App now using below link\n\nYou will get discounts and lightning fast delivery on your every order.\n\nDownload now :\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject text");
                startActivity(Intent.createChooser(shareIntent, "Share this"));
            }catch(Exception e){
                Log.d("TAG",e.getMessage().toString());
            }

            return null;
        }
    }

    private boolean checkPermission() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED || camera1 != PackageManager.PERMISSION_GRANTED ) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
            return false;
        }
        return true;
    }
    public void showRateDialoge1(Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.rate_app_dialoge1, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView cancel=promptView.findViewById(R.id.cancel);
        LinearLayout lineUnsatisfied=promptView.findViewById(R.id.lineUnsatisfied);
        LinearLayout lineLovedIt=promptView.findViewById(R.id.lineLovedIt);
        LinearLayout lineExpand=promptView.findViewById(R.id.lineExpand);
        EditText edit_text=promptView.findViewById(R.id.edit_text);
        TextView submit=promptView.findViewById(R.id.submit);


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        lineUnsatisfied.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lineExpand.setVisibility(View.VISIBLE);
                    }
                }
        );

        lineLovedIt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lineExpand.setVisibility(View.GONE);
                        showRateDialoge2(ctx);
                        alert2.dismiss();
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edit_text.getText().toString().length()>0){
                            submitFeedback(edit_text.getText().toString(),alert2);
                        }
                    }
                }
        );
//        alert2.setCanceledOnTouchOutside(false);

        alert2.show();

    }
    public void showRateDialoge2(Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.rate_app_dialoge2, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView cancel=promptView.findViewById(R.id.nope);
        TextView sure=promptView.findViewById(R.id.sure);


        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        sure.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
                        startActivity(intent);
                    }
                }
        );


//        alert2.setCanceledOnTouchOutside(false);

        alert2.show();

    }

    private void submitFeedback(String txt,android.app.AlertDialog alert2) {
        ProgressDialog progressDialog=ProgressDialog.show(ProfileActivity.this,"","Loading");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.submitFeedback(user.getId(),txt);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                        String res=jsonObject.getString("res");
                        if(res.equals("success"))
                        {
                            alert2.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();
            }
        });

    }
}