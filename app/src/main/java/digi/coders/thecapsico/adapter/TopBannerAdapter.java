package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.OfferRestaurentsActivity;
import digi.coders.thecapsico.databinding.ItemRatingLayoutBinding;
import digi.coders.thecapsico.databinding.TopBannerLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.Banner2Model;


public class TopBannerAdapter extends RecyclerView.Adapter<TopBannerAdapter.MyHolder> {


    ArrayList<Banner2Model> name;
    Context ctx;

    public TopBannerAdapter(ArrayList<Banner2Model> name, Context ctx){
        this.ctx=ctx;
        this.name=name;
    }
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.top_banner_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        Picasso.get()
                .load(AppConstraints.BASE_URL+name.get(position).getImage()).placeholder(R.drawable.placehold)
                .into(holder.binding.image);

        holder.binding.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OfferRestaurentsActivity.id=name.get(position).getId();
                        Intent intent=new Intent(ctx, OfferRestaurentsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TopBannerLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding= TopBannerLayoutBinding.bind(itemView);
        }
    }
}
