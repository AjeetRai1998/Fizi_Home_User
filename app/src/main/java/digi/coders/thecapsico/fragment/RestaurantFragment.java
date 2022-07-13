package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.AllCategoriesActivity;
import digi.coders.thecapsico.activity.AllRestaurant;
import digi.coders.thecapsico.activity.BrowesStoreActivity;
import digi.coders.thecapsico.activity.DashboardActivity;
import digi.coders.thecapsico.activity.SearchActivity;
import digi.coders.thecapsico.adapter.CategoriesAdapter;
import digi.coders.thecapsico.adapter.MerchantCategoryAdapter;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.RestaurantItemAdapter;
import digi.coders.thecapsico.adapter.SliderAdapter;
import digi.coders.thecapsico.adapter.TopRestaurantAdapter;
import digi.coders.thecapsico.adapter.TopRestaurantAdapter1;
import digi.coders.thecapsico.adapter.TopRestaurantAdapter2;
import digi.coders.thecapsico.adapter.TopRestaurantAdapter3;
import digi.coders.thecapsico.adapter.TopRestaurantAdapter4;
import digi.coders.thecapsico.databinding.FragmentBlankBinding;
import digi.coders.thecapsico.databinding.FragmentRestaurantBinding;
import digi.coders.thecapsico.fragmentmodel.HomeViewModel;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MYMvp;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Category;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MerchantCategory;
import digi.coders.thecapsico.model.Slider;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
RestaurantFragment extends Fragment implements MYMvp {
    FragmentRestaurantBinding binding;
    public static SingleTask singleTask;
    private List<Merchant> merchantList;
    private List<Merchant> Highlightet_merchantList,Highlightet_merchantList1,Highlightet_merchantList2,Highlightet_merchantList3,Highlightet_merchantList4;
    private List<Category> categoryList;
    private List<Slider> sliderList;
    //public static   MerchantCategory  merchantCategory;
    private RecyclerView restaurantList;
    private HomeViewModel homeViewModel;
    private String merchantCategoryId;
    private int i=1;
    private List<MerchantCategory> merchantCategoryList;

    ShimmerFrameLayout shimmercontainer;
    String city_id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
                            loadSlider(city_id);
//                            Toast.makeText(getActivity(),city_id,Toast.LENGTH_LONG).show();
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

    FragmentManager childFragment;
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        shimmercontainer = view.findViewById(R.id.shimmer_view_container_1);


        childFragment=getChildFragmentManager();
        singleTask = (SingleTask) getActivity().getApplication();
        restaurantList = view.findViewById(R.id.restaurantList_1);
        merchantCategoryId = getArguments().getString("merchatCategoryId");
        Log.e("idmer", merchantCategoryId);
        i = getArguments().getInt("i");


        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
            String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.i("jmhnbvc", addresses + "");
            if(addresses.size()>0) {
                String add = addresses.get(0).getAddressLine(0);

                String feature = addresses.get(0).getFeatureName();
                Log.i("feature", feature + "");

//                getCity(addresses.get(0).getLocality());
                if(addresses.get(0).getSubLocality()!=null) {
                    binding.address.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }else{
                    binding.address.setText( addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getPostalCode() + ", " + addresses.get(0).getCountryName());
                }

            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.refersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i == 1) {
                            loadSlider(city_id);
                            loadDifferentServices();
                        }
                        else {
                            binding.imageSlider.setVisibility(View.GONE);
                            binding.servicesList.setVisibility(View.GONE);
                            binding.categoryName.setVisibility(View.GONE);
                            binding.tvViewAllDishes.setVisibility(View.GONE);
                            binding.nearStoreWord.setText("Local Store Near By");
                        }
                        loadSlider(city_id);
                        merchantCategory();
                    }
                }, 2000);
            }
        });

        binding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        binding.llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });


        //handle fro
        if (i == 1) {
            loadDifferentServices();
        } else {
            binding.imageSlider.setVisibility(View.GONE);
            binding.servicesList.setVisibility(View.GONE);
            binding.categoryName.setVisibility(View.GONE);
            binding.nearStoreWord.setText("Local Store Near By");
        }

        loadSlider(city_id);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        binding.tvViewAllDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllCategoriesActivity.merchantCatregoryId= merchantCategoryId;
                startActivity(new Intent(getContext(), AllCategoriesActivity.class));
            }
        });
        merchantCategory();

        binding.tvViewAllRestro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AllRestaurant.class);
                intent.putExtra("merchantCategoryId",merchantCategoryId);
                intent.putExtra("count","1");
                startActivity(intent);
            }
        });
        binding.tvViewAllRestro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AllRestaurant.class);
                intent.putExtra("merchantCategoryId",merchantCategoryId);
                intent.putExtra("count","2");
                startActivity(intent);
            }
        });
        binding.tvViewAllRestro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AllRestaurant.class);
                intent.putExtra("merchantCategoryId",merchantCategoryId);
                intent.putExtra("count","3");
                startActivity(intent);
            }
        });
        binding.tvViewAllRestro3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AllRestaurant.class);
                intent.putExtra("merchantCategoryId",merchantCategoryId);
                intent.putExtra("count","4");
                startActivity(intent);
            }
        });
        binding.tvViewAllRestro4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AllRestaurant.class);
                intent.putExtra("merchantCategoryId",merchantCategoryId);
                intent.putExtra("count","5");
                startActivity(intent);
            }
        });




    }

    private void loadDifferentServices() {
        binding.servicesList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.servicesList.setAdapter(new CategoriesAdapter(childFragment));

    }

    private void loadSlider(String city_id) {
        Log.e("dff", "hello");

        shimmercontainer.startShimmer();
        shimmercontainer.setVisibility(View.VISIBLE);
        binding.mainLayout.setVisibility(View.GONE);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getSlider(user.getId(),city_id,lat,merchantCategoryId,lon);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                loadRestaurant();

                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                        Log.e("sdsd", jsonObject.toString());
                        if (res.equals("success")) {
                            shimmercontainer.stopShimmer();
                            shimmercontainer.setVisibility(View.GONE);
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            sliderList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                Slider slider = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Slider.class);
                                sliderList.add(slider);
                            }
                            Log.e("sdsd", sliderList.size() + "sdddddd");
                            binding.imageSlider.setVisibility(View.VISIBLE);
                            binding.imageSlider.setSliderAdapter(new SliderAdapter(sliderList));
                            //binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            //        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                            //      binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                            binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                            binding.imageSlider.startAutoCycle();

                        } else {
                            binding.imageSlider.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                binding.refersh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.refersh.setRefreshing(false);
            }
        });

    }

    private void loadRestaurantItem() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getAllCategories(user.getId(), "", merchantCategoryId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                AddTopRestroApi();

                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    if (res.equals("success")) {
                        binding.mainLayout.setVisibility(View.VISIBLE);
                        shimmercontainer.stopShimmer();
                        shimmercontainer.setVisibility(View.GONE);
                        binding.categoryName.setVisibility(View.VISIBLE);
                        binding.tvViewAllDishes.setVisibility(View.VISIBLE);

                        binding.itemCategoryList.setVisibility(View.VISIBLE);
                        binding.cardDishesh.setVisibility(View.VISIBLE);
                        categoryList = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            if(i<8) {
                                Category category = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Category.class);
                                categoryList.add(category);
                            }
                        }
                        binding.itemCategoryList.setLayoutManager(new GridLayoutManager(getContext(),4));
                        binding.itemCategoryList.setAdapter(new RestaurantItemAdapter(1, categoryList));
                    } else {
                        binding.cardDishesh.setVisibility(View.GONE);
                        binding.itemCategoryList.setVisibility(View.GONE);




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

    private void loadRestaurant() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getAllMerchant(user.getId(), merchantCategoryId, "", "", lat, lon);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                loadRestaurantItem();

                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");

                    if (res.equals("success")) {

                        binding.cardRestorant.setVisibility(View.VISIBLE);

                        merchantList = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            merchantList.add(merchant);
                            if(merchant.getDiscount()!=0){
                                DashboardActivity.line_offer.setVisibility(View.VISIBLE);
                            }
                        }

                        restaurantList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        restaurantList.setAdapter(new RestaurantAdapter(1, merchantList));
                        binding.nearStoreWord.setVisibility(View.VISIBLE);
                        binding.bannerBottom.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.cardRestorant.setVisibility(View.GONE);

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

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/

    @Override
    public void onResume() {
        super.onResume();
//        loadRestaurantItem();
        loadSlider(city_id);
        Log.e("dsd", "resume");
    }


    private void merchantCategory() {

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
                            merchantCategoryList.clear();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                MerchantCategory merchantCategory = new Gson().fromJson(jsonArray1.getJSONObject(j).toString(), MerchantCategory.class);
                                merchantCategoryList.add(merchantCategory);

                            }
                            binding.merchantCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            MerchantCategoryAdapter adapter = new MerchantCategoryAdapter(merchantCategoryList,RestaurantFragment.this);
                            binding.merchantCategory.setAdapter(adapter);

                          //  binding.animation.setVisibility(View.GONE);
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
    @Override
    public void getData(String id) {
        merchantCategoryId=id;
        loadSlider(city_id);

    }

   public void AddTopRestroApi(){
        binding.tvDemoText.setSelected(true);
           String js = singleTask.getValue("user");
           User user = new Gson().fromJson(js, User.class);
           MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
           String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
           String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);
           Call<JsonArray> call = myApi.getHighlightetMerchant(user.getId(), merchantCategoryId,"1");

           call.enqueue(new Callback<JsonArray>() {
               @Override
               public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                   AddTopRestroApi1();

                   try {
                       JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                       JSONObject jsonObject = jsonArray.getJSONObject(0);
                       String res = jsonObject.getString("res");
                       String msg = jsonObject.getString("message");


                       if (res.equals("success")) {
                           binding.cardRestroOfMonth.setVisibility(View.VISIBLE);
                           String highlighted_title = jsonObject.getString("highlighted_title");
                           binding.tvRestroName.setText(highlighted_title);
                           Highlightet_merchantList = new ArrayList<>();
                           JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                           for (int i = 0; i < jsonArray1.length(); i++) {
                               Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                               Highlightet_merchantList.add(merchant);
//                               if(merchant.getDiscount()!=0){
//                                   DashboardActivity.line_offer.setVisibility(View.VISIBLE);
//                               }
                           }

                           binding.recyclerTopRestro.setHasFixedSize(true);
                           binding.recyclerTopRestro.setNestedScrollingEnabled(false);
                           binding.recyclerTopRestro.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                           binding.recyclerTopRestro.setAdapter(new TopRestaurantAdapter(getContext(),Highlightet_merchantList,user.getId()));
                       } else {
                           binding.cardRestroOfMonth.setVisibility(View.GONE);

                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                       Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onFailure(Call<JsonArray> call, Throwable t) {

               }
           });
       }
    public void AddTopRestroApi1(){
        binding.tvDemoText.setSelected(true);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getHighlightetMerchant(user.getId(), merchantCategoryId,"2");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                AddTopRestroApi2();
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");


                    if (res.equals("success")) {
                        binding.cardRestroOfMonth1.setVisibility(View.VISIBLE);
                        String highlighted_title = jsonObject.getString("highlighted_title");
                        binding.tvRestroName1.setText(highlighted_title);
                        Highlightet_merchantList1 = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            Highlightet_merchantList1.add(merchant);
//                               if(merchant.getDiscount()!=0){
//                                   DashboardActivity.line_offer.setVisibility(View.VISIBLE);
//                               }
                        }

                        binding.recyclerTopRestro1.setHasFixedSize(true);
                        binding.recyclerTopRestro1.setNestedScrollingEnabled(false);
                        binding.recyclerTopRestro1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerTopRestro1.setAdapter(new TopRestaurantAdapter1(getContext(),Highlightet_merchantList1,user.getId()));
                    } else {
                        binding.cardRestroOfMonth1.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void AddTopRestroApi2(){
        binding.tvDemoText.setSelected(true);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getHighlightetMerchant(user.getId(), merchantCategoryId,"3");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                AddTopRestroApi3();

                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");


                    if (res.equals("success")) {
                        binding.cardRestroOfMonth2.setVisibility(View.VISIBLE);
                        String highlighted_title = jsonObject.getString("highlighted_title");
                        binding.tvRestroName2.setText(highlighted_title);
                        Highlightet_merchantList2 = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            Highlightet_merchantList2.add(merchant);
//                               if(merchant.getDiscount()!=0){
//                                   DashboardActivity.line_offer.setVisibility(View.VISIBLE);
//                               }
                        }

                        binding.recyclerTopRestro2.setHasFixedSize(true);
                        binding.recyclerTopRestro2.setNestedScrollingEnabled(false);
                        binding.recyclerTopRestro2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerTopRestro2.setAdapter(new TopRestaurantAdapter2(getContext(),Highlightet_merchantList2,user.getId()));
                    } else {
                        binding.cardRestroOfMonth2.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void AddTopRestroApi3(){
        binding.tvDemoText.setSelected(true);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getHighlightetMerchant(user.getId(), merchantCategoryId,"4");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                AddTopRestroApi4();
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");


                    if (res.equals("success")) {
                        binding.cardRestroOfMonth3.setVisibility(View.VISIBLE);
                        String highlighted_title = jsonObject.getString("highlighted_title");
                        binding.tvRestroName3.setText(highlighted_title);
                        Highlightet_merchantList3 = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            Highlightet_merchantList3.add(merchant);
//                               if(merchant.getDiscount()!=0){
//                                   DashboardActivity.line_offer.setVisibility(View.VISIBLE);
//                               }
                        }

                        binding.recyclerTopRestro3.setHasFixedSize(true);
                        binding.recyclerTopRestro3.setNestedScrollingEnabled(false);
                        binding.recyclerTopRestro3.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerTopRestro3.setAdapter(new TopRestaurantAdapter3(getContext(),Highlightet_merchantList3,user.getId()));
                    } else {
                        binding.cardRestroOfMonth3.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void AddTopRestroApi4(){
        binding.tvDemoText.setSelected(true);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        String lat = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.getHighlightetMerchant(user.getId(), merchantCategoryId,"5");

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                shimmercontainer.stopShimmer();
                shimmercontainer.setVisibility(View.GONE);
                binding.mainLayout.setVisibility(View.VISIBLE);

                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    if (res.equals("success")) {
                        binding.cardRestroOfMonth4.setVisibility(View.VISIBLE);
                        String highlighted_title = jsonObject.getString("highlighted_title");
                        binding.tvRestroName4.setText(highlighted_title);
                        Highlightet_merchantList4 = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            Highlightet_merchantList4.add(merchant);
//                               if(merchant.getDiscount()!=0){
//                                   DashboardActivity.line_offer.setVisibility(View.VISIBLE);
//                               }
                        }

                        binding.recyclerTopRestro4.setHasFixedSize(true);
                        binding.recyclerTopRestro4.setNestedScrollingEnabled(false);
                        binding.recyclerTopRestro4.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerTopRestro4.setAdapter(new TopRestaurantAdapter4(getContext(),Highlightet_merchantList4,user.getId()));
                    } else {
                        binding.cardRestroOfMonth4.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }












}