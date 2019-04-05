package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.AllIntegralRecordModule;
import com.joker.module_personal.mvp.contract.AllIntegralRecordContract;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.AllIntegralRecordFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/05/2019 16:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = AllIntegralRecordModule.class, dependencies = AppComponent.class)
public interface AllIntegralRecordComponent {
    void inject(AllIntegralRecordFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AllIntegralRecordComponent.Builder view(AllIntegralRecordContract.View view);

        AllIntegralRecordComponent.Builder appComponent(AppComponent appComponent);

        AllIntegralRecordComponent build();
    }
}