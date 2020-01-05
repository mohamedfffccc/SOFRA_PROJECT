package com.example.sofra.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.sofra.R;
import com.example.sofra.data.model.generalresponse.RegionsData;

import java.util.ArrayList;
import java.util.List;

public class GeneralResponseAdapter extends BaseAdapter {

    private Context context;
    private List<RegionsData> generalResponseDataList = new ArrayList<>();
    private LayoutInflater inflter;
    public int selectedId = 0;
    private Typeface type;

    public GeneralResponseAdapter(Context applicationContext, List<RegionsData> generalResponseDataList, String hint) {
        this.context = applicationContext;
        this.generalResponseDataList = generalResponseDataList;
        inflter = (LayoutInflater.from(applicationContext));
        generalResponseDataList.add(new RegionsData(0, hint));
    }

    @Override
    public int getCount() {
        return generalResponseDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item, null);
        type = Typeface.createFromAsset(context.getAssets(), "fonts/bigfont5.otf");

        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setTypeface(type);

        names.setText(generalResponseDataList.get(i).getName());
//        selectedId = generalResponseDataList.get(i).getId();

        return view;
    }
}