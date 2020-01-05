package com.example.sofra.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.data.model.offers.OffersData;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstants.NAME;
import static com.example.sofra.data.local.room.RoomManger.getInistance;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrderFragment extends BaseFragment {

    public OffersData offerdata;
    @BindView(R.id.addorder_iv)
    ImageView addorderIv;
    @BindView(R.id.addorder_tvordername)
    TextView addorderTvordername;
    @BindView(R.id.addorder_tvordedesc)
    TextView addorderTvordedesc;
    @BindView(R.id.addorder_tvprice)
    TextView addorderTvprice;
    @BindView(R.id.addorder_lin)
    LinearLayout addorderLin;
    @BindView(R.id.addorder_etspecialorder)
    EditText addorderEtspecialorder;
    @BindView(R.id.addorder_tvcount)
    TextView addorderTvcount;
    @BindView(R.id.addorder_ivadd)
    ImageView addorderIvadd;
    @BindView(R.id.addorder_ivremove)
    ImageView addorderIvremove;
    int i = 1;
    @BindView(R.id.addorder_btnaddtostore)
    CircularImageView addorderBtnaddtostore;
    private RoomDao roomDao;
    OrderItem item;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_order, container, false);
        ButterKnife.bind(this, root);
        setUpActivity();
        setData();
        setSharedPreferences(getActivity());
        roomDao = getInistance(getActivity()).roomDao();


        return root;
    }

    void setData() {
        Glide.with(getActivity()).load(offerdata.getPhotoUrl()).into(addorderIv);
        addorderTvordername.setText(offerdata.getName());
        addorderTvordedesc.setText(offerdata.getDescription());
        addorderTvprice.setText(offerdata.getOfferPrice());
        if (offerdata.getHasOffer()==true) {
            addorderTvprice.setText(offerdata.getOfferPrice());


        }
        else
        {
            addorderTvprice.setText(offerdata.getPrice());
        }
    }

    @OnClick({R.id.addorder_ivadd, R.id.addorder_ivremove, R.id.addorder_btnaddtostore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addorder_ivadd:
                i++;
                addorderTvcount.setText(i + "");
//                Executors.newSingleThreadExecutor().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        item.setQuantity(i);
//
//
//                        roomDao.update(item);
//                    }
//
//                });
                break;
            case R.id.addorder_ivremove:
                while (i > 1) {
                    i--;
                    addorderTvcount.setText(i + "");
//                    Executors.newSingleThreadExecutor().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            item.setQuantity(i);
//
//
//                            roomDao.update(item);}
//
//                    });
                }
                break;
            case R.id.addorder_btnaddtostore:
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                         item = new OrderItem(offerdata.getId(), Integer.parseInt(offerdata.getRestaurantId()),
                                LoadData(getActivity(), NAME), offerdata.getPhotoUrl(),
                                Double.parseDouble(addorderTvprice.getText().toString()), addorderEtspecialorder.getText().toString()
                                , Integer.parseInt(addorderTvcount.getText().toString()) , offerdata.getName());
                        roomDao.addItem(item);




                    }

                });
                Toast.makeText(getActivity(), "item" + offerdata.getId() + "added to store", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onback() {
        super.onback();
//        getActivity().finish();
    }
}
