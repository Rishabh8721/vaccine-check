package com.floplabs.vaccinecheck.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

import com.floplabs.vaccinecheck.NotifierChannelActivity;
import com.floplabs.vaccinecheck.R;
import com.floplabs.vaccinecheck.entity.NotifierChannel;

import java.util.HashMap;
import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {
    private final List<NotifierChannel> notifierChannels;
    private final Context context;
    private HashMap<String, WorkInfo.State> activeChannels;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView district;
        private final LinearLayout centersLayout;
        private final LinearLayout vaccinesLayout;
        private final TextView dose;
        private final TextView age;
        private final TextView state;
        private final TextView fee;
        private final Button delete;
        private final Button edit;
        private final Button start;

        public ViewHolder(View view) {
            super(view);
            district = view.findViewById(R.id.district);
            centersLayout = view.findViewById(R.id.centersLayout);
            vaccinesLayout = view.findViewById(R.id.vaccinesLayout);
            dose = view.findViewById(R.id.dose);
            age = view.findViewById(R.id.age);
            fee = view.findViewById(R.id.fee);
            delete = view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);
            start = view.findViewById(R.id.start);
            state = view.findViewById(R.id.state);
        }

        public TextView getDistrict() {
            return district;
        }

        public LinearLayout getCentersLayout() {
            return centersLayout;
        }

        public LinearLayout getVaccinesLayout() {
            return vaccinesLayout;
        }

        public TextView getDose() {
            return dose;
        }

        public TextView getAge() {
            return age;
        }

        public TextView getFee() {
            return fee;
        }

        public Button getDelete() {
            return delete;
        }

        public Button getEdit() {
            return edit;
        }

        public Button getStart() {
            return start;
        }

        public TextView getState() {
            return state;
        }
    }

    public ChannelListAdapter(List<NotifierChannel> notifierChannels, HashMap<String, WorkInfo.State> activeChannels, Context context) {
        this.notifierChannels = notifierChannels;
        this.context = context;
        this.activeChannels = activeChannels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.channel_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        NotifierChannelActivity notifierChannelActivity = (NotifierChannelActivity) context;
        viewHolder.getDistrict().setText(notifierChannels.get(position).getDistrictName());
        viewHolder.getDose().setText((notifierChannels.get(position).isSecondDose()) ? "Dose 2" : "Dose 1");
        viewHolder.getAge().setText(notifierChannels.get(position).getAge() + "+");

        switch (notifierChannels.get(position).getFeeType()) {
            case 0:
                viewHolder.getFee().setText("Any");
                break;
            case 1:
                viewHolder.getFee().setText("Free");
                break;
            case 2:
                viewHolder.getFee().setText("Paid");
                break;
        }

        for (String vaccine : notifierChannels.get(position).getVaccines())
            viewHolder.getVaccinesLayout().addView(getPillTextView(vaccine, R.drawable.pill_blue));

        if (notifierChannels.get(position).getCenters().isEmpty())
            viewHolder.getCentersLayout().addView(getPillTextView("All", R.drawable.pill_green));
        else
            for (String centerName : notifierChannels.get(position).getCenters().values())
                viewHolder.getCentersLayout().addView(getPillTextView(centerName, R.drawable.pill_green));

        if (activeChannels.containsKey(notifierChannels.get(position).getWorkId())) {
            if(activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.RUNNING) {
                viewHolder.state.setText("Running");
                viewHolder.start.setText("Stop");
                viewHolder.start.setTextColor(ContextCompat.getColor(context, R.color.negative_red));
            }else if(activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.CANCELLED)
                viewHolder.state.setText("Stopped");
            else if(activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.ENQUEUED)
                viewHolder.state.setText("Queued");
            else if(activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.SUCCEEDED)
                viewHolder.state.setText("Succeeded");
            else if(activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.FAILED)
                viewHolder.state.setText("Failed");
            else if(activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.RUNNING)
                viewHolder.state.setText("Running");
        }

        viewHolder.delete.setOnClickListener(v -> notifierChannelActivity.deleteFromLocal(notifierChannels.get(position).getDid()));
        viewHolder.edit.setOnClickListener(v -> Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show());
        viewHolder.start.setOnClickListener(v -> {
            if (activeChannels.containsKey(notifierChannels.get(position).getWorkId()) && activeChannels.get(notifierChannels.get(position).getWorkId()) == WorkInfo.State.RUNNING)
                notifierChannelActivity.stopChannel(notifierChannels.get(position).getDid());
            else
                notifierChannelActivity.startChannel(notifierChannels.get(position));
        });
    }

    private View getPillTextView(String time, int pill) {
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
        timeTextView.setBackgroundResource(pill);
        timeTextView.setPadding(7, 2, 7, 2);
        return timeTextView;
    }

    @Override
    public int getItemCount() {
        return notifierChannels.size();
    }
}


