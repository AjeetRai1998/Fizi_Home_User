package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.AddressAdapter;
import digi.coders.thecapsico.adapter.CouponAdapter;
import digi.coders.thecapsico.databinding.ActivityCouponBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Coupon;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponActivity extends AppCompatActivity {

    private RecyclerView couponList;

    ActivityCouponBinding binding;
    private SingleTask singleTask;
    private List<Coupon> coupons;
    public  static  String merchantId;
    private int key;
    public static Activity CouponActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCouponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CouponActivity=this;
        singleTask=(SingleTask)getApplication();
        initView();
        key=getIntent().getIntExtra("key",0);
        if(key==1)
        {
            binding.mainLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.mainLayout.setVisibility(View.GONE);
        }
        loadCouponList();
        binding.applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CheckOutActivity.couponCode=binding.couponCode.getText().toString();
//                startActivity(new Intent(CouponActivity.this, CheckOutActivity.class));
            }
        });
    }
    private void loadCouponList() {
        ShowProgress.getShowProgress(this).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call=null;
        if(merchantId!=null)
        {
            Log.e("ddf",merchantId+"");
            call=myApi.getAllCoupons(user.getId(),merchantId,lat,lon,"");
        }
        else
        {
             call=myApi.getAllCoupons(user.getId(),"",lat,lon,"");
        }

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
                        if(res.equals("success")) {
                            ShowProgress.getShowProgress(CouponActivity.this).hide();
//                            Toast.makeText(CouponActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            coupons = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                Coupon coupon = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Coupon.class);

                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = null;
                                try {
                                    date = format1.parse(coupon.getExpiryDate());
                                    Date date1=new Date();
                                    if(date.after(date1)||date.equals(date1)){
                                        coupons.add(coupon);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(CouponActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }


                            }
                            couponList.setLayoutManager(new LinearLayoutManager(CouponActivity.this,LinearLayoutManager.VERTICAL,false));
                            couponList.setAdapter(new CouponAdapter(key,coupons,CouponActivity.this));
                            if(coupons.size()>0) {
                                binding.txt.setVisibility(View.VISIBLE);
                                binding.lineNoData.setVisibility(View.GONE);
                            }else{
                                binding.txt.setVisibility(View.GONE);
                                binding.lineNoData.setVisibility(View.VISIBLE);
                            }

                        }
                        else
                        {
                            ShowProgress.getShowProgress(CouponActivity.this).hide();
                            binding.lineNoData.setVisibility(View.VISIBLE);
                            binding.txt.setVisibility(View.GONE);
                            //Toast.makeText(CouponActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CouponActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(CouponActivity.this).hide();
                Toast.makeText(CouponActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void initView() {
        couponList=findViewById(R.id.coupon_list);
    }

    public void goBack(View view) {
        finish();
    }
}