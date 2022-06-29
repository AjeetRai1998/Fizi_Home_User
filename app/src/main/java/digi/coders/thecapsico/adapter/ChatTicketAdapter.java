package digi.coders.thecapsico.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ChatTicketDesignBinding;
import digi.coders.thecapsico.model.ChatTicket;

public class ChatTicketAdapter extends RecyclerView.Adapter<ChatTicketAdapter.MyHolder> {

    private List<ChatTicket> list;

    public ChatTicketAdapter(List<ChatTicket> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_ticket_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        ChatTicket ticket=list.get(position);
        holder.binding.subject.setText(ticket.getSubject());
        holder.binding.ticketId.setText(ticket.getOrder_id());
        holder.binding.orderStatus.setText(ticket.getOrder_status());
        holder.binding.query.setText(ticket.getMessage());
        if(!ticket.getIsStatus().equalsIgnoreCase("success")){
            holder.binding.status.setTextColor(Color.parseColor("#ff0000"));
        }else{
            holder.binding.status.setTextColor(Color.parseColor("#44AC48"));
        }
        holder.binding.status.setText(ticket.getIsStatus());
        if(!ticket.getIsStatus().equals("pending"))
        {
            holder.binding.remark.setText(ticket.getRemark());
        }
        else
        {
            holder.binding.remarKLayout.setVisibility(View.GONE);
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(ticket.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt+"\n"+ticket.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ChatTicketDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=ChatTicketDesignBinding.bind(itemView);
        }
    }
}
