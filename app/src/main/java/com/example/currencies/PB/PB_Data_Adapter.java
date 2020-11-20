package com.example.currencies.PB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencies.R;

import java.util.List;

public class PB_Data_Adapter extends RecyclerView.Adapter<PB_Data_Adapter.ViewHolder> {

    private LinearLayout prevView;
    private List<PB_data> pb_datas;
    private Context context;

    public PB_Data_Adapter(Context context, List<PB_data> pb_datas) {
        this.pb_datas = pb_datas;
        this.context = context;
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView pb_ccy, pb_buy, pb_sell;

        public ViewHolder(View itemView) {
            super(itemView);
            pb_ccy = (TextView) itemView.findViewById(R.id.pb_ccy);
            pb_buy = (TextView) itemView.findViewById(R.id.pb_buy);
            pb_sell = (TextView) itemView.findViewById(R.id.pb_sell);
            pb_ccy.setOnClickListener(this);
            pb_buy.setOnClickListener(this);
            pb_sell.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            TextView tmp = (TextView) (((LinearLayout) (v.getParent())).getChildAt(0));

            if (tmp != null) {
                EditText tv = ((View) (v.getParent().getParent().getParent().getParent())).findViewById(R.id.text);
                tv.setText(String.valueOf(tmp.getText()));
            }

            if (prevView != null)
                prevView.setBackgroundResource(R.color.white);

            prevView = (LinearLayout) v.getParent();

            ((LinearLayout) (v.getParent())).setBackgroundResource(R.color.green);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);


        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PB_data movie = pb_datas.get(position);
        holder.pb_ccy.setText(movie.getCurrency());
        holder.pb_buy.setText(String.valueOf(movie.getPurchaseRate()));
        holder.pb_sell.setText(String.valueOf(movie.getSaleRate()));


    }

    @Override
    public int getItemCount() {
        return pb_datas.size();
    }
}
