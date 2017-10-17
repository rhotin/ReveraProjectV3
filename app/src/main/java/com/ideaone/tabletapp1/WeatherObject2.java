package com.ideaone.tabletapp1;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class WeatherObject2 implements Parcelable {

    double latitude;
    double longitude;
    String location;
    int m1Time;
    String m1SummaryC;
    String m1IconC;
    String m1Summary;
    String m1Icon;
    double m1Temperature;
    double m1TemperatureMin;
    double m1TemperatureMax;
    double m1TemperatureNight;
    double m1TemperatureEve;
    double m1TemperatureMorn;
    int m2Time;
    String m2Summary;
    String m2Icon;
    double m2Temperature;
    double m2TemperatureMin;
    double m2TemperatureMax;
    double m2TemperatureNight;
    double m2TemperatureEve;
    double m2TemperatureMorn;
    int m3Time;
    String m3Summary;
    String m3Icon;
    double m3Temperature;
    double m3TemperatureMin;
    double m3TemperatureMax;
    double m3TemperatureNight;
    double m3TemperatureEve;
    double m3TemperatureMorn;
    int m4Time;
    String m4Summary;
    String m4Icon;
    double m4Temperature;
    double m4TemperatureMin;
    double m4TemperatureMax;
    double m4TemperatureNight;
    double m4TemperatureEve;
    double m4TemperatureMorn;

    WeatherObject2(double weather_latitude, double weather_longitude, String weather_location,
                   int weather_1_time, String weather_1_summary_C, String weather_1_icon_C, String weather_1_summary, String weather_1_icon, double weather_1_temperature,
                   double weather_1_temperatureMin, double weather_1_temperatureMax, double weather_1_temperatureNight,
                   double weather_1_temperatureEve, double weather_1_temperatureMorn,
                   int weather_2_time, String weather_2_summary, String weather_2_icon, double weather_2_temperature,
                   double weather_2_temperatureMin, double weather_2_temperatureMax, double weather_2_temperatureNight,
                   double weather_2_temperatureEve, double weather_2_temperatureMorn,
                   int weather_3_time, String weather_3_summary, String weather_3_icon, double weather_3_temperature,
                   double weather_3_temperatureMin, double weather_3_temperatureMax, double weather_3_temperatureNight,
                   double weather_3_temperatureEve, double weather_3_temperatureMorn,
                   int weather_4_time, String weather_4_summary, String weather_4_icon, double weather_4_temperature,
                   double weather_4_temperatureMin, double weather_4_temperatureMax, double weather_4_temperatureNight,
                   double weather_4_temperatureEve, double weather_4_temperatureMorn) {

        this.latitude = weather_latitude;
        this.longitude = weather_longitude;
        this.location = weather_location;

        this.m1Time = weather_1_time;
        this.m1SummaryC = weather_1_summary_C;
        this.m1IconC = weather_1_icon_C;
        this.m1Summary = weather_1_summary;
        this.m1Icon = weather_1_icon;
        this.m1Temperature = weather_1_temperature;
        this.m1TemperatureMin = weather_1_temperatureMin;
        this.m1TemperatureMax = weather_1_temperatureMax;
        this.m1TemperatureNight = weather_1_temperatureNight;
        this.m1TemperatureEve = weather_1_temperatureEve;
        this.m1TemperatureMorn = weather_1_temperatureMorn;

        this.m2Time = weather_2_time;
        this.m2Summary = weather_2_summary;
        this.m2Icon = weather_2_icon;
        this.m2Temperature = weather_2_temperature;
        this.m2TemperatureMin = weather_2_temperatureMin;
        this.m2TemperatureMax = weather_2_temperatureMax;
        this.m2TemperatureNight = weather_2_temperatureNight;
        this.m2TemperatureEve = weather_2_temperatureEve;
        this.m2TemperatureMorn = weather_2_temperatureMorn;

        this.m3Time = weather_3_time;
        this.m3Summary = weather_3_summary;
        this.m3Icon = weather_3_icon;
        this.m3Temperature = weather_3_temperature;
        this.m3TemperatureMin = weather_3_temperatureMin;
        this.m3TemperatureMax = weather_3_temperatureMax;
        this.m3TemperatureNight = weather_3_temperatureNight;
        this.m3TemperatureEve = weather_3_temperatureEve;
        this.m3TemperatureMorn = weather_3_temperatureMorn;

        this.m4Time = weather_4_time;
        this.m4Summary = weather_4_summary;
        this.m4Icon = weather_4_icon;
        this.m4Temperature = weather_4_temperature;
        this.m4TemperatureMin = weather_4_temperatureMin;
        this.m4TemperatureMax = weather_4_temperatureMax;
        this.m4TemperatureNight = weather_4_temperatureNight;
        this.m4TemperatureEve = weather_4_temperatureEve;
        this.m4TemperatureMorn = weather_4_temperatureMorn;

        Log.e("WEATHER", "1 " + this.m1Icon + "-2 " + this.m2Icon + "-3 " + this.m3Icon);
    }

    protected WeatherObject2(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        location = in.readString();
        m1Time = in.readInt();
        m1SummaryC = in.readString();
        m1IconC = in.readString();
        m1Summary = in.readString();
        m1Icon = in.readString();
        m1Temperature = in.readDouble();
        m1TemperatureMin = in.readDouble();
        m1TemperatureMax = in.readDouble();
        m1TemperatureNight = in.readDouble();
        m1TemperatureEve = in.readDouble();
        m1TemperatureMorn = in.readDouble();
        m2Time = in.readInt();
        m2Summary = in.readString();
        m2Icon = in.readString();
        m2Temperature = in.readDouble();
        m2TemperatureMin = in.readDouble();
        m2TemperatureMax = in.readDouble();
        m2TemperatureNight = in.readDouble();
        m2TemperatureEve = in.readDouble();
        m2TemperatureMorn = in.readDouble();
        m3Time = in.readInt();
        m3Summary = in.readString();
        m3Icon = in.readString();
        m3Temperature = in.readDouble();
        m3TemperatureMin = in.readDouble();
        m3TemperatureMax = in.readDouble();
        m3TemperatureNight = in.readDouble();
        m3TemperatureEve = in.readDouble();
        m3TemperatureMorn = in.readDouble();
        m4Time = in.readInt();
        m4Summary = in.readString();
        m4Icon = in.readString();
        m4Temperature = in.readDouble();
        m4TemperatureMin = in.readDouble();
        m4TemperatureMax = in.readDouble();
        m4TemperatureNight = in.readDouble();
        m4TemperatureEve = in.readDouble();
        m4TemperatureMorn = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(location);
        dest.writeInt(m1Time);
        dest.writeString(m1SummaryC);
        dest.writeString(m1IconC);
        dest.writeString(m1Summary);
        dest.writeString(m1Icon);
        dest.writeDouble(m1Temperature);
        dest.writeDouble(m1TemperatureMin);
        dest.writeDouble(m1TemperatureMax);
        dest.writeDouble(m1TemperatureNight);
        dest.writeDouble(m1TemperatureEve);
        dest.writeDouble(m1TemperatureMorn);
        dest.writeInt(m2Time);
        dest.writeString(m2Summary);
        dest.writeString(m2Icon);
        dest.writeDouble(m2Temperature);
        dest.writeDouble(m2TemperatureMin);
        dest.writeDouble(m2TemperatureMax);
        dest.writeDouble(m2TemperatureNight);
        dest.writeDouble(m2TemperatureEve);
        dest.writeDouble(m2TemperatureMorn);
        dest.writeInt(m3Time);
        dest.writeString(m3Summary);
        dest.writeString(m3Icon);
        dest.writeDouble(m3Temperature);
        dest.writeDouble(m3TemperatureMin);
        dest.writeDouble(m3TemperatureMax);
        dest.writeDouble(m3TemperatureNight);
        dest.writeDouble(m3TemperatureEve);
        dest.writeDouble(m3TemperatureMorn);
        dest.writeInt(m4Time);
        dest.writeString(m4Summary);
        dest.writeString(m4Icon);
        dest.writeDouble(m4Temperature);
        dest.writeDouble(m4TemperatureMin);
        dest.writeDouble(m4TemperatureMax);
        dest.writeDouble(m4TemperatureNight);
        dest.writeDouble(m4TemperatureEve);
        dest.writeDouble(m4TemperatureMorn);
    }

    @SuppressWarnings("unused")
    public static final Creator<WeatherObject2> CREATOR = new Creator<WeatherObject2>() {
        @Override
        public WeatherObject2 createFromParcel(Parcel in) {
            return new WeatherObject2(in);
        }

        @Override
        public WeatherObject2[] newArray(int size) {
            return new WeatherObject2[size];
        }
    };
}