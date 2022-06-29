package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantItemAdapter;
import digi.coders.thecapsico.adapter.SearchAdapter;
import digi.coders.thecapsico.adapter.WishlistAdapter;
import digi.coders.thecapsico.databinding.ActivitySearchBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.SearchItem;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    private SingleTask singleTask;
    private List<SearchItem> searchItemList;
    public static Activity SearchActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SearchActivity=this;
        singleTask=(SingleTask)getApplication();

        Glide.with(this)
                .load(R.raw.search)
                .into(binding.noSearch);

        Glide.with(this)
                .load(R.raw.no_search)
                .into(binding.noSearch1);


        //handle back
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(  DashboardActivity.refresh!=null) {
                    DashboardActivity.refresh.onRefresh();
                }
            }
        });


        //clear text
        binding.ivCroos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchProduct.setText("");
                binding.searchList.setVisibility(View.GONE);
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
        String lat = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LATITUDE);
        String lon = SharedPrefManagerLocation.getInstance(getApplicationContext()).locationModel(Constraint.LONGITUDE);

        Call<JsonArray> call = myApi.search(user.getId(), lat,lon,s);
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
                            binding.searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.searchList.setAdapter(new SearchAdapter(searchItemList,0,SearchActivity.this,getSupportFragmentManager()));
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
        if( DashboardActivity.refresh!=null) {
            DashboardActivity.refresh.onRefresh();
        }
    }
}