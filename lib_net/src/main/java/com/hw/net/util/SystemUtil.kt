package com.hw.net.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.tencent.mmkv.MMKV
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * @author David
 * @date 2020/7/1
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description
 */
object SystemUtil {
    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    val systemLanguage: String
        get() = Locale.getDefault().language

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    val systemLanguageList: Array<Locale>
        get() = Locale.getAvailableLocales()

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val systemModel: String
        get() = Build.MODEL

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val deviceBrand: String
        get() = Build.BRAND
    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
//    public static String getIMEI(Context ctx) {
//        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//        if (tm != null) {
//            return tm.getDeviceId();
//        }
//        return null;
//    }//we make this look like a valid IMEI
    //13 digits
    /******
     * Pseudo-Unique ID
     */
    val pU_ID: String?
        get() {
            try {
                val m_szDevIDShort = "35" + //we make this look like a valid IMEI
                        Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 digits
//                E("getPU_ID=$m_szDevIDShort")
                return m_szDevIDShort
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    fun getAndroid_ID(context: Context): String? {
        try {
            val m_szAndroidID = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
//            E("getAndroid_ID=$m_szAndroidID")
            return m_szAndroidID
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getMacAddress(context: Context): String? {
        try {
            val wm =
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val m_szWLANMAC = wm.connectionInfo.macAddress
//            E("getMacAddress=$m_szWLANMAC")
            return m_szWLANMAC
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getDeviceId(context: Context): String? {
        try {
            val TelephonyMgr =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var szImei: String? = null
            if (TelephonyMgr != null) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
                    return null
                }
                szImei = TelephonyMgr.deviceId
            }
//            E("getDeviceId=$szImei")
            return szImei
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getOnlyNum(context: Context): String {
        val m_szLongID = (getDeviceId(context) + pU_ID
                + getAndroid_ID(context) + getMacAddress(context))
        // compute md5
        var m: MessageDigest? = null
        try {
            m = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        m!!.update(m_szLongID.toByteArray(), 0, m_szLongID.length)
        // get md5 bytes
        val p_md5Data = m.digest()
        // create a hex string
        var m_szUniqueID = ""
        for (i in p_md5Data.indices) {
            val b = 0xFF and p_md5Data[i].toInt()
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF) m_szUniqueID += "0"
            // add number to string
            m_szUniqueID += Integer.toHexString(b)
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase()
//        E("getOnlyNum=$m_szUniqueID")
        return m_szUniqueID
    }

    fun getIMEI(context: Context): String {
        val kv = MMKV.defaultMMKV()
        var deviceId = kv.decodeString("device_id")
        if(deviceId.isNullOrEmpty()) {
            deviceId = getOnlyNum(context)
            kv.encode("device_id", deviceId)
        }
        return deviceId
    }
}