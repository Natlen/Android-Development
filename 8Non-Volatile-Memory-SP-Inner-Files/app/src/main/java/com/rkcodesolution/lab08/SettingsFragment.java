package com.rkcodesolution.lab08;


import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String SAVE_KEY = "save";
    public static final String METHOD = "savingMethod";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey); // Load the Preferences from the XML file
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /* Switch text for switches*/

        SwitchPreferenceCompat switchPref;
        switchPref = (SwitchPreferenceCompat) getPreferenceManager().findPreference(SAVE_KEY);
        switchPref.setSummaryOff(R.string.countries_unsaved);
        switchPref.setSummaryOn(R.string.countries_saved);

        switchPref = (SwitchPreferenceCompat)  getPreferenceManager().findPreference(METHOD);
        switchPref.setSummaryOff(R.string.method_inner_file);
        switchPref.setSummaryOn(R.string.method_sp);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


}