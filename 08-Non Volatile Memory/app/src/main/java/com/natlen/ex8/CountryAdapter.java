package com.natlen.ex8;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.natlen.ex8.MainActivity.MYTAG;

public class CountryAdapter extends ArrayAdapter {

    private final Activity mContext;
    private final ArrayList<Country> mCountries;
    private final Set<String> mRemovedCountries;  // represents the removed countries.

    static class CountryView { // as a data type structure.
        public TextView CountryName;
        public TextView CountryDescription;
        public ImageView CountryImage;

    }

    public CountryAdapter(Activity context, ArrayList<Country> countries, Set<String> removedCountries) {
        super(context, R.layout.row, countries);
        mContext = context;
        mCountries = countries;
        if (removedCountries != null) {
            mRemovedCountries = removedCountries; // no need to delete because xml parser ignored.
        }
        else {
            mRemovedCountries = new LinkedHashSet<>(); // initializing a new set
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // Inflate and create view only if doesn't exist.
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row, null);

            // configure view holder
            CountryView countryView = new CountryView();
            countryView.CountryName = rowView.findViewById(R.id.countryName);
            countryView.CountryImage = rowView.findViewById(R.id.flag);
            countryView.CountryDescription = rowView.findViewById(R.id.description);
            rowView.setTag(countryView);
        }

        /* Get Country */
        final Country country = mCountries.get(position);
        final CountryView holder = (CountryView) rowView.getTag();

        /* Country Name */
        holder.CountryName.setText(country.name);

        /* Country Flag */
        int flagId = getContext().getResources().getIdentifier(country.flag, "drawable", getContext().getPackageName());
        holder.CountryImage.setImageResource(flagId);

        /* Country Description */
        holder.CountryDescription.setText(country.getShorty());

        return rowView;
    }
    public void remove(int position) {
        try {
            String removedCountry = mCountries.remove(position).name;
            notifyDataSetChanged();
            mRemovedCountries.add(removedCountry);
        }
        catch (Exception e) {
            Log.e(MYTAG, e.getMessage());
        }
    }

    public Set<String> getRemovedCountries() {
        return mRemovedCountries;
    }

}
