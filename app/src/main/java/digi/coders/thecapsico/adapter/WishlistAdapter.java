package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.WishlistLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyHolder> {

    private List<Merchant> wishlistList;
    private SingleTask singleTask;
    private Context ctx;
    
    
    
    public WishlistAdapter(List<Merchant> wishlistList,SingleTask singleTask) {
        this.wishlistList = wishlistList;
        this.singleTask=singleTask;
    }
    

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Merchant merchant=wishlistList.get(position);
        holder.binding.merchantName.setText(merchant.getName());
        Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT_BANNER + merchant.getBanner())
                .placeholder(R.drawable.placeholder1).into(holder.binding.merchaneImage);
        Glide.with(ctx)
                .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                .placeholder(R.drawable.placeholder1).into(holder.binding.logo);
        //holder.binding.rating.setText(merchant.getRating());
//        holder.binding.estimatedTime.setText(merchant.getEstimatedDelivery());
        holder.binding.discount.setText(merchant.getDiscount()+""+"%");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ctx, StoreDetailsActivity.class);
                in.putExtra("key", 1);
                in.putExtra("merchant_id", merchant.getId());
                ctx.startActivity(in);
            }
        });

        holder.binding.wishlistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress.getShowProgress(ctx).show();
                String js=singleTask.getValue("user");
                User user=new Gson().fromJson(js,User.class);
                MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                Call<JsonArray> call=myApi.addToWishList(user.getId(),merchant.getId(),"sds");
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject= jsonArray.getJSONObject(0);
                                String res=jsonObject.getString("res");
                                String msg=jsonObject.getString("message");
                                if(res.equals("success"))
                                {
                                    ShowProgress.getShowProgress(ctx).hide();
                                    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                                    wishlistList.remove(position);
                                    notifyItemRemoved(position);


                                }
                                else
                                {
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
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return wishlistList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        WishlistLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=WishlistLayoutBinding.bind(itemView);
        }
    }

}
