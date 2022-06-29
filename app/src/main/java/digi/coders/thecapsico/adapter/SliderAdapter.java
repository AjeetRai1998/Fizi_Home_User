package digi.coders.thecapsico.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.BrowesStoreActivity;
import digi.coders.thecapsico.activity.CouponActivity;
import digi.coders.thecapsico.activity.StoreDetailsActivity;
import digi.coders.thecapsico.databinding.SliderLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.Slider;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyHolder> {

    private List<Slider> mSliderItems ;
    private Context ctx;

    public SliderAdapter(List<Slider> mSliderItems) {
        this.mSliderItems = mSliderItems;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Log.e("tr","dere");
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder\
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Slider slider=mSliderItems.get(position);

        Log.e("sdsd",mSliderItems.size()+"");
        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.SLIDER+slider.getIcon())
                .placeholder(R.drawable.placeholder).into(holder.binding.image);
        holder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slider.getType().equalsIgnoreCase("Category"))
                {
                    Intent in=new Intent(ctx, BrowesStoreActivity.class);
                    //in.putExtra("categoryName",category.getName());
                    in.putExtra("categoryId",slider.getClickId());
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(in);
                    //in.putExtra("merchant_category_id",category.getMerchantCategoryId());
                }
                else if(slider.getType().equalsIgnoreCase("Merchant"))
                {
                    Intent in=new Intent(ctx, StoreDetailsActivity.class);
                    in.putExtra("merchant_id",slider.getClickId());
                    in.putExtra("key",1);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(in);

                }
                else if(slider.getType().equalsIgnoreCase("Coupon"))
                {
                    Intent in=new Intent(ctx, CouponActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(in);

                }
                else if(slider.getType().equalsIgnoreCase("url")){
                    String url = slider.getClickId();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        ctx.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        ctx.startActivity(intent);
                    }

                }

            }
        });
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public void renewItems(List<Slider> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Slider sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }
    public class MyHolder extends SliderViewAdapter.ViewHolder
    {
        SliderLayoutBinding binding;
        public MyHolder(View itemView)
        {
            super(itemView);
            binding=SliderLayoutBinding.bind(itemView);
        }
    }
}
