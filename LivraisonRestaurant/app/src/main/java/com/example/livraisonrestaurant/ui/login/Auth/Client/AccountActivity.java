package com.example.livraisonrestaurant.ui.login.Auth.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AccountActivity extends Fragment {
    ListView myListView;
    ArrayList<RowItem> myRowItems;
    HashMap<String, Intent> test = new HashMap<String, Intent>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Read xml file and return View object.
        // inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)
        View view = inflater.inflate(R.layout.activity_account, container, false);

        super.onCreate(savedInstanceState);
        myRowItems = new ArrayList<RowItem>();

        myListView = (ListView) view.findViewById(R.id.listview);

        fillArrayList();
        test.put("Paramètres",new Intent(getActivity(),SettingsActivity.class));
        test.put("Livrer avec nous",new Intent(getActivity(), rider.class));
        test.put("Devenir restaurant partenaire ",new Intent(getActivity(),devenirRest.class));
        CustomAdapter myAdapter = new CustomAdapter(getActivity(), myRowItems);
        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                RowItem list_row = myRowItems.get(position);
                if (list_row.getHeading() =="Livrer avec nous"){
                    riderHelper.createRider(FirebaseAuth.getInstance().getUid());
                    userHelper.updateIsRider(FirebaseAuth.getInstance().getUid(),true);
                    startActivity(test.get(list_row.getHeading()));
                    getActivity().finish();
                }


                startActivity(test.get(list_row.getHeading()));

                // Toast.makeText(getApplicationContext(), item,Toast.LENGTH_LONG);
            }

        });

    return view;
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



