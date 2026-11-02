package com.example.rentconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentconnect.R;
import com.example.rentconnect.models.Ticket;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private Context context;
    private List<Ticket> ticketList;

    public TicketAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.tvCategory.setText("Loại sự cố: " + ticket.getCategory());
        holder.tvPriority.setText("Ưu tiên: " + ticket.getPriority());
        holder.tvDesc.setText("Mô tả: " + ticket.getDescription());
        holder.tvStatus.setText("Trạng thái: " + ticket.getStatus());
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvPriority, tvDesc, tvStatus;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvTicketCategory);
            tvPriority = itemView.findViewById(R.id.tvTicketPriority);
            tvDesc = itemView.findViewById(R.id.tvTicketDesc);
            tvStatus = itemView.findViewById(R.id.tvTicketStatus);
        }
    }
}
