package com.floplabs.vaccinecheck.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.floplabs.vaccinecheck.R;
import com.floplabs.vaccinecheck.model.Slot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SlotListAdapter extends RecyclerView.Adapter<SlotListAdapter.ViewHolder> {
    private List<Slot> slots;
    private Context context;
    private DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy (EEE)");

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView vaccineAndAge;
        private final TextView capacity;
        private final TextView date;
        private final TextView fee;
        private final TextView center;
        private final TextView block;
        private final TextView pincode;
        private final LinearLayout slotTimingLayout;
        private final RelativeLayout titleBackground;

        public TextView getVaccineAndAge() {
            return vaccineAndAge;
        }

        public TextView getCapacity() {
            return capacity;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getFee() {
            return fee;
        }

        public TextView getCenter() {
            return center;
        }

        public TextView getBlock() {
            return block;
        }

        public TextView getPincode() {
            return pincode;
        }

        public LinearLayout getSlotTimingLayout() {
            return slotTimingLayout;
        }

        public RelativeLayout getTitleBackground() {
            return titleBackground;
        }

        public ViewHolder(View view) {
            super(view);
            vaccineAndAge = (TextView) view.findViewById(R.id.vaccineAndAge);
            capacity = (TextView) view.findViewById(R.id.capacity);
            date = (TextView) view.findViewById(R.id.date);
            fee = (TextView) view.findViewById(R.id.fee);
            center = (TextView) view.findViewById(R.id.center);
            block = (TextView) view.findViewById(R.id.block);
            pincode = (TextView) view.findViewById(R.id.pincode);
            slotTimingLayout = (LinearLayout) view.findViewById(R.id.slotTimingLayout);
            titleBackground = (RelativeLayout) view.findViewById(R.id.titleBackground);
        }
    }

    public SlotListAdapter(List<Slot> slots, Context context) {
        this.slots = slots;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getVaccineAndAge().setText(slots.get(position).getVaccine() + " (" + slots.get(position).getMinAgeLimit() + "+)");
        if(slots.get(position).getAvailableCapacity() == 0)
            viewHolder.getTitleBackground().setBackgroundResource(R.color.negative_red_background);
        else if(slots.get(position).getAvailableCapacity() < 10)
            viewHolder.getTitleBackground().setBackgroundResource(R.color.medium_orange_background);
        else
            viewHolder.getTitleBackground().setBackgroundResource(R.color.positive_green_background);

        if(slots.get(position).getAvailableCapacity() <= 1)
            viewHolder.getCapacity().setText(slots.get(position).getAvailableCapacity() + " dose");
        else
            viewHolder.getCapacity().setText(slots.get(position).getAvailableCapacity() + " doses");

        viewHolder.getDate().setText(dateFormat.format(slots.get(position).getDate()));
        if (slots.get(position).getFee().equals("Free"))
            viewHolder.getFee().setTextColor(ContextCompat.getColor(context, R.color.positive_green));
        else
            viewHolder.getFee().setTextColor(ContextCompat.getColor(context, R.color.negative_red));

        viewHolder.getFee().setText(slots.get(position).getFee());
        viewHolder.getCenter().setText(slots.get(position).getCenterName());
        viewHolder.getBlock().setText(slots.get(position).getBlock());
        viewHolder.getPincode().setText("(" + slots.get(position).getPincode() + ")");
        viewHolder.getBlock().setText(slots.get(position).getBlock());

        for (String time : slots.get(position).getSlots())
            viewHolder.getSlotTimingLayout().addView(getPillTextView(time));
    }

    private View getPillTextView(String time) {
        TextView timeTextView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 10, 0);
        timeTextView.setLayoutParams(params);

        timeTextView.setText(time);
        timeTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        timeTextView.setTypeface(Typeface.DEFAULT_BOLD);
        timeTextView.setBackgroundResource(R.drawable.pill);
        timeTextView.setPadding(7, 2, 7, 2);
        return timeTextView;
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }
}


