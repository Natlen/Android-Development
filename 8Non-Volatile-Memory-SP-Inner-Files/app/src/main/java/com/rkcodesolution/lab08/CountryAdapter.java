package com.rkcodesolution.lab08;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.rkcodesolution.lab08.MainActivity.MYTAG;

public class CountryAdapter extends ArrayAdapter<Country> {
    private final Activity mContext;
    private final ArrayList<Country> mCountries;
    private final Set<String> mRemovedCountries;  // represents the removed countries.
    private final HashMap<String, Anthem> mPlayers;      // anthem players

    /**
     * inner class to hold properties of a row.
     */
    static class ViewHolder {
        public TextView CountryName;
        public ImageView CountryImage;
        public TextView CountryDescription;
        public ImageView PlayerButton;
    }

    /**
     * Inner class to hold anthem and play button image.
     */
    private class Anthem {
        MediaPlayer Player;
        ImageView PlayButton;
        public Anthem(MediaPlayer player, ImageView playButton) {
            Player = player;
            PlayButton = playButton;
        }
    }

    public CountryAdapter(Activity context, ArrayList<Country> countries, Set<String> removedCountries) {
        super(context, R.layout.raw_item, countries);
        mContext = context;
        mCountries = countries;
        mPlayers = new HashMap<>();
        if (removedCountries != null) {
            mRemovedCountries = removedCountries; // no need to delete because xml parser ignored.
        }
        else {
            mRemovedCountries = new LinkedHashSet<>();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // Inflate and create view only if doesn't exist.
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.raw_item, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.CountryName = rowView.findViewById(R.id.countryName);
            viewHolder.CountryImage = rowView.findViewById(R.id.flag);
            viewHolder.CountryDescription = rowView.findViewById(R.id.description);
            viewHolder.PlayerButton = rowView.findViewById(R.id.anthem);
            rowView.setTag(viewHolder);
        }

        /* Get Country */
        final Country country = mCountries.get(position);
        final ViewHolder holder = (ViewHolder) rowView.getTag();

        /* Country Name */
        holder.CountryName.setText(country.name);

        /* Country Flag */
        int flagId = getContext().getResources().getIdentifier(country.flag, "drawable", getContext().getPackageName());
        holder.CountryImage.setImageResource(flagId);

        /* Country Description */
        holder.CountryDescription.setText(country.getShorty());

        /* Country Anthem */
        int anthem = getContext().getResources().getIdentifier(country.anthem, "raw", getContext().getPackageName());
        final MediaPlayer mp = MediaPlayer.create(getContext(), anthem);
        holder.PlayerButton.setImageResource(R.drawable.play_button);     // initial icon.
        holder.PlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    try {
                        mp.prepare();       // needed to replay
                    }
                    catch (IOException e){
                        // don't care
                    }
                    holder.PlayerButton.setImageResource(R.drawable.play_button);
                } else {
                    stopAll();      // before starting to play a new anthem.
                    mp.start();
                    holder.PlayerButton.setImageResource(R.drawable.stop_button);
                }
            }
        });
        mPlayers.put(country.name, new Anthem(mp, holder.PlayerButton));
        return rowView;
    }

    /**
     * Stop all anthems from playing.
     */
    public void stopAll() {
        for (Anthem anthem : mPlayers.values()) {
            if (anthem.Player.isPlaying()) {
                anthem.Player.stop();
                try {
                    anthem.Player.prepare();     // needed to replay
                }
                catch (IOException e){
                    // don't care
                }
                anthem.PlayButton.setImageResource(R.drawable.play_button);
            }
        }
    }

    /**
     * Remove a country in specific position.
     * Add removed country name to removed countries list.
     * @param position
     */
    public void remove(int position) {
        try {
            String removedCountry = mCountries.remove(position).name;
            notifyDataSetChanged();
            mPlayers.get(removedCountry).Player.stop();
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
