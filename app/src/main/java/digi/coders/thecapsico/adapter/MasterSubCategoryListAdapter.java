package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.ItemDetailsActivity;
import digi.coders.thecapsico.databinding.FoodLayoutBinding;
import digi.coders.thecapsico.fragment.BottomDialogFragment;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.Product;

public class MasterSubCategoryListAdapter extends RecyclerView.Adapter<MasterSubCategoryListAdapter.MyHolder> {

    public static   BottomDialogFragment addPhotoBottomDialogFragment;
    private Context ctx;
    private FragmentManager fm;
    private int status;
    private View view;
    FragmentActivity f;
    private List<Product> list;
    private String merchantId;

 /*   public MasterSubCategoryListAdapter(FragmentManager fm, int status,FragmentActivity f) {
        this.fm = fm;
        this.status = status;
        this.f=f;
    }*/

    public MasterSubCategoryListAdapter(FragmentManager fm, int status, FragmentActivity f,List<Product> list,String merchantId) {
        this.fm = fm;
        this.status = status;
        this.f = f;
        this.list=list;
        this.merchantId=merchantId;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        if(status==1)
        {
            //show Grid view
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout_grid_layout,parent,false);
        }
        else
        {
            //show vertical view


            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout,parent,false);

        }

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Product product=list.get(position);
        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.MASTER_PRODUCT+product.getIcon())
                .placeholder(R.drawable.placeholder1).into(holder.binding.proImage);
        holder.binding.proName.setText(product.getName());
        holder.binding.proPrice.setText("₹"+product.getSellPrice());
        holder.binding.proDescription.setText(Html.fromHtml(product.getDescription()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailsActivity.product=product;
                ItemDetailsActivity.merchantId=merchantId;
                ctx.startActivity(new Intent(ctx, ItemDetailsActivity.class));
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private MaterialButton add;
        private LinearLayout increDeceLayout;
        private LinearLayout minus,plus;
        private TextView quanityTxt;
        FoodLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=FoodLayoutBinding.bind(itemView);
            /*add=itemView.findViewById(R.id.add_item);
            increDeceLayout=itemView.findViewById(R.id.incredecrelayout);
            minus=itemView.findViewById(R.id.minus_button);
            plus=itemView.findViewById(R.id.plus_button);
            quanityTxt=itemView.findViewById(R.id.quantity_text_view);*/
        }
    }
}
