package digi.coders.thecapsico.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.ShopFullViewActivity;
import digi.coders.thecapsico.fragment.OtherStoreFragment;
import digi.coders.thecapsico.fragment.RestaurantFragment;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyHolder> {

    private Context ctx;
    private FragmentManager fm;

    public CategoriesAdapter(FragmentManager fm) {
        this.fm = fm;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
                //fm.beginTransaction().replace(R.id.home_container,new RestaurantFragment()).commit();

                } else
                {
                    //fm.beginTransaction().replace(R.id.home_container,new OtherStoreFragment()).commit();
                }
                //                ctx.startActivity(new Intent(ctx, ShopFullViewActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
