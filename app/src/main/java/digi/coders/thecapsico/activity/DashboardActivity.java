package digi.coders.thecapsico.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.MerchantCategoryAdapter;
import digi.coders.thecapsico.adapter.MyViewPagerAdapter;
import digi.coders.thecapsico.adapter.TopBannerAdapter;
import digi.coders.thecapsico.databinding.ActivityDashboardBinding;
import digi.coders.thecapsico.databinding.LastOrderBottomShhetLayoutBinding;
import digi.coders.thecapsico.databinding.ViewOrderBottomSheetLayoutBinding;
import digi.coders.thecapsico.fragment.BookingFragment;
import digi.coders.thecapsico.fragment.CartFragment;
import digi.coders.thecapsico.fragment.RestaurantFragment;
import digi.coders.thecapsico.fragmentmodel.HomeViewModel;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.GPSTracker;
import digi.coders.thecapsico.helper.MYMvp;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Banner2Model;
import digi.coders.thecapsico.model.CartItem;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MerchantCategory;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.OrderStatus;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.UserAddress;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements Refresh , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, MYMvp {

    ActivityDashboardBinding binding;
    Fragment fragment;
    private SingleTask singleTask;
    private List<MerchantCategory> merchantCategoryList;
    private int key;
    private HomeViewModel homeViewModel;
    FragmentManager fm;
    String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    CartItem cartItem;
    public static Refresh refresh;
    int transStatus=0;
    public static LinearLayout line_offer;

    public static int updateStatus=0,locationUpdateTransaction=0;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            getLastOrder();

        }
    };

    public static Activity DashboardActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DashboardActivity=this;
        singleTask = (SingleTask) getApplication();
        fm = getSupportFragmentManager();

        clickStatus=0;
        line_offer=findViewById(R.id.offerLine);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));



        Glide.with(this)
                .load(R.raw.loader)
                .into(binding.animation);

//        showUpdate(DashboardActivity.this);

        locationEnabled();
        refresh=this;
        SharedPrefManagerLocation location = SharedPrefManagerLocation.getInstance(DashboardActivity.this);
        String loc = location.locationModel("location");
        Log.e("dsd", loc + "");

        // handle intent
        key = getIntent().getIntExtra("key", 0);
        Log.e("ssd", "wow");


        loadCartCount();

        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        if(updateStatus==0) {
            updateStatus=1;
            getUpdate();
        }else {
            if (user.getDob().isEmpty()) {
                editProfileConfirmationDialog(DashboardActivity.this);
            }

        }
        binding.clearCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearCart();
                    }
                }
        );
        binding.viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CheckOutActivity.merchant =cartItem.getMerchant()[0];
                if(!cartItem.getMerchant()[0].getIsOpen().equalsIgnoreCase("close")) {
//                    StoreDetailsActivity.merchant1=cartItem.getMerchant()[0];
                    Intent in = new Intent(getApplicationContext(), StoreDetailsActivity.class);
                    in.putExtra("key", 1);
                    in.putExtra("merchant_id", cartItem.getMerchant()[0].getId());
                    startActivity(in);
                }else{
                    Toast.makeText(getApplicationContext(),cartItem.getMerchant()[0].getName()+" is Closed !",Toast.LENGTH_LONG).show();
                }
//                startActivity(new Intent(DashboardActivity.this, StoreDetailsActivity.class));
//                bottomSheetDialog.dismiss();
            }
        });


        binding.layoutCartData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cartItem.getMerchant()[0].getIsOpen().equalsIgnoreCase("close")) {
//                    StoreDetailsActivity.merchant1=cartItem.getMerchant()[0];
                    Intent in = new Intent(getApplicationContext(), StoreDetailsActivity.class);
                    in.putExtra("key", 1);
                    in.putExtra("merchant_id", cartItem.getMerchant()[0].getId());
                    startActivity(in);
                }else{
                    Toast.makeText(getApplicationContext(),cartItem.getMerchant()[0].getName()+" is Closed !",Toast.LENGTH_LONG).show();
                }
