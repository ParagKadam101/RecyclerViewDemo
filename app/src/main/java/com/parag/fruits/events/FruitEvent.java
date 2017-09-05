package com.parag.fruits.events;

import com.parag.fruits.model.Fruit;

public class FruitEvent {
    public Fruit[] fruits;
    public FruitEvent(Fruit[] fruits)
    {
        this.fruits = fruits;
    }
}
