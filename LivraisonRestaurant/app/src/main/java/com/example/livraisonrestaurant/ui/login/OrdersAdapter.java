package com.example.livraisonrestaurant.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livraisonrestaurant.R;

import java.util.ArrayList;

public class OrdersAdapter extends BaseAdapter {
        private ArrayList<RowItem> singleRow;
        private LayoutInflater thisInflater;

        public OrdersAdapter(Context context, ArrayList<RowItem> aRow) {

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
                convertView = thisInflater.inflate( R.layout.row_orders, parent, false );
                TextView theHeading = (TextView) convertView.findViewById(R.id.textView89);
                TextView theSubHeading = (TextView) convertView.findViewById(R.id.textView12);
                TextView theFooter = (TextView) convertView.findViewById(R.id.textView13);
                Button theButton = (Button) convertView.findViewById(R.id.button5);

                RowItem currentRow = (RowItem) getItem(position);


                theHeading.setText(currentRow.getHeading());
                theHeading.setTextSize(25);
                theHeading.setTextColor(Color.BLACK);
                theHeading.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                theHeading.setGravity(Gravity.CENTER | Gravity.LEFT);
                theSubHeading.setText(currentRow.getSubHeading());
                theSubHeading.setGravity(Gravity.CENTER | Gravity.LEFT);
                theFooter.setGravity(Gravity.CENTER | Gravity.LEFT);
                theFooter.setText(currentRow.getTheFooter());
                theButton.setText("Voir l'établissement");
            }

            return convertView;
        }
    }

