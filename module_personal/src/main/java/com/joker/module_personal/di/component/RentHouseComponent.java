package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.RentHouseModule;
import com.joker.module_personal.mvp.contract.RentHouseContract;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.RentHouseFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 14:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = RentHouseModule.class, dependencies = AppComponent.class)
public interface RentHouseComponent {
    void inject(RentHouseFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RentHouseComponent.Builder view(RentHouseContract.View view);

        RentHouseComponent.Builder appComponent(AppComponent appComponent);

        RentHouseComponent build();
    }
}