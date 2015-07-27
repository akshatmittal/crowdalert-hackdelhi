package com.roalts.hackdelhiclient;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Displays basic information about beacon.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class BeaconListAdapter extends BaseAdapter {

    private ArrayList<Beacon> beacons;
    private LayoutInflater inflater;

    public BeaconListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.beacons = new ArrayList<>();
    }

    public void replaceWith(Collection<Beacon> newBeacons) {
        this.beacons.clear();
        this.beacons.addAll(newBeacons);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Beacon getItem(int position) {
        return beacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflateIfRequired(view, position, parent);
        bind(getItem(position), view);
        return view;
    }

    private void bind(Beacon beacon, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();

            if(Integer.valueOf(App.MINOR) == beacon.getMinor() && Integer.valueOf(App.MAJOR) == beacon.getMajor()) {
                Log.d("Value : ", "" + Utils.computeAccuracy(beacon));
                holder.macTextView.setText(String.format("NAME: %s (%.2fm)", App.NAME, Utils.computeAccuracy(beacon)));
                holder.majorTextView.setText("Major: " + beacon.getMajor());
                holder.minorTextView.setText("Minor: " + beacon.getMinor());
//                holder.measuredPowerTextView.setText("MPower: " + beacon.getMeasuredPower());
//                holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
            } else if(Integer.valueOf(App.FRIEND_MINOR) == beacon.getMinor() && Integer.valueOf(App.FRIEND_MAJOR) == beacon.getMajor()){
                holder.macTextView.setText(String.format("NAME: %s (%.2fm)", App.FRIEND_NAME, Utils.computeAccuracy(beacon)));
                holder.majorTextView.setText("Major: " + beacon.getMajor());
                holder.minorTextView.setText("Minor: " + beacon.getMinor());
//                holder.measuredPowerTextView.setText("MPower: " + beacon.getMeasuredPower());
//                holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
            } else {
                Log.d("Value : ", "" + Utils.computeAccuracy(beacon));
                holder.macTextView.setText(String.format("MAC: %s (%.2fm)", beacon.getMacAddress(), Utils.computeAccuracy(beacon)));
                holder.majorTextView.setText("Major: " + beacon.getMajor());
                holder.minorTextView.setText("Minor: " + beacon.getMinor());
//                holder.measuredPowerTextView.setText("MPower: " + beacon.getMeasuredPower());
//                holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
            }
    }

    private View inflateIfRequired(View view, int position, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.beacon_item, null);
            view.setTag(new ViewHolder(view));
        }
        return view;
    }

    static class ViewHolder {
        final TextView macTextView;
        final TextView majorTextView;
        final TextView minorTextView;
//        final TextView measuredPowerTextView;
//        final TextView rssiTextView;

        ViewHolder(View view) {
            macTextView = (TextView) view.findViewWithTag("mac");
            majorTextView = (TextView) view.findViewWithTag("major");
            minorTextView = (TextView) view.findViewWithTag("minor");
//            measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
//            rssiTextView = (TextView) view.findViewWithTag("rssi");
        }
    }
}
