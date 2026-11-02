package com.example.rentconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentconnect.R;
import com.example.rentconnect.models.Room;
import java.text.DecimalFormat;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;
    private DecimalFormat formatter = new DecimalFormat("#,### VNĐ");

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvTitle.setText(room.getTitle());
        holder.tvAddress.setText(room.getAddress());
        holder.tvPrice.setText("Giá: " + formatter.format(room.getPrice()));
        holder.tvStatus.setText("Trạng thái: " + room.getStatus());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAddress, tvPrice, tvStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvRoomTitle);
            tvAddress = itemView.findViewById(R.id.tvRoomAddress);
            tvPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvStatus = itemView.findViewById(R.id.tvRoomStatus);
        }
    }
}
