package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.CleanHouseModule;
import com.joker.module_personal.mvp.contract.CleanHouseContract;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.CleanHouseFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 14:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = CleanHouseModule.class, dependencies = AppComponent.class)
public interface CleanHouseComponent {
    void inject(CleanHouseFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CleanHouseComponent.Builder view(CleanHouseContract.View view);

        CleanHouseComponent.Builder appComponent(AppComponent appComponent);

        CleanHouseComponent build();
    }
}