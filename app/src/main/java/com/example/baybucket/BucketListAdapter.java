package com.example.baybucket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder>{

    LayoutInflater layoutInflater;
    List<BucketListItems> bucketListItemsList;

    public final static String TAG = "BucketListAdapter";

    public BucketListAdapter(Context context, List<BucketListItems> bucketListItemsList){
        this.layoutInflater = LayoutInflater.from(context);
        this.bucketListItemsList = bucketListItemsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listItemName, distance;
        public CheckBox cb_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.tv_activityName);
            distance = itemView.findViewById(R.id.tv_distance);
            cb_item = itemView.findViewById(R.id.cb_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bucket_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemName.setText(bucketListItemsList.get(position).getBucketListItemName());
        holder.distance.setText(bucketListItemsList.get(position).getDistance());
        holder.cb_item.setChecked(bucketListItemsList.get(position).getVisited());

    }

    @Override
    public int getItemCount() {
        return bucketListItemsList.size();
    }


}
