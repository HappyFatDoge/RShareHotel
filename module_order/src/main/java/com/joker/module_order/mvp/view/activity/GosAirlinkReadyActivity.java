package com.joker.module_order.mvp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.commonres.utils.AssetsUtils;
import com.example.commonres.utils.ToolUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosAirlinkReadyComponent;
import com.joker.module_order.di.module.GosAirlinkReadyModule;
import com.joker.module_order.mvp.contract.GosAirlinkReadyContract;
import com.joker.module_order.mvp.presenter.GosAirlinkReadyPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosAirlinkReadyActivity
    extends GosConfigModuleBaseActivity<GosAirlinkReadyPresenter>
    implements GosAirlinkReadyContract.View {

    /**
     * The tv Ready
     */
    @BindView(R2.id.tvReady)
    TextView tvReady;

    private int sum = 0;

    private List<String> modeList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosAirlinkReadyComponent
            .builder()
            .appComponent(appComponent)
            .gosAirlinkReadyModule(new GosAirlinkReadyModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_airlink_ready;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
        initView();
    }

    private void initView(){
        SpannableString spannableString = new SpannableString(getString(R.string.common_ready_message));
        if (AssetsUtils.isZh(this)) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), 28, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvReady.setText(spannableString);
    }

    @OnClick({R2.id.btnNext,R2.id.back})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back)
            killMyself();
        else if (viewId == R.id.btnNext)
            if (ToolUtils.noDoubleClick()) {
                sum = 0;
                Intent intent = new Intent(this, GosAirlinkConfigCountdownActivity.class);
                startActivity(intent);
            }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
