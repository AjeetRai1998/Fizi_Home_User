package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.MyViewPagerAdapter;
import digi.coders.thecapsico.databinding.ActivityStoreDetailsBinding;
import digi.coders.thecapsico.databinding.MoreDetailsDialogBinding;
import digi.coders.thecapsico.fragment.DeliveryFragment;
import digi.coders.thecapsico.fragmentmodel.DeliveryViewModel;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.OrderItem;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class   StoreDetailsActivity extends AppCompatActivity implements Refresh  {

    ActivityStoreDetailsBinding binding;
    public static Merchant merchant,merchant1;
        private SingleTask singleTask;
    private DeliveryViewModel deliveryViewModel;
    private int key;
    private String merchantId;
    ShimmerFrameLayout shimmerFrameLayout;

    public static Refresh refresh;
    public static Activity StoreDetailsActivity1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStoreDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            ConstantActionBar.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            ConstantActionBar.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//
        StoreDetailsActivity1=this;
        DeliveryFragment.menu_type="";
        refresh=this;
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container_1);

        singleTask=(SingleTask)getApplication();
        key=getIntent().getIntExtra("key",0);
        if(key==1)
        {
            merchantId=getIntent().getStringExtra("merchant_id");
            loadMerchant(merchantId);
        }
        else
        {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.hideShimmer();
            setData();
        }




        binding.viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckOutActivity.merchant=merchant;
                startActivity(new Intent(StoreDetailsActivity.this,CheckOutActivity.class));
                finish();

                                /*ViewOrderActivity.merchant=merchant;
                                startActivity(new Intent(StoreDetailsActivity.this,ViewOrderActivity.class));*/
            }
        });
        binding.layoutCartData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!merchant1.getIsOpen().equalsIgnoreCase("close")) {
                CheckOutActivity.merchant=merchant1;
                startActivity(new Intent(StoreDetailsActivity.this,CheckOutActivity.class));
                finish();
                }else{
                    Toast.makeText(getApplicationContext(),merchant1.getName()+" is Closed !",Toast.LENGTH_LONG).show();
                }
                                /*ViewOrderActivity.merchant=merchant;
                                startActivity(new Intent(StoreDetailsActivity.this,ViewOrderActivity.class));*/
            }
        });
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DashboardActivity.refresh!=null){
                    DashboardActivity.refresh.onRefresh();
                }
               finish();

            }
        });

