package digi.coders.thecapsico.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.CategorWiseProductAdapter;
import digi.coders.thecapsico.adapter.MasterProductAdapter;
import digi.coders.thecapsico.adapter.MasterSubCategoryListAdapter;
import digi.coders.thecapsico.adapter.ProductTabAdapter;
import digi.coders.thecapsico.adapter.RestaurantItemAdapter;
import digi.coders.thecapsico.databinding.FragmentDeliveryBinding;
import digi.coders.thecapsico.fragmentmodel.DeliveryViewModel;
import digi.coders.thecapsico.fragmentmodel.HomeViewModel;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.model.Category;
import digi.coders.thecapsico.model.CategoryProduct;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.Product;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryFragment extends Fragment implements Refresh {

    FragmentDeliveryBinding binding;
    private SingleTask singleTask;
    private List<Category> categoryList;
    //public static Merchant merchant;
    private DeliveryViewModel deliveryViewModel;
    private List<String> categoryName;
    private boolean isUserScrolling = false;
    private boolean isListGoingUp = true;
    private List<CategoryProduct> categoryProductList;
    private LinearLayoutManager linearLayoutManager;
    private String merchantId;
    private String merchantCategoryId;
ShimmerFrameLayout shimmerFrameLayout;

public static Refresh refresh;
public static String menu_type="";
FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false);
        refresh=this;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager=getChildFragmentManager();
        singleTask = (SingleTask) getActivity().getApplication();
        //loadMasterProduct
        /*deliveryViewModel=new ViewModelProvider(requireActivity()).get(DeliveryViewModel.class);
        merchant=deliveryViewModel.getMerchant();*/
        //loadMasterProduct();
        //laodMasterProductItemList
        //set Default community support  fragment
        //getParentFragmentManager().beginTransaction().replace(R.id.productContainer,new RestaurantSupportFragment()).commit();
       /* //check switch on or off
        binding.grid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    loadMasterProductList(1);
                    //show Grid View
                }
                else
                {
                    //show vertical view
                    loadMasterProductList(2);
                }
            }

        });*/
        shimmerFrameLayout=view.findViewById(R.id.deliveryshimar);
        //loadMasterProductList(2);
        merchantId = getArguments().getString("merchant_id");
        merchantCategoryId = getArguments().getString("merchant_category_id");
        loadCategoryWiseProductList();

    }

    private void loadCategoryWiseProductList() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        //Log.e("dssd",user.getId()+""+merchant.getId()+""+merchant.getMerchantCategoryId()+"");
        Call<JsonArray> call = myApi.getCategoryWiseProduct(user.getId(), merchantId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    if (res.equals("success")) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.hideShimmer();

                        binding.progressBar.setVisibility(View.GONE);

                        categoryProductList = new ArrayList<>();
                        categoryName = new ArrayList<>();
                        binding.tabLayout.removeAllTabs();
                        //categoryName.add(0,"Restaurant Support");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        JSONArray jsonArray2 = jsonObject.getJSONArray("categoriesdata");

                        String status = jsonObject.getString("close_status");

                        for (int i = 0; i < jsonArray2.length(); i++) {
                            String categoryNa = jsonArray2.get(i).toString();
                            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(categoryNa));
                            Log.e("category", categoryNa);
                            categoryName.add(categoryNa);
                        }
                        if (jsonArray1.length() > 0) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                CategoryProduct categoryProduct = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), CategoryProduct.class);

                                if(menu_type.equalsIgnoreCase("Veg")) {
                                    Product[] product =categoryProduct.getProduct();
                                    ArrayList<Product> newProductList =new ArrayList<>();

                                    for(int k=0;k<product.length;k++) {
                                        if(product[k].getMenu_type().equalsIgnoreCase("Veg")) {
                                            newProductList.add(product[k]);

                                        }
                                    }

                                    Product[] productnew=new Product[newProductList.size()];
                                    for(int l=0;l<newProductList.size();l++) {
                                        productnew[l]=newProductList.get(l);
                                    }
                                    if(productnew.length>0) {
                                        categoryProduct.setProduct(productnew);
                                        categoryProductList.add(categoryProduct);
                                    }
                                }else{
                                    categoryProductList.add(categoryProduct);
                                }

                            }
                            ProductTabAdapter adapter = new ProductTabAdapter(getFragmentManager(), binding.tabLayout.getTabCount(), categoryName);
                            handleProductList(categoryProductList, status);
