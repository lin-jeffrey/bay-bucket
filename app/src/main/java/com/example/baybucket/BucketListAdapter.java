package com.example.baybucket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder>{

    LayoutInflater layoutInflater;
    List<BucketListItems> bucketListItemsList;
    public final static String TAG = "Success";

    public BucketListAdapter(Context context, List<BucketListItems> bucketListItemsList){
        this.layoutInflater = LayoutInflater.from(context);
        this.bucketListItemsList = bucketListItemsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listItemName, distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.tv_activityName);
            distance = itemView.findViewById(R.id.tv_distance);

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
        Log.i(TAG, "position: "+position);
        holder.listItemName.setText(bucketListItemsList.get(position).getBucketListItemName());
        holder.distance.setText(bucketListItemsList.get(position).getDistance());

    }

    @Override
    public int getItemCount() {
        return bucketListItemsList.size();
    }


}
