package com.example.testscan

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    internal var sm: ScanDevice? = null
    private var barcodeStr: String? = null
    private var showScanResult: EditText? = null


    private val mScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // TODO Auto-generated method stub

            val barocode = intent.getByteArrayExtra("barocode")
            val barocodelen = intent.getIntExtra("length", 0)
            val temp = intent.getByteExtra("barcodeType", 0.toByte())
            android.util.Log.i("debug", "----codetype--$temp")
            barcodeStr = String(barocode, 0, barocodelen)
            showScanResult!!.append("�㲥�����")
            showScanResult!!.append(barcodeStr)
            showScanResult!!.append("\n")
            //       showScanResult.setText(barcodeStr);
            sm!!.stopScan()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sm = ScanDevice()
        val ch = findViewById<View>(R.id.checkBox1) as CheckBox

        if (sm!!.outScanMode == 1) {
            ch.isChecked = true
        } else {
            ch.isChecked = false
        }
        ch.setOnCheckedChangeListener { arg0, arg1 ->
            // TODO Auto-generated method stub
            if (arg1) {
                sm!!.outScanMode = 1   //直接输出到文本框
            } else {
                sm!!.outScanMode = 0 //接收广播
            }
        }

        showScanResult = findViewById<View>(R.id.editText1) as EditText
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.openScanner -> {
                println("openScanner = " + sm!!.outScanMode)
                sm!!.openScan()
            }
            R.id.closeScanner -> sm!!.closeScan()
            R.id.startDecode -> sm!!.startScan()
            R.id.stopDecode -> sm!!.stopScan()
            R.id.start_continue -> sm!!.setScanLaserMode(4)
            R.id.stop_continue -> sm!!.setScanLaserMode(8)
            R.id.close -> finish()
            else -> {
            }
        }
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        if (sm != null) {
            sm!!.stopScan()
        }
        unregisterReceiver(mScanReceiver)
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(SCAN_ACTION)
        registerReceiver(mScanReceiver, filter)
    }

    companion object {
        private val SCAN_ACTION = "scan.rcv.message"
    }


}