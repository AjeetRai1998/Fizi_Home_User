package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.WishlistAdapter;
import digi.coders.thecapsico.databinding.ActivityWishlistBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.CartItem;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.model.Wishlist;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {


    ActivityWishlistBinding  binding;
    private SingleTask singleTask;
    private List<Merchant> wishlistList;
    public static Activity WishlistActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WishlistActivity=this;
        singleTask=(SingleTask)getApplication();
        loadWishList();


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadWishList() {
        ShowProgress.getShowProgress(WishlistActivity.this).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getWishlist(user.getId());
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
                            ShowProgress.getShowProgress(WishlistActivity.this).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            wishlistList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Merchant merchant=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Merchant.class);
                                wishlistList.add(merchant);
                            }
                            binding.wishlist.setLayoutManager(new LinearLayoutManager(WishlistActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.wishlist.setAdapter(new WishlistAdapter(wishlistList,singleTask));

                        }
                        else
                        {
                            ShowProgress.getShowProgress(WishlistActivity.this).hide();
                            binding.lineNoData.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(WishlistActivity.this).hide();
            }
        });

    }
}