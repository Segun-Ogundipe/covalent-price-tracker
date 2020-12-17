package com.phoenixtech.covalentpricetracker.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phoenixtech.covalentpricetracker.R;
import com.phoenixtech.covalentpricetracker.model.ResponseModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TickerAdapter extends RecyclerView.Adapter<TickerAdapter.ViewHolder> {
    private List<ResponseModel.Ticker> tickers;

    public TickerAdapter() {
        tickers = new ArrayList<>();
    }

    @NonNull
    @Override
    public TickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TickerAdapter.ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("###.##");
        ResponseModel.Ticker ticker = tickers.get(position);
        Picasso.get().load(ticker.logoURL).resize(30, 30).placeholder(R.drawable.onemillion).into(holder.logo);
        holder.ticker.setText(ticker.symbol);
        holder.value.setText(df.format(Float.parseFloat(ticker.fiatValue)));
    }

    @Override
    public int getItemCount() {
        return tickers.size();
    }

    public void setData(List<ResponseModel.Ticker> data) {
        this.tickers.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView ticker;
        TextView value;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.t_logo);
            ticker = itemView.findViewById(R.id.ticker);
            value = itemView.findViewById(R.id.value);
        }
    }
}
