package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.ItemDetailsActivity;
import digi.coders.thecapsico.databinding.CartItemLayoutBinding;
import digi.coders.thecapsico.databinding.OrderItemDesignBinding;
import digi.coders.thecapsico.fragment.CartAddonFragment;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.OrderItem;
import digi.coders.thecapsico.model.Product;

public class CartOrderItemAdapter extends RecyclerView.Adapter<CartOrderItemAdapter.MyHolder> {
    List<OrderItem> orderList;
    GetPosition getPosition;
    private Context ctx;
    FragmentManager fm;
    public CartOrderItemAdapter(List<OrderItem> orderList,FragmentManager fm) {
        this.orderList = orderList;
        this.fm = fm;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        OrderItem item = orderList.get(position);
        if(item.getProduct().getIsStatus().equalsIgnoreCase("true")) {
            holder.binding.outOfStock.setVisibility(View.GONE);
            holder.binding.lineQty.setVisibility(View.VISIBLE);
            holder.binding.amount.setVisibility(View.VISIBLE);

        }else{
            holder.binding.outOfStock.setVisibility(View.VISIBLE);
            holder.binding.lineQty.setVisibility(View.GONE);
            holder.binding.amount.setVisibility(View.GONE);
        }
        holder.binding.productName.setText(item.getProduct().getName());
        //holder.binding.quantityTextView.setText("  × " +item.getQty());
        holder.binding.quantityTextView.setText(item.getQty());
        //holder.binding.addOnItem.setText(item.getProduct().getAddOnProductName());

        if(item.getProduct().getMenu_type().equalsIgnoreCase("Veg")){
            Picasso.get().load(R.drawable.vegdot).into(holder.binding.productimage);

        }else{
            Picasso.get().load(R.drawable.non_veg).into(holder.binding.productimage);
        }



        if (item.getProduct().getAddOnProductName().equals("")){
            holder.binding.addOnItem.setVisibility(View.GONE);
        }else {
            holder.binding.addOnItem.setVisibility(View.VISIBLE);
        }
        holder.binding.addOnItem.setText(item.getProduct().getAddOnProductName());
        holder.binding.amount.setText("₹ "+item.getProduct().getItemPrice()+"");
        //quantity= Integer.parseInt(item.getProduct().getQuantity());
        //Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.MASTER_PRODUCT+item.getProduct().getIcon()).into(holder.binding.productimage);
        getPosition.findPos(position,holder,holder.binding,"out");
        if(item.getAddOnProductPrize().equalsIgnoreCase("[]")||item.getAddOnProductPrize().equalsIgnoreCase(""))
        {
            holder.binding.customize.setVisibility(View.GONE);
        }
        else
        {
            holder.binding.customize.setVisibility(View.VISIBLE);

        }
        holder.binding.customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product=item.getProduct();
                product.setMerchant_id(item.getMerchantId());
                product.setId(item.getProductId());
                CartAddonFragment bottomSheet = new CartAddonFragment("cust",product,holder.binding.quantityTextView.getText().toString(),null,null,null,0,item.getId());
//                        bottomSheet.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                bottomSheet.show(fm, "");
//
            }
        });
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getPosition.findPos(position,holder,holder.binding,"delete");

            }
        });
    }

    public void findMyPosition(GetPosition getPosition)
    {
        this.getPosition=getPosition;
    }

    public interface  GetPosition
    {
        void findPos(int position, MyHolder holder, OrderItemDesignBinding binding,String status);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        OrderItemDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = OrderItemDesignBinding.bind(itemView);
        }
    }
}
