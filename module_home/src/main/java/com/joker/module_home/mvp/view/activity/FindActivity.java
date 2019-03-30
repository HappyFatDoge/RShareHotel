package com.joker.module_home.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.Hotel;
import com.example.commonres.dialog.ProgresDialog;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_home.R;
import com.joker.module_home.R2;
import com.joker.module_home.di.component.DaggerFindComponent;
import com.joker.module_home.di.module.FindModule;
import com.joker.module_home.mvp.contract.FindContract;
import com.joker.module_home.mvp.presenter.FindPresenter;
import com.joker.module_home.mvp.view.adapter.FindRecyclerViewAdapter;
import com.joker.module_home.mvp.view.adapter.ListDropDownAdapter;
import com.yyydjk.library.DropDownMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 主页 -> 搜索
 */
@Route(path = RouterHub.HOME_FINDACTIVITY)
public class FindActivity extends BaseActivity<FindPresenter> implements FindContract.View {


    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.ddm_find_filter)
    DropDownMenu mDropDownMenu;

    private ProgresDialog progressDialog;
    private List<View> popupViews = new ArrayList<>();
    private String headers[] = {"户型", "价格", "类型"};
    private String initHeaders[] = {"户型", "价格", "类型"};
    private ListDropDownAdapter styleAdapter;
    private ListDropDownAdapter priceAdapter;
    private ListDropDownAdapter modeAdapter;
    private String style[] = {"不限", "1室0厅1卫", "1室1厅1卫", "2室1厅1卫", "3室1厅1卫", "3室1厅2卫", "4室1厅2卫"};
    private String price[] = {"不限", "1000以下", "1000-2000", "2000-3000", "3000以上"};
    private String mode[] = {"不限", "民宿", "酒店公寓"};
    private String code[] = {"All", "House", "HotelApartment"};
    private int codePos = 0;

    private FindRecyclerViewAdapter mFindRecyclerViewAdapter;
    private String checkInDate;
    private String checkOutDate;
    private String searchMode;
    private String city;
    private String search;
    private BmobQuery<Hotel> dateQuery;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private ArrayList<BmobQuery> mQueryList;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFindComponent
            .builder()
            .appComponent(appComponent)
            .findModule(new FindModule(this))
            .build()
            .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_find;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        searchMode = intent.getStringExtra("SearchMode");
        checkInDate = intent.getStringExtra("CheckInDate");
        checkOutDate = intent.getStringExtra("CheckOutDate");
        dateQuery = new BmobQuery<>("Hotel");
        city = intent.getStringExtra("City");
        search = intent.getStringExtra("Search");
        try {
            dateQuery.addWhereLessThanOrEqualTo("startDate",new BmobDate(sdf.parse(checkInDate)));
            dateQuery.addWhereGreaterThanOrEqualTo("endDate",new BmobDate(sdf.parse(checkOutDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        initView();//初始化控件

        //初始化的筛选栏的text显示
        if (searchMode.equals("House")) {
            modeAdapter.setCheckItem(1);
            initHeaders[2] = mode[1];
        } else if (searchMode.equals("HotelApartment")) {
            modeAdapter.setCheckItem(2);
            initHeaders[2] = mode[2];
        }

        //设置下拉筛选
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        mDropDownMenu.setDropDownMenu(Arrays.asList(initHeaders), popupViews, contentView);

        showLoading();
        if (searchMode.equals("House")) {
            codePos = 1;
        } else if (searchMode.equals("HotelApartment")) {
            codePos = 2;
        } else {
            codePos = 0;
        }
    }


    private void initView(){
        mFindRecyclerViewAdapter = new FindRecyclerViewAdapter();
        mFindRecyclerViewAdapter.setOnItemClickListener(new HotelItemClickedListener());
        mRecyclerView.setAdapter(mFindRecyclerViewAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //下拉筛选设置
        //户型
        ListView styleView = new ListView(this);
        styleView.setDividerHeight(0);
        styleAdapter = new ListDropDownAdapter(this,Arrays.asList(style));
        styleView.setAdapter(styleAdapter);

        //价格
        ListView priceView = new ListView(this);
        priceView.setDividerHeight(0);
        priceAdapter = new ListDropDownAdapter(this, Arrays.asList(price));
        priceView.setAdapter(priceAdapter);

        //模式
        ListView modeView = new ListView(this);
        modeView.setDividerHeight(0);
        modeAdapter = new ListDropDownAdapter(this,Arrays.asList(mode));
        modeView.setAdapter(modeAdapter);

        popupViews.add(styleView);
        popupViews.add(priceView);
        popupViews.add(modeView);
        styleView.setOnItemClickListener(new StyleViewItemClickedListener());
        priceView.setOnItemClickListener(new PriceViewItemClickedListener());
        modeView.setOnItemClickListener(new ModeViewItemClickedListener());
    }


    @OnClick({R2.id.back,R2.id.right})
    public void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.back){//返回
            hideLoading();
            finish();
            //提示VM清理缓存
            System.gc();
        }else if (viewId == R.id.right)//筛选
            Utils.navigation(this,RouterHub.HOME_FILTERHOTELACTIVITY);

    }

    /**
     * 获取hotel列表结果
     * @param result
     * @param hotelList
     * @param tips
     */
    @Override
    public void searchResult(Boolean result, List<Hotel> hotelList, String tips) {
        if (result)
            mFindRecyclerViewAdapter.setItems(hotelList);
        else{
            mFindRecyclerViewAdapter.setItems(new ArrayList<>());
            ToastUtil.makeText(this,tips);
        }
        hideLoading();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    /**
     * hotel item点击事件监听
     */
    class HotelItemClickedListener implements DefaultAdapter.OnRecyclerViewItemClickListener<Hotel>{

        @Override
        public void onItemClick(View view, int viewType, Hotel data, int position) {
            Log.i(TAG, "选择：" + data.getName());
            ARouter.getInstance()
                .build(RouterHub.HOME_HOTELSELECTEDINFOACTIVITY)
                .withSerializable("HotelBundle",data)
                .withString("CheckInDate", checkInDate)
                .withString("CheckOutDate", checkOutDate)
                .navigation(getViewContext());
        }
    }


    /**
     * hotel 户型item点击事件监听
     */
    class StyleViewItemClickedListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            styleAdapter.setCheckItem(position);
            mDropDownMenu.setTabText(position == 0 ? headers[0] : style[position]);
            BmobQuery<Hotel> styleQuery = new BmobQuery<>("Hotel");
            if (position == 0) {
                styleQuery.addWhereNotEqualTo("houseType", "0室0厅0卫");
            } else {
                styleQuery.addWhereEqualTo("houseType", style[position]);
            }
            mQueryList = null;
            mQueryList = new ArrayList<>();
            mQueryList.add(styleQuery);
            mQueryList.add(dateQuery);
            mDropDownMenu.closeMenu();
            showLoading();
            mPresenter.searchHotel(city, search, code[codePos], mQueryList);
        }
    }


    /**
     * hotel 价格item点击事件监听
     */
    class PriceViewItemClickedListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            priceAdapter.setCheckItem(position);
            mDropDownMenu.setTabText(position == 0 ? headers[1] : price[position]);
            BmobQuery<Hotel> priceQuery = new BmobQuery<>("Hotel");
            BmobQuery<Hotel> priceQuery2 = new BmobQuery<>("Hotel");
            mQueryList = null;
            mQueryList = new ArrayList<>();
            mQueryList.add(dateQuery);
            if (position == 0) {
                priceQuery.addWhereNotEqualTo("price", 0);
            } else if (position == 1) {
                priceQuery.addWhereLessThan("price", 1000);
                mQueryList.add(priceQuery);
            } else if (position == 2) {
                priceQuery.addWhereLessThan("price", 2000);
                priceQuery2.addWhereGreaterThanOrEqualTo("price", 1000);
                mQueryList.add(priceQuery);
                mQueryList.add(priceQuery2);
            } else if (position == 3) {
                priceQuery.addWhereLessThan("price", 3000);
                priceQuery2.addWhereGreaterThanOrEqualTo("price", 2000);
                mQueryList.add(priceQuery);
                mQueryList.add(priceQuery2);
            } else if (position == 4) {
                priceQuery.addWhereGreaterThanOrEqualTo("price", 3000);
                mQueryList.add(priceQuery);
            }
            mDropDownMenu.closeMenu();
            showLoading();
            mPresenter.searchHotel(city, search, code[codePos], mQueryList);
        }
    }


    /**
     * hotel 类型item点击事件监听
     */
    class ModeViewItemClickedListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mQueryList = null;
            mQueryList = new ArrayList<>();
            mQueryList.add(dateQuery);
            codePos = position;
            modeAdapter.setCheckItem(position);
            mDropDownMenu.setTabText(position == 0 ? headers[2] : mode[position]);
            mDropDownMenu.closeMenu();
            showLoading();
            mPresenter.searchHotel(city, search, code[codePos], mQueryList);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mDropDownMenu.isShowing())
            mDropDownMenu.closeMenu();
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mQueryList = null;
        mQueryList = new ArrayList<>();
        mQueryList.add(dateQuery);
        mPresenter.searchHotel(city, search, searchMode, mQueryList);
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        progressDialog = new ProgresDialog(this);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
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
