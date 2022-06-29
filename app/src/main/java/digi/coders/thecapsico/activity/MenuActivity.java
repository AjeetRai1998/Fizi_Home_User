package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.MyViewPagerAdapter;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Coupon;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    ViewPager viewPager;
    public static Merchant name;
    ImageView left,right;
    int currentPage=0;
    SingleTask singleTask;
    RelativeLayout line_menu;
    TextView noText;
    LinearLayout bannerBottom;
    TextView addressFull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        singleTask=(SingleTask)getApplication();
        TextView textView=findViewById(R.id.rest_name);
         line_menu=findViewById(R.id.line_pager);
         noText=findViewById(R.id.noText);
        bannerBottom=findViewById(R.id.bannerBottom);
        addressFull=findViewById(R.id.addressFull);
        textView.setText(name.getName());
        findViewById(R.id.back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
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
                if(addresses.get(0).getSubLocality()!=null) {
                   addressFull.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                  }else{
                    addressFull.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());

                }
                  }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        left=findViewById(R.id.left);
        right=findViewById(R.id.right);
        left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(currentPage-1);
                    }
                }
        );

        right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(currentPage+1);
                    }
                }
        );

        loadMenu();
    }

    private void loadMenu() {
        ShowProgress.getShowProgress(this).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getMenus(user.getId(),name.getId(),"0","100");
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
                                    coupons, getApplicationContext(),0);
                            viewPager = findViewById(R.id.menuItems);
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
                                            currentPage = position;
                                            if (position == coupons.size()-1) {
                                                right.setVisibility(View.INVISIBLE);
                                                left.setVisibility(View.VISIBLE);
                                            } else if (position == 0) {
                                                left.setVisibility(View.INVISIBLE);
                                                right.setVisibility(View.VISIBLE);
                                            } else {
                                                left.setVisibility(View.VISIBLE);
                                                right.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    }
                            );

                            line_menu.setVisibility(View.VISIBLE);
                            noText.setVisibility(View.GONE);
                            bannerBottom.setVisibility(View.VISIBLE);
                        }else{
                            line_menu.setVisibility(View.GONE);
                            bannerBottom.setVisibility(View.GONE);
                            noText.setVisibility(View.VISIBLE);
//                            Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        line_menu.setVisibility(View.GONE);
                        bannerBottom.setVisibility(View.GONE);
                        noText.setVisibility(View.VISIBLE);
//                        Toast.makeText(MenuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                ShowProgress.getShowProgress(MenuActivity.this).hide();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                line_menu.setVisibility(View.GONE);
                bannerBottom.setVisibility(View.GONE);
                noText.setVisibility(View.VISIBLE);
                ShowProgress.getShowProgress(MenuActivity.this).hide();
//                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}