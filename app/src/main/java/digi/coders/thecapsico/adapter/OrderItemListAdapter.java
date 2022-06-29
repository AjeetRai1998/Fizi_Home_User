package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.OrderItemLayoutBinding;
import digi.coders.thecapsico.model.OrderItem;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.MyHolder> {
    private List<OrderItem> list;
    private Context ctx;

    public OrderItemListAdapter(List<OrderItem> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        OrderItem orderItem=list.get(position);

        holder.binding.productName.setText(orderItem.getProduct().getName()+"  ×  "+orderItem.getQty());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        OrderItemLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=OrderItemLayoutBinding.bind(itemView);
        }
    }
}
