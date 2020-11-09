package com.rkcodesolution.lab07;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public class CountryListActivity extends ListActivity {
    public static final String MYTAG = "MYTAG";
    private CountryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Country> countries = CountryXMLParser.parseCountries(this);
        mAdapter = new CountryAdapter(this, countries);
        setListAdapter(mAdapter);

        /* Set click listener to remove a country on a long click */
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.remove(position);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        mAdapter.stopAll();      // for orientations changes.
        super.onDestroy();
    }
}
