package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.ReceiveClearOrderModule;
import com.joker.module_personal.mvp.contract.ReceiveClearOrderContract;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.ReceiveClearOrderActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ReceiveClearOrderModule.class, dependencies = AppComponent.class)
public interface ReceiveClearOrderComponent {
    void inject(ReceiveClearOrderActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ReceiveClearOrderComponent.Builder view(ReceiveClearOrderContract.View view);

        ReceiveClearOrderComponent.Builder appComponent(AppComponent appComponent);

        ReceiveClearOrderComponent build();
    }
}