package com.example.rentconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentconnect.R;
import com.example.rentconnect.models.Invoice;
import java.text.DecimalFormat;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private Context context;
    private List<Invoice> invoiceList;
    private DecimalFormat formatter = new DecimalFormat("#,### VNĐ");

    public InvoiceAdapter(Context context, List<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);
        holder.tvMonth.setText("Hóa Đơn Kỳ: " + invoice.getMonthYear());
        holder.tvTotal.setText("Tổng tiền: " + formatter.format(invoice.getTotalAmount()));
        holder.tvStatus.setText("Trạng thái: " + invoice.getStatus());
        holder.tvQrPayload.setText("Mã VietQR: " + invoice.getQrContent());
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvTotal, tvStatus, tvQrPayload;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvInvoiceMonth);
            tvTotal = itemView.findViewById(R.id.tvInvoiceTotal);
            tvStatus = itemView.findViewById(R.id.tvInvoiceStatus);
            tvQrPayload = itemView.findViewById(R.id.tvInvoiceQrPayload);
        }
    }
}
