package com.example.commonres.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonres.R;
import com.example.commonres.beans.LockBean;
import com.example.commonres.dialog.adapter.BluetoothListAdapter;
import com.jess.arms.base.DefaultAdapter;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/3/18.
 */
public class BluetoothLockDialog extends Dialog {

    private TextView lockTips;
    private RecyclerView lockList;
    private TextView bluetoothTips;
    private RecyclerView bluetoothList;
    private Button scan;

    private ScanButtonClickListener mScanButtonClickListener;
    private BluetoothListAdapter lockListAdapter;
    private BluetoothListAdapter bluetoothListAdapter;


    public BluetoothLockDialog(@NonNull Context context) {
        this(context, R.style.public_bluetooth_lock_dialog);
    }

    public BluetoothLockDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BluetoothLockDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_bluetooth_lock_dialog);

        lockTips = findViewById(R.id.lockTips);
        lockList = findViewById(R.id.lockList);
        bluetoothTips = findViewById(R.id.bluetoothTips);
        bluetoothList = findViewById(R.id.bluetoothList);
        scan = findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mScanButtonClickListener != null)
                    mScanButtonClickListener.scan();
            }
        });

        lockListAdapter = new BluetoothListAdapter();
        bluetoothListAdapter = new BluetoothListAdapter();
        lockList.setAdapter(lockListAdapter);
        lockList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        lockList.setItemAnimator(new DefaultItemAnimator());
        lockList.setLayoutManager(new LinearLayoutManager(getContext()));

        bluetoothList.setAdapter(bluetoothListAdapter);
        bluetoothList.setLayoutManager(new LinearLayoutManager(getContext()));
        bluetoothList.setItemAnimator(new DefaultItemAnimator());
        bluetoothList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        setCanceledOnTouchOutside(true);
    }


    public void setScanButtonClickListener(ScanButtonClickListener scanButtonClickListener){
        this.mScanButtonClickListener = scanButtonClickListener;
    }


    public void setLockConnectListener(DefaultAdapter.OnRecyclerViewItemClickListener lockConnectListener){
        lockListAdapter.setOnItemClickListener(lockConnectListener);
    }

    public void setOtherConnectListener(DefaultAdapter.OnRecyclerViewItemClickListener otherConnectListener){
        bluetoothListAdapter.setOnItemClickListener(otherConnectListener);
    }


    /**
     * 添加蓝牙门锁item
     * @param lock
     */
    public void addLock(LockBean lock){
        lockListAdapter.addDevice(lock);
        lockTips.setVisibility(View.GONE);
        lockList.setVisibility(View.VISIBLE);
    }


    /**
     * 没有蓝牙门锁的添加
     */
    public void addNoLock(){
        lockListAdapter.clearDevice();
        lockTips.setVisibility(View.VISIBLE);
        lockList.setVisibility(View.GONE);
    }


    /**
     * 添加其他设备item
     * @param device
     */
    public void addOtherDevice(LockBean device){
        bluetoothListAdapter.addDevice(device);
        bluetoothTips.setVisibility(View.GONE);
        bluetoothList.setVisibility(View.VISIBLE);
    }

    /**
     * 没有其他设备的添加
     */
    public void addNoOtherDevice(){
        bluetoothListAdapter.clearDevice();
        bluetoothTips.setVisibility(View.VISIBLE);
        bluetoothList.setVisibility(View.GONE);
    }

    /**
     * 蓝牙设备数量
     * @return
     */
    public int getDeviceCount(){
        return bluetoothListAdapter.getItemCount();
    }

    /**
     * 蓝牙扫描中，设置按钮不可点击
     */
    public void isScanning(){
        scan.setClickable(false);
        scan.setText("扫描中...");
        addNoOtherDevice();
    }

    /**
     * 结束扫描，恢复按钮点击事件
     */
    public void endScan(){
        scan.setClickable(true);
        scan.setText("扫描设备");
    }

    public interface ScanButtonClickListener{
        void scan();
    }

}
