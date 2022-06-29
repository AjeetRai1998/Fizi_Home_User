package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.CartAdapter;
import digi.coders.thecapsico.databinding.ActivityViewOrderBinding;
import digi.coders.thecapsico.databinding.CartItemLayoutBinding;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.CartItem;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.OrderItem;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderActivity extends AppCompatActivity {

    ActivityViewOrderBinding binding;
    private SingleTask singleTask;
    private List<OrderItem> orderItemList;
    public static Merchant merchant;
    private LinearLayout emptyLayout;
    public static Activity ViewOrderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewOrderActivity=this;
        singleTask = (SingleTask) getApplication();
        emptyLayout=binding.cartEmptyLayout;

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutActivity.merchant = merchant;
                startActivity(new Intent(ViewOrderActivity.this, CheckOutActivity.class));
            }
        });

        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewOrderActivity.this, ItemDetailsActivity.class));
            }
        });

        loadCartCount();

        //load Order Item List
        loadOrderItemList();

    }

    private void loadOrderItemList() {
        Log.e("orderitem","item");
        ShowProgress.getShowProgress(this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartItem(user.getId());
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
                            ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            if (jsonArray1.length() > 0) {
                                orderItemList = new ArrayList<>();
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    OrderItem orderItem = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), OrderItem.class);
                                    orderItemList.add(orderItem);
                                }
                                /*binding.orderItemList.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this,LinearLayoutManager.VERTICAL,false));
                                binding.orderItemList.setAdapter(new OrderItemListAdapter(orderItemList));*/

                                binding.orderItemList.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this, LinearLayoutManager.VERTICAL, false));
                                CartAdapter cartAdapter = new CartAdapter(orderItemList, singleTask);
                                cartAdapter.findMyPosition(new CartAdapter.GetPosition() {
                                    @Override
                                    public void findPos(int position, CartAdapter.MyHolder holder, CartItemLayoutBinding binding) {
                                        binding.plusButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int qty = Integer.parseInt(binding.quantityTextView.getText().toString());
                                                if (qty < 1000) {
                                                    //holder.plusbutton.setVisibility(View.INVISIBLE );
                                                    updateQuantity(holder, position, orderItemList.get(position), qty,cartAdapter);
                                                    qty++;
                                                    binding.quantityTextView.setText(qty + "");
                                                }

                                            }
                                        });
                                        binding.minusButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                int qty = Integer.parseInt(binding.quantityTextView.getText().toString());
                                                if (qty > 1) {
                                                    //holder.minusbutton.setVisibility( View.INVISIBLE );
                                                    qty--;
                                                    binding.quantityTextView.setText(qty + "");
                                                    updateQuantity(holder, position, orderItemList.get(position), qty, cartAdapter);
                                                } else {
                                                    updateQuantity(holder, position, orderItemList.get(position), 0, cartAdapter);
                                                    emptyLayout.setVisibility(View.VISIBLE);
                                                    orderItemList.remove(position);
                                                    cartAdapter.notifyItemRemoved(position);
                                                    cartAdapter.notifyItemRangeChanged(position, orderItemList.size());

                                                /*binding.layout.setVisibility(View.GONE);
                                                    binding.cartEmptyLayout.setVisibility(View.VISIBLE);*/
                                                }
                                            }
                                        });

                                    }
                                });
                                binding.orderItemList.setAdapter(cartAdapter);

                            } else {
                                ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                                emptyLayout.setVisibility(View.VISIBLE);
                                binding.checkOut.setVisibility(View.GONE);

                            }
                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                            binding.checkOut.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
            }
        });


    }

    private void loadCartCount() {
        Log.e("cardcount","cart");
        //ShowProgress.getShowProgress(ViewOrderActivity.this).show();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getCartCount(user.getId());
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
          //                  ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            CartItem cartItem = new Gson().fromJson(jsonObject1.toString(), CartItem.class);
                            binding.price.setText("₹" + cartItem.getTotalprice() + "");
                            //loadCartCount();
                        } else {
                            //ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                            binding.checkOut.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            //    ShowProgress.getShowProgress(ViewOrderActivity.this).hide();

            }
        });


    }

    private void updateQuantity(CartAdapter.MyHolder holder, int position, OrderItem item, int qty, CartAdapter cartAdapter) {
        ShowProgress.getShowProgress(ViewOrderActivity.this).show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.updateQuantity(user.getId(),item.getMerchantId(),item.getProductId(),String.valueOf(qty),"","","");
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
                            ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                            loadCartCount();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            Toast.makeText(ViewOrderActivity.this, msg, Toast.LENGTH_SHORT).show();
                            cartAdapter.notifyItemChanged(position);


                            if(jsonArray1.length()>0)
                            {

                            }
                            else
                            {

                            }

                        }
                        else
                        {
                            ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                            Toast.makeText(ViewOrderActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ViewOrderActivity.this).hide();
                Toast.makeText(ViewOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}