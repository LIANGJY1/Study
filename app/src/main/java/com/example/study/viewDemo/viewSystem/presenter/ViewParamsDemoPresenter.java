package com.example.study.viewDemo.viewSystem.presenter;

import android.util.Log;
import android.view.View;

import com.example.study.base.BasePresenter;
import com.example.study.util.tool.DensityUtil;

public class ViewParamsDemoPresenter extends BasePresenter {

    /**
     * get view coordinates
     */
    public void getViewCoordinates(View view) {
        int left = view.getLeft();
        int right = view.getRight();
        int top = view.getTop();
        int bottom = view.getBottom();
        Log.i("getViewCoordinates", "left is " + left + "; right is " + DensityUtil.pxToDp(view.getContext(), right) +"; top is " + top +"; bottom is " + bottom);
    }

    /**
     * get view left_and_top vertex coordinates
     */
    public void getLeftAndTopVertexCoordinates(View view) {
        float x = view.getX();
        float y = view.getY();
        Log.i("getLeftAndTopVertexCoordinates", "x is " + x + "; y is " + y);
    }

    /**
     * get view width and height
     */
    public void getViewWidthAndHeight(View view) {
        int left = view.getLeft();
        int right = view.getRight();
        int top = view.getTop();
        int bottom = view.getBottom();
        int width = right - left;
        int height = bottom - top;
        Log.i("getViewWidthAndHeight", "width is " + width + "; height is " + height);
    }

    /**
     * translate view and then get it's left_and_top vertex coordinates
     */
    public void translateViewAndGetLeftAndTopVertexCoordinates(View view){
        view.setTranslationX(10f);
        view.setTranslationX(10f);

        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        Log.i("translation", "translationX is " + translationX + "; translationY is " + translationY);

        float x = view.getX();
        float y = view.getY();
        Log.i("getLeftAndTopVertexCoordinates", "x is " + x + "; y is " + y);
    }
}
