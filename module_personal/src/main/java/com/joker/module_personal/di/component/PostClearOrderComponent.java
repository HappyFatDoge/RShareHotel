package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.PostClearOrderModule;
import com.joker.module_personal.mvp.contract.PostClearOrderContract;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.PostClearOrderActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 13:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PostClearOrderModule.class, dependencies = AppComponent.class)
public interface PostClearOrderComponent {
    void inject(PostClearOrderActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PostClearOrderComponent.Builder view(PostClearOrderContract.View view);

        PostClearOrderComponent.Builder appComponent(AppComponent appComponent);

        PostClearOrderComponent build();
    }
}