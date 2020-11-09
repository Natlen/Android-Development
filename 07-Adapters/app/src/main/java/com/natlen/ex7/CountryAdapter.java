package com.natlen.ex7;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<Country> countries;

    static class CountryView { // as a data type structure.
        public TextView CountryName;
        public TextView CountryDescription;
        public ImageView CountryImage;

    }

    public CountryAdapter(Activity context, ArrayList<Country> countries) { //constructor
        super(context, R.layout.row, countries);
        this.context = context;
        this.countries = countries;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView; // defining the view representing a country.

        // Inflate and create view only if doesn't exist.
        if (convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.row, null);


            // initializing the CountryView struct.
            CountryView countryView = new CountryView();
            countryView.CountryName = row.findViewById(R.id.countryName);
            countryView.CountryDescription = row.findViewById(R.id.description);
            countryView.CountryImage = row.findViewById(R.id.flag);
            row.setTag(countryView); // sets the row tag to be the view.
        }

        // retrieve a country  - either firstly created above, or already exists.
        Country country = countries.get(position);
        CountryView countryView = (CountryView) row.getTag();

        //Initializes the country struct if country is already exists
        countryView.CountryName.setText(country.name);
        countryView.CountryDescription.setText(country.getShorty());

        // retrieve the proper image using the set1imageResource method.
        countryView.CountryImage.setImageResource(getContext().getResources().getIdentifier(country.flag, "drawable", getContext().getPackageName()));

        return row; // row view is finally initialized and ready to be presented.
    }
    public void remove(int position)
    {
        try{
            countries.remove(position);
            notifyDataSetChanged(); // notifies the observer that data has changed inflicting a refresh in view as well.
        }
        catch(Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
