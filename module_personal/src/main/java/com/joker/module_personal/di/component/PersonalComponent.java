package com.joker.module_personal.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.PersonalModule;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.PersonalFragment;

@FragmentScope
@Component(modules = PersonalModule.class, dependencies = AppComponent.class)
public interface PersonalComponent {
    void inject(PersonalFragment fragment);
}