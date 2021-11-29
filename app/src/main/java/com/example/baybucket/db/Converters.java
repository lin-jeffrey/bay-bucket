package com.example.baybucket.db;

import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Converters {
    Gson gson = new Gson();
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");

    @TypeConverter
    public static ArrayList<Integer> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public String fromArrayList(ArrayList<Integer> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static Date fromTimeStamp(String value) {
        if (value != null) {
            try {
                TimeZone timeZone = TimeZone.getTimeZone("PST");
                df.setTimeZone(timeZone);
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimeStamp(Date value) {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        df.setTimeZone(timeZone);
        return value == null ? null : df.format(value);
    }

    @TypeConverter
    public static LatLng stringToLatLong(String value){
        String[] latlong =  value.split(",");
        LatLng location = new LatLng(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
        return location;
    }

    @TypeConverter
    public static String latLongToString(LatLng value){
        double lat = value.latitude;
        double lng = value.longitude;
        String latlong = lat + "," + lng;
        return latlong;
    }
}
