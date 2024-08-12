package com.example.javatest.designPattern.decorator;

public class MilkCoffee extends CoffeeDecorator{
    public MilkCoffee(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + milk";
    }

    @Override
    public double cost() {
        return coffee.cost() + 2;
    }
}
