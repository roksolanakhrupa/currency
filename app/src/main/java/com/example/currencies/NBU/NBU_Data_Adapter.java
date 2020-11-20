package com.example.currencies.NBU;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencies.R;

import java.util.List;

public class NBU_Data_Adapter extends RecyclerView.Adapter<NBU_Data_Adapter.ViewHolder> {

    private List<NBU_data> nbu_datas;
    private Context context;



    public NBU_Data_Adapter(Context context, List<NBU_data> nbu_datas) {
        this.nbu_datas = nbu_datas;
        this.context = context;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nbu_cc, nbu_txt, nbu_rate;
        LinearLayout rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            nbu_cc = (TextView) itemView.findViewById(R.id.nbu_cc);
            nbu_txt = (TextView) itemView.findViewById(R.id.nbu_txt);
            nbu_rate = (TextView) itemView.findViewById(R.id.nbu_rate);

            rootView = (LinearLayout) itemView.findViewById(R.id.root_element);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nbu, parent, false);


        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NBU_data movie = nbu_datas.get(position);
        holder.nbu_cc.setText(movie.getCc());
        holder.nbu_txt.setText(movie.getTxt());
        holder.nbu_rate.setText(String.format("%.2f", movie.getRate()));

        if (position % 2 == 0)
            holder.rootView.setBackgroundResource(R.color.lightgreen);
        else
            holder.rootView.setBackgroundResource(R.color.white);

    }

    @Override
    public int getItemCount() {
        return nbu_datas.size();
    }
}

