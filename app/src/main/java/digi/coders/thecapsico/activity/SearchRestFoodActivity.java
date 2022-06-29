package digi.coders.thecapsico.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.adapter.SearchAdapter;
import digi.coders.thecapsico.databinding.ActivitySearchBinding;
import digi.coders.thecapsico.fragment.DeliveryFragment;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.model.SearchItem;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRestFoodActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    private SingleTask singleTask;
    private List<SearchItem> searchItemList;
    public static String merchantID="";
    public static Activity SearchRestFoodActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SearchRestFoodActivity=this;
        singleTask=(SingleTask)getApplication(); 
        //handle back
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(DeliveryFragment.refresh!=null){
                    DeliveryFragment.refresh.onRefresh();
                }
            }
        });


        //clear text
        binding.ivCroos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchProduct.setText("");
            }
        });

        //make focus to search bar
        binding.etSearchProduct.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.etSearchProduct, InputMethodManager.SHOW_IMPLICIT);


        // loadPopularCuisines();
        //loadPopularCuisines();
        
        
        // wathcer for search text
        TextWatcher tw=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.lineNoData.setVisibility(View.VISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() >0) {

                    searchApi(s.toString());
                    binding.searchList.setVisibility(View.GONE);
                }
                else{
                    binding.lineNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        
        binding.etSearchProduct.addTextChangedListener(tw);
    }

    private void searchApi(String s) {
        binding.progressBar.setVisibility(View.VISIBLE);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.searchFood(user.getId(), s,merchantID);
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
                            binding.progressBar.setVisibility(View.GONE);
                            searchItemList = new ArrayList<>();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            //Toast.makeText(SearchActivity.this, msg, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                SearchItem searchItem = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), SearchItem.class);
                                searchItemList.add(searchItem);
                            }
                            binding.searchList.setVisibility(View.VISIBLE);
                            binding.searchList.setLayoutManager(new LinearLayoutManager(SearchRestFoodActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.searchList.setAdapter(new SearchAdapter(searchItemList,1,SearchRestFoodActivity.this,getSupportFragmentManager()));
                            binding.lineNoData.setVisibility(View.GONE);
                            binding.lineNoData1.setVisibility(View.GONE);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            //Toast.makeText(SearchActivity.this, msg, Toast.LENGTH_SHORT).show();
                            binding.lineNoData1.setVisibility(View.VISIBLE);
                            binding.lineNoData.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadSearchList() {
        
    }

    /**/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(DeliveryFragment.refresh!=null){
            DeliveryFragment.refresh.onRefresh();
        }
    }
}