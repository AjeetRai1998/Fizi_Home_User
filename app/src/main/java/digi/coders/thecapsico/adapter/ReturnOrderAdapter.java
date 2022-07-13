package digi.coders.thecapsico.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CheckOutActivity;
import digi.coders.thecapsico.activity.OrderSummaryActivity;
import digi.coders.thecapsico.activity.RatingActivity;
import digi.coders.thecapsico.activity.ReturnOrderSummaryActivity;
import digi.coders.thecapsico.databinding.PastOrderLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.MyOrder;
import digi.coders.thecapsico.model.Orderproduct;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnOrderAdapter extends RecyclerView.Adapter<ReturnOrderAdapter.MyHolder> {
    private Activity ctx;
    private List<MyOrder> myOrderList;

    private SingleTask singleTask;
    public ReturnOrderAdapter(List<MyOrder> myOrderList, Activity ctx) {
        this.myOrderList = myOrderList;
        this.ctx = ctx;
        singleTask = (SingleTask) ctx.getApplication();
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.past_order_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        MyOrder myOrder= myOrderList.get(position);
        Log.e("sded",myOrderList.toString());
        Merchant merchant=myOrder.getMerchant()[0];
        Log.e("sds",myOrder.getOrderproduct().length+"sdsd");
        Glide.with(ctx)
                .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                .placeholder(R.drawable.placeholder1).into(holder.binding.merchantLogo);
        holder.binding.merchantName.setText(merchant.getName());
        String orderproucts="";
        for(int i=0;i<myOrder.getOrderproduct().length;i++){
            if(i==0){
                orderproucts=myOrder.getOrderproduct()[i].getName();
            }else{
                orderproucts=orderproucts+","+myOrder.getOrderproduct()[i].getName();
            }
        }
        holder.binding.merchantCity.setText(myOrder.getOrderproduct().length+" Items - "+orderproucts);
        if(myOrder.getOrderproduct().length>0) {
            Orderproduct order = myOrder.getOrderproduct()[0];
            holder.binding.productName.setText(order.getName());
            holder.binding.productDate.setText(order.getCreatedAt());
        }

        holder.binding.price.setText("₹ "+myOrder.getAmount());
        if(myOrder.getOrderStatus().equalsIgnoreCase("Missed")){
            holder.binding.orderStatus.setText("Not Placed");
        }else {
            holder.binding.orderStatus.setText(myOrder.getOrderStatus());
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt+"\n"+myOrder.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }


        if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")){
            holder.binding.rateForBoy.setVisibility(View.VISIBLE);
            holder.binding.rateForMerchant.setVisibility(View.VISIBLE);
        }else{
            holder.binding.rateForBoy.setVisibility(View.GONE);
            holder.binding.rateForMerchant.setVisibility(View.GONE);
        }
        if(myOrder.getMerchantratingstatus().equals("true"))
        {
            holder.binding.rateForBoy.setText("Rated");
        }
        else
        {
            holder.binding.rateForBoy.setText("Rate Food");
        }
        holder.binding.rateForBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.binding.rateForBoy.getText().toString().equals("Rate Food"))
                {
                    RatingActivity.myOrder=myOrder;
                    RatingActivity.staus=1;
                    ctx.startActivity(new Intent(ctx, RatingActivity.class));
                }
                else
                {
                    Toast.makeText(ctx, "Already rated", Toast.LENGTH_SHORT).show();
                }

            }

        });
        holder.binding.rateForMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StoreDetailsActivity.merchant=myOrder.getMerchant()[0];
//                Intent  inent=new Intent(ctx, StoreDetailsActivity.class);
//                inent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(inent);
                reorder(myOrder.getOrderId(),myOrder.getUserId());
            }

        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnOrderSummaryActivity.myOrder=myOrder;
                ReturnOrderSummaryActivity.orderId=myOrder.getReturn_order_id();
                ctx.startActivity(new Intent(ctx, ReturnOrderSummaryActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {
        PastOrderLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=PastOrderLayoutBinding.bind(itemView);

        }
    }


    public void showDialoge(Context ctx,String id,String userId) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.reorder_confirmation, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView add=promptView.findViewById(R.id.add);
        TextView cancel=promptView.findViewById(R.id.cancel);


        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reorder(id,userId);
                        alert2.dismiss();
                    }
                }
        );

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert2.dismiss();
                    }
                }
        );


        alert2.show();

    }

    private void reorder(String id,String userId) {
        ShowProgress.getShowProgress(ctx).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.reorder(userId,id);
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
                            ShowProgress.getShowProgress(ctx).hide();
                            Intent intent=new Intent(ctx, CheckOutActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(intent);
//                            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            ShowProgress.getShowProgress(ctx).hide();
                            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(ctx).hide();
                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
