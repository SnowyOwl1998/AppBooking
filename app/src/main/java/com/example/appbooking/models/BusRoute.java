package com.example.appbooking.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BusRoute implements Parcelable {
    private int id, price;
    private String from, to, date, time, type, company;

    private int slotAvailable;

    public BusRoute(int id, String from, String to, String date, String time, String type, String company, int price, Integer slotAvailable) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.type = type;
        this.company = company;
        this.price = price;
        this.slotAvailable = slotAvailable;
    }

    protected BusRoute(Parcel in) {
        id = in.readInt();
        from = in.readString();
        to = in.readString();
        date = in.readString();
        time = in.readString();
        type = in.readString();
        company = in.readString();
        price = in.readInt();
        slotAvailable = in.readInt();
    }

    public static final Creator<BusRoute> CREATOR = new Creator<BusRoute>() {
        @Override
        public BusRoute createFromParcel(Parcel in) {
            return new BusRoute(in);
        }

        @Override
        public BusRoute[] newArray(int size) {
            return new BusRoute[size];
        }
    };

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSlotAvailable() {
        return slotAvailable;
    }

    public void setSlotAvailable(int slotAvailable) {
        this.slotAvailable = slotAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeString(company);
        dest.writeInt(price);
        dest.writeInt(slotAvailable);
    }
}
