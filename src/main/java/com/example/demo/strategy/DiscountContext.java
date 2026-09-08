package com.example.demo.strategy;

public class DiscountContext {
    private DiscountStrategy strategy;

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double executeDiscount(double price) {
        if (strategy == null) {
            return price;
        }
        return strategy.applyDiscount(price);
    }
}