package com.joker.module_order.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_order.di.module.OrderDetailModule;
import com.joker.module_order.mvp.contract.OrderDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_order.mvp.view.activity.OrderDetailActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 16:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = OrderDetailModule.class, dependencies = AppComponent.class)
public interface OrderDetailComponent {
    void inject(OrderDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OrderDetailComponent.Builder view(OrderDetailContract.View view);

        OrderDetailComponent.Builder appComponent(AppComponent appComponent);

        OrderDetailComponent build();
    }
}