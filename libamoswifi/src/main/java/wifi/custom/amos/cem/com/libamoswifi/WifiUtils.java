package wifi.custom.amos.cem.com.libamoswifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Meterbox iScope for 380 v1.5
 * @Package: meterbox.scope380.cem.customwifi
 * @ClassName: WifiUtils
 * @Description: java类作用描述
 * @Author: Amos
 * @CreateDate: 2020/4/2 13:52
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/2 13:52
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class WifiUtils {
    // 上下文Context对象
    private Context mContext;
    // WifiManager对象
    private WifiManager mWifiManager;
    //
    public String str_select_SSID;

    public WifiUtils(Context mContext) {
        str_select_SSID="";
        this.mContext = mContext;
        mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 判断手机是否连接在Wifi上
     */
    public boolean isConnectWifi() {
        // 获取ConnectivityManager对象
        ConnectivityManager conMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        // 获取连接的方式为wifi
        NetworkInfo.State wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        if (info != null && info.isAvailable() && wifi == NetworkInfo.State.CONNECTED)

        {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取当前手机所连接的wifi信息
     */
    public WifiInfo getCurrentWifiInfo() {
        return mWifiManager.getConnectionInfo();
    }

    /**
     * 添加一个网络并连接 传入参数：WIFI发生配置类WifiConfiguration
     */
    public boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        return mWifiManager.enableNetwork(wcgID, true);
    }

    /**
     * 搜索附近的热点信息，并返回所有热点为信息的SSID集合数据
     */
    public List<String> getScanWifiResult() {
        // 扫描的热点数据
        List<ScanResult> resultList;
        // 开始扫描热点

        mWifiManager.startScan();

        resultList = mWifiManager.getScanResults();

        ArrayList<String> ssids = new ArrayList<String>();
        if (resultList != null) {
            for (ScanResult scan : resultList) {
                String str_SSID=scan.SSID;
                if((str_select_SSID=="")||(str_select_SSID==null)) {
                    ssids.add(str_SSID);// 遍历数据，取得ssid数据集
                }else {
                    Log.e("---------","当前搜索的ssid："+str_SSID  +"要筛选的SSID："+str_select_SSID);
                    if(str_SSID.contains(str_select_SSID))
                    {
                        ssids.add(str_SSID);// 遍历数据，取得ssid数据集
                    }
                }

            }
        }
        return ssids;
    }

    /**
     * 连接wifi 参数：wifi的ssid及wifi的密码
     */
    public boolean connectWifiTest(final String ssid, final String pwd,final int wifitype) {
        boolean isSuccess = false;
        boolean flag = false;
        mWifiManager.disconnect();
        boolean addSucess = addNetwork(CreateWifiInfo(ssid, pwd, wifitype));
        if (addSucess) {
            while (!flag && !isSuccess) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                String currSSID = getCurrentWifiInfo().getSSID();
                if (currSSID != null)
                    currSSID = currSSID.replace("\"", "");
                int currIp = getCurrentWifiInfo().getIpAddress();
                if (currSSID != null && currSSID.equals(ssid) && currIp != 0) {
                    isSuccess = true;
                } else {
                    flag = true;
                }
            }
        }
        return isSuccess;

    }

    /**
     * 创建WifiConfiguration对象 分为三种情况：1没有密码;2用wep加密;3用wpa加密
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public WifiConfiguration CreateWifiInfo(String SSID, String Password,
                                            int Type) {
        Log.e("---- Type","SSID:"+SSID+"Password:"+Password+"Type:"+Type);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) // WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;

        }
        if (Type == 2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }
}
