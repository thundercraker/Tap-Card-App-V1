package com.yumashish.tapcardv1;

import android.graphics.drawable.Drawable;

/**
 * Created by lightning on 5/30/15.
 */
public class TapCard {
    private int mId;
    private String mName;
    private String mWork;
    private Drawable mProfilePic;
    private String mLocation;

    public Drawable getProfilePic()
    {
        return mProfilePic;
    }

    public int getId() {
        return mId;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getWork() {
        return mWork;
    }

    public void setWork(String mWork) {
        this.mWork = mWork;
    }



    public TapCard(int id, Drawable drawable, String name, String work, String location)
    {
        mId = id;
        mProfilePic = drawable;
        mName = name;
        mWork = work;
        mLocation = location;
    }
}
