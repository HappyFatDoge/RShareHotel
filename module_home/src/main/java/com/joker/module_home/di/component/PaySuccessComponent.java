package com.joker.module_home.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_home.di.module.PaySuccessModule;
import com.joker.module_home.mvp.contract.PaySuccessContract;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_home.mvp.view.activity.PaySuccessActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/30/2019 22:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PaySuccessModule.class, dependencies = AppComponent.class)
public interface PaySuccessComponent {
    void inject(PaySuccessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PaySuccessComponent.Builder view(PaySuccessContract.View view);

        PaySuccessComponent.Builder appComponent(AppComponent appComponent);

        PaySuccessComponent build();
    }
}