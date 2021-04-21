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

import java.util.ArrayList;
import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    HashMap<String, Intent> test = new HashMap<String,Intent>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

            final ListView listview = (ListView) findViewById(R.id.listview);
            String[] values = new String[] { "Vos favoris", "Paiement", "Programme de fidélité restaurant",
                    "Préférences professionelles", "Aide", "Promotions", "Deliver with Delivfood", "Paramètres"};

            test.put("Paramètres",new Intent(this,SettingsActivity.class));

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
                list.add(values[i]);
            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final String item = (String) parent.getItemAtPosition(position);
                    System.out.println("iciiiiiiiiiiiiiiiiiiii" + item);
                    startActivity(test.get(item));

                   // Toast.makeText(getApplicationContext(), item,Toast.LENGTH_LONG);
                }

            });
        }

        private class StableArrayAdapter extends ArrayAdapter<String> {

            HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

            public StableArrayAdapter(AccountActivity context, int textViewResourceId,
                                      ArrayList<String> objects) {
                super(context, textViewResourceId, objects);
                for (int i = 0; i < objects.size(); ++i) {
                    mIdMap.put(objects.get(i), i);
                }
            }

            @Override
            public long getItemId(int position) {
                String item = getItem(position);
                return mIdMap.get(item);
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

        }

    }
