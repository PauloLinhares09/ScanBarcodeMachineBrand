package com.example.testscan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second_test.*

class SecondTestActivity : AppCompatActivity() {

    internal var sm: ScanDevice? = null
    private var barcodeStr: String? = null


    private val mScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val barocode = intent.getByteArrayExtra("barocode")
            val barocodelen = intent.getIntExtra("length", 0)
            val temp = intent.getByteExtra("barcodeType", 0.toByte())
            barcodeStr = String(barocode, 0, barocodelen)
            etBarcode!!.append("�㲥�����")
            etBarcode!!.append(barcodeStr)
            etBarcode!!.append("\n")
            //       showScanResult.setText(barcodeStr);
            sm!!.stopScan()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_test)

        sm = ScanDevice()

    }



    override fun onPause() {
        super.onPause()
        if (sm != null) {
            sm!!.stopScan()
        }
        unregisterReceiver(mScanReceiver)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(SCAN_ACTION)
        registerReceiver(mScanReceiver, filter)
    }

    companion object {
        private val SCAN_ACTION = "scan.rcv.message"
    }



}
