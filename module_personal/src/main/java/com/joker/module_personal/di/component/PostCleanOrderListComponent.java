package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.PostCleanOrderListModule;
import com.joker.module_personal.mvp.contract.PostCleanOrderListContract;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.PostCleanOrderListFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 15:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = PostCleanOrderListModule.class, dependencies = AppComponent.class)
public interface PostCleanOrderListComponent {
    void inject(PostCleanOrderListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PostCleanOrderListComponent.Builder view(PostCleanOrderListContract.View view);

        PostCleanOrderListComponent.Builder appComponent(AppComponent appComponent);

        PostCleanOrderListComponent build();
    }
}