//                bottomSheetDialog.dismiss();
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("sd", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Log.e("sdsd", "firbase");

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.e("sds", "ge" + token);
                        Log.d("sads", token + "");
                        sendNotification(token);
                    }
                });


        //handle chat
        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestPermission()) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + "8081941879"));
                        startActivity(callIntent);
                    } catch (ActivityNotFoundException activityException) {
                        Toast.makeText(getApplicationContext(), "not working call", Toast.LENGTH_SHORT).show();
                        Log.e("Calling a Phone Number", "Call failed", activityException);
                    }
                }
//                }
//                startActivity(new Intent(DashboardActivity.this, ChatActivity.class));

            }
        });
        binding.offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CouponActivity.merchantId=null;
//                Intent in = new Intent(DashboardActivity.this, CouponActivity.class);
//                in.putExtra("key", 2);
//                startActivity(in);

                startActivity(new Intent(getApplicationContext(),OfferPage.class));
            }
        });

        binding.localityName.setSelected(true);

        //handle menu

        binding.localityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(DashboardActivity.this,DeliveryLocationActivity.class));
                Intent in = new Intent(DashboardActivity.this, DeliveryLocationActivity.class);
                in.putExtra("key", 3);
                startActivity(in);

            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardActivity.this, permission, 500);
        }




        updateVersionName();
    }
    private boolean requestPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
            return false;
        }
    }
    public void editProfileConfirmationDialog(Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.add_dob, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView add=promptView.findViewById(R.id.add);
        TextView noNow=promptView.findViewById(R.id.notNow);

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DashboardActivity.this, EditAccountActivity.class);
                        startActivity(intent);
                        alert2.dismiss();
                    }
                }
        );

        noNow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     alert2.dismiss();
                    }
                }
        );

        alert2.setCanceledOnTouchOutside(false);

        alert2.show();

    }

    public void showUpdate(Context ctx,String important) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.app_update_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView add=promptView.findViewById(R.id.add);
        TextView noNow=promptView.findViewById(R.id.notNow);

        if(important.equalsIgnoreCase("true")){
            noNow.setVisibility(View.GONE);
        }else{
            noNow.setVisibility(View.VISIBLE);
        }
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=emergence.infotech.thecapsico"));
                      startActivity(intent);
                      alert2.dismiss();
                    }
                }
        );

        noNow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        alert2.setCanceledOnTouchOutside(false);
        alert2.setCancelable(false);
        alert2.show();

    }
    private void checkCity() {

        Log.e("swe", "tr");
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        String latitude = SharedPrefManagerLocation.getInstance(DashboardActivity.this).locationModel(Constraint.LATITUDE);
        String longitude = SharedPrefManagerLocation.getInstance(DashboardActivity.this).locationModel(Constraint.LONGITUDE);

        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("sdsd", SharedPrefManagerLocation.getInstance(DashboardActivity.this).locationModel(Constraint.LOCATION) + "");
        Call<JsonArray> call = myApi.checkCity(user.getId(), latitude,longitude);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.e("reponse after crash", response.body().toString());
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        if (res.equals("success")) {

                            Log.e("sdsd", response.body().toString() + latitude + "   " + longitude);

                            showLastOrder();
                            loadMerchantCategory();
                        } else {
                            startActivity(new Intent(DashboardActivity.this, NoServiceAvaliable.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                transStatus=0;
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("wron", t.getMessage());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==500){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                List<Address> addresses;
                double latitude = new GPSTracker(this).getLatitude();
                double longitude = new GPSTracker(this).getLongitude();
                Log.i("aaadad11", latitude + "  " + longitude);
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.i("jmhnbvc", addresses + "");
                    String add = addresses.get(0).getAddressLine(0);
//                binding.localityName.setText(add);
                    String feature = addresses.get(0).getFeatureName();
                    Log.i("feature", feature + "");
                    Constraint c = new Constraint(add, String.valueOf(latitude), String.valueOf(longitude), addresses.get(0).getFeatureName());
                    SharedPrefManagerLocation.getInstance(DashboardActivity.this).userLocation(c);
                    binding.localityName.setText(add);
                    showLastOrder();
                    checkCity();

                } catch (Exception e) {
                    e.printStackTrace();
                    Intent in = new Intent(DashboardActivity.this, DeliveryLocationActivity.class);
                    in.putExtra("key", 3);
                    startActivity(in);
                    Log.i("asdaf", e.getMessage());
                }
            }else{
           loadAddressList();
            }
            }
    }

    @Override
    public void getData(String id) {


    }

    public class GetLocation extends AsyncTask<String,String,String> {

//        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog=new ProgressDialog(DashboardActivity.this);
//            progressDialog.setMessage("Fetching Location...");
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          fetchCurrentLocation();
        }

        @Override
        protected String doInBackground(String... strings) {
//            progressDialog.dismiss();
            return null;
        }
    }
    private void fetchCurrentLocation() {

        Log.e("ssd", "curen");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardActivity.this, permission, 500);
        } else {

            List<Address> addresses;
            double latitude = new GPSTracker(this).getLatitude();
            double longitude = new GPSTracker(this).getLongitude();
            Log.i("aaadad11", latitude + "  " + longitude);
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                Log.i("jmhnbvc", addresses + "");
                if(addresses.size()>0) {
                    String add = addresses.get(0).getAddressLine(0);
//                binding.localityName.setText(add);
                    String feature = addresses.get(0).getFeatureName();
                    Log.i("feature", feature + "");
                    Constraint c = new Constraint(add, String.valueOf(latitude), String.valueOf(longitude), addresses.get(0).getFeatureName());
                    SharedPrefManagerLocation.getInstance(DashboardActivity.this).userLocation(c);
                    binding.localityName.setText(add);
                    showLastOrder();
                    checkCity();
                }else{
                    setUpGClient();
                }
            } catch (Exception e) {
                e.printStackTrace();

//                fetchCurrentLocation();

//                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                Log.i("asdaf", e.getMessage());
            }
        }
    }

    private void showLastOrder() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonObject> call = myApi.lastOrder(user.getId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("sdsds", response.toString());
                    try {
                        JSONArray jsonArray1 = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray1.getJSONObject(0);
                        String res = jsonObject.getString("res");

                        if (res.equals("success")) {
                            String orderId = jsonObject.getString("order_id");
                            String orderStatus = jsonObject.getString("order_status");
                            openBottomSheet(binding.getRoot(), orderStatus, orderId);
                        } else {


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



    private void openBottomSheet(View view, String orderStatus, String orderId) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.last_order_bottom_shhet_layout, (FrameLayout) findViewById(R.id.standard_bottom_sheet), false);
        LastOrderBottomShhetLayoutBinding binding = LastOrderBottomShhetLayoutBinding.bind(view1);
        binding.lastOrderStatus.setText("Status :" + orderStatus);
        binding.orderId.setText("Order Id: " + orderId);
        binding.trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderSummaryActivity.orderId = orderId;
                Intent in = new Intent(DashboardActivity.this, OrderSummaryActivity.class);
                in.putExtra("sta", 1);
                startActivity(in);
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    private void loadMerchantCategory() {
        MyApi myApi = singleTask.getMerchaneRetrofit().create(MyApi.class);
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        Call<JsonArray> call = myApi.getMerchantsCategory(user.getId());
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
                            merchantCategoryList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                MerchantCategory merchantCategory = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), MerchantCategory.class);
                                merchantCategoryList.add(merchantCategory);
                            }
                            setInAdapter(merchantCategoryList);
                            if (key == 1) {
                                exchangeFragment(new CartFragment());
                            } else {

                                // handle bottom navigation
                                    /*homeViewModel=new ViewModelProvider(DashboardActivity.this).get(HomeViewModel.class);
                                    homeViewModel.setCategory(merchantCategoryList.get(0));
                                    homeViewModel.setStaus(1);*/
                                    /*HomeFragment.i=1;
                                    HomeFragment.merchantCategory=merchantCategoryList.get(0);*/
                                Bundle bundle = new Bundle();
                                bundle.putString("merchatCategoryId", merchantCategoryList.get(0).getId());
                                bundle.putInt("i", 1);
                                    /*HomeFragment fragment=new HomeFragment();
                                    exchangeFragment(fragment);*/
                                RestaurantFragment fragment1 = new RestaurantFragment();
                                fragment1.setArguments(bundle);
                                exchangeFragment(fragment1);


                            }
                            binding.animation.setVisibility(View.GONE);
                        } else {

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

    private void sendNotification(String token) {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.sentToken(user.getId(), token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.e("sdsd", response.toString());

                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("")) {
                            Log.e("sdsd", msg);
                        }
                        Log.e("dcdfd", res);
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

    private void setInAdapter(List<MerchantCategory> merchantCategoryList) {
        binding.merchantCategory.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false));
        MerchantCategoryAdapter adapter = new MerchantCategoryAdapter(merchantCategoryList, digi.coders.thecapsico.activity.DashboardActivity.this);
        adapter.getPosition(new MerchantCategoryAdapter.FindPosition() {
            @Override
            public void find(View view, int position) {
                if (position == 0) {
                    /*homeViewModel=new ViewModelProvider(DashboardActivity.this).get(HomeViewModel.class);
                    homeViewModel.setCategory(merchantCategoryList.get(0));
                    homeViewModel.setStaus(1);
                    HomeFragment fragment=new HomeFragment();
                    exchangeFragment(fragment);*/

                    /*HomeFragment.i=1;
                    HomeFragment.merchantCategory=merchantCategoryList.get(0);*/
                    Bundle bundle = new Bundle();
                    bundle.putString("merchatCategoryId", merchantCategoryList.get(0).getId());
                    bundle.putInt("i", 1);
                    RestaurantFragment fragment1 = new RestaurantFragment();
                    fragment1.setArguments(bundle);
                    exchangeFragment(fragment1);
                } else {
/*                    homeViewModel=new ViewModelProvider(DashboardActivity.this).get(HomeViewModel.class);
                    homeViewModel.setCategory(merchantCategoryList.get(1));
                    homeViewModel.setStaus(2);
                    HomeFragment fragment=new HomeFragment();
                    exchangeFragment(fragment);*/
                    /*HomeFragment.i=2;
                    HomeFragment.merchantCategory=merchantCategoryList.get(0);*/
                    Bundle bundle = new Bundle();
                    bundle.putString("merchatCategoryId", merchantCategoryList.get(1).getId());
                    bundle.putInt("i", 2);
                    RestaurantFragment fragment1 = new RestaurantFragment();
                    fragment1.setArguments(bundle);
                    exchangeFragment(fragment1);
                    //exchangeFragment(new HomeFragment(2,merchantCategoryList.get(1)));
                }
            }
        });
        binding.merchantCategory.setAdapter(adapter);


        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        /*homeViewModel=new ViewModelProvider(DashboardActivity.this).get(HomeViewModel.class);
                        homeViewModel.setCategory(merchantCategoryList.get(0));
                        homeViewModel.setStaus(1);
                        HomeFragment fragment1=new HomeFragment();
                        exchangeFragment(fragment1);*/
                        /*HomeFragment.i=1;
                        HomeFragment.merchantCategory=merchantCategoryList.get(0);*/
                        Bundle bundle = new Bundle();
                        bundle.putString("merchatCategoryId", merchantCategoryList.get(0).getId());
                        bundle.putInt("i", 1);
                        //HomeFragment fragment1=new HomeFragment();
                        RestaurantFragment fragment1 = new RestaurantFragment();
                        fragment1.setArguments(bundle);
                        exchangeFragment(fragment1);

                        break;
                    case R.id.booking:
                        //fragment=new BookingFragment();
//                        fragment = new BookingFragment();
//                        exchangeFragment(fragment);
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        break;
                    case R.id.account:
                        //fragment=new ProfileFragment();

                        if(clickStatus==0) {
                            clickStatus=1;
                            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                        }
                        break;
                }
                return true;
            }
        });
    }

   public static int clickStatus=0;

    public void exchangeFragment(androidx.fragment.app.Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
        }
    }
    @Override
    public void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("method", "resume");
