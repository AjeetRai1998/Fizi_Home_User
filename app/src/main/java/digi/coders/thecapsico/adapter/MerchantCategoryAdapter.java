package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.telephony.ims.RcsUceAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.MergePathsContent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.MerchantCategoryLayoutBinding;
import digi.coders.thecapsico.model.MerchantCategory;

public class MerchantCategoryAdapter extends RecyclerView.Adapter<MerchantCategoryAdapter.MyHolder> {

    private List<MerchantCategory> list;
    int row_index=0;
    private Context ctx;
    FindPosition findPosition;

    public MerchantCategoryAdapter(List<MerchantCategory> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_category_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        MerchantCategory merchantCategory=list.get(position);
        holder.binding.categoryName.setText(merchantCategory.getName());
        if(row_index==position){
            holder.binding.categoryName.setTextColor(ctx.getResources().getColor(R.color.color_white));
            holder.binding.categoryName.setBackgroundColor(ctx.getResources().getColor(R.color.pure_black));
        }
        else
        {
            holder.binding.categoryName.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));
            holder.binding.categoryName.setTextColor(ctx.getResources().getColor(R.color.color_black));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
                findPosition.find(v,position);
            }
        });


    }


    public  void getPosition(FindPosition findPosition)
    {
        this.findPosition=findPosition;
    }

    public interface  FindPosition
    {
        void find(View view,int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        MerchantCategoryLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=MerchantCategoryLayoutBinding.bind(itemView);
        }
    }
}
