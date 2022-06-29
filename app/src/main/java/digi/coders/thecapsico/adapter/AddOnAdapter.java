package digi.coders.thecapsico.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.Visibility;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.fragment.CartAddonFragment;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.helper.Refresh;
import digi.coders.thecapsico.model.AddOnGroup;
import digi.coders.thecapsico.model.AddOnProduct;

public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.MyHolder> {

    private List<AddOnProduct> list;
    private Context ctx;
    public static AddOnGroup addOnGroup;
    private int lastCheckedPosition = 0;
    int pos;
    int parentPos;
    public AddOnAdapter(List<AddOnProduct> list,int parentPos) {
        this.list = list;
        this.parentPos = parentPos;
    }

    int checkstatus=-1;
    RadioGroup radioGroup;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        radioGroup=new RadioGroup(ctx);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

        AddOnProduct product = list.get(position);
        if (product.getPrice().equalsIgnoreCase("0")){
            holder.productp.setText(product.getName() );
       }else{
            holder.productp.setText(product.getName() + "  " + "+ ₹" + product.getPrice());
        }
        if(product.getType().equals("veg"))
        {
            Picasso.get().load(R.drawable.vegdot).into( holder.type);
//            holder.type.setBackgroundResource(R.drawable.vegdot);
        }
        else
        {  Picasso.get().load(R.drawable.non_veg).into( holder.type);
//            holder.type.setBackgroundResource(R.drawable.non_veg);
        }


        if(checkstatus==-1){
            if ( position==0) {
                holder.radioButton.setChecked(true);
            } else {

                holder.radioButton.setChecked(false);
            }
        }else {
            if (checkstatus == position) {
                holder.radioButton.setChecked(true);
                if(CartAddonFragment.refresh!=null){
                    if(!CartAddonFragment.parentList.get(parentPos).contains(product.getName())) {
                        CartAddonFragment.parentList.get(parentPos).set(position,product.getName());
                        CartAddonFragment.parentPriceList.get(parentPos).set(position,product.getPrice());
                        CartAddonFragment.parentIDList.get(parentPos).set(position,product.getId());
                    }else{
                        CartAddonFragment.parentList.get(parentPos).set(position,"");
                        CartAddonFragment.parentPriceList.get(parentPos).set(position,"");
                        CartAddonFragment.parentIDList.get(parentPos).set(position,"");
                    }
//                    else{
//                        CartAddonFragment.name =CartAddonFragment.name+","+ product.getName();
//                    }
                    CartAddonFragment.refresh.onRefresh();

                }
            } else {

                if(CartAddonFragment.parentList.get(parentPos).contains(product.getName())) {
                    CartAddonFragment.parentList.get(parentPos).set(position,"");
                    CartAddonFragment.parentPriceList.get(parentPos).set(position,"");
                    CartAddonFragment.parentIDList.get(parentPos).set(position,"");
                }
                CartAddonFragment.refresh.onRefresh();
                holder.radioButton.setChecked(false);
            }
        }
        holder.radioButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkstatus != position) {
                            if (holder.radioButton.isChecked()) {
                                holder.radioButton.setChecked(true);
                                Constraint.addonId = product.getId();
                                Constraint.addonPrice = product.getPrice();
                                checkstatus = position;
                                notifyDataSetChanged();
                            } else {
                                holder.radioButton.setChecked(false);

                            }
                        }
                    }
                }
        );
        if (addOnGroup.getType().equals("single")) {
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
        }else{
            holder.radioButton.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
        }



        if(checkstatus==-1) {
            if (addOnGroup.getType().equals("single")) {
                holder.radioButton.setVisibility(View.VISIBLE);
                holder.checkBox.setVisibility(View.GONE);
                if ( position==0) {
                    if(CartAddonFragment.refresh!=null){
                        if(!CartAddonFragment.parentList.get(parentPos).contains(product.getName())) {
                            CartAddonFragment.parentList.get(parentPos).set(position,product.getName());
                            CartAddonFragment.parentPriceList.get(parentPos).set(position,product.getPrice());
                            CartAddonFragment.parentIDList.get(parentPos).set(position,product.getId());
                        }else{
                            CartAddonFragment.parentList.get(parentPos).set(position,"");
                            CartAddonFragment.parentPriceList.get(parentPos).set(position,"");
                            CartAddonFragment.parentIDList.get(parentPos).set(position,"");
                        }
//                    else{
//                        CartAddonFragment.name =CartAddonFragment.name+","+ product.getName();
//                    }
                        CartAddonFragment.refresh.onRefresh();

                    }
                    holder.radioButton.setChecked(true);
                } else {

                    if (product.getCartStatus().equals("true")) {
                        holder.radioButton.setChecked(true);
                    } else {
                        holder.radioButton.setChecked(false);
                    }
                }

            } else {
                holder.radioButton.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.VISIBLE);

                if (product.getCartStatus().equals("true")) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }

            }
        }
        holder.checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (CartAddonFragment.refresh != null) {
                                if(!CartAddonFragment.parentList.get(parentPos).contains(product.getName())) {
                                    CartAddonFragment.parentList.get(parentPos).set(position,product.getName());
                                    CartAddonFragment.parentPriceList.get(parentPos).set(position,product.getPrice());
                                    CartAddonFragment.parentIDList.get(parentPos).set(position,product.getId());
                                }else{
                                    CartAddonFragment.parentList.get(parentPos).set(position,"");
                                    CartAddonFragment.parentPriceList.get(parentPos).set(position,"");
                                    CartAddonFragment.parentIDList.get(parentPos).set(position,"");
                                }
                                CartAddonFragment.refresh.onRefresh();
                            }
                        } else {
                            CartAddonFragment.parentList.get(parentPos).set(position,"");
                            CartAddonFragment.parentPriceList.get(parentPos).set(position,"");
                            CartAddonFragment.parentIDList.get(parentPos).set(position,"");
                            CartAddonFragment.refresh.onRefresh();
                        }
                    }
                }
        );


        if(checkstatus==-1) {
            if (position == list.size() - 1) {
                checkstatus = 0;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        RadioButton radioButton;
        CheckBox checkBox;
        TextView productp;
        ImageView type;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            radioButton=itemView.findViewById(R.id.radioButton);
            checkBox=itemView.findViewById(R.id.checkBox);
            productp=itemView.findViewById(R.id.productP);
            type=itemView.findViewById(R.id.type);
        }
    }
}
