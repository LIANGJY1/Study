package com.example.javatest.designPattern.decorator;

public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        if (coffee != null) {
            return coffee.getDescription();
        }
        return "";
    }

    public double cost() {
        if (coffee != null) {
            return coffee.cost();
        }
        return 0;
    }
}
