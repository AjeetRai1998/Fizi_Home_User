package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.CouponActivity;
import digi.coders.thecapsico.activity.OffersActivity;

public class BestOffersAdapter  extends RecyclerView.Adapter<BestOffersAdapter.MyHolder> {

    private Context ctx;
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.best_offers_design,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
       /* GradientDrawable myGrad = (GradientDrawable)holder.layout.getBackground();
        myGrad.setStroke(2, Color.RED);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, CouponActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private CardView layout;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.card);
        }
    }
}
