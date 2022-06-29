package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ItemRatingLayoutBinding;
import digi.coders.thecapsico.databinding.ShopLayoutBinding;
import digi.coders.thecapsico.databinding.ShopLayoutNewBinding;


public class TopRestAdapter extends RecyclerView.Adapter<TopRestAdapter.MyHolder> {


    String[] name;
    Context ctx;

    public TopRestAdapter(String[] name, Context ctx){
        this.ctx=ctx;
        this.name=name;
    }
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_layout_new,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ShopLayoutNewBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding= ShopLayoutNewBinding.bind(itemView);
        }
    }
}
