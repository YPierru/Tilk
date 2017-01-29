package com.tilk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tilk.R;
import com.tilk.models.FlowDetail;

import java.util.ArrayList;

/**
 * Created by YPierru on 29/01/2017.
 */

public class FlowDetailAdapter extends ArrayAdapter<FlowDetail>{

    private ArrayList<FlowDetail> listFlowDetails;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvTotal;
        TextView tvAvg;
        TextView tvStartTime;
        TextView tvEndTime;
    }

    public FlowDetailAdapter(ArrayList<FlowDetail> data, Context context) {
        super(context, R.layout.item_flow_detail, data);
        this.listFlowDetails = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FlowDetail flowDetail = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_flow_detail, parent, false);
            viewHolder.tvTotal = (TextView) convertView.findViewById(R.id.tv_total);
            viewHolder.tvAvg = (TextView) convertView.findViewById(R.id.tv_avg);
            viewHolder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_starttime);
            viewHolder.tvEndTime = (TextView) convertView.findViewById(R.id.tv_endtime);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTotal.setText(flowDetail.getTotalWater()+"L");
        viewHolder.tvAvg.setText("Débit moyen : "+flowDetail.getAverageFlow()+"L/s");
        viewHolder.tvStartTime.setText("Du "+flowDetail.getStartTime());
        viewHolder.tvEndTime.setText("Au "+flowDetail.getEndTime());
        // Return the completed view to render on screen
        return convertView;
    }
}