//

        binding.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
               if(isChecked){
                   DeliveryFragment.menu_type="Veg";
                   if(DeliveryFragment.refresh!=null) {
                       DeliveryFragment.refresh.onRefresh();
                   }
                   Toast.makeText(getApplicationContext(),"ON",Toast.LENGTH_LONG).show();
               }else{
                   DeliveryFragment.menu_type="";
                   if(DeliveryFragment.refresh!=null) {
                       DeliveryFragment.refresh.onRefresh();
                   }
                   Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_LONG).show();
               }
            }
        });

        //handle seach
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchRestFoodActivity.merchantID=merchantId;
                startActivity(new Intent(StoreDetailsActivity.this,SearchRestFoodActivity.class));
            }
        });
        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(StoreDetailsActivity.this);
                View view= LayoutInflater.from(StoreDetailsActivity.this).inflate(R.layout.more_details_dialog,null);
                AlertDialog alertDialog=builder.create();
                alertDialog.setView(view);
                alertDialog.show();
                MoreDetailsDialogBinding binding=MoreDetailsDialogBinding.bind(view);
                binding.openIngTime.setText(merchant.getOpeningTime());
                binding.capsicoScore.setText(merchant.getRating());

                binding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        //handle search




        binding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    Log.e("sdsd","collapse");
                    binding.merchantNa.setTextColor(getResources().getColor(R.color.colorAccent));
                    binding.address.setTextColor(getResources().getColor(R.color.pure_black));
                    binding.card.setVisibility(View.GONE);
                }
                else
                {
                    Log.e(  "sdsd","expanaded");
                    //Expanded
                    binding.merchantNa.setTextColor(getResources().getColor(R.color.color_white));
                    binding.address.setTextColor(getResources().getColor(R.color.color_white));
                    binding.card.setVisibility(View.VISIBLE );
                }
            }
        });

        /*deliveryViewModel=new ViewModelProvider(StoreDetailsActivity.this).get(DeliveryViewModel.class);
        deliveryViewModel.setMerchant(merchant);*/

        if(key!=1) {
            //DeliveryFragment.merchant = merchant;
            Bundle bundle=new Bundle();
            bundle.putString("merchant_id",merchant.getId());
            bundle.putString("merchant_category_id",merchant.getMerchantCategoryId());
            DeliveryFragment fragment= new DeliveryFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            loadCartOrderItem();
        }
        //handle checkout
        binding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(StoreDetailsActivity.this,LocationActivity.class));
            }
        });
    }

    private void loadMerchant(String merchantId) {

//        binding.shimmerViewContainer2.startShimmer();
//        binding.shimmerViewContainer2.setVisibility(View.VISIBLE);

        Log.e("dcsds",merchantId);
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat= SharedPrefManagerLocation.getInstance(StoreDetailsActivity.this).locationModel(Constraint.LATITUDE);
        String lon=SharedPrefManagerLocation.getInstance(StoreDetailsActivity.this).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getAllMerchant(user.getId(), "","", merchantId,lat,lon);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject= jsonArray.getJSONObject(0);
                    String res=jsonObject.getString("res");
                    String msg=jsonObject.getString("message");
                    if(res.equals("success"))
                    {
                        /* binding.cordinatorrr.setVisibility(View.VISIBLE); */
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        binding.search.setVisibility(View.VISIBLE);
                        binding.wishlistIcon.setVisibility(View.VISIBLE);
//                        binding.reviews.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.hideShimmer();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Merchant.class);
                        StoreDetailsActivity.merchant = merchant;

                        if(merchant.getType().equalsIgnoreCase("both")){
                            binding.switchButton.setVisibility(View.VISIBLE);
                            binding.txtVeg.setVisibility(View.VISIBLE);
                            binding.txtVeg1.setVisibility(View.GONE);
                        }else{
                            binding.switchButton.setVisibility(View.GONE);
                            binding.txtVeg.setVisibility(View.GONE);
                            binding.txtVeg1.setVisibility(View.VISIBLE);
                        }
                        setData();

//                        binding.shimmerViewContainer2.stopShimmer();
//                        binding.shimmerViewContainer2.setVisibility(View.GONE);

                        if(merchant.getCloseStatus().equals("true"))
                        {
//                            binding.restaurantClosed.setVisibility(View.VISIBLE);
//                            binding.container.setVisibility(View.GONE);
                        }
                        else
                        {
                            Bundle bundle=new Bundle();
                            bundle.putString("merchant_id",merchant.getId());
                            bundle.putString("merchant_category_id",merchant.getMerchantCategoryId());
                            DeliveryFragment fragment= new DeliveryFragment();
                            fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                            loadCartOrderItem();

                        }
                        binding.moreImg.setVisibility(View.VISIBLE);
                        binding.more1.setVisibility(View.VISIBLE);

                    }
                    else
                    {

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

    Merchant cartItem;
    private void loadCartCount() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getCartCount(user.getId());
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
                            binding.layoutCartData.setVisibility(View.VISIBLE);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("merchant");
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
//                            cartItem = new Gson().fromJson(jsonObject1.toString(), CartItem.class);
                            String st=jsonObject1.getString("totaladdonprize");
                            String qt=jsonObject1.getString("qty");
                            String[] arqty=qt.split(",");
                            String[] adonarr=st.split(",");

                            double aoncar=0;
                            for(int i=0;i<arqty.length;i++){
                                if(!adonarr[i].equalsIgnoreCase("")&&adonarr[i]!=null&&adonarr[i]!="") {
                                    JSONArray jsonArray2 = new JSONArray(adonarr[i]);
                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        aoncar = aoncar + Double.parseDouble(jsonArray2.getString(j));
                                    }
                                }
                                aoncar=aoncar*(Integer.parseInt(arqty[i]));
                            }
                           double to_price=aoncar+Double.parseDouble(jsonObject1.getString("totalprice"));
                            binding.Amount.setText("\u20b9"+to_price);
                            binding.countIitems.setText(jsonObject1.getString("totalcart")+" ITEMS");
//                            Picasso.get().load(AppConstraints.BASE_URL+"merchant"+jsonObject2.getString("banner")).placeholder(R.drawable.logo).into(binding.imageView);


                        }
                        else
                        {
                            binding.layoutCartData.setVisibility(View.GONE);
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

    private void loadCartOrderItem() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartItem(user.getId());
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
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            double aoncar=0;
                            if (jsonArray1.length() > 0) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    OrderItem orderItem = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), OrderItem.class);
                                    aoncar=aoncar+orderItem.getProduct().getItemPrice();
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("MerchantResults");
                                     merchant1 = new Gson().fromJson(jsonObject1.toString(), Merchant.class);
                                }
                                binding.Amount.setText("\u20b9"+aoncar);
                                binding.countIitems.setText(jsonArray1.length()+" ITEMS");
                                binding.layoutCartData.setVisibility(View.VISIBLE);

                            } else {


                                binding.layoutCartData.setVisibility(View.GONE);
                            }
                        } else {
                            binding.layoutCartData.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        binding.layoutCartData.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.layoutCartData.setVisibility(View.GONE);
            }
        });


    }


    private void setData() {
        if(merchant!=null)
        {
            Log.e("dsdsd","dsd");
            binding.merchantName.setText(merchant.getName());
            Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.MERCHANT_BANNER+merchant.getBanner()).placeholder(R.drawable.placeholder).into(binding.merchantImage);
            Glide.with(getApplicationContext())
                    .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                    .placeholder(R.drawable.placeholder).into(binding.logo);
            int estMinut= Integer.parseInt(merchant.getEstimatedDelivery())%60;
            int estTIme= Integer.parseInt(merchant.getEstimatedDelivery())/60;
            if(estTIme>0){
                if(estMinut>0) {
                    String estTImeForValue = estTIme + " Hr " + estMinut + " Mins";
                    binding.estimatedTime.setText(estTImeForValue);
                }else{
                    String estTImeForValue = estTIme + " Hour";
                    binding.estimatedTime.setText(estTImeForValue);
                }
            }else{
                String estTImeForValue=estMinut+" Mins";
                binding.estimatedTime.setText(estTImeForValue);
            }
//            binding.estimatedTime.setText(merchant.getEstimatedDelivery()+" mins");
            //Log.e("dsdd",merchant.getWishliststatus());
            if(merchant.getWishliststatus()!=null)
            {
                if(merchant.getWishliststatus().equals("false"))
                {
                    binding.wishlistIcon.setImageDrawable(getResources().getDrawable(R.drawable.favourit_icon));
                }
                else
                {
                    binding.wishlistIcon.setImageDrawable(getResources().getDrawable(R.drawable.fill_wishlist_icon));
                }
            }

            binding.wishlistIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(merchant.getWishliststatus()!=null) {
                        if (merchant.getWishliststatus().equals("false")) {
                            addWishlist("");
                        } else {
                            addWishlist("clear");
                        }
                    }else{
                        addWishlist("");
                    }
                }
            });
            String lat= SharedPrefManagerLocation.getInstance(StoreDetailsActivity.this).locationModel(Constraint.LATITUDE);
            String lon=SharedPrefManagerLocation.getInstance(StoreDetailsActivity.this).locationModel(Constraint.LATITUDE);

            double distance=distance(Double.parseDouble(lat),Double.parseDouble(lon),Double.parseDouble(merchant.getLatitude()),Double.parseDouble(merchant.getLongitude()));
            //binding.address.setText(merchant.getAddress());
            double a = distance;
            double roundOff = Math.round(a*100)/100;
            binding.lineCost.setVisibility(View.VISIBLE);
            binding.areaKm.setText(merchant.getArea()+" | "+merchant.getDistance()+" km");
            binding.costTag.setText("₹"+merchant.getCost_tag()+" for two");
            loadMenu();
            binding.reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        MenuActivity.name = merchant;
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                }
            });
            binding.category.setText(merchant.getCategoriesname());
            binding.merchantNa.setText(merchant.getName());
            binding.address.setText(merchant.getArea());
            binding.vegSwitch.setVisibility(View.VISIBLE);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.60934;
        //   Log.i("distance", dist+"");
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void addWishlist(String clear)
    {
            ShowProgress.getShowProgress(StoreDetailsActivity.this).show();
                String js=singleTask.getValue("user");
                User user=new Gson().fromJson(js,User.class);
                MyApi myApi=singleTask.getRetrofit().create(MyApi.class);

                Call<JsonArray> call=myApi.addToWishList(user.getId(),merchant.getId(),clear);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject= jsonArray.getJSONObject(0);
                                String res=jsonObject.getString("res");
                                String msg=jsonObject.getString("message");
                                if(res.equals("success"))
                                {
                                    ShowProgress.getShowProgress(StoreDetailsActivity.this).hide();
                                    if(clear.equals(""))
                                    {
                                        binding.wishlistIcon.setImageDrawable(getResources().getDrawable(R.drawable.fill_wishlist_icon));
                                        merchant.setWishliststatus("true");
                                    }
                                    else
                                    {
                                        binding.wishlistIcon.setImageDrawable(getResources().getDrawable(R.drawable.favourit_icon));
                                        merchant.setWishliststatus("false");
                                    }
                                    Toast.makeText(StoreDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    ShowProgress.getShowProgress(StoreDetailsActivity.this).hide();
                                    Toast.makeText(StoreDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        //loadCartCount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(DashboardActivity.refresh!=null){
            DashboardActivity.refresh.onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        loadCartOrderItem();
    }

    private void loadMenu() {

        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getMenus(user.getId(),merchant.getId(),"0","100");
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

                            if(jsonArray1.length()>0){
                                binding.reviews.setVisibility(View.VISIBLE);
                            }else{
                                binding.reviews.setVisibility(View.GONE);
                            }
                        }else{
                            binding.reviews.setVisibility(View.GONE);

//                            Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        binding.reviews.setVisibility(View.GONE);

//                        Toast.makeText(MenuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.reviews.setVisibility(View.GONE);

//                Toast.makeText(MenuActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}