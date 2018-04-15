package com.example.lk.splitter;

/**
 * Created by leeketee on 1/22/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

/**
 * Provides UI for the view with List.
 */
public class PaxFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static PaxFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PaxFragment fragment = new PaxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pax_fragment, container, false);
        NumberPicker np = view.findViewById(R.id.paxNumberPicker);
        np.setValue(4);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
        return view;
    }
}