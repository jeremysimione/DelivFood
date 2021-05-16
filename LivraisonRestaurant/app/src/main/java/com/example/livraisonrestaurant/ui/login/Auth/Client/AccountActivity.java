package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.livraisonrestaurant.R;
import com.example.livraisonrestaurant.ui.login.Auth.rider;
import com.example.livraisonrestaurant.ui.login.CustomAdapter;
import com.example.livraisonrestaurant.ui.login.RowItem;
import com.example.livraisonrestaurant.ui.login.api.riderHelper;
import com.example.livraisonrestaurant.ui.login.api.userHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {
    ListView myListView;
    ArrayList<RowItem> myRowItems;
    HashMap<String, Intent> test = new HashMap<String, Intent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        myRowItems = new ArrayList<RowItem>();

        myListView = (ListView) findViewById(R.id.listview);

        fillArrayList();
        test.put("Paramètres",new Intent(this,SettingsActivity.class));
        test.put("Livrer avec nous",new Intent(this, rider.class));
        CustomAdapter myAdapter = new CustomAdapter(getApplicationContext(), myRowItems);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                RowItem list_row = myRowItems.get(position);
                if (list_row.getHeading() =="Livrer avec nous"){
                    riderHelper.createRider(FirebaseAuth.getInstance().getUid());
                    userHelper.updateIsRider(FirebaseAuth.getInstance().getUid(),true);
                }

                startActivity(test.get(list_row.getHeading()));
                finish();
                // Toast.makeText(getApplicationContext(), item,Toast.LENGTH_LONG);
            }

        });


    }


    private void fillArrayList() {

        RowItem row_one = new RowItem();
        row_one.setHeading("Vos favoris");
        row_one.setSubHeading("");
        row_one.setSmallImageName(R.drawable.ic_fi_sr_receipt);

        myRowItems.add(row_one);

        RowItem row_two = new RowItem();
        row_two.setHeading("Programme de fidélité restaurant");
        row_two.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_two);


        RowItem row_three = new RowItem();
        row_three.setHeading("Paiement");
        row_three.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_three);


        RowItem row_four = new RowItem();
        row_four.setHeading("Aide");
        row_four.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_four);


        RowItem row_five = new RowItem();
        row_five.setHeading("Promotions");
        row_five.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_five);


        RowItem row_six = new RowItem();
        row_six.setHeading("Livrer avec nous");
        row_six.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_six);


        RowItem row_8 = new RowItem();
        row_8.setHeading("Devenir restaurant partenaire ");
        row_8.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_8);

        RowItem row_seven = new RowItem();
        row_seven.setHeading("Paramètres");
        row_seven.setSmallImageName(R.drawable.ic_fi_sr_user);
        myRowItems.add(row_seven);


    }

}



