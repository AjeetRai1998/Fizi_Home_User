package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.databinding.ActivityBrowesShopBinding;
import digi.coders.thecapsico.databinding.ActivityDashboardBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowesStoreActivity extends AppCompatActivity {

    ImageView back;
    LinearLayout food;
    ActivityBrowesShopBinding binding;
    private String categoryId,categoryName,merchantCategoryId;
    private SingleTask singleTask;

    private List<Merchant> merchantList;
    private RecyclerView shopList;
    public static Activity BrowesStoreActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBrowesShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BrowesStoreActivity=this;
        singleTask=(SingleTask)getApplication();
        categoryId=getIntent().getStringExtra("categoryId");
        categoryName=getIntent().getStringExtra("categoryName");
        merchantCategoryId=getIntent().getStringExtra("merchant_category_id");

        binding.categoryName.setText(categoryName);


        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
            String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.i("jmhnbvc", addresses + "");
            if(addresses.size()>0) {
                String add = addresses.get(0).getAddressLine(0);

                String feature = addresses.get(0).getFeatureName();
                Log.i("feature", feature + "");
                if(addresses.get(0).getSubLocality()!=null) {
                    binding.address.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                    binding.address1.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }else{
                    binding.address.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                    binding.address1.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());

                }
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // load shop list
        loadShopList();
        binding.goBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrowesStoreActivity.this, DashboardActivity.class));
                finish();
            }
        });
    }

    private void loadShopList() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat= SharedPrefManagerLocation.getInstance(BrowesStoreActivity.this).locationModel(Constraint.LATITUDE);
        String lon=SharedPrefManagerLocation.getInstance(BrowesStoreActivity.this).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getAllMerchant(user.getId(),merchantCategoryId,categoryId,"",lat,lon);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    Log.e("res",jsonArray.toString());
                    if (res.equals("success")) {
                        binding.progressBar.setVisibility(View.GONE);
                        merchantList = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        if(jsonArray1.length()>0)
                        {
                            for (int i = 0; i < jsonArray1.length(); i++) {

                                Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                                    /*if (Double.parseDouble(merchant.getDistance()) > Double.parseDouble(merchant.getDistance()))
                                    {
                                        merchantList.add(j,merchant);
                                    }*/
                                String[] cat=merchant.getCategories().split(",");
                                List<String> ar= Arrays.asList(cat);
                                if(ar.contains(categoryId)) {
                                    merchantList.add( merchant);
                                }

                            }
                            binding.shopList.setVisibility(View.VISIBLE);
                            binding.shopList.setLayoutManager(new LinearLayoutManager(BrowesStoreActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.shopList.setAdapter(new RestaurantAdapter(1, merchantList));

                            if(merchantList.size()>=2){
                                binding.bannerBottom.setVisibility(View.VISIBLE);
                            }else{
                                binding.bannerBottom1.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.notAvailable.setVisibility(View.GONE);
                            Toast.makeText(BrowesStoreActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.notAvailable.setVisibility(View.VISIBLE);
                        Toast.makeText(BrowesStoreActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                }
             catch (JSONException e) {
                    e.printStackTrace();
                }
            }


                @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(BrowesStoreActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



}


    }