//                            binding.message.setVisibility(View.VISIBLE);
                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.hideShimmer();
                            binding.progressBar.setVisibility(View.GONE);
//                            binding.message.setVisibility(View.GONE);
                            binding.noTxt.setVisibility(View.VISIBLE);
                        }
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        shimmerFrameLayout.hideShimmer();
                        binding.progressBar.setVisibility(View.GONE);
//                        binding.message.setVisibility(View.GONE);
                        binding.noTxt.setVisibility(View.VISIBLE);
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

    private void handleProductList(List<CategoryProduct> categoryProductList, String status) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.masterProductItemList.setLayoutManager(linearLayoutManager);
        binding.masterProductItemList.setAdapter(new CategorWiseProductAdapter(getActivity(),categoryProductList, status,fragmentManager));
        binding.masterProductItemList.setHasFixedSize(true);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isUserScrolling = false;
                int position = tab.getPosition();
                binding.masterProductItemList.smoothScrollToPosition(position);
                /*if(position==0){
                    binding.masterProductItemList.smoothScrollToPosition(0);
                }else if(position==1){
                    binding.masterProductItemList.smoothScrollToPosition(1);
                }else if(position==2){
                    binding.masterProductItemList.smoothScrollToPosition(2);
                }else if(position==3){
                    binding.masterProductItemList.smoothScrollToPosition(3);
                }
*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.masterProductItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true;
                    if (isListGoingUp) {
                        //my recycler view is actually inverted so I have to write this condition instead
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == categoryProductList.size()) {
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (isListGoingUp) {
//                                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == categoryProductList.size()) {
//                                            //Toast.makeText(getActivity(), "exeute something", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//                            }, 50);
                            //waiting for 50ms because when scrolling down from top, the variable isListGoingUp is still true until the onScrolled method is executed
                        }
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = linearLayoutManager.findFirstVisibleItemPosition();


                if (isUserScrolling) {
                    TabLayout.Tab tab = binding.tabLayout.getTabAt(itemPosition);
                    tab.select();
                    /*if (itemPosition == 0) { //  item position of uses
                        TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
                        tab.select();
                    } else if (itemPosition == 30) {//  item position of side effects
                        TabLayout.Tab tab =binding.tabLayout.getTabAt(1);
                        tab.select();
                    } else if (itemPosition == 60) {//  item position of how it works
                        TabLayout.Tab tab = binding.tabLayout.getTabAt(2);
                        tab.select();
                    } else if (itemPosition == 90) {//  item position of precaution
                        TabLayout.Tab tab = binding.tabLayout.getTabAt(3);
                        tab.select();
                    }*/
                }
            }
        });
    }


    private void loadMasterProductList(int i) {
           /* if(i==1) {
                binding.masterProductItemList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                binding.masterProductItemList.setAdapter(new  MasterSubCategoryListAdapter(getParentFragmentManager(),i,getActivity()));
            }
            else
            {
                binding.masterProductItemList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                binding.masterProductItemList.setAdapter(new  MasterSubCategoryListAdapter(getParentFragmentManager(),i,getActivity()));

            }*/
    }

    private void loadMasterProduct() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        //Log.e("dssd",user.getId()+""+merchant.getId()+""+merchant.getMerchantCategoryId()+"");
        Call<JsonArray> call = myApi.getAllCategories(user.getId(), merchantId, "");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    if (res.equals("success")) {

                        categoryList = new ArrayList<>();
                        categoryName = new ArrayList<>();
                        categoryName.add(0, "Restaurant Support");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Category category = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Category.class);
                            Log.e("category", category.getMerchantId() + "");
                            categoryName.add(category.getName());
                            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(category.getName() + ""));
                            categoryList.add(category);

                        }
                        Log.e("sdsd", binding.tabLayout.getTabCount() + "");
                        //ProductTabAdapter adapter=new ProductTabAdapter(getFragmentManager(),binding.tabLayout.getTabCount(),categoryName,categoryList);
                        //binding.viewPage.setAdapter(adapter);
                        //binding.tabLayout.setupWithViewPager(binding.viewPage);
                        //binding.viewPage.setOffscreenPageLimit(0);
                        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {

                                //      binding.viewPage.setCurrentItem(tab.getPosition());
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });
                        /*binding.masterProductList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        binding.masterProductList.setAdapter(new MasterProductAdapter(getFragmentManager(),categoryList,requireActivity()));*/
                    } else {

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
        loadCategoryWiseProductList();
    }

  /*  @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/
}
