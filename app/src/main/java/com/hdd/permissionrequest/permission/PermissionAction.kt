package com.hdd.permissionrequest.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.lang.ref.WeakReference

/**
 *
 * Author：hdd
 * Date：2020/9/28
 * Description：
 *
 */
class PermissionAction(context: Activity?, permission: String, requestCode: Int) {
    private val mContext = WeakReference<Activity>(context)
    private val mPermission = permission
    private val mRequestCode = requestCode
    private var shouldShowRational = false

    private var mRequestCallback: PermissionCallback? = null

    fun request(callback: PermissionCallback?){
        mRequestCallback = callback

        val act = mContext.get()
        act?.let {
            shouldShowRational = ActivityCompat.shouldShowRequestPermissionRationale(it, mPermission)
            Log.i(PermissionRequest.TAG, "request permission:code=$mRequestCode,$mPermission")
        }
    }

    fun onRequestPermissionsResult(grantResult: Int){
//        Log.i(PermissionRequest.TAG, "permission result:$grantResult")
        if(grantResult == PackageManager.PERMISSION_GRANTED){
            onPermissionGranted()
        }else if(grantResult == PackageManager.PERMISSION_DENIED){
            onPermissionDenied()
        }
    }

    /**
     * permission granted
     */
    private fun onPermissionGranted(){
        mRequestCallback?.onPermissionGranted(mPermission)
    }

    /**
     * permission denied
     */
    private fun onPermissionDenied(){
        val act = mContext.get()
        act?.let {
            if ((shouldShowRational && !ActivityCompat.shouldShowRequestPermissionRationale(it, mPermission))) {
                //it is refused before,shouldShowRational=true, request again,choose not ask again,
                //then shouldShowRational=false.
                onPermissionNotAsk()
            } else if (!shouldShowRational && !ActivityCompat.shouldShowRequestPermissionRationale(it, mPermission)){
                //it has chosen not ask again, request again, go to permission setting page.
                onPermissionSetting()
            } else {
                mRequestCallback?.onPermissionDenied(mPermission)
            }
        }
    }

    /**
     * permission do not ask
     */
    private fun onPermissionNotAsk(){
        mRequestCallback?.onPermissionNotAsk(mPermission)
    }

    /**
     * permission go to setting
     */
    private fun onPermissionSetting(){
        val list = ArrayList<String>()
        list.add(mPermission)
        mRequestCallback?.onPermissionSetting(list)
    }
}