//        loadMerchantCategory();
//        locationEnabled();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("GPSLocationUpdates"));

        binding.bottomNavigation.setSelectedItemId(R.id.home);
//        getUpdate();

    }


    //load cart order on Dashboard Activity

    private void loadCartCount() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartCount(user.getId());
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

                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("merchant");
                            Merchant merchant = new Gson().fromJson(jsonObject1.getJSONArray("merchant").getJSONObject(0).toString(), Merchant.class);
//                            JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                             cartItem = new Gson().fromJson(jsonObject1.toString(), CartItem.class);
                            binding.layoutCartData.setVisibility(View.VISIBLE);
                            binding.Name.setText(merchant.getName());
                            Glide.with(getApplicationContext())
                                    .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                                    .placeholder(R.drawable.placeholder).into(binding.imageView);
//                          Picasso.get().load(AppConstraints.BASE_URL+"merchant"+merchant.getIcon()).placeholder(R.drawable.logo).into(binding.imageView);
//                            openBottomSheet(cartItem);
                            cartVisible=1;
                            getLastOrder();
                        } else {
                            cartVisible=0;
                            getLastOrder();
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

    private void openBottomSheet(CartItem cartItem) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);
        View view1 = LayoutInflater.from(this).inflate(R.layout.view_order_bottom_sheet_layout, (RelativeLayout) findViewById(R.id.bottom_sheet_container), false);
        ViewOrderBottomSheetLayoutBinding binding = ViewOrderBottomSheetLayoutBinding.bind(view1);
