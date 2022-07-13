package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.PastOrderAdapter;
import digi.coders.thecapsico.adapter.ReturnOrderAdapter;
import digi.coders.thecapsico.databinding.ActivityReturnOrderBinding;
import digi.coders.thecapsico.databinding.ReturnListBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnOrder extends AppCompatActivity {

    ActivityReturnOrderBinding binding;
    private SingleTask singleTask;
    private List<MyOrder> myOrderList;
    public static Activity MyOrdersActivity;

    public static Refresh refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReturnOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //hanlde pastOrderList
        loadPastOrders(0);
    }
    private void loadPastOrders(int i) {
        if(i==0) {
            ShowProgress.getShowProgress(this).show();
        }
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.myReturnOrder(user.getId());
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
                                if(i==0) {
                                    ShowProgress.getShowProgress(ReturnOrder.this).hide();
                                }
                                myOrderList=new ArrayList<>();

                                for(int i=0;i<jsonArray1.length();i++)
                                {

                                    MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
                                    myOrderList.add(myOrder);
                                }
                                binding.pastOrders.setLayoutManager(new LinearLayoutManager(ReturnOrder.this,LinearLayoutManager.VERTICAL,false));
                                binding.pastOrders.setAdapter(new ReturnOrderAdapter(myOrderList,ReturnOrder.this));
                            }
                            else
                            {
                                if(i==0) {
                                    ShowProgress.getShowProgress(ReturnOrder.this).hide();
                                }
                                binding.lineNoData.setVisibility(View.VISIBLE);
                            }
                        }

                        else
                        {
                            if(i==0) {
                                ShowProgress.getShowProgress(ReturnOrder.this).hide();
                            }
                            binding.lineNoData.setVisibility(View.VISIBLE);

                        }
                    } catch (JSONException e) {
                        Toast.makeText(ReturnOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(i==0) {
                        ShowProgress.getShowProgress(ReturnOrder.this).hide();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                if(i==0) {
                    ShowProgress.getShowProgress(ReturnOrder.this).hide();
                }
                Toast.makeText(ReturnOrder.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}