package com.joker.module_message.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.joker.module_message.mvp.contract.MessageContract;
import com.joker.module_message.mvp.model.MessageModel;


@Module
public class MessageModule {
    private MessageContract.View view;

    /**
     * 构建MessageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MessageModule(MessageContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    MessageContract.View provideMessageView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    MessageContract.Model provideMessageModel(MessageModel model) {
        return model;
    }
}