package com.example.appbooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appbooking.R;
import com.example.appbooking.models.BusRoute;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListBusesAdapter extends ArrayAdapter<BusRoute> {

    private ArrayList<BusRoute> busRoutes;

    public ListBusesAdapter(@NonNull Context context, int resource, ArrayList<BusRoute> busRoutes) {
        super(context, resource, busRoutes);
        this.busRoutes = busRoutes;
    }

    public BusRoute getItem(int postition){
        return busRoutes.get(postition);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_buses_layout, parent, false);
        }

        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(busRoutes.get(position).getPrice());

        TextView companyTxt = convertView.findViewById(R.id.company_Tv);
        TextView typeTxt = convertView.findViewById(R.id.type_Tv);
        TextView priceTxt = convertView.findViewById(R.id.price_Tv);
        TextView timeTxt = convertView.findViewById(R.id.time_Tv);

        companyTxt.setText(busRoutes.get(position).getCompany());
        typeTxt.setText(busRoutes.get(position).getType());
        priceTxt.setText(price);
        timeTxt.setText(busRoutes.get(position).getTime());


        return convertView;
    }
}
