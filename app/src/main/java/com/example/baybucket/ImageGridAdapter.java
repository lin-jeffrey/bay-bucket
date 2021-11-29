package com.example.baybucket;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Uri> uriList;

    public ImageGridAdapter(Context context, ArrayList<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    public int getCount() {
        return this.uriList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(this.uriList.get(position));
        return imageView;
    }
}
