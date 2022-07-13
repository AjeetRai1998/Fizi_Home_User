package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.RestaurantAdapter;
import digi.coders.thecapsico.adapter.TopRestaurantAdapter;
import digi.coders.thecapsico.databinding.ActivityAllRestaurantBinding;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRestaurant extends AppCompatActivity {
    ActivityAllRestaurantBinding binding;
    private String categoryId,categoryName,merchantCategoryId;
    private SingleTask singleTask;

    private List<Merchant> merchantList;
    public static Activity AllRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAllRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        merchantCategoryId=getIntent().getStringExtra("merchantCategoryId");
        AddTopRestroApi(getIntent().getStringExtra("count"));

    }

    public void AddTopRestroApi(String count){
        binding.progressBar.setVisibility(View.VISIBLE);
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getHighlightetMerchant(user.getId(), merchantCategoryId,count);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg = jsonObject.getString("message");
                    String highlighted_title = jsonObject.getString("highlighted_title");
                    if (res.equals("success")) {
                        binding.shopList.setVisibility(View.VISIBLE);
                        binding.categoryName.setText(highlighted_title);
                        merchantList = new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Merchant merchant = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Merchant.class);
                            merchantList.add(merchant);
//                               if(merchant.getDiscount()!=0){
//                                   DashboardActivity.line_offer.setVisibility(View.VISIBLE);
//                               }
                        }

                        binding.shopList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        binding.shopList.setAdapter(new RestaurantAdapter(1, merchantList));
                        binding.bannerBottom.setVisibility(View.VISIBLE);
                    }
                    else {


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

}