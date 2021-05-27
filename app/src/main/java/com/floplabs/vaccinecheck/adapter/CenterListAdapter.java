package com.floplabs.vaccinecheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.floplabs.vaccinecheck.CenterSelect;
import com.floplabs.vaccinecheck.R;
import com.floplabs.vaccinecheck.model.CenterBasic;

import java.util.List;

public class CenterListAdapter extends RecyclerView.Adapter<CenterListAdapter.ViewHolder> {
    private final List<CenterBasic> centerBasics;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox name;
        private final TextView address;
        private final TextView block;
        private final TextView pincode;
        private final TextView feeType;
        private final RelativeLayout titleBackground;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            block = view.findViewById(R.id.block);
            pincode = view.findViewById(R.id.pincode);
            feeType = view.findViewById(R.id.feeType);
            titleBackground = view.findViewById(R.id.titleBackground);
        }

        public CheckBox getName() {
            return name;
        }

        public TextView getAddress() {
            return address;
        }

        public TextView getBlock() {
            return block;
        }

        public TextView getPincode() {
            return pincode;
        }

        public TextView getFeeType() {
            return feeType;
        }

        public RelativeLayout getTitleBackground() {
            return titleBackground;
        }
    }

    public CenterListAdapter(List<CenterBasic> centerBasics, Context context) {
        this.centerBasics = centerBasics;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.center_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CenterSelect centerSelectActivity = (CenterSelect) context;

        if (centerSelectActivity.centerIsPresent(centerBasics.get(position).getId())) {
            viewHolder.getName().setChecked(true);
            viewHolder.getTitleBackground().setBackgroundColor(ContextCompat.getColor(context, R.color.contrast_darkblue));
        }else{
            viewHolder.getName().setChecked(false);
            viewHolder.getTitleBackground().setBackgroundColor(ContextCompat.getColor(context, R.color.contrast_blue_background));
        }

        viewHolder.getName().setText(centerBasics.get(position).getName());
        viewHolder.getAddress().setText(centerBasics.get(position).getAddress());
        viewHolder.getBlock().setText(centerBasics.get(position).getBlock());
        viewHolder.getPincode().setText("(" + centerBasics.get(position).getPincode() + ")");
        viewHolder.getFeeType().setText(centerBasics.get(position).getFeeType());

        viewHolder.getName().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!buttonView.isPressed()) {
                return;
            }

            if (isChecked){
                viewHolder.getTitleBackground().setBackgroundColor(ContextCompat.getColor(context, R.color.contrast_darkblue));
                centerSelectActivity.insertCenter(centerBasics.get(position).getId(), centerBasics.get(position).getName());
            }else{
                viewHolder.getTitleBackground().setBackgroundColor(ContextCompat.getColor(context, R.color.contrast_blue_background));
                centerSelectActivity.removeCenter(centerBasics.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return centerBasics.size();
    }
}


