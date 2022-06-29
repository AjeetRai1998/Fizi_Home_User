package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.CategoryWiseProductDesignBinding;
import digi.coders.thecapsico.model.CategoryProduct;

public class CategorWiseProductAdapter extends RecyclerView.Adapter<CategorWiseProductAdapter.MyHolder> {

    private List<CategoryProduct> list;
    private Context ctx;
    private String status;
    FragmentManager fm;

    public CategorWiseProductAdapter(Context ctx,List<CategoryProduct> list,String status,FragmentManager fm) {
        this.list = list;
        this.status=status;
        this.fm=fm;
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_wise_product_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        CategoryProduct categoryProduct=list.get(position);
        holder.binding.categoryName.setText(categoryProduct.getCategoryName());
        holder.binding.productList.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
        holder.binding.productList.setAdapter(new SimpleProductAdapter(ctx,categoryProduct.getProduct(),status,fm));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        CategoryWiseProductDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=CategoryWiseProductDesignBinding.bind(itemView);
        }

    }

}
