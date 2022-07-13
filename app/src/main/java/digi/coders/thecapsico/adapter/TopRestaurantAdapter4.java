package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.ReviewActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.HighlightetRestroBinding;
import digi.coders.thecapsico.fragment.RestaurantFragment;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.MyApi;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.helper.ShowProgress;
import digi.coders.thecapsico.model.Merchant;
import digi.coders.thecapsico.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRestaurantAdapter4 extends RecyclerView.Adapter<TopRestaurantAdapter4.AdapterViewHolder> {
    Context context;
    private List<Merchant> merchantList;
    private SingleTask singleTask;
    Merchant merchant;
    String u_id="";

    public TopRestaurantAdapter4(Context context, List<Merchant> merchantList,String u_id) {
        this.context = context;
        this.merchantList = merchantList;
        this.u_id=u_id;
    }

    @NonNull
    @Override
    public TopRestaurantAdapter4.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.highlightet_restro,parent,false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopRestaurantAdapter4.AdapterViewHolder holder, int position) {
        if (merchantList != null) {
            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                    .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.7f) //the alpha of the underlying children
                    .setHighlightAlpha(0.6f) // the shimmer alpha amount
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build();
            ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);

            merchant = merchantList.get(position);
            Log.e("close status", merchant.getCloseStatus());

//            holder.binding.notAccept.setVisibility(View.GONE);
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);


            if(merchantList.get(position).getWishliststatus()!=null)
            {
                if(merchantList.get(position).getWishliststatus().equals("false"))
                {
                    holder.binding.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.favourit_icon));
                }
                else
                {
                    holder.binding.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_wishlist_icon));
                }
            }

            holder.binding.ivFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(merchant.getWishliststatus()!=null) {
                        if (merchant.getWishliststatus().equals("false")) {
                            addWishlist("",holder,merchantList.get(position).getId());
                        } else {
                            addWishlist("clear",holder,merchantList.get(position).getId());
                        }
                    }else{
                        addWishlist("",holder,merchantList.get(position).getId());
                    }
                }
            });


            holder.binding.merchantName.setText(merchantList.get(position).getName());

            Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT_BANNER + merchantList.get(position).getBanner())
                    .placeholder(R.drawable.placeholder1).into(holder.binding.merchaneImage);
//            Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
//                    .placeholder(R.drawable.placeholder).into(holder.binding.logo);
            if(merchantList.get(position).getRating().equalsIgnoreCase("No Ratings Yet")) {

                holder.binding.rating.setText("0");
                holder.binding.noRating.setText("(0)");
                holder.binding.ratingBar.setRating(0);
            }else{

                holder.binding.rating.setText(merchantList.get(position).getRating());
                holder.binding.noRating.setText("("+merchantList.get(position).getRatcount() + ")");
                holder.binding.ratingBar.setRating(Float.parseFloat(merchantList.get(position).getRating()));
            }


            if (merchantList.get(position).getIsOpen().equals("close")) {
                holder.binding.status.setText("CLOSE");
                holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#FF0000"));
                holder.binding.merchaneImage.setColorFilter(filter);

            } else {
                holder.binding.status.setText("OPEN");
                holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#44AC48"));


            }



            String lat = SharedPrefManagerLocation.getInstance(context).locationModel(Constraint.LATITUDE);
            String lon = SharedPrefManagerLocation.getInstance(context).locationModel(Constraint.LONGITUDE);
            Log.e("sdsd", lat + " sdsdsd " + lon);
            double distance = distance(Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(merchantList.get(position).getLatitude()), Double.parseDouble(merchantList.get(position).getLongitude()));
            Log.e("sds", distance + "");
            double a = distance;
            double roundOff = Math.round(a * 100) / 100;
//            holder.binding.costForTwo.setText("₹ " + merchant.getCost_tag() + " For Two ");
//            holder.binding.categoryList.setText(merchant.getCategoriesname());
            int estMinut= Integer.parseInt(merchantList.get(position).getEstimatedDelivery())%60;
            int estTIme= Integer.parseInt(merchantList.get(position).getEstimatedDelivery())/60;
            if(estTIme>0){
                if(estMinut>0) {
                    String estTImeForValue = estTIme + " Hr " + estMinut + " Mins";
                    holder.binding.estimatedTime.setText(estTImeForValue);
                }else{
                    String estTImeForValue = estTIme + " Hour";
                    holder.binding.estimatedTime.setText(estTImeForValue);
                }
            }else{
                String estTImeForValue=estMinut+" Mins";
                holder.binding.estimatedTime.setText(estTImeForValue);
            }



            //holder.binding.estimatedTime.setText(merchant.getCategories());



            holder.binding.tvMerchantDistance.setText(merchantList.get(position).getDistance() + " km ");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //StoreDetailsActivity.merchant=merchant;
                    if (merchantList.get(position).getIsOpen().equalsIgnoreCase("close")) {
                        String mername=merchantList.get(position).getName();
                        Toast.makeText(context, "Restaurant Closed", Toast.LENGTH_SHORT).show();
                    } else {
//                            StoreDetailsActivity.merchant1=merchant;
                        String mername=merchantList.get(position).getName();
                        Intent in = new Intent(context, StoreDetailsActivity.class);
                        in.putExtra("key", 1);
                        in.putExtra("merchant_id", merchantList.get(position).getId());
                        context.startActivity(in);
                    }


                }
            });


            holder.binding.ratingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (merchant.getCloseStatus().equals("true")) {

                    } else {
                        ReviewActivity.merchant = merchant;
                        context.startActivity(new Intent(context, ReviewActivity.class));
                    }
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return merchantList.size();
    }
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        HighlightetRestroBinding binding;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=HighlightetRestroBinding.bind(itemView);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.60934;
        //   Log.i("distance", dist+"");
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    private void addWishlist(String clear, TopRestaurantAdapter4.AdapterViewHolder holder, String id)
    {
        ShowProgress.getShowProgress(context).show();
        MyApi myApi= RestaurantFragment.singleTask.getRetrofit().create(MyApi.class);

        Call<JsonArray> call=myApi.addToWishList(u_id,id,clear);
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
                            ShowProgress.getShowProgress(context).hide();
                            if(clear.equals(""))
                            {
                                holder.binding.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.fill_wishlist_icon));
                                merchant.setWishliststatus("true");
                            }
                            else
                            {
                                holder.binding.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.favourit_icon));
                                merchant.setWishliststatus("false");
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            ShowProgress.getShowProgress(context).hide();
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }


}
