package com.joker.module_order.mvp.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonres.beans.LockBean;
import com.example.commonres.beans.Order;
import com.example.commonres.dialog.BluetoothLockDialog;
import com.example.commonres.utils.LoginUtil;
import com.example.commonres.utils.ToastUtil;
import com.example.commonsdk.core.RouterHub;
import com.example.commonsdk.utils.Utils;
import com.example.commonservice.BluetoothChatService;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.joker.module_order.R;
import com.joker.module_order.R2;
import com.joker.module_order.di.component.DaggerHousingComponent;
import com.joker.module_order.di.module.HousingModule;
import com.joker.module_order.mvp.contract.HousingContract;
import com.joker.module_order.mvp.presenter.HousingPresenter;
import com.joker.module_order.mvp.view.adapter.HousingListAdapter;
import com.joker.module_order.mvp.view.receiver.BluetoothBroadcastReceiver;
import com.joker.module_personal.mvp.view.activity.FaceVerificationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 订单Fragment -> 待入住订单Fragment
 */
public class HousingFragment extends BaseFragment<HousingPresenter>
        implements HousingContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView housingList;

    private HousingListAdapter housingListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothChatService bluetoothChatService;
    private String mConnectedDeviceName;
    private String lockAddress;
    private BluetoothLockDialog bluetoothLockDialog;
    private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;

    private static final Integer STATE_HOUSING = 2;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private Order mOrder;

    public static HousingFragment newInstance() {
        HousingFragment fragment = new HousingFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHousingComponent
            .builder()
            .appComponent(appComponent)
            .housingModule(new HousingModule(this))
            .build()
            .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_housing, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        housingListAdapter = new HousingListAdapter();
        housingList.setLayoutManager(new LinearLayoutManager(getContext()));
        housingList.setItemAnimator(new DefaultItemAnimator());
        housingList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        housingList.setAdapter(housingListAdapter);

        housingListAdapter.setCheckInListener(new CheckInListener());
        housingListAdapter.setMoreOperationListener(new MoreOperationListener());

        mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver(bluetoothLockDialog);

        //蓝牙适配
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothChatService = new BluetoothChatService(getContext(),handler);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(mBluetoothBroadcastReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getContext().registerReceiver(mBluetoothBroadcastReceiver, filter);
    }


    /**
     * 获取待入住订单结果
     * @param result
     * @param tips
     * @param list
     */
    @Override
    public void getCheckInOrdersResult(Boolean result, String tips, List<Order> list) {
        Log.d("HousingFragment", tips);
        if (result)
            housingListAdapter.setItems(list);
        else
            housingListAdapter.setItems(new ArrayList<>());
    }


    /**
     * 蓝牙发送数据开锁结果
     * @param result
     * @param tips
     */
    @Override
    public void sendMessageResult(Boolean result, String tips) {
        ToastUtil.makeText(getContext(), tips);
    }


    /**
     * 入住事件监听器,开锁入住，通过蓝牙进行人脸识别打开门锁
     */
    class CheckInListener implements HousingListAdapter.CheckInListener{
        @Override
        public void checkIn(View view, int position) {
            mOrder = housingListAdapter.getItem(position);
            // 若进行过人脸注册，则进行人脸验证打开门锁，否则先进行人脸注册
            if (!LoginUtil.getInstance().getUser().getFaceRegister()){
                ToastUtil.makeText(getContext(), "您尚未进行人脸注册，请先进行人脸注册");
                Utils.navigation(getContext(), RouterHub.PERSONAL_FACEREGISTERACTIVITY);
                return;
            }

            if(mBluetoothAdapter == null){
                ToastUtil.makeText(getContext(), "您的设备不支持蓝牙");
                return;
            }

            bluetoothLockDialog = new BluetoothLockDialog(getContext());
            bluetoothLockDialog.show();
            bluetoothLockDialog.setLockConnectListener(itemClickListener);
            bluetoothLockDialog.setOtherConnectListener(itemClickListener);

            //请求启动蓝牙
            requestBluetoothPermission();

            bluetoothLockDialog.setScanButtonClickListener(new BluetoothLockDialog.ScanButtonClickListener() {
                @Override
                public void scan() {
                    if (mBluetoothAdapter.isDiscovering())
                        mBluetoothAdapter.cancelDiscovery();
                    //开始扫描
                    mBluetoothAdapter.startDiscovery();
                    bluetoothLockDialog.isScanning();
                }
            });
        }
    }

    /**
     * 更多操作事件监听器
     */
    class MoreOperationListener implements HousingListAdapter.MoreOperationListener{
        @Override
        public void operate(View view, int position) {
            mOrder = housingListAdapter.getItem(position);
            //更多操作
            ARouter.getInstance()
                .build(RouterHub.ORDER_MOREOPERATIONACTIVITY)
                .withSerializable("order",mOrder)
                .navigation(getContext());
        }
    }

    private DefaultAdapter.OnRecyclerViewItemClickListener itemClickListener = new DefaultAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int viewType, Object data, int position) {
            mBluetoothAdapter.cancelDiscovery();
            //进行蓝牙连接
            LockBean info = (LockBean) data;
//            Toast.makeText(getContext(),info.getLockName(),Toast.LENGTH_SHORT).show();
            lockAddress = info.getAddress();
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(lockAddress);
            bluetoothChatService.connect(device);
        }
    };





    /**
     * Handler处理
     */
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_STATE_CHANGE://蓝牙连接状态提示
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Toast.makeText(getActivity(), "已连接："
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            //停止蓝牙扫描
                            mBluetoothAdapter.cancelDiscovery();
                            bluetoothLockDialog.dismiss();

                            ToastUtil.makeText(getContext(), "进行人脸验证");
                            //进行人脸验证
                            Intent intent = new Intent(getContext(), FaceVerificationActivity.class);
                            intent.putExtra("userId",mOrder.getUser().getAccount());
                            intent.putExtra("order",mOrder);
                            intent.putExtra("lockAddress",lockAddress);
                            intent.putExtra("checkOut",false);
                            startActivityForResult(intent,1);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            ToastUtil.makeText(getContext(),"连接中...");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_DEVICE_NAME://开始蓝牙连接提示
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    ToastUtil.makeText(getContext(), "开始连接："
                        + mConnectedDeviceName);
                    break;
                case MESSAGE_TOAST://连接提示
                    ToastUtil.makeText(getContext(),msg.getData().getString(TOAST));
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (LoginUtil.getInstance().isLogin())
            mPresenter.getCheckInOrders(STATE_HOUSING,LoginUtil.getInstance().getUser().getAccount());
    }



    /**
     * 请求开启蓝牙,扫描并添加已经配对的蓝牙设备
     */
    private void requestBluetoothPermission(){
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else
        if (bluetoothChatService == null) setupChat();
        //扫描并添加已经配对的蓝牙设备
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0)
            for (BluetoothDevice device : pairedDevices)
                bluetoothLockDialog.addLock(new LockBean(device.getName(),device.getAddress()));
        else
            bluetoothLockDialog.addNoLock();

    }

    /**
     * 创建蓝牙服务
     */
    private void setupChat() {
        bluetoothChatService = new BluetoothChatService(getContext(), handler);
        if (bluetoothChatService != null)
            if (bluetoothChatService.getState() == BluetoothChatService.STATE_NONE)
                bluetoothChatService.start();
        //扫描并添加已经配对的蓝牙设备
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0)
            for (BluetoothDevice device : pairedDevices)
                bluetoothLockDialog.addLock(new LockBean(device.getName(),device.getAddress()));
        else
            bluetoothLockDialog.addNoLock();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1://开门解锁的人脸验证结果
                if (data.getBooleanExtra("verification", false))
                    //打开门锁
                    mPresenter.sendMessage("a", bluetoothChatService);
                break;
        }

        switch (requestCode){
            case REQUEST_ENABLE_BT://启动蓝牙结果
                if (resultCode == getActivity().RESULT_OK)
                    setupChat();
                else{//未启动蓝牙，关闭dialog
                    ToastUtil.makeText(getContext(),"蓝牙未启用");
                    bluetoothLockDialog.dismiss();
                }
        }
    }

    @Override
    public void onDestroy() {
        //停止蓝牙服务
        if (bluetoothChatService != null)
            bluetoothChatService.stop();
        getActivity().unregisterReceiver(mBluetoothBroadcastReceiver);
        super.onDestroy();
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
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {
    }
}
