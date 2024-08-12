package com.example.javatest.designPattern.decorator;

public class SugarCoffee extends CoffeeDecorator{
    public SugarCoffee(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + sugar";
    }

    @Override
    public double cost() {
        return super.cost() + 1;
    }
}
