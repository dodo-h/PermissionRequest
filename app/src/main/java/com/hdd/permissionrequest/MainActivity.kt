package com.hdd.permissionrequest

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hdd.permissionrequest.permission.PermissionCallback
import com.hdd.permissionrequest.permission.PermissionRequest
import kotlinx.android.synthetic.main.activity_main.*

/**
 * a simple permission request demo
 */
class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_permission.setOnClickListener {
            val request = PermissionRequest()
            val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
            request.request(this, permissions, mPermissionCallback)
        }
    }

    private val mPermissionCallback = object : PermissionCallback{
        override fun onPermissionGranted(permission: String) {
            Log.i(TAG, "onPermissionGranted:$permission")
        }

        override fun onPermissionDenied(permission: String) {
            Log.e(TAG, "onPermissionDenied:$permission")
        }

        override fun onPermissionNotAsk(permission: String) {
            Log.e(TAG, "onPermissionNotAsk:$permission")
        }

        override fun onPermissionSetting(permissions: List<String>) {
            Log.e(TAG, "go to permission setting:$permissions")
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }
}
