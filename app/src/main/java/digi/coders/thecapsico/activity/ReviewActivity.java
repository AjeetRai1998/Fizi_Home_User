package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.adapter.ReviewAdapter;
import digi.coders.thecapsico.databinding.ActivityReviewBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.Review;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    ActivityReviewBinding binding;
    private SingleTask singleTask;
    public static Merchant merchant;
    private List<Review> reviewList;
    public static Activity ReviewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ReviewActivity=this;
        singleTask=(SingleTask)getApplication();
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //hanlde load review list


        loadReviewList();



    }

    private void loadReviewList() {

        ShowProgress.getShowProgress(this).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.merchantRating(user.getId(),merchant.getId());
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
                            ShowProgress.getShowProgress(ReviewActivity.this).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            reviewList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Review review=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Review.class);
                                reviewList.add(review);
                            }
                            binding.reviewList.setLayoutManager(new LinearLayoutManager(ReviewActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.reviewList.setAdapter(new ReviewAdapter(reviewList,getApplicationContext()));
                        }



                        else
                        {
                            Toast.makeText(ReviewActivity.this, message, Toast.LENGTH_SHORT).show();
                            ShowProgress.getShowProgress(ReviewActivity.this).hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ReviewActivity.this).hide();
                Toast.makeText(ReviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}