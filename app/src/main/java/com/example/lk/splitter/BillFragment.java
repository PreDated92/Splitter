package com.example.lk.splitter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by leeketee on 1/22/2018.
 */

class DataMember {
    Double ItemCost; // Recorded item cost (input)
    Double ActualCost; // Calculated item cost (output)
    Boolean IsSelected; // Is it selected?
    Integer SplitCount; // What is the current split claim count?
    Integer SplitTotal; // Total number of people this item is split by
    Boolean IsClaimed; // Is the current item claimed?
}

public class BillFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private String mLastItemCost;
    private List<DataMember> mDataMemberList = new ArrayList<>();
    private MySimpleArrayAdapter mDataMemberAdapter;

    private ListView mListView1;
    private TextView mTotalHighlightedTextView;
    private TextView mPercentageTextView;
    private EditText mEtCost;
    private EditText mEtTotalCost;

    public static BillFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        BillFragment fragment = new BillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_fragment, container, false);

        mEtCost = view.findViewById(R.id.addItemCost);
        mEtTotalCost = view.findViewById(R.id.totalCost);
        mTotalHighlightedTextView = view.findViewById(R.id.totalHighlighted);
        mPercentageTextView = view.findViewById(R.id.percentage);
        mListView1 = view.findViewById(R.id.listview);

        mEtTotalCost.setOnFocusChangeListener(CreateTotalCostFocusChangeHandler());

        Button addItemBtn = view.findViewById(R.id.addItemCostButton);
        addItemBtn.setOnClickListener(CreateAddCostClickHandler());

        Button repeatItemBtn = view.findViewById(R.id.repeatLastItem);
        repeatItemBtn.setOnClickListener(CreateRepeatItemClickHandler());

        Button clearSelectBtn = view.findViewById(R.id.clearSelection);
        clearSelectBtn.setOnClickListener(CreateClearSelectionClickHandler());

        Button deleteSelectionBtn = view.findViewById(R.id.deleteSelection);
        deleteSelectionBtn.setOnClickListener(CreateDeleteSelectionClickHandler());

        mDataMemberAdapter = new MySimpleArrayAdapter(getActivity(), mDataMemberList);
        mListView1.setAdapter(mDataMemberAdapter);
        mListView1.setOnItemClickListener(CreateListViewClickHandler());
        registerForContextMenu(mListView1);

        return view;
    }

    private View.OnClickListener CreateAddCostClickHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LkUtil.isEmpty(mEtCost)) {
                    mEtCost.setError("Cannot be empty");
                    return;
                }
                AddCostRow();
            }
        };
    }

    private View.OnClickListener CreateRepeatItemClickHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LkUtil.isEmpty(mLastItemCost)) {
                    mEtCost.setError("No previous item to repeat");
                    return;
                }

                mEtCost.setText(mLastItemCost);
                AddCostRow();
            }
        };
    }

    private AdapterView.OnItemClickListener CreateListViewClickHandler() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataMember dm = mDataMemberList.get(position);
                dm.IsSelected = !dm.IsSelected;
                mListView1.invalidateViews(); // Refresh the view
                CalculateTotalHighlighted();
            }
        };
    }

    private View.OnClickListener CreateClearSelectionClickHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (DataMember dm : mDataMemberList) {
                    dm.IsSelected = false;
                }
                mListView1.invalidateViews(); // Refresh the view
                CalculateTotalHighlighted();
            }
        };
    }

    private View.OnClickListener CreateDeleteSelectionClickHandler() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoDeleteSelection();
            }
        };
    }

    private void DoDeleteSelection(){
        Iterator<DataMember> iter = mDataMemberList.iterator();
        while (iter.hasNext()) {
            DataMember dm = iter.next();
            if (dm.IsSelected) {
                iter.remove();
            }
        }
        RecalculateAllValues();
        CalculateTotalHighlighted();
    }

    private View.OnFocusChangeListener CreateTotalCostFocusChangeHandler() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    RecalculateAllValues();
                }
            }
        };
    }

    private void CalculateTotalHighlighted() {
        double totalHighlighted = 0;

        for (DataMember dm : mDataMemberList) {
            if (dm.IsSelected) {
                totalHighlighted += dm.ActualCost;
            }
        }

        mTotalHighlightedTextView.setText(Double.toString(Math.round(totalHighlighted * 100.0) / 100.0));
    }

    private void RecalculateAllValues() {
        if (LkUtil.isEmpty(mEtTotalCost)) {
            mEtTotalCost.setText("0");
        }

        // Get factor
        double total = 0;
        double totalCost = Double.parseDouble(mEtTotalCost.getText().toString());

        for (DataMember dm : mDataMemberList) {
            total += dm.ItemCost;
        }

        double factor;
        if (total == 0) {
            factor = 0;
        } else {
            factor = totalCost / total;
        }
        String factorText = Double.toString(Math.round((factor - 1) * 10000.0) / 100.0);
        mPercentageTextView.setText(factorText);

        //Iterate each cost and calculate the corresponding actual cost based on the factor calculated earlier
        for (int i = 0; i < mDataMemberList.size(); i++) {
            double cost = mDataMemberList.get(i).ItemCost;
            mDataMemberList.get(i).ActualCost = Math.round(cost * factor * 100.0) / 100.0;
        }
        mDataMemberAdapter.notifyDataSetChanged();
    }

    private void AddCostRow() {
        if (LkUtil.isEmpty(mEtTotalCost)) {
            mEtTotalCost.setText("0");
        }

        // Remember the last item cost and clear
        mLastItemCost = mEtCost.getText().toString();
        mEtCost.setText("");

        // Create data structure
        DataMember dm = new DataMember();
        dm.ItemCost = Double.parseDouble(mLastItemCost);
        dm.ActualCost = 0.0;
        dm.IsSelected = false;
        dm.SplitCount = 1;
        dm.SplitTotal = 1;
        dm.IsClaimed = false;
        mDataMemberList.add(dm);

        RecalculateAllValues();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ListView lv = (ListView) v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        DataMember selectedDm = (DataMember) lv.getItemAtPosition(acmi.position);

        // Make current position selected
        selectedDm.IsSelected = true;
        mListView1.invalidateViews();

        // How many selections are made?
        int count = 0;
        for (DataMember dm : mDataMemberList) {
            if (dm.IsSelected) {
                count++;
            }
        }

        if (count == 1) {
            menu.setHeaderTitle("Selected Item Action");
            menu.add(0, v.getId(), 0, "Edit");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Split");
        } else {
            menu.setHeaderTitle("Selected Items Action");
            menu.add(0, v.getId(), 0, "Merge");
        }


        menu.add(0, v.getId(), 0, "Delete");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String action = item.getTitle().toString();
        switch (action)
        {
            case "Delete":
                DoDeleteSelection();
                break;
        }

        if (item.getTitle() == "Edit") {
            Toast.makeText(getActivity(), "calling code", Toast.LENGTH_LONG).show();
        } else if (item.getTitle() == "Split...") {
            Toast.makeText(getActivity(), "sending sms code", Toast.LENGTH_LONG).show();
        } else {
            return false;
        }
        return true;
    }
}

class MySimpleArrayAdapter extends ArrayAdapter<DataMember> {
    private final Context context;
    private final List<DataMember> values;

    public MySimpleArrayAdapter(Context context, List<DataMember> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView1 = rowView.findViewById(R.id.label1);
        TextView textView2 = rowView.findViewById(R.id.label2);
        textView1.setText(values.get(position).ItemCost.toString());
        textView2.setText(values.get(position).ActualCost.toString());

        DataMember dm = values.get(position);
        if (dm.IsSelected) {
            rowView.setBackgroundResource(R.drawable.pressed_color);
        } else {
            rowView.setBackgroundResource(R.drawable.normal_color);
        }

        return rowView;
    }
}
