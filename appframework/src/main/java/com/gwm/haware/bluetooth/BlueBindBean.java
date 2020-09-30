package com.gwm.haware.bluetooth;



import android.os.Parcel;
import android.os.Parcelable;


public class BlueBindBean implements Parcelable {


    public String bluetoothMac;
    public String serverUUID;
    public String characterUUID;
    public BlueBindBean(){}

    protected BlueBindBean(Parcel in) {
        bluetoothMac = in.readString();
        serverUUID = in.readString();
        characterUUID = in.readString();
    }

    public static final Creator<BlueBindBean> CREATOR = new Creator<BlueBindBean>() {
        @Override
        public BlueBindBean createFromParcel(Parcel in) {
            return new BlueBindBean(in);
        }

        @Override
        public BlueBindBean[] newArray(int size) {
            return new BlueBindBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bluetoothMac);
        dest.writeString(serverUUID);
        dest.writeString(characterUUID);
    }
}
