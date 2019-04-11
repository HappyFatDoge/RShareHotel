package com.joker.module_personal.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.joker.module_personal.di.module.FaceVerificationModule;
import com.joker.module_personal.mvp.contract.FaceVerificationContract;

import com.jess.arms.di.scope.ActivityScope;
import com.joker.module_personal.mvp.view.activity.FaceVerificationActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/11/2019 16:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = FaceVerificationModule.class, dependencies = AppComponent.class)
public interface FaceVerificationComponent {
    void inject(FaceVerificationActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FaceVerificationComponent.Builder view(FaceVerificationContract.View view);

        FaceVerificationComponent.Builder appComponent(AppComponent appComponent);

        FaceVerificationComponent build();
    }
}