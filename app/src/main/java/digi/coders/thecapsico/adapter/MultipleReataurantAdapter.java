package digi.coders.thecapsico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.MultipleRestroListBinding;

public class MultipleReataurantAdapter extends RecyclerView.Adapter<MultipleReataurantAdapter.AdapterViewHolder> {
   Context context;
    @NonNull
    @Override
    public MultipleReataurantAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.multiple_restro_list,parent,false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleReataurantAdapter.AdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        MultipleRestroListBinding binding;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=MultipleRestroListBinding.bind(itemView);
        }
    }
}
