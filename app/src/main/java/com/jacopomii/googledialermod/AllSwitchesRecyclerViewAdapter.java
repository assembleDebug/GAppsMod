package com.jacopomii.googledialermod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllSwitchesRecyclerViewAdapter extends RecyclerView.Adapter<AllSwitchesRecyclerViewAdapter.AllSwitchesViewHolder> implements Filterable {
    Context mContext;
    List<SwitchRowItem> mData;
    List<SwitchRowItem> mDataFiltered;

    public AllSwitchesRecyclerViewAdapter(Context context, List<SwitchRowItem> data) {
        mContext = context;
        mData = data;
        mDataFiltered = data;
    }

    @NonNull
    @Override
    public AllSwitchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.switch_row_item, parent, false);
        AllSwitchesViewHolder viewHolder = new AllSwitchesViewHolder(v);

        viewHolder.mS.setOnClickListener(view -> { // TODO: doesn't work if user slide the switch
            mDataFiltered.get(viewHolder.getAdapterPosition()).setSwitchChecked(viewHolder.mS.isChecked());
            DBFlagsSingleton.getInstance(mContext).updateDBFlag(viewHolder.mT.getText().toString(), viewHolder.mS.isChecked());
            notifyItemChanged(viewHolder.getAdapterPosition());
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllSwitchesViewHolder holder, int position) {
        holder.mT.setText(mDataFiltered.get(position).getSwitchText());
        holder.mS.setChecked(mDataFiltered.get(position).getSwitchChecked());
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                try {
                    JSONObject filterConfig = new JSONObject(charSequence.toString());
                    String key = filterConfig.getString("key");
                    String mode = filterConfig.getString("mode");

                    List<SwitchRowItem> lstFiltered = new ArrayList<>();
                    for (SwitchRowItem row : mData) {
                        if (row.getSwitchText().toLowerCase().contains(key.toLowerCase())) {
                            boolean switchStatus = row.getSwitchChecked();
                            if (mode.equals("all") || (mode.equals("enabled_only") && switchStatus) || (mode.equals("disabled_only") && !switchStatus))
                                lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }



            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataFiltered = (List<SwitchRowItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class AllSwitchesViewHolder extends RecyclerView.ViewHolder {
        private final TextView mT;
        private final Switch mS;

        public AllSwitchesViewHolder(View itemView) {
            super(itemView);
            mT = itemView.findViewById(R.id.switch_row_item_textview);
            mS = itemView.findViewById(R.id.switch_row_item_switch);
        }
    }
}
