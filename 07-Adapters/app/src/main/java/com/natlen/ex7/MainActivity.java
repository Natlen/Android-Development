package com.natlen.ex7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import java.util.ArrayList;

public class MainActivity extends ListActivity{

    private CountryAdapter countryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Country> countries = CountryXMLParser.parseCountries(this); // retrieving all contries into one array list.
        countryAdapter = new CountryAdapter(this, countries ); // creating an instance of all countries as an adapter.
        setListAdapter(countryAdapter); // assigning the adapter

        //assigning a on-long-row-press listener
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                countryAdapter.remove(position); // removes the current row by its given pressed position, implemented on its class.
                return true;
            }
        });
    }
}
