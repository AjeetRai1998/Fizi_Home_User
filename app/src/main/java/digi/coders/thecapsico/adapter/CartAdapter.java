package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.CartItemLayoutBinding;
import digi.coders.thecapsico.helper.AppConstraints;
import digi.coders.thecapsico.model.OrderItem;
import digi.coders.thecapsico.singletask.SingleTask;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    private List<OrderItem> list;
    private Context ctx;
    private SingleTask singleTask;
    GetPosition getPosition;

    public CartAdapter(List<OrderItem> list,SingleTask singleTask) {
        this.list = list;
        this.singleTask=singleTask;
    }


    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
        return new MyHolder(view);
    }

    int quantity;
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        OrderItem item = list.get(position);
        holder.binding.productName.setText(item.getProduct().getName());
        holder.binding.quantityTextView.setText("  × " +item.getQty());
        holder.binding.quantityTextView.setText(item.getQty());
        quantity= Integer.parseInt(item.getProduct().getQuantity());
        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.MASTER_PRODUCT+item.getProduct().getIcon())
                .placeholder(R.drawable.placeholder).into(holder.binding.productimage);
        getPosition.findPos(position,holder,holder.binding);


    }

    public void findMyPosition(GetPosition getPosition)
    {
        this.getPosition=getPosition;
    }

    public interface  GetPosition
    {
        void findPos(int position, MyHolder holder, CartItemLayoutBinding binding);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private ImageView plusbutton,minusbutton;
        private TextView quantity_text_view;
        CartItemLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            plusbutton =itemView.findViewById( R.id.pulsbutton );
            minusbutton=itemView.findViewById( R.id.minusbutton );
            quantity_text_view=itemView.findViewById(R.id.quantity_text_view);
            binding=CartItemLayoutBinding.bind(itemView);
        }
    }


}
