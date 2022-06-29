package digi.coders.thecapsico.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import digi.coders.thecapsico.BuildConfig;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CheckOutActivity;
import digi.coders.thecapsico.activity.CouponActivity;
import digi.coders.thecapsico.activity.DashboardActivity;
import digi.coders.thecapsico.activity.NoServiceAvaliable;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.CouponLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ProgressDisplay;
import digi.coders.thecapsico.model.Coupon;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponAdapter  extends RecyclerView.Adapter<CouponAdapter.MyHolder> {

    private int i=0;
    private Context ctx;
    private int key;
    private List<Coupon> list;
    Activity activity;
    SingleTask singleTask;
    ProgressDisplay progressDisplay;
    public CouponAdapter(int key, List<Coupon> list, Activity activity) {
        this.key = key;
        this.list = list;
        singleTask=(SingleTask) activity.getApplicationContext();
        progressDisplay=new ProgressDisplay(activity);
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_layout,parent,false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Coupon coupon=list.get(position);
        holder.binding.couponCode.setText(coupon.getCouponCode());

//        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.COUPON+coupon.getIcon())
//                .placeholder(R.drawable.placeholder).into(holder.binding.couponLogo);
        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.COUPON+coupon.getIcon())
                .into(holder.binding.image);
        if(coupon.getType().equals("amount"))
        {
            holder.binding.couponTitle.setText("Get flat \u20b9"+coupon.getDiscount()+" off");
            holder.binding.couponDesc.setText("Valid on total items worth ₹"+coupon.getMinimumPurchase());
        }
        else
        {
            holder.binding.couponTitle.setText("Get "+coupon.getDiscount()+"% off upto ₹"+coupon.getMaxAmountuse());
            holder.binding.couponDesc.setText("Valid on total items worth ₹"+coupon.getMinimumPurchase());
        }


        if(coupon.getApply_count().equalsIgnoreCase("1")){
            holder.binding.elcomeMsg.setVisibility(View.VISIBLE);
        }else{
            holder.binding.elcomeMsg.setVisibility(View.GONE);
        }
        if(CheckOutActivity.couponCode!=null){
            if(CheckOutActivity.couponCode.getId().equalsIgnoreCase(coupon.getId())){
                holder.binding.applyCoupon.setText("Applied!");
//                holder.binding.applyCoupon.setBackgroundColor(Color.parseColor("#44AC48"));
            }else{
                holder.binding.applyCoupon.setText("Apply");
//                holder.binding.applyCoupon.setBackgroundColor(Color.parseColor("#000000"));
            }
        }
//        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
//        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
//        Date date = null;
//        try {
//            date = format1.parse(coupon.getExpiryDate());

            holder.binding.expiryDate.setText("Expiring On : "+coupon.getExpiryDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        holder.binding.applyCoupon.setVisibility(View.GONE);


            if(Arrays.asList(coupon.getMerchantId().split(",")).contains(CheckOutActivity.merchant.getId())) {

            holder.binding.not.setVisibility(View.GONE);
                if (Double.parseDouble(coupon.getMinimumPurchase()) <= CheckOutActivity.subTotal) {
                    holder.binding.notApply.setVisibility(View.GONE);
                    holder.binding.couponMsg.setVisibility(View.GONE);
                    holder.binding.applyCoupon.setVisibility(View.VISIBLE);
                }else{
                    holder.binding.couponMsg.setText("Add items more worth \u20b9"+(Double.parseDouble(coupon.getMinimumPurchase())-CheckOutActivity.subTotal)+" to apply this offer");
                    holder.binding.notApply.setVisibility(View.VISIBLE);
                    holder.binding.couponMsg.setVisibility(View.VISIBLE);
                    holder.binding.applyCoupon.setVisibility(View.GONE);
                }
        }else{
            holder.binding.couponMsg.setVisibility(View.GONE);
            holder.binding.applyCoupon.setVisibility(View.GONE);
            holder.binding.not.setVisibility(View.VISIBLE);
        }
    holder.binding.applyCoupon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if(key==1) {
                if (!holder.binding.applyCoupon.getText().toString().equalsIgnoreCase("Applied!")) {
                    if (Double.parseDouble(coupon.getMinimumPurchase()) <= CheckOutActivity.subTotal) {
                        CheckOutActivity.couponCode = list.get(position);
                        checkCoupon(coupon.getId());
                    } else {
                        Toast.makeText(ctx.getApplicationContext(), "Add items worth \u20b9" + (Double.parseDouble(coupon.getMinimumPurchase()) - CheckOutActivity.subTotal) + " more to apply this offer !", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ctx.getApplicationContext(), "Already Applied !", Toast.LENGTH_LONG).show();
                }
//            }
//            else
//            {
//                ClipboardManager clipboard = (ClipboardManager)
//                        ctx.getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("coupon_code", coupon.getCouponCode());
//                clipboard.setPrimaryClip(clip);
//                Toast.makeText(ctx, "Coupon Code Copied", Toast.LENGTH_SHORT).show();
//            }
        }

    });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!coupon.getMerchantId().equalsIgnoreCase("0")) {
//                    Intent in = new Intent(ctx, StoreDetailsActivity.class);
//                    in.putExtra("key", 1);
//                    in.putExtra("merchant_id", coupon.getMerchantId());
//                    ctx.startActivity(in);
//                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private WebView termWebView;
        private LinearLayout more;
        CouponLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=CouponLayoutBinding.bind(itemView);
            more=itemView.findViewById(R.id.more);

        }
    }

    private void checkCoupon(String couponId) {
        progressDisplay.showProgress();
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = null;
        call = myApi.checkCouponValid(user.getId(),couponId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.e("reponse after crash", response.body().toString());
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        if (res.equals("success")) {

                            if (CheckOutActivity.CheckOutActivity1 != null) {
                                CheckOutActivity.CheckOutActivity1.finish();
                            }

                            ctx.startActivity(new Intent(ctx, CheckOutActivity.class));
                            if (CouponActivity.CouponActivity != null) {
                                CouponActivity.CouponActivity.finish();
                            }
                        } else {
                            Toast.makeText(ctx.getApplicationContext(), "Coupon Applied Limit Exceeded !", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ctx.getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }
                }
                progressDisplay.hideProgress();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDisplay.hideProgress();
                Toast.makeText(ctx.getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
