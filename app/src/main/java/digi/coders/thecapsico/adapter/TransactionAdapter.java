package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.TransactionDesignBinding;
import digi.coders.thecapsico.model.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyHolder> {

    private Context ctx;
    private List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Transaction transaction=list.get(position);
        holder.binding.msg.setText(transaction.getMsg());
        if (transaction.getType().equals("debit")){
            holder.binding.type.setText(transaction.getType());
            holder.binding.type.setTextColor(ctx.getResources().getColor(R.color.red));

        }else {
            holder.binding.type.setText(transaction.getType());
            holder.binding.type.setTextColor(ctx.getResources().getColor(R.color.color_green));
        }
        holder.binding.amt.setText("₹ "+transaction.getAmount());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(transaction.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt+" "+transaction.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }
//        holder.binding.date.setText(transaction.getCreatedAt());
        holder.binding.txtId.setText(transaction.getTxtId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TransactionDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=TransactionDesignBinding.bind(itemView);
        }
    }

}
