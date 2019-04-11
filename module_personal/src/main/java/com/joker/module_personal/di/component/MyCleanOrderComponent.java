package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.MyCleanOrderModule;
import com.joker.module_personal.mvp.contract.MyCleanOrderContract;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.MyCleanOrderFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = MyCleanOrderModule.class, dependencies = AppComponent.class)
public interface MyCleanOrderComponent {
    void inject(MyCleanOrderFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MyCleanOrderComponent.Builder view(MyCleanOrderContract.View view);

        MyCleanOrderComponent.Builder appComponent(AppComponent appComponent);

        MyCleanOrderComponent build();
    }
}