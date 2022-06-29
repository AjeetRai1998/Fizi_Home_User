package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ItemForRatingBinding;
import digi.coders.thecapsico.databinding.OrderItemsLayoutBinding;
import digi.coders.thecapsico.model.Orderproduct;

public class OrderItemRatingAdapter extends RecyclerView.Adapter<OrderItemRatingAdapter.MyHolder> {


    Orderproduct[] orderItems;
    Context mContext;
    public static ArrayList<String> arrayList;

    public OrderItemRatingAdapter(Orderproduct[] orderItems, Context mContext) {
        this.orderItems = orderItems;
        this.mContext = mContext;
        arrayList=new ArrayList<>(orderItems.length);
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_rating,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.binding.productNameWithQty.setText(orderItems[position].getName());
        holder.binding.addonItems.setHint("How was "+orderItems[position].getName()+"? (Optional)");
        arrayList.add(position,"");
        holder.binding.addonItems.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        arrayList.set(position,s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

//        holder.binding.productPrice.setText("\u20b9"+(Integer.parseInt(orderItems[position].getQty())*(Double.parseDouble(orderItems[position].getPrice())+Double.parseDouble(orderItems[position].getAddonproduct_prize1()))));

    }

    @Override
    public int getItemCount() {
        return orderItems.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ItemForRatingBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=ItemForRatingBinding.bind(itemView);
        }
    }
}
