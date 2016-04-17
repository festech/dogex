package com.samsung.rd.dogex.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.samsung.rd.dogex.R;
import com.samsung.rd.dogex.adapters.ExpandableAdapter;
import com.samsung.rd.dogex.model.DogeDataProvider;
import com.samsung.rd.dogex.model.EmptyDogeDataProvider;
import com.samsung.rd.dogex.model.RandomDogeDataProvider;

import java.io.IOException;
import java.io.InputStream;


public class DogeListFragment extends Fragment {
    private static final String TAG = DogeListFragment.class.getSimpleName();

    private static final String JSON_DATA = "data.json";

    private ExpandableListView dogeListView;
    private ExpandableAdapter dogesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doge_list_fragment, container, false);
        dogeListView = (ExpandableListView)rootView.findViewById(R.id.product_list);
        dogesAdapter = new ExpandableAdapter(createDataProvider(), inflater, getActivity(), dogeListView);
        dogeListView.setAdapter(dogesAdapter);
        return rootView;
    }

    /**
     * Factory method which attempts to create JSON data provider.
     * If JSON data asset is not accessible, fallback to the empty data provider.
     * @return best reachable data provider
     */
    private DogeDataProvider createDataProvider() {
        DogeDataProvider dataProvider;
        AssetManager assetManager = getActivity().getAssets();
        try {
            InputStream jsonInputStream = assetManager.open(JSON_DATA);
            dataProvider = new RandomDogeDataProvider(jsonInputStream);
        } catch (IOException e) {
            dataProvider = new EmptyDogeDataProvider();
            e.printStackTrace();
        }
        return dataProvider;
    }


}