//        binding.Amount.setText("₹ " + cartItem.getTotalprice());
        binding.viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutActivity.merchant = cartItem.getMerchant()[0];
                startActivity(new Intent(DashboardActivity.this, CheckOutActivity.class));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }


    @Override
    public void onRefresh() {
        binding.localityName.setText(SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LOCATION));


        if(transStatus==0) {
            transStatus=1;
            checkCity();
            loadCartCount();
        }
    }


    private void locationEnabled () {
        LocationManager lm = (LocationManager)
                getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            binding.layoutNotice.setVisibility(View.VISIBLE);
            binding.trangle.setVisibility(View.VISIBLE);
            locationUpdateTransaction=1;
            binding.tunrOn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setUpGClient();
                        }
                    }
            );
//            new AlertDialog.Builder(DashboardActivity. this )
//                    .setMessage( "Your Device Location is Turn OFF !" )
//                    .setPositiveButton( "Settings" , new
//                            DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
//                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
//                                }
//                            })
//                    .setNegativeButton( "Cancel" , null )
//                    .show() ;
        }else{
            binding.layoutNotice.setVisibility(View.GONE);
            binding.trangle.setVisibility(View.GONE);
            if(transStatus==0) {
                transStatus=1;
//                checkCity();
                new GetLocation().execute(null,null,null);

//                Toast.makeText(getApplicationContext(),"Got It",Toast.LENGTH_LONG).show();
                loadCartCount();
            }
        }
    }

    private void getLastOrder() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.myOrder(user.getId(),"");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        Log.e("sdsd",jsonArray.toString());
                        String res=jsonObject.getString("res");
                        String message=jsonObject.getString("message");
                        if(res.equals("success"))
                        {

                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            Log.e("data",jsonArray1.toString());
                            if(jsonArray1.length()>0)
                            {
                                    MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(),MyOrder.class);

                                 if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")){
                                     if(myOrder.getMerchantratingstatus().equalsIgnoreCase("false")) {
                                         binding.layoutOrderRate.setVisibility(View.VISIBLE);
                                         binding.layoutCartData.setVisibility(View.GONE);
                                         binding.layoutOrderTrack.setVisibility(View.GONE);
                                         binding.orderAmount.setText("How was your order?");
                                         binding.orderStatus.setText("Please tap to rate your food.");
                                         Glide.with(getApplicationContext())
                                                 .load(R.raw.rating_dash)
                                                 .placeholder(R.drawable.placeholder).into(binding.OrderImage);
                                         binding.clearOrder.setOnClickListener(
                                                 new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         binding.layoutOrderRate.setVisibility(View.GONE);
                                                     }
                                                 }
                                         );

                                         binding.rate.setOnClickListener(
                                                 new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         RatingActivity.myOrder=myOrder;
                                                         RatingActivity.staus=1;
                                                         startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                                                         binding.layoutOrderRate.setVisibility(View.GONE);
                                                     }
                                                 }
                                         );
                                     }else{
                                         binding.layoutOrderRate.setVisibility(View.GONE);
                                     }

                                 }else {
                                     binding.trackStatus.setText(myOrder.getMerchant()[0].getName());

                                     binding.viewOrder.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             OrderSummaryActivity.myOrder = myOrder;
                                             OrderSummaryActivity.orderId = myOrder.getOrderId();
                                             startActivity(new Intent(getApplicationContext(), OrderSummaryActivity.class));
                                         }
                                     });
                                     binding.layoutOrderTrack.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             OrderSummaryActivity.myOrder = myOrder;
                                             OrderSummaryActivity.orderId = myOrder.getOrderId();
                                             startActivity(new Intent(getApplicationContext(), OrderSummaryActivity.class));
                                         }
                                     });
