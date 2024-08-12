package com.example.javatest.designPattern.decorator;

public class TestDemo {
    public static void main(String[] args) {
        Coffee blackCoffee = new BlackCoffee();
        System.out.println(blackCoffee.getDescription() + " : " + blackCoffee.cost() + "￥");
        Coffee milkCoffee = new MilkCoffee(blackCoffee);
        System.out.println(milkCoffee.getDescription() + " : " + milkCoffee.cost() + "￥");
        Coffee sugarCoffee = new SugarCoffee(milkCoffee);
        System.out.println(sugarCoffee.getDescription() + " : " + sugarCoffee.cost() + "￥");

    }
}
