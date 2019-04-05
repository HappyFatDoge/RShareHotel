package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.IntegralRecordModule;
import com.joker.module_personal.mvp.contract.IntegralRecordContract;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.IntegralRecordActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/05/2019 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = IntegralRecordModule.class, dependencies = AppComponent.class)
public interface IntegralRecordComponent {
    void inject(IntegralRecordActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        IntegralRecordComponent.Builder view(IntegralRecordContract.View view);

        IntegralRecordComponent.Builder appComponent(AppComponent appComponent);

        IntegralRecordComponent build();
    }
}