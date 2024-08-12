package com.example.javatest.designPattern.decorator;

public class BlackCoffee implements Coffee {

    @Override
    public String getDescription() {
        return "Black Coffee";
    }

    @Override
    public double cost() {
        return 10;
    }
}
