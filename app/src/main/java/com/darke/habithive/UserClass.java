package com.darke.habithive;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import java.util.ArrayList;

public class UserClass implements Parcelable {
    private Timestamp createdAt;
    private String userName;
    private String userEmail;

    // Required empty constructor
    public UserClass() {}

    // Constructor with all fields
    public UserClass(String userName,String userEmail ,Timestamp createdAt) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }

    // Parcelable implementation
    protected UserClass(Parcel in) {
        userName = in.readString();
        userEmail = in.readString();
        createdAt = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeParcelable(createdAt, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<UserClass> CREATOR = new Parcelable.Creator<UserClass>() {
        @Override
        public UserClass createFromParcel(Parcel in) {
            return new UserClass(in);
        }

        @Override
        public UserClass[] newArray(int size) {
            return new UserClass[size];
        }
    };

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}