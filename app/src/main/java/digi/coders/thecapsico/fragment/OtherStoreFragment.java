package digi.coders.thecapsico.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.RestaurantItemAdapter;
import digi.coders.thecapsico.databinding.FragmentOtherStoreBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Category;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MerchantCategory;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OtherStoreFragment extends Fragment {


    FragmentOtherStoreBinding binding;
    MerchantCategory merchantCategory;
    private SingleTask singleTask;
    private List<Merchant> merchantList;
    private List<Category> categoryList;

    public OtherStoreFragment(MerchantCategory merchantCategory) {
        this.merchantCategory = merchantCategory;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentOtherStoreBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTask=(SingleTask)getActivity().getApplication();
        //load resturant

        loadStore();

        //loadItemCategory
        loadRestaurantItem();
    }

    private void loadRestaurantItem() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllCategories(user.getId(),"",merchantCategory.getId());
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

                        categoryList=new ArrayList<>();
                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray1.length();i++)
                        {
                            Category category=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Category.class);
                            categoryList.add(category);

                        }
                        binding.itemCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        binding.itemCategoryList.setAdapter(new RestaurantItemAdapter(1,categoryList));
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


    private void loadStore() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String lat= SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LATITUDE);
        String lon=SharedPrefManagerLocation.getInstance(getActivity()).locationModel(Constraint.LONGITUDE);
        Call<JsonArray> call=myApi.getAllMerchant(user.getId(),merchantCategory.getId(),"","",lat,lon);
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
                        merchantList=new ArrayList<>();
                        JSONArray jsonArray1=jsonObject.getJSONArray("data");

                        for(int i=0;i<jsonArray1.length();i++)
                        {
                            Merchant merchant=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Merchant.class);
                            merchantList.add(merchant);

                        }
                        binding.restaurantList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        binding.restaurantList.setAdapter(new RestaurantAdapter(1,merchantList));

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

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }*/
}