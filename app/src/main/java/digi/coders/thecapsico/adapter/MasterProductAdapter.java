package digi.coders.thecapsico.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.MasterProductLayoutBinding;
import digi.coders.thecapsico.fragment.RestaurantSupportFragment;
import digi.coders.thecapsico.fragment.ProductListFragment;
import digi.coders.thecapsico.fragmentmodel.HomeViewModel;
import digi.coders.thecapsico.fragmentmodel.ProductViewModel;
import digi.coders.thecapsico.model.Category;

public class MasterProductAdapter extends RecyclerView.Adapter<MasterProductAdapter.MyHolder> {

    private Context ctx;
    private int i=0;
    private FragmentManager fm;
    private int row_index=0;
    private List<Category> list;
    private ProductViewModel productViewModel;
    private FragmentActivity activity;

    public MasterProductAdapter(FragmentManager fm,List<Category> list,FragmentActivity activity) {
        this.fm = fm;
        this.list=list;
        this.activity=activity;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.master_product_layout,parent,false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        Category category=list.get(position);
        Log.e("sds",category.getMerchantId()+"sd");
        holder.binding.title.setText(category.getName());
        /*holder.masterProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0)
                {
                    holder.masterSubCategoryList.setVisibility(View.GONE);
                    holder.down.setVisibility(View.GONE);
                    holder.forward.setVisibility(View.VISIBLE);
                    i++;
                }
                else{
                    holder.masterSubCategoryList.setVisibility(View.VISIBLE);
                    holder.down.setVisibility(View.VISIBLE);
                    holder.forward.setVisibility(View.GONE);
                    i=0;
                }
            }
        });


    holder.masterSubCategoryList.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
    holder.masterSubCategoryList.setAdapter(new MasterSubCategoryListAdapter(fm));*/
        if(position==0)
        {
            holder.binding.title.setText("Restaurant support");

        }

        holder.masterProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                notifyDataSetChanged();
                Log.e("sdsd",position+"");

                if(position==0)
                {
                    if(fm!=null) {
                        productViewModel=new ViewModelProvider(activity).get(ProductViewModel.class);
                        productViewModel.setCategory(category);
//                        fm.beginTransaction().replace(R.id.productContainer, new RestaurantSupportFragment()).addToBackStack("t").commit();
                    }
                }
                else
                {

                    if(fm!=null)
                    {
                        productViewModel=new ViewModelProvider(activity).get(ProductViewModel.class);
                        productViewModel.setCategory(category);
  //                      fm.beginTransaction().replace(R.id.productContainer,new ProductListFragment()).commit();
                    }


                }
            }
        });
        if(row_index==position){
            //holder.masterProductLayout.setBackgroundColor(ctx.getResources().getColor(R.color.oragne));
            holder.binding.indicator.setVisibility(View.VISIBLE);

        }
        else
        {
            //holder.masterProductLayout.setBackgroundColor(ctx.getResources().getColor(R.color.color_blue));
            holder.binding.indicator.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private RecyclerView masterSubCategoryList;
        private LinearLayout masterProductLayout;
        private ImageView forward,down;
        MasterProductLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {

            super(itemView);
            binding=MasterProductLayoutBinding.bind(itemView);
            //masterSubCategoryList=itemView.findViewById(R.id.master_product_sub_category_list);
            masterProductLayout=itemView.findViewById(R.id.master_layout);
            forward=itemView.findViewById(R.id.arrow__forware_img);
            down=itemView.findViewById(R.id.arrow_down_img);

        }
    }

}
