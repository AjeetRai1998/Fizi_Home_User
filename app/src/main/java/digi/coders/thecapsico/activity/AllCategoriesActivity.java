package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.adapter.RestaurantItemAdapter;
import digi.coders.thecapsico.databinding.ActivityAllCategoriesBinding;
import digi.coders.thecapsico.fragmentmodel.HomeViewModel;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.model.Category;
import digi.coders.thecapsico.model.MerchantCategory;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoriesActivity extends AppCompatActivity {

    ActivityAllCategoriesBinding binding;
    private List<Category> categoryList;
    private SingleTask singleTask;
    public  static String merchantCatregoryId;
    public static Activity AllCategoriesActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAllCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        AllCategoriesActivity=this;

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //laod all categories

        loadAllCategories();
    }

    private void loadAllCategories() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);

        Call<JsonArray> call=myApi.getAllCategories(user.getId(),"",merchantCatregoryId);
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
                        binding.progressBar.setVisibility(View.GONE);
                        categoryList=new ArrayList<>();
                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray1.length();i++)
                        {
                            Category category=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Category.class);
                            categoryList.add(category);
                        }
                        binding.allCategoryList.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
                        binding.allCategoryList.setAdapter(new RestaurantItemAdapter(2,categoryList));
                    }
                    else
                    {
                        binding.progressBar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t)
            {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AllCategoriesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}