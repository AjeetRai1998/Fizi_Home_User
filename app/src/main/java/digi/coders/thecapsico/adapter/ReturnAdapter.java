package digi.coders.thecapsico.adapter;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.OrderSummaryActivity;
import digi.coders.thecapsico.databinding.ReturnListBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.helper.MYMvp;
import digi.coders.thecapsico.model.CheckboxModel;
import digi.coders.thecapsico.model.ProductId;
import digi.coders.thecapsico.model.ReturnProductModel;

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.AdapterViewHolder> {
    Context context;
    List<ReturnProductModel> models;
    List<CheckboxModel> checkboxModels;
    MYMvp myMvp;

    public ReturnAdapter(Context context,  List<ReturnProductModel> models,MYMvp myMvp) {
        this.context = context;
        this.models = models;
        this.myMvp = myMvp;
//        checkboxModels=new ArrayList<>();
    }

    @NonNull
    @Override
    public ReturnAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(digi.coders.thecapsico.R.layout.return_list,parent,false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnAdapter.AdapterViewHolder holder, int position) {
        Picasso.get().load(models.get(position).getImage())
                .placeholder(digi.coders.thecapsico.R.drawable.placeholder1).into(holder.binding.image);

        holder.binding.cbName.setText(models.get(position).getName());
        holder.binding.tvPrice.setText("\u20B9 "+models.get(position).getPrice());
        holder.binding.tvQty.setText("Qty X "+models.get(position).getQty());

        holder.binding.cbName.setChecked(models.get(position).getSelected());
        holder.binding.cbName.setTag(position);

        holder.binding.cbName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.binding.cbName.getTag();
//                Toast.makeText(context, checkboxModels.get(pos).getId()+ " clicked!", Toast.LENGTH_SHORT).show();

                if (models.get(pos).getSelected()) {
                    models.get(pos).setSelected(false);
//                    OrderSummaryActivity.addProduct.remove(models.get(pos).getId());
                    myMvp.getData(models.get(pos).getId());
//                    Toast.makeText(context, models.get(pos).getName()+ "un checked!", Toast.LENGTH_SHORT).show();
                } else {
                    models.get(pos).setSelected(true);
                    myMvp.getData(models.get(pos).getId());
//                    if (!OrderSummaryActivity.addProduct.contains(models.get(pos).getId())){
//                        OrderSummaryActivity.addProduct.add(new ProductId(models.get(pos).getId()));
//                    }else {
//                        Toast.makeText(context, models.get(pos).getId()+" Already added", Toast.LENGTH_SHORT).show();
//                    }

//                    Toast.makeText(context, models.get(pos).getName()+ " checked!", Toast.LENGTH_SHORT).show();

                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return models.size();
    }
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        ReturnListBinding binding;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReturnListBinding.bind(itemView);
        }
    }
}