//                                Toast.makeText(getApplicationContext(),myOrder.getMerchant()[0].getName(),Toast.LENGTH_LONG).show();

                                     if (!myOrder.getOrderStatus().equalsIgnoreCase("Missed")) {
                                         loadStatus(myOrder.getOrderId());
                                     }else{

                                     }
                                 }
                            }
                            else
                            {
                                if(cartVisible==0) {
                                    binding.layoutCartData.setVisibility(View.GONE);
                                }else{
                                    binding.layoutCartData.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        else
                        {
                            if(cartVisible==0) {
                                binding.layoutCartData.setVisibility(View.GONE);
                            }else{
                                binding.layoutCartData.setVisibility(View.VISIBLE);
                            }

                        }
                    } catch (JSONException e) {
                        if(cartVisible==0) {
                            binding.layoutCartData.setVisibility(View.GONE);
                        }else{
                            binding.layoutCartData.setVisibility(View.VISIBLE);
                        }
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                if(cartVisible==0) {
                    binding.layoutCartData.setVisibility(View.GONE);
                }else{
                    binding.layoutCartData.setVisibility(View.VISIBLE);
                }
            }
        });

    }

     List<OrderStatus> statusList;
    private void loadStatus(String orderId) {
        //Log.e("dsd","dfdff");

        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getOrderStatus(orderId,user.getId());
        Log.e("ordrId",orderId+"ef");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray1=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject= jsonArray1.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        if(res.equals("success"))
                        {
                            JSONArray jsonArray2=jsonObject.getJSONArray("data");
                            statusList=new ArrayList<>();
                            int count=0;
                            for(int i=0;i<jsonArray2.length();i++)
                            {
                                OrderStatus status=new Gson().fromJson(jsonArray2.getJSONObject(i).toString(),OrderStatus.class);
                                statusList.add(status);

                            }
                            count=statusList.size();

//                            binding.statusList.setLayoutManager(new LinearLayoutManager(OrderSummaryActivity.this,LinearLayoutManager.VERTICAL,false));
//                            binding.statusList.setAdapter(new OrderStatusAdapter(statusList));

                            String statusString=statusList.get(count-1).getOrder_status();
                            changeStatus(statusString);
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

    private void getUpdate() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        String [] fname=user.getName().split(" ");

        Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.USER + user.getIcon())
                .placeholder(R.drawable.profile_placeholder).into(binding.image);
        binding.tvName.setText(fname[0]);
        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.getUpdate("User");
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
                            String versionName=jsonObject1.getString("version_name");
                            String VersionCode=jsonObject1.getString("version_code");
                            String important=jsonObject1.getString("importance");

//                            user.getIcon();

//                            Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT_BANNER + user.getIcon())
//                                    .placeholder(R.drawable.profile_placeholder).into(binding.image);
//
//                            binding.tvName.setText(user.getName());



                            if(Integer.parseInt(VersionCode)> BuildConfig.VERSION_CODE){
                                showUpdate(DashboardActivity.this,important);
                            }
                            if (user.getDob().isEmpty()) {
                                editProfileConfirmationDialog(DashboardActivity.this);
                            }
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

    int cartVisible=0;
    void changeStatus(String str){
        if(str.equalsIgnoreCase("Prepared")){
            binding.status2.setText("Your Food is Ready");
            Picasso.get().load(R.drawable.pre_img).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.VISIBLE);
            binding.layoutCartData.setVisibility(View.GONE);
        }else if(str.equalsIgnoreCase("Delivered")){
            binding.status2.setText("Your Order has been Delivered");
            Picasso.get().load(R.drawable.delivered).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.GONE);
            if(cartVisible==0) {
                binding.layoutCartData.setVisibility(View.GONE);
            }else{
                binding.layoutCartData.setVisibility(View.VISIBLE);
            }
        }else if(str.equalsIgnoreCase("Waiting")){
            binding.status2.setText("Restaurant will Accept your order soon");
            Picasso.get().load(R.drawable.order_placed).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.VISIBLE);
            binding.layoutCartData.setVisibility(View.GONE);
        }else if(str.equalsIgnoreCase("Cancelled")){
            binding.status2.setText("Your Order has been Cancelled Successfully");
            Glide.with(getApplicationContext()).load(R.drawable.cancel_order1).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.GONE);
            if(cartVisible==0) {
                binding.layoutCartData.setVisibility(View.GONE);
            }else{
                binding.layoutCartData.setVisibility(View.VISIBLE);
            }
        }else if(str.equalsIgnoreCase("Preparing")){
            binding.status2.setText("Your Food is being Prepared");
            Glide.with(getApplicationContext()).load(R.raw.preparing_food).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.VISIBLE);
            binding.layoutCartData.setVisibility(View.GONE);
        }else if(str.equalsIgnoreCase("Rejected")){
            binding.status2.setText("Your Order has been Rejected by the Restaurant ");
            Picasso.get().load(R.drawable.rejected).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.GONE);
            if(cartVisible==0) {
                binding.layoutCartData.setVisibility(View.GONE);
            }else{
                binding.layoutCartData.setVisibility(View.VISIBLE);
            }
        }else if(str.equalsIgnoreCase("Missed")){
            binding.status2.setText("Missed by Restaurant");
//            binding.orderStatus1.setVisibility(View.GONE);
//            binding.layoutNotice.setText("Order not placed.. Please Try Again.");
            Picasso.get().load(R.drawable.not_placed).into(binding.trackImage);
//            binding.layoutDeliveryBoy.setVisibility(View.GONE);
//            binding.delNotice.setVisibility(View.VISIBLE);
//            binding.orderTime.setVisibility(View.GONE);
//            if(myOrder.getMethod().equalsIgnoreCase("COD")) {
//                binding.refundTxt.setVisibility(View.GONE);
//            }else{
//                binding.refundTxt.setVisibility(View.VISIBLE);
//            }
//            binding.taptoReorder.setVisibility(View.VISIBLE);

            if(cartVisible==0) {
                binding.layoutCartData.setVisibility(View.GONE);
            }else{
                binding.layoutCartData.setVisibility(View.VISIBLE);
            }
        }else if(str.equalsIgnoreCase("Picked")){
            binding.status2.setText("Food is On it's Way");
            binding.layoutOrderTrack.setVisibility(View.VISIBLE);
            binding.layoutCartData.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(R.raw.rider).into(binding.trackImage);
        }else if(str.equalsIgnoreCase("Placed")){
            binding.status2.setText("Waiting for restaurant to confirm your order.");
            Picasso.get().load(R.drawable.order_placed).into(binding.trackImage);
            binding.layoutOrderTrack.setVisibility(View.VISIBLE);
            binding.layoutCartData.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            transStatus=0;
            locationEnabled();
//            Toast.makeText(getApplicationContext(),"Get It",Toast.LENGTH_LONG).show();
        }
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

                            JSONArray jsonArray1=jsonObject.getJSONArray("data");

                                UserAddress address=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(),UserAddress.class);

                                        Constraint c = new Constraint(address.getAddress(), String.valueOf(address.getLatitude()), String.valueOf(address.getLongitude()), "");
                                        Log.e("loc",c.getLoc()+c.getLatitude()+"sds"+c.getLongitude());
                                        SharedPrefManagerLocation.getInstance(DashboardActivity.this).logout();
                                        SharedPrefManagerLocation.getInstance(DashboardActivity.this).userLocation(c);
                                        binding.localityName.setText(address.getAddress());
                                        checkCity();

                        }
                        else
                        {
                            Intent in = new Intent(DashboardActivity.this, DeliveryLocationActivity.class);
                            in.putExtra("key", 3);
                            startActivity(in);
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
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    int clientId=0;
    private synchronized void setUpGClient() {
        if(googleApiClient!=null) {
            googleApiClient.disconnect();
            clientId=clientId+1;
        }
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, clientId, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();

    }


    public class GetL extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            getMyLocation();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(DashboardActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(Result result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(DashboardActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(DashboardActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    //finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    private Location mylocation;
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Your Device Reject the Request !",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mylocation = location;
        if (mylocation != null) {
            binding.layoutNotice.setVisibility(View.GONE);
            binding.trangle.setVisibility(View.GONE);
            if(locationUpdateTransaction==1) {
                locationUpdateTransaction=0;
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(mylocation.getLatitude(), mylocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.i("jmhnbvc", addresses + "");
                    String add = addresses.get(0).getAddressLine(0);
//                binding.localityName.setText(add);
                    String feature = addresses.get(0).getFeatureName();
                    Log.i("feature", feature + "");
                    Constraint c = new Constraint(add, String.valueOf(mylocation.getLatitude()), String.valueOf(mylocation.getLongitude()), addresses.get(0).getFeatureName());
                    SharedPrefManagerLocation.getInstance(DashboardActivity.this).userLocation(c);
                    binding.localityName.setText(add);
                    showLastOrder();
                    checkCity();

                } catch (Exception e) {
                    e.printStackTrace();
                    Intent in = new Intent(DashboardActivity.this, DeliveryLocationActivity.class);
                    in.putExtra("key", 3);
                    startActivity(in);
                    Log.i("asdaf", e.getMessage());
                }
            }
            googleApiClient.disconnect();
            mylocation.reset();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
       Toast.makeText(getApplicationContext(),"Your Device Reject the Request !",Toast.LENGTH_LONG).show();
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(DashboardActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
           new GetL().execute();
        }

    }

    private void showAlertDialog() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(DashboardActivity.this);
        android.app.AlertDialog dialog = alert.create();
        alert.setMessage("Would you like to discard all the items in your cart?");
        alert.setTitle("Clear Cart");
        alert.setCancelable(false);
//        alert.setIcon(ctx.getResources().getDrawable(R.drawable.logo));
        alert.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCart();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    private void clearCart() {
//        ShowProgress.getShowProgress(ctx).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call = null;
        call = myApi.clearCart("clearCart",user.getId());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("sdsd", response.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        Log.e("sdsd", jsonObject.toString());
                        String res = jsonObject.getString("res");
                        String message = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            loadCartCount();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        } else if(res.equalsIgnoreCase("remove")) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                ShowProgress.getShowProgress(ctx).hide();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private void updateVersionName() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = null;
        call = myApi.updateVersionName(user.getId(),user.getId(),BuildConfig.VERSION_NAME);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {


                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }



    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}



