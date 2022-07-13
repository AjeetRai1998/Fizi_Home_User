package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.ReviewActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.ShopLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.SharedPrefManagerLocation;
import digi.coders.thecapsico.model.Merchant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyHolder> {

    private Context ctx;
    private int i;
    private List<Merchant> merchantList;

    public RestaurantAdapter(int i, List<Merchant> merchantList) {
        this.i = i;
        this.merchantList = merchantList;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
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

            Merchant merchant = merchantList.get(position);
            Log.e("close status", merchant.getCloseStatus());

            holder.binding.notAccept.setVisibility(View.GONE);
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            if (merchant.getIsOpen().equals("close")) {
                holder.binding.status.setText("CLOSE");
                holder.binding.notAccept.setVisibility(View.VISIBLE);
                holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#FF0000"));
                holder.binding.layout.setBackgroundColor(ctx.getResources().getColor(R.color.gray1));
                holder.binding.imageCover.setVisibility(View.VISIBLE);
                holder.binding.logo.setColorFilter(filter);
                holder.binding.merchaneImage.setColorFilter(filter);

            } else {
                holder.binding.status.setText("OPEN");
                holder.binding.cardStatus.setCardBackgroundColor(Color.parseColor("#44AC48"));
                holder.binding.imageCover.setVisibility(View.GONE);

            }

            holder.binding.merchantName.setText(merchant.getName());
            if (merchant.getType().equals("veg")) {
                holder.binding.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.vegdot));
            } else if (merchant.getType().equals("non-veg")) {
                holder.binding.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.non_veg));
            } else {
                holder.binding.image.setVisibility(View.GONE);
            }
            Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT_BANNER + merchant.getBanner())
                    .placeholder(R.drawable.placeholder1).into(holder.binding.merchaneImage);
//            Picasso.get().load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
//                    .placeholder(R.drawable.placeholder).into(holder.binding.logo);
            Glide.with(ctx)
                    .load(AppConstraints.BASE_URL + AppConstraints.MERCHANT + merchant.getIcon())
                    .placeholder(R.drawable.placeholder1).into(holder.binding.logo);
            if(merchant.getRating().equalsIgnoreCase("No Ratings Yet")) {
                holder.binding.ratLayout.setVisibility(View.GONE);
                holder.binding.rating.setText("0");
                holder.binding.noRating.setText("(0)");
                holder.binding.ratingBar.setRating(0);
            }else{
                holder.binding.ratLayout.setVisibility(View.GONE);
                holder.binding.rating.setText(merchant.getRating());
                holder.binding.noRating.setText("("+merchant.getRatcount() + ")");
                holder.binding.ratingBar.setRating(Float.parseFloat(merchant.getRating()));
            }


            String lat = SharedPrefManagerLocation.getInstance(ctx).locationModel(Constraint.LATITUDE);
            String lon = SharedPrefManagerLocation.getInstance(ctx).locationModel(Constraint.LONGITUDE);
            Log.e("sdsd", lat + " sdsdsd " + lon);
            double distance = distance(Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(merchant.getLatitude()), Double.parseDouble(merchant.getLongitude()));
            Log.e("sds", distance + "");
            double a = distance;
            double roundOff = Math.round(a * 100) / 100;
            holder.binding.costForTwo.setText("₹ " + merchant.getCost_tag() + " For Two ");
            holder.binding.categoryList.setText(merchant.getCategoriesname());
           int estMinut= Integer.parseInt(merchant.getEstimatedDelivery())%60;
           int estTIme= Integer.parseInt(merchant.getEstimatedDelivery())/60;
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

            if (merchant.getDiscount() == 0) {
//                holder.binding.discountLayout.setVisibility(View.GONE);
                holder.binding.rlDiscount.setVisibility(View.GONE);
            } else {
                holder.binding.rlDiscount.setVisibility(View.VISIBLE);
                if(merchant.getDiscount_type().equalsIgnoreCase("percentage")) {
                    holder.binding.discount.setText((int) merchant.getDiscount() + "" + "% OFF");
                    holder.binding.upto.setText("up to \u20b9"+merchant.getMax_use());
                }else{
                    holder.binding.discount.setText("\u20b9"+(int) merchant.getDiscount() + "" + " OFF");
                    holder.binding.upto.setText("above \u20b9"+merchant.getMinimum_purchase());
                }


            }
            holder.binding.address.setText(merchant.getArea());
            holder.binding.tvMerchantDistance.setText(merchant.getDistance() + " km ");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i == 1) {
                        //StoreDetailsActivity.merchant=merchant;
                        if (merchant.getIsOpen().equalsIgnoreCase("close")) {
                            Toast.makeText(ctx, "Restaurant Closed", Toast.LENGTH_SHORT).show();
                        } else {
//                            StoreDetailsActivity.merchant1=merchant;
                            Intent in = new Intent(ctx, StoreDetailsActivity.class);
                            in.putExtra("key", 1);
                            in.putExtra("merchant_id", merchant.getId());
                            ctx.startActivity(in);
                        }

                    } else {
                        //  ctx.startActivity(new Intent(ctx, ShopFullViewActivity.class));

                    }
                }
            });


            holder.binding.ratLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (merchant.getCloseStatus().equals("true")) {

                    } else {
                        ReviewActivity.merchant = merchant;
                        ctx.startActivity(new Intent(ctx, ReviewActivity.class));
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {

        return merchantList.size();

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ShopLayoutBinding binding;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ShopLayoutBinding.bind(itemView);
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
}
