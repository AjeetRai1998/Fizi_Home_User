package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.MyViewPagerAdapter;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.TopBannerAdapter;
import digi.coders.thecapsico.adapter.TopRestAdapter;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Banner2Model;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferPage extends AppCompatActivity implements Refresh {

    RecyclerView horizontalOffer,toprest;

    ViewPager viewPager;
    SingleTask singleTask;
    Timer timer;
    int DELAY_MS=5000,PERIOD_MS=5000;
    Handler handler;
    int currentPage = 0;
    Runnable Update;
    private List<Merchant> merchantList;
    TextView address,fullAddress,addressBottom;
    public static Refresh refresh;
    LinearLayout bannerImage;
    String city,city_id;
    NestedScrollView lineOffer;
    ImageView animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);

        refresh=this;
        singleTask=(SingleTask)getApplicationContext();
        horizontalOffer=findViewById(R.id.horizontalOffer);
        toprest=findViewById(R.id.toprest);
        viewPager=findViewById(R.id.bannerImages);
        address=findViewById(R.id.address);
        fullAddress=findViewById(R.id.fullAddress);
        addressBottom=findViewById(R.id.addressBottom);
        bannerImage=findViewById(R.id.bannerBottom);
        lineOffer=findViewById(R.id.lineOffer);
        animation=findViewById(R.id.animation);

        Glide.with(this)
                .load(R.raw.loader)
                .into(animation);

        findViewById(R.id.back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );


        address.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(OfferPage.this, DeliveryLocationActivity.class);
                        in.putExtra("key", 3);
                        startActivity(in);
                    }
                }
        );

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

//                getCity(addresses.get(0).getLocality());
                if(addresses.get(0).getSubLocality()!=null) {
                    address.setText(addresses.get(0).getSubLocality());
                    fullAddress.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                    addressBottom.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }else{
                    address.setText( addresses.get(0).getLocality());
                    fullAddress.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                    addressBottom.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }

            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadBanner1();

    }


    private void loadBanner1() {

        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getOfferBanner1(user.getId(),city_id,lat,lon);
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
                        if(res.equals("false")) {
                            List<String> coupons = new ArrayList<>();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("rows");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                coupons.add(jsonArray1.getJSONObject(i).getString("img"));

                            }
                            MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(
                                    coupons, getApplicationContext(),1);
                            viewPager.setAdapter(myViewPagerAdapter);
                            int pagerPadding = 16;
                            viewPager.setClipToPadding(true);
                            viewPager.setPageMargin(pagerPadding);
                            viewPager.setPadding(pagerPadding, pagerPadding, pagerPadding, pagerPadding);
                            viewPager.addOnPageChangeListener(
                                    new ViewPager.OnPageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                                        }

                                        @Override
                                        public void onPageSelected(int position) {

                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    }
                            );

                            handler = new Handler();
                            Update = new Runnable() {
                                public void run() {
                                    if (currentPage == coupons.size()) {
                                        currentPage = 0;
                                    }
                                    viewPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            timer = new Timer(); // This will create a new Thread

                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                        }else{
//                            Toast.makeText(OfferPage.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        Toast.makeText(OfferPage.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                loadBanner2();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

//                Toast.makeText(OfferPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void loadBanner2() {

        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getOfferBanner2(user.getId(),city_id,lat,lon);
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
                        if(res.equals("false")) {
                            ArrayList<Banner2Model> coupons = new ArrayList<>();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("rows");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                coupons.add(new Banner2Model(jsonArray1.getJSONObject(i).getString("banner_id"),jsonArray1.getJSONObject(i).getString("img"),jsonArray1.getJSONObject(i).getString("discount")));

                            }
                            horizontalOffer.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
                            horizontalOffer.setHasFixedSize(true);
                            TopBannerAdapter topBannerAdapter=new TopBannerAdapter(coupons,getApplicationContext());
                            horizontalOffer.setAdapter(topBannerAdapter);


                        }else{
//                            Toast.makeText(OfferPage.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        Toast.makeText(OfferPage.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                loadRestaurant();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

//                Toast.makeText(OfferPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadRestaurant() {


        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getAllMerchant(user.getId(), "", "", "", lat, lon);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");

                    if (res.equals("success")) {

                        merchantList = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            if (merchant.getDiscount() != 0) {
                                merchantList.add(merchant);
                            }
                        }

                        Collections.sort(merchantList, new Comparator<Merchant>() {
                            @Override
                            public int compare(Merchant o1, Merchant o2) {
                                return (o2.getDiscount()+"").compareTo(o1.getDiscount()+"");
                            }
                        });


                        toprest.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        toprest.setAdapter(new RestaurantAdapter(1, merchantList));
                        bannerImage.setVisibility(View.VISIBLE);
                        lineOffer.setVisibility(View.VISIBLE);
                        animation.setVisibility(View.GONE);
                    }
                    else {
//                        lineOffer.setVisibility(View.VISIBLE);
//                        animation.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
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

//                getCity(addresses.get(0).getLocality());
                if(addresses.get(0).getSubLocality()!=null) {
                    address.setText(addresses.get(0).getSubLocality());
                    fullAddress.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                    addressBottom.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }else{
                    address.setText( addresses.get(0).getLocality());
                    addressBottom.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }

            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadBanner1();
    }

    private void getCity(String city) {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.getCity(city);
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
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            city_id=jsonObject1.getString("id");
                            loadBanner1();
//                            Toast.makeText(getApplicationContext(),city_id,Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}