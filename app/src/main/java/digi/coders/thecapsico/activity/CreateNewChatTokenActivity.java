package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.CouponAdapter;
import digi.coders.thecapsico.adapter.PastOrderAdapter;
import digi.coders.thecapsico.databinding.ActivityCreateNewChatTokenBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ProgressDisplay;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Coupon;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.Order;
import digi.coders.thecapsico.model.Orderproduct;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewChatTokenActivity extends AppCompatActivity {

    ActivityCreateNewChatTokenBinding binding;
    private  SingleTask singleTask;
    public static Activity CreateNewChatTokenActivity;

    ArrayList<MyOrder> arrayList=new ArrayList<>();
    String orderid="",subject="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateNewChatTokenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CreateNewChatTokenActivity=this;
        singleTask=(SingleTask)getApplication();

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadPastOrders();

        ArrayAdapter<String> height =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,new String[]{"-- Select Subject --","Payment issue","Issue with quality of delivered items","Order not received","Issue with delivery experience","Different item received","Items missing with the order","Just want to leave feedback for Capsico team","Other issue"});
        binding.subject.setAdapter(height);
        height.setDropDownViewResource(R.layout.spinner_item);
        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    subject = (String) parent.getItemAtPosition(position);

                }else{
                    binding.lineItems.setVisibility(View.GONE);
                }
//
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.generateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderid.length()>0) {
                    if (subject.length() > 0) {
//                        if (binding.query.getText().toString().length() > 0) {
                            ShowProgress.getShowProgress(CreateNewChatTokenActivity.this).show();
                            String js = singleTask.getValue("user");
                            User user = new Gson().fromJson(js, User.class);
                            MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                            Call<JsonArray> call = myApi.generateToken(subject, binding.query.getText().toString(), orderid, user.getId());
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
                                            ShowProgress.getShowProgress(CreateNewChatTokenActivity.this).hide();
                                            Toast.makeText(CreateNewChatTokenActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(CreateNewChatTokenActivity.this, ChatActivity.class));
                                            finish();
                                        } else {
                                            ShowProgress.getShowProgress(CreateNewChatTokenActivity.this).hide();
                                            Toast.makeText(CreateNewChatTokenActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonArray> call, Throwable t) {
                                ShowProgress.getShowProgress(CreateNewChatTokenActivity.this).hide();
                                Toast.makeText(CreateNewChatTokenActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Please Write Query", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Subject", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Select Order", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }



    private void loadPastOrders() {

        ProgressDialog pd= ProgressDialog.show(CreateNewChatTokenActivity.this,"","Loading...");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.myOrder(user.getId(),"");
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


                                for(int i=0;i<jsonArray1.length();i++)
                                {
                                    MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
//                                    if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")||) {
                                        arrayList.add(myOrder);
//                                    }
                                }

                                String[] str=new String[arrayList.size()+1];
                                str[0]="-- Select Order --";
                                for(int i=0;i<arrayList.size();i++){
                                    str[i+1]=arrayList.get(i).getMerchant()[0].getName()+"\n"+arrayList.get(i).getOrderId()+" (\u20b9"+arrayList.get(i).getAmount()+")";
                                }

                                ArrayAdapter<String> height =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,str);
                                binding.orders.setAdapter(height);
                                height.setDropDownViewResource(R.layout.spinner_item);
                                binding.orders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if(position!=0) {
                                            orderid = arrayList.get(position-1).getOrderId();
                                            binding.lineItems.setVisibility(View.VISIBLE);
                                            MyOrder myOrder=arrayList.get(position-1);
                                            Merchant merchant=myOrder.getMerchant()[0];
                                            Log.e("sds",myOrder.getOrderproduct().length+"sdsd");
                                            Glide.with(getApplicationContext())
                                                    .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                                                    .placeholder(R.drawable.placeholder1).into(binding.merchantLogo);
                                            binding.merchantName.setText(merchant.getName());
                                            String orderproucts="";
                                            for(int i=0;i<myOrder.getOrderproduct().length;i++){
                                                if(i==0){
                                                    orderproucts=myOrder.getOrderproduct()[i].getName();
                                                }else{
                                                    orderproucts=orderproucts+","+myOrder.getOrderproduct()[i].getName();
                                                }
                                            }
                                            binding.merchantCity.setText(myOrder.getOrderproduct().length+" Items - "+orderproucts);

                                            binding.price.setText("₹ "+myOrder.getAmount());
                                            try {
                                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt().split(" ")[0]);
                                                String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
                                                binding.date.setText(dtt+" "+myOrder.getCreatedAt().split(" ")[1]);
                                            }catch (Exception e){

                                            }
//                                            binding.date.setText(myOrder.getCreatedAt().split(" ")[0]+"\n"+myOrder.getCreatedAt().split(" ")[1]);



                                        }else{
                                            binding.lineItems.setVisibility(View.GONE);
                                        }
//                                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else
                            {

                            }
                        }

                        else
                        {

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{

                }

                pd.dismiss();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                pd.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}