package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.AddressAdapter;
import digi.coders.thecapsico.databinding.ActivityDeliveryLocationBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryLocationActivity extends AppCompatActivity implements Refresh  {

    ActivityDeliveryLocationBinding binding;
    private List<UserAddress> addressLis;
    private SingleTask singleTask;
    private int key;

    public static Refresh refresh;
    public static Activity DeliveryLocationActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeliveryLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DeliveryLocationActivity=this;
        refresh=this;

        singleTask=(SingleTask)getApplication();
        key=getIntent().getIntExtra("key",0);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.asap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.asapImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_radio_button_checked_24));
                binding.preOrderImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_radio_button_unchecked_24));
                Intent in=new Intent(DeliveryLocationActivity.this,CheckOutActivity.class);
                in.putExtra("option","ASAP");
                startActivity(in);
                finish();

            }
        });
        binding.preOrderForLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.asapImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_radio_button_unchecked_24));
                binding.preOrderImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_radio_button_checked_24));
                Intent in=new Intent(DeliveryLocationActivity.this,CheckOutActivity.class);
                in.putExtra("option","PRE_ORDER FOR LATE");
                startActivity(in);
                finish();
            }
        });
        binding.currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.status=0;
                //Toast.makeText(DeliveryLocationActivity.this, "current location found", Toast.LENGTH_SHORT).show();
                Intent in=new Intent(DeliveryLocationActivity.this,MapActivity.class);
                in.putExtra("key",key);
                startActivity(in);
                finish();


            }
        });


        binding.newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.status=1;
                Intent in=new Intent(DeliveryLocationActivity.this,MapActivity.class);
                in.putExtra("key",key);
                startActivity(in);
                finish();
                //startActivity(new Intent(DeliveryLocationActivity.this,LocationActivity.class));
            }
        });

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DashboardActivity.refresh!=null) {
                    DashboardActivity.refresh.onRefresh();
                }
                finish();
            }
        });

        loadAddressList();
    }

    private void loadAddressList() {

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
                            binding.progressBar.setVisibility(View.GONE);

                            //Toast.makeText(DeliveryLocationActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            addressLis=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                UserAddress address=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),UserAddress.class);
                               if(CheckOutActivity.address!=null) {
                                   if (!address.getId().equalsIgnoreCase(CheckOutActivity.address.getId())) {
                                       CheckOutActivity.address = null;
                                   }
                               }
                                addressLis.add(address);

                            }
                            binding.addressList.setLayoutManager(new LinearLayoutManager(DeliveryLocationActivity.this,LinearLayoutManager.VERTICAL,false));

                            AddressAdapter adapter=new AddressAdapter(1,addressLis,singleTask);
                            adapter.findPosition(new AddressAdapter.GetPosition() {
                                @Override
                                public void position(View v, int position,UserAddress addres) {
                                    UserAddress address=addressLis.get(position);
                                    CheckOutActivity.address=address;
                                    if(key==3)
                                    {
//                                        Intent in = new Intent(DeliveryLocationActivity.this, DashboardActivity.class);

                                        Constraint c = new Constraint(address.getAddress(), String.valueOf(address.getLatitude()), String.valueOf(addres.getLongitude()), "");
                                        Log.e("loc",c.getLoc()+c.getLatitude()+"sds"+c.getLongitude());
                                        SharedPrefManagerLocation.getInstance(DeliveryLocationActivity.this).logout();
                                        SharedPrefManagerLocation.getInstance(DeliveryLocationActivity.this).userLocation(c);

//                                        startActivity(in);
                                        if(DashboardActivity.refresh!=null) {
                                            DashboardActivity.refresh.onRefresh();
                                        }
                                        if(OfferPage.refresh!=null) {
                                            OfferPage.refresh.onRefresh();
                                        }
                                        finish();
                                    }else if(key==10)
                                    {
//                                        Intent in = new Intent(DeliveryLocationActivity.this, DashboardActivity.class);

                                        Constraint c = new Constraint(address.getAddress(), String.valueOf(address.getLatitude()), String.valueOf(addres.getLongitude()), "");
                                        Log.e("loc",c.getLoc()+c.getLatitude()+"sds"+c.getLongitude());
                                        SharedPrefManagerLocation.getInstance(DeliveryLocationActivity.this).logout();
                                        SharedPrefManagerLocation.getInstance(DeliveryLocationActivity.this).userLocation(c);
                                        Intent in = new Intent(DeliveryLocationActivity.this, DashboardActivity.class);
                                        startActivity(in);
                                        finish();
                                    }
                                    else {
                                        AppConstraints.isFromDelLoc=false;
                                        Intent in = new Intent(DeliveryLocationActivity.this, CheckOutActivity.class);
                                        startActivity(in);
                                        finish();
                                        if(CheckOutActivity.checkOut!=null){
                                            CheckOutActivity.checkOut.finish();
                                        }
                                    }

                                }
                            });
                            binding.addressList.setAdapter(adapter);
                        }
                        else
                        {
                            //Toast.makeText(DeliveryLocationActivity.this, msg, Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.lineNoData.setVisibility(View.VISIBLE);
                            CheckOutActivity.address = null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(DeliveryLocationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(DashboardActivity.refresh!=null) {
            DashboardActivity.refresh.onRefresh();
        }
        finish();
    }

    @Override
    public void onRefresh() {
        loadAddressList();
    }
}