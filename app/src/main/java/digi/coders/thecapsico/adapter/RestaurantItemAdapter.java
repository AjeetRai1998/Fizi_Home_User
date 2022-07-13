package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.AllCategoriesActivity;
import digi.coders.thecapsico.activity.BrowesStoreActivity;
import digi.coders.thecapsico.databinding.CategoryGridLayoutBinding;
import digi.coders.thecapsico.databinding.CategoryLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.Category;

public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.MyHolder> {
    private Context ctx;
    private int length=8;
    private int i;
    private View view ;
    private List<Category> list;


    public RestaurantItemAdapter(int i, List<Category> list) {
        this.i = i;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();

        if(i==1)
        {
            //horizontal
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        }
        else
        {
            //Grid  category_grid_layout
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        }

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
        if(list!=null)
        {


            Category category=list.get(position);

            if(i==1) {

                holder.binding1.categoryTitle.setText(category.getName());
                Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.CATEGORY+category.getIcon())
                        .placeholder(R.drawable.placeholder1).into(holder.binding1.categoryImage);
                    if (position == 7) {
                        holder.binding1.mainLayout.setVisibility(View.GONE);
                        holder.binding1.viewAllLayout.setVisibility(View.VISIBLE);
                        holder.binding1.viewAllLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AllCategoriesActivity.merchantCatregoryId= category.getMerchantCategoryId();
                                ctx.startActivity(new Intent(ctx, AllCategoriesActivity.class));
                            }
                        });
                    } else {
                        holder.binding1.mainLayout.setVisibility(View.VISIBLE);
                        holder.binding1.viewAllLayout.setVisibility(View.GONE);
                     }

            }
            else
            {
                holder.binding2.categoryTitle.setText(category.getName());
                Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.CATEGORY+category.getIcon())
                        .placeholder(R.drawable.placeholder1).into(holder.binding2.categoryImage);

            }

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, BrowesShopActivity.class));
            }
        });*/
            if(i==1)
            {
                holder.binding1.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(ctx, BrowesStoreActivity.class);
                        in.putExtra("categoryName",category.getName());
                        in.putExtra("categoryId",category.getId());
                        in.putExtra("merchant_category_id",category.getMerchantCategoryId());
                        ctx.startActivity(in);
                    }
                });
            }
            else
            {
                holder.binding2.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(ctx, BrowesStoreActivity.class);
                        in.putExtra("categoryName",category.getName());
                        in.putExtra("categoryId",category.getId());
                        in.putExtra("merchant_category_id",category.getMerchantCategoryId());
                        ctx.startActivity(in);
                    }
                });

            }
        }


        //length--;
    }

    @Override
    public int getItemCount() {

            return  list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CategoryLayoutBinding binding1;
        CategoryLayoutBinding binding2;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            if(i==1)
            {
                binding1= CategoryLayoutBinding.bind(itemView);
            }
            else
            {
                binding2=CategoryLayoutBinding.bind(itemView);
            }

        }
    }
}


