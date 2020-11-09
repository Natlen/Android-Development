package com.natlen.ex8;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.natlen.ex8.MainActivity.MYTAG;
import static com.natlen.ex8.SettingsFrag.METHOD;
import static com.natlen.ex8.SettingsFrag.SAVE_KEY;

public class CountryListFrag extends ListFragment {
    private CountryAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean filterRemovedCountries =  PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(SAVE_KEY, false);
        Set<String> removedCountries = null;
        if (filterRemovedCountries) {
            removedCountries = getRemovedCountriesFromFile();
        }
        final ArrayList<Country> countries = CountryXMLParser.parseCountries(getContext(), removedCountries);
        mAdapter = new CountryAdapter(getActivity(), countries, removedCountries);
        setListAdapter(mAdapter);

        /* Set click listener to remove a country on a long click */
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.remove(position);
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        /* Save removed countries on destroy. Filtering will be by setting on app start */
        saveRemovedCountries();
        super.onDestroy();
    }


    public enum ESavingMethod {
        SAVE_INNER_FILE,       // Method 1
        SAVE_WITH_SP           // Method 3
    }
    private static final String COUNTRY_SET = "countriesSet";
    final String FILENAME = "ex8settings.txt";

    /* Save removed countries by given method */
    public Set<String> getRemovedCountriesFromFile() {
        boolean useSpMethod =  PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(METHOD, false);
        ESavingMethod savingMethod = useSpMethod ?
                ESavingMethod.SAVE_WITH_SP :
                ESavingMethod.SAVE_INNER_FILE;

        Set<String> removedCountries = new LinkedHashSet<>();
        try {
            switch (savingMethod) {
                case SAVE_INNER_FILE:
                    Log.i(MYTAG, "Reading removed countries from inner file. (Method 1).");
                    FileInputStream fin = getContext().openFileInput(FILENAME);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                    String country;
                    while ((country = reader.readLine()) != null) {
                        removedCountries.add(country);
                    }
                    break;

                case SAVE_WITH_SP:
                    Log.i(MYTAG, "Reading removed countries from SP. (Method 3).");
                    Context context = getContext();
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    removedCountries = sharedPref.getStringSet(COUNTRY_SET, removedCountries);
                    break;
            }   // switch

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(MYTAG, e.getMessage());
        } finally {
            return removedCountries;
        }
    }

    /**
     * Save removed countries with BOTH methods to avoid sync problems.
     */
    public void saveRemovedCountries() {
        try {       /* SAVE WITH METHOD 1 - INNER FILE */
            final String separator = System.getProperty("line.separator");
            FileOutputStream fos = getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            for (String country : mAdapter.getRemovedCountries()) {
                writer.write(country + separator);
            }
            writer.close();
            fos.close();
        } catch (Exception e) {
            Log.e(MYTAG, e.getMessage());
        }

        try {   /* SAVE WITH METHOD 3 - SP */
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putStringSet(COUNTRY_SET, mAdapter.getRemovedCountries());
            editor.apply();
        } catch (Exception e) {
            Log.e(MYTAG, e.getMessage());
        }
    }   // saveRemovedCountries


}   // CountryListFragment
