package com.example.livraisonrestaurant.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private ArrayList<RowItem> singleRow;
    private LayoutInflater thisInflater;

    public CustomAdapter(Context context, ArrayList<RowItem> aRow) {

        this.singleRow = aRow;
        thisInflater = ( LayoutInflater.from(context) );

    }
    @Override
    public int getCount() {
        return singleRow.size( );
    }

    @Override
    public Object getItem(int position) {
        return singleRow.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate( R.layout.list_view_row, parent, false );
            TextView theHeading = (TextView) convertView.findViewById(R.id.textHeading);
            TextView theSubHeading = (TextView) convertView.findViewById(R.id.textSubHeading);
            ImageView theImage = (ImageView) convertView.findViewById(R.id.imageView2);

            RowItem currentRow = (RowItem) getItem(position);

            theHeading.setText(currentRow.getHeading() );
            theSubHeading.setText(currentRow.getSubHeading());
            theImage.setImageResource(currentRow.getSmallImageName());
        }

        return convertView;
    }
}
