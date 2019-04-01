package com.joker.module_order.mvp.view.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.commonres.beans.LockBean;
import com.example.commonres.dialog.BluetoothLockDialog;

/**
 * @author ZCB 2575490085@qq.com
 * Created on 2019/4/1.
 * BroadcastReceiver蓝牙广播监听器
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private BluetoothLockDialog bluetoothLockDialog;

    public BluetoothBroadcastReceiver(BluetoothLockDialog bluetoothLockDialog){
        this.bluetoothLockDialog = bluetoothLockDialog;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //进行蓝牙设备扫描
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                bluetoothLockDialog.addOtherDevice(new LockBean(device.getName(),device.getAddress()));
            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //扫描完成
            if (bluetoothLockDialog.getDeviceCount() == 0)
                bluetoothLockDialog.addNoOtherDevice();
            bluetoothLockDialog.endScan();
        }
    }
}
