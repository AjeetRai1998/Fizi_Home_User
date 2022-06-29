package digi.coders.thecapsico.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyHolder> {

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
