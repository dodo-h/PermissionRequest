package com.hdd.permissionrequest.permission

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 *
 * Author：hdd
 * Date：2020/9/28
 * Description：
 *
 */
class PermissionRequest: Fragment() {
    companion object{
        const val TAG = "PermissionRequest"
        const val REQUEST_CODE = 100
    }

    private var mPermissionHandler: PermissionHandler? = null
    private var mFragmentManager: FragmentManager? = null

    fun request(context: FragmentActivity, permissions: Array<String>, callback: PermissionCallback){
        Log.i(TAG, "start request.")

        val fm = context.supportFragmentManager
        val fragment = this
        try {
            fm.beginTransaction().add(fragment, "fragment_permission").commitNow()
        } catch (e: Exception) {
            Log.e(TAG, "add fragment error.", e)
            return
        }

        mFragmentManager = fm
        mPermissionHandler = PermissionHandler()
        context?.let { mPermissionHandler?.request(fragment, permissions, callback) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHandler?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onEnd()
    }

    private fun onEnd(){
        val task = Runnable {
            try {
                mFragmentManager?.beginTransaction()?.remove(this)?.commitNow()
            } catch (e: Exception) {
                Log.e(TAG, "remove fragment error.", e)
            }
        }
        Handler().postDelayed(task, 300)
    }
}