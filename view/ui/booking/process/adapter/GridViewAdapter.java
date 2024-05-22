package com.example.asg02.view.ui.booking.process.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asg02.R;
import com.example.asg02.model.Manager;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Manager> {
    public GridViewAdapter(@NonNull Context context, List<Manager> managers) {
        super(context, 0, managers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_cinema_complex, parent, false);
        }

        Manager manager = getItem(position);
        ImageView imageView = listitemView.findViewById(R.id.avatar);
        TextView textView = listitemView.findViewById(R.id.name);

        textView.setText(manager.getName());
//        imageView.setImageBitmap(Utils.decodeBitmap(manager.getAvatar()));
        return listitemView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
