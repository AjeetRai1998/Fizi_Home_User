package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CouponFullViewActivity;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.MyHolder> {


    private Context ctx;

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, CouponFullViewActivity.class));
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
