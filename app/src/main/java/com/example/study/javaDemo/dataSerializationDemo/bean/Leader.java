package com.example.study.javaDemo.dataSerializationDemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Leader implements Parcelable {
    private String name;
    private int age;

    public Leader(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected Leader(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<Leader> CREATOR = new Creator<Leader>() {
        @Override
        public Leader createFromParcel(Parcel in) {
            return new Leader(in);
        }

        @Override
        public Leader[] newArray(int size) {
            return new Leader[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns the content description of the current object, typically 0,
     * and only if a file descriptor is present in the current object, this method returns 1.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }
}
