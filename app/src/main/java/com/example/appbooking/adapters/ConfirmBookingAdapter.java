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
import com.example.appbooking.models.BookedTicket;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ConfirmBookingAdapter extends ArrayAdapter<BookedTicket> {

    ArrayList<BookedTicket> bookedTickets;

    public ConfirmBookingAdapter(@NonNull Context context, int resource, ArrayList<BookedTicket> bookedTickets) {
        super(context, resource, bookedTickets);
        this.bookedTickets = bookedTickets;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_buses_layout, parent, false);
        }

        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(Integer.parseInt(bookedTickets.get(position).getPrice()));

        TextView companyTxt = convertView.findViewById(R.id.company_Tv);
        TextView typeTxt = convertView.findViewById(R.id.type_Tv);
        TextView priceTxt = convertView.findViewById(R.id.price_Tv);
        TextView timeTxt = convertView.findViewById(R.id.time_Tv);

        companyTxt.setText(bookedTickets.get(position).getCompany());
        typeTxt.setText(bookedTickets.get(position).getType());
        priceTxt.setText(price);
        timeTxt.setText(bookedTickets.get(position).getTime());

        return convertView;
    }
}
