package digi.coders.thecapsico.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.thecapsico.R;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.MyHolder> {

    private int lastCheckedPosition = -1;
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_address_layout,parent,false);
        return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        holder.radioButton.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private RadioButton radioButton;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio_button);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);

                }
            });
        }
    }
}
