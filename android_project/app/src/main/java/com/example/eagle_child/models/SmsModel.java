package com.example.eagle_child.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class SmsModel implements Parcelable {
    private String _id;
    private String _address;
    private String _msg;


    public SmsModel(Parcel in) {
        _id = in.readString();
        _address = in.readString();
        _msg = in.readString();
    }

    public static final Creator<SmsModel> CREATOR = new Creator<SmsModel>() {
        @Override
        public SmsModel createFromParcel(Parcel in) {
            return new SmsModel(in);
        }

        @Override
        public SmsModel[] newArray(int size) {
            return new SmsModel[size];
        }
    };

    public SmsModel() {

    }

    public String getId(){
        return _id;
    }
    public String getAddress(){
        return _address;
    }
    public String getMsg(){
        return _msg;
    }

    public void setId(String id){
        _id = id;
    }
    public void setAddress(String address){
        _address = address;
    }
    public void setMsg(String msg){
        _msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsModel smsModel = (SmsModel) o;
        return _address.equals(smsModel._address) && _msg.equals(smsModel._msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_address, _msg);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_address);
        dest.writeString(_msg);
    }
}