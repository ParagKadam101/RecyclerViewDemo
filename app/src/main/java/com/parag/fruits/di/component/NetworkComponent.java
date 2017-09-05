package com.parag.fruits.di.component;


import com.parag.fruits.activities.MainActivity;
import com.parag.fruits.di.module.NetworkModule;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
     void inject(MainActivity mainActivity);
}
