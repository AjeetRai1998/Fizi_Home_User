package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.AddOnGroupLayoutBinding;
import digi.coders.thecapsico.databinding.AddOnLayoutBinding;
import digi.coders.thecapsico.fragment.CartAddonFragment;
import digi.coders.thecapsico.helper.Constraint;
import digi.coders.thecapsico.model.AddOnGroup;
import digi.coders.thecapsico.model.AddOnProduct;

public class AddOnGroupAdapter extends RecyclerView.Adapter<AddOnGroupAdapter.MyHolder> {

    private Context context;
    private List<AddOnGroup> list;
    private List<AddOnProduct> addOnProductList=null;

    public AddOnGroupAdapter(List<AddOnGroup> list) {
        this.list = list;
//        Constraint.addOnList.clear();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_group_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        AddOnGroup addOnGroup = list.get(position);
//        holder.binding.groupName.setText(addOnGroup.getName() + " ( " + addOnGroup.getType() + " )");
        holder.binding.groupName.setText(addOnGroup.getName());
        Log.e("dssd", addOnGroup.getAddOnList().length + "sdsds");

        System.out.println("ttt=="+addOnGroup.getName());

        holder.binding.addOnList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        addOnProductList = new ArrayList<>();
        CartAddonFragment.parentList.add(new ArrayList<>());
        CartAddonFragment.parentIDList.add(new ArrayList<>());
        CartAddonFragment.parentPriceList.add(new ArrayList<>());
        List<String> dummy=new ArrayList<>();
        List<String> dummy1=new ArrayList<>();
        List<String> dummy2=new ArrayList<>();
        for (int i = 0; i < addOnGroup.getAddOnList().length; i++) {

            dummy.add("");
            dummy1.add("");
            dummy2.add("");
            addOnProductList.add(addOnGroup.getAddOnList()[i]);
        }
        CartAddonFragment.parentList.set(position,dummy);
        CartAddonFragment.parentIDList.set(position,dummy1);
        CartAddonFragment.parentPriceList.set(position,dummy2);

        AddOnAdapter.addOnGroup = addOnGroup;
        AddOnAdapter addOnAdapter = new AddOnAdapter(addOnProductList,position);
//        addOnAdapter.getAdapterPosition(new AddOnAdapter.GetPosition() {
//            @Override
//            public void findPosition(int position, AddOnProduct addOnProduct, AddOnLayoutBinding binding, AddOnGroup addOnGroup1, List<AddOnProduct> list, AddOnAdapter.MyHolder holder1) {
//                getPosition.findPosition(position, addOnProduct, binding, addOnGroup, addOnProductList, holder1);
//            }
//        });
//        Constraint.addOnList.add(position,"");
        holder.binding.addOnList.setAdapter(addOnAdapter);
    }

/*    public interface  GetPosition
    {
        void findPosition(int pos, AddOnProduct addOnProduct, AddOnLayoutBinding binding, AddOnGroup addOnGroup, List<AddOnProduct> addOnList, AddOnAdapter.MyHolder holder1);
    }*/
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        AddOnGroupLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=AddOnGroupLayoutBinding.bind(itemView);
        }
    }
}
