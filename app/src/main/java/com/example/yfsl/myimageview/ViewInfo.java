package com.example.yfsl.myimageview;

import android.os.Parcel;
import android.os.Parcelable;

public class ViewInfo implements Parcelable {
    private float width;
    private float height;
    private float left;
    private float top;
    private int viewId;

    protected ViewInfo(Parcel in) {
        width = in.readFloat();
        height = in.readFloat();
        left = in.readFloat();
        top = in.readFloat();
        viewId = in.readInt();
    }

    public static final Creator<ViewInfo> CREATOR = new Creator<ViewInfo>() {
        @Override
        public ViewInfo createFromParcel(Parcel in) {
            return new ViewInfo(in);
        }

        @Override
        public ViewInfo[] newArray(int size) {
            return new ViewInfo[size];
        }
    };

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public ViewInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(width);
        dest.writeFloat(height);
        dest.writeFloat(left);
        dest.writeFloat(top);
        dest.writeInt(viewId);
    }
}
