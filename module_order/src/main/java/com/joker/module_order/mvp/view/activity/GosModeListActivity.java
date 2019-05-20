package com.joker.module_order.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.commonres.utils.ToastUtil;
import com.example.commonres.utils.ToolUtils;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerGosModeListComponent;
import com.joker.module_order.di.module.GosModeListModule;
import com.joker.module_order.mvp.contract.GosModeListContract;
import com.joker.module_order.mvp.presenter.GosModeListPresenter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GosModeListActivity
    extends GosConfigModuleBaseActivity<GosModeListPresenter>
    implements GosModeListContract.View {

    /**
     * The lv Mode
     */
    @BindView(R2.id.lvMode)
    ListView lvMode;

    /**
     * The data
     */
    List<String> modeList;

    /**
     * The Adapter
     */
    ModeListAdapter modeListAdapter;

    List<Integer> list = new ArrayList<Integer>();

    private boolean isAirlink = true;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGosModeListComponent
            .builder()
            .appComponent(appComponent)
            .gosModeListModule(new GosModeListModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_gos_mode_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        isAirlink = getIntent().getBooleanExtra("isAirlink", true);
        spf = getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
        String modules = spf.getString("modulestyles", null);
        if (modules != null) {
            try {
                JSONArray array = new JSONArray(modules);
                for (int i = 0; i < array.length(); i++) {
                    int type = (Integer) array.get(i);
                    list.add(type);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
        modeListAdapter = new ModeListAdapter(this, modeList);

        lvMode.setAdapter(modeListAdapter);// 初始化
        lvMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                boolean isRemove = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == arg2) {
                        list.remove(i);
                        isRemove = true;
                    }
                }
                if (!isRemove) {
                    list.add(arg2);
                }
                modeListAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R2.id.back,R2.id.right_image,R2.id.btnOk})
    public void onClick(View v){
        int viewId = v.getId();
        if (viewId == R.id.back)
            killMyself();
        else if (viewId == R.id.right_image) {
            if (ToolUtils.noDoubleClick()) {
                startActivity(new Intent(GosModeListActivity.this, GosChooseModuleHelpActivity.class));
            }
        }
        else if (viewId == R.id.btnOk) {
            if (ToolUtils.noDoubleClick()) {
                if (list.size() == 0) {
                    ToastUtil.makeText(this, getString(R.string.selece_module_type));
                } else {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < list.size(); i++) {
                        array.put(list.get(i));
                    }
                    Log.e("TAG", "onClick: " + array.toString());
                    spf.edit().putString("modulestyles", array.toString()).commit();
                    if (isAirlink) {
                        // config_airlink-false-start
                        Intent intent = new Intent(this, GosAirlinkReadyActivity.class);
                        startActivity(intent);
                        // config_airlink-false-end
                    } else {
                        //config_softap-false-start
                        Intent intent = new Intent(this, GosDeviceReadyActivity.class);
                        startActivity(intent);
                        //config_softap-false-end
                    }
                }
            }
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

    class ModeListAdapter extends BaseAdapter {

        Context context;
        List<String> modeList;

        public ModeListAdapter(Context context, List<String> modeList) {
            super();
            this.context = context;
            this.modeList = modeList;
        }

        @Override
        public int getCount() {
            return modeList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(context, R.layout.item_gos_mode_list, null);
            }

            TextView tvModeText = (TextView) convertView.findViewById(R.id.tvModeText);

            String modeText = modeList.get(position);
            tvModeText.setText(modeText);

            ImageView ivChoosed = (ImageView) convertView.findViewById(R.id.ivChoosed);
            if (list.contains(position)) {
                ivChoosed.setVisibility(View.VISIBLE);
            } else {
                ivChoosed.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}
