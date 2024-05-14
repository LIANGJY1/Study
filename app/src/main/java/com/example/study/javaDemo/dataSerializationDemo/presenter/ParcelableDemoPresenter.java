package com.example.study.javaDemo.dataSerializationDemo.presenter;

import com.example.study.base.BasePresenter;
import com.example.study.javaDemo.dataSerializationDemo.bean.Leader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ParcelableDemoPresenter extends BasePresenter {
    /**
     * save object using externalizable api
     */
    public void saveObject(Object object, String externalFilesDirString) {
        if (object == null) {
            return;
        }

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(externalFilesDirString + "/leader.out"));
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Leader readObject(String externalFilesDirString) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(externalFilesDirString + "/leader.out"));
            return (Leader) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
