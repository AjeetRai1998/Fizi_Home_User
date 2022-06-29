package digi.coders.thecapsico.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.StatusLayoutBinding;
import digi.coders.thecapsico.model.OrderStatus;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.MyHolder> {

    List<OrderStatus> statusList;

    public OrderStatusAdapter(List<OrderStatus> statusList) {
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.status_layout,parent,false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        OrderStatus status=statusList.get(position);
        holder.binding.title.setText(status.getOrder_status());
        holder.binding.msg.setText(status.getMsg());
        holder.binding.date.setText(status.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        StatusLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=StatusLayoutBinding.bind(itemView);
        }
    }



}
