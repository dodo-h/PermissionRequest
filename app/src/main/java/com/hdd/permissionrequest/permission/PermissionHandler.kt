package com.hdd.permissionrequest.permission

import android.util.Log
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.HashMap

/**
 *
 * Author：hdd
 * Date：2020/9/28
 * Description：
 *
 */
class PermissionHandler {
    private val mPermissionActions = HashMap<String, PermissionAction>()
    private val mGotoSettingPermissions = ArrayList<String>()
    private var mRequestCallback: PermissionCallback? = null
    private var isRequestNow = false

    fun request(fragment: Fragment, permissions: Array<String>, callback: PermissionCallback){
        if(isRequestNow){
            Log.w(PermissionRequest.TAG, "is request now,skip it.")
            return
        }

        isRequestNow = true
        mRequestCallback = callback

        for(perm in permissions){
            val action = PermissionAction(fragment.activity, perm, PermissionRequest.REQUEST_CODE)
            mPermissionActions[perm] = action
            action.request(mPermissionCallback)
        }

        //invoke fragment's requestPermissions()，then callback fragment's onRequestPermissionsResult()
        fragment.requestPermissions(permissions, PermissionRequest.REQUEST_CODE)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        if(permissions != null && grantResults != null){
            val size = permissions.size
            for(index in 0 until size){
                val permission = permissions[index]
                val action = mPermissionActions[permission]
                action?.onRequestPermissionsResult(grantResults[index])
            }

            if(mGotoSettingPermissions.size > 0){
                gotoPermissionSetting(mGotoSettingPermissions)
            }
        }else{
            Log.w(PermissionRequest.TAG, "onRequestPermissionsResult:" +
                    "permissions=${permissions.contentToString()},grantResults=${grantResults.contentToString()}")
        }

        onEnd()
    }

    private val mPermissionCallback = object : PermissionCallback{
        override fun onPermissionGranted(permission: String) {
            mRequestCallback?.onPermissionGranted(permission)
        }

        override fun onPermissionDenied(permission: String) {
            mRequestCallback?.onPermissionDenied(permission)
        }

        override fun onPermissionNotAsk(permission: String) {
            mRequestCallback?.onPermissionNotAsk(permission)
        }

        override fun onPermissionSetting(permissions: List<String>) {
            mGotoSettingPermissions.addAll(permissions)
        }
    }

    private fun gotoPermissionSetting(permissions: List<String>){
        mRequestCallback?.onPermissionSetting(permissions)
    }

    private fun onEnd(){
        mPermissionActions.clear()
        mGotoSettingPermissions.clear()
        mRequestCallback = null
        isRequestNow = false
    }
}