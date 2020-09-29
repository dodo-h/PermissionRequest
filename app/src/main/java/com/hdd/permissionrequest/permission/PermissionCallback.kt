package com.hdd.permissionrequest.permission

/**
 *
 * Author：hdd
 * Date：2020/9/28
 * Description：
 *
 */
interface PermissionCallback {
    /**
     * permission granted
     */
    fun onPermissionGranted(permission: String)

    /**
     * permission denied
     */
    fun onPermissionDenied(permission: String)

    /**
     * permission not ask
     * it means it has been denied yet and should not ask any more
     */
    fun onPermissionNotAsk(permission: String)

    /**
     * permission goto setting
     * maybe a list of permissions need to be set
     */
    fun onPermissionSetting(permission: List<String>)
}