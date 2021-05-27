package com.example.livraisonrestaurant.ui.login;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.api.orderHelper;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.models.orders;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PickupAdapter extends BaseAdapter {
    private ArrayList<RowItem> singleRow;
    private LayoutInflater thisInflater;
    public PickupAdapter(Context applicationContext, ArrayList<RowItem> myRowItems2) {
        this.singleRow = myRowItems2;
        thisInflater = ( LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return singleRow.size();
    }

    @Override
    public Object getItem(int position) {
       return singleRow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.list_view_pickup, parent, false);
            TextView theHeading = (TextView) convertView.findViewById(R.id.textView15);
            TextView theSubHeading = (TextView) convertView.findViewById(R.id.textView24);
            RadioButton rb = (RadioButton) convertView.findViewById(R.id.radioButton);
            RowItem currentRow = (RowItem) getItem(position);


            theHeading.setText(currentRow.getHeading());
            theHeading.setTextSize(35);
            theHeading.setTextColor(Color.BLACK);
            theHeading.setGravity(Gravity.START);
            theSubHeading.setText(currentRow.getSubHeading());
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Commande prise en charge",Toast.LENGTH_LONG).show();
                    orderHelper.getOrdersCollection().whereEqualTo("rider_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).whereEqualTo("status", 1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       queryDocumentSnapshots.getDocuments().get(0).toObject(orders.class);
                       orderHelper.updateStatus(queryDocumentSnapshots.getDocuments().get(0).toObject(orders.class).getUid(),2);
                        }
                    });

                }
            });
        }

        return convertView;
    }

}
