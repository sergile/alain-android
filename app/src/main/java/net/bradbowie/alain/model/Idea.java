package net.bradbowie.alain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bradbowie on 4/11/16.
 */
public class Idea implements Parcelable {
    public static final Creator<Idea> CREATOR = getCreator();

    private String left;
    private int leftColor;
    private String right;
    private int rightColor;

    public Idea() {
    }

    protected Idea(Parcel in) {
        left = in.readString();
        leftColor = in.readInt();
        right = in.readString();
        rightColor = in.readInt();
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public Idea withLeft(String left) {
        setLeft(left);
        return this;
    }

    public int getLeftColor() {
        return leftColor;
    }

    public void setLeftColor(int leftColor) {
        this.leftColor = leftColor;
    }
    
    public Idea withLeftColor(int leftColor) {
        setLeftColor(leftColor);
        return this;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public Idea withRight(String right) {
        setRight(right);
        return this;
    }

    public int getRightColor() {
        return rightColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    public Idea withRightColor(int rightColor) {
        setRightColor(rightColor);
        return this;
    }

    public String toString() {
        return left + right;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(left);
        dest.writeInt(leftColor);
        dest.writeString(right);
        dest.writeInt(rightColor);
    }

    public static Idea build(String left, int leftColor, String right, int rightColor) {
        return new Idea()
                .withLeft(left)
                .withLeftColor(leftColor)
                .withRight(right)
                .withRightColor(rightColor);
    }

    public static Creator<Idea> getCreator() {
        return new Creator<Idea>() {
            @Override
            public Idea createFromParcel(Parcel in) {
                return new Idea(in);
            }

            @Override
            public Idea[] newArray(int size) {
                return new Idea[size];
            }
        };
    }
}
