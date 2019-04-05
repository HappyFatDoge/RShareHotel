package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.AllCouponModule;
import com.joker.module_personal.mvp.contract.AllCouponContract;

import com.jess.arms.di.scope.FragmentScope;
import com.joker.module_personal.mvp.view.fragment.AllCouponFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/05/2019 15:54
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = AllCouponModule.class, dependencies = AppComponent.class)
public interface AllCouponComponent {
    void inject(AllCouponFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AllCouponComponent.Builder view(AllCouponContract.View view);

        AllCouponComponent.Builder appComponent(AppComponent appComponent);

        AllCouponComponent build();
    }
}