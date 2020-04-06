package wifi.custom.amos.cem.com.libamoswifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * @ProjectName: WIFI
 * @Package: wifi.custom.amos.cem.com.libamoswifi
 * @ClassName: AmosWiFiView
 * @Description: java类作用描述
 * @Author: Amos
 * @CreateDate: 2020/4/3 15:25
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/3 15:25
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class AmosWiFiView extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    private Button search_btn,close_btn;
    private TextView txt_title;
    private ListView wifi_lv;
    private WifiUtils mUtils;
    private List<String> result;
    private String wifiSSID;
    public String str_selectSSID;
    public String str_Con_Success;
    public String str_Con_Failed;
    public String str_Loading;
    public String str_Search;
    private int wifiType;  //WiFi type 路由器决定，1没有密码;2用wep加密;3用wpa加密
    private CustomProgressDialog progressdlg = null;
    @Override
    protected void onResume() {
        super.onResume();
        View gameView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            gameView.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            gameView.setSystemUiVisibility(uiOptions);
        }
        Intent intent = getIntent();
        boolean isWindows = intent.getBooleanExtra("isWindows",true);
        wifiType=intent.getIntExtra("wifiType",2);
        str_selectSSID=intent.getStringExtra("selectWifiSSID");
        str_Con_Success=intent.getStringExtra("str_Con_Success");
        str_Con_Failed=intent.getStringExtra("str_Con_Failed");
        str_Loading=intent.getStringExtra("str_Loading");
        str_Search=intent.getStringExtra("str_Search");
       // Log.e("str_selectSSID:",str_selectSSID);
        if(isWindows) {
            //窗口显示
            float hh = intent.getFloatExtra("z_height",0.8f);
            float ww = intent.getFloatExtra("z_width",0.9f);
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay();
            android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
            p.height = (int) (d.getHeight() * hh);
            p.width = (int) (d.getWidth() * ww);
            p.dimAmount = 0.0f;
            getWindow().setAttributes(p);
        }

        findViews();
        setLiteners();
        mUtils = new WifiUtils(this);
        mUtils.str_select_SSID=str_selectSSID;
        if(mUtils.isConnectWifi())
        {
            WifiInfo wi= mUtils.getCurrentWifiInfo();
            txt_title.setText(wi.getSSID());
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifishow_main);

    }

    private void findViews() {
        this.search_btn = (Button) findViewById(R.id.search_btn);
        if(str_Search!=null)
        {
            if(str_Search!=""){
                this.search_btn.setText(str_Search);
            }
        }
        this.wifi_lv = (ListView) findViewById(R.id.wifi_lv);
        this.close_btn=(Button)findViewById(R.id.showwifi_close);
        this.txt_title=(TextView)findViewById(R.id.showwifi_title);
    }

    private void setLiteners() {
        search_btn.setOnClickListener(this);
        close_btn.setOnClickListener(this);
        wifi_lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn) {
            showDialog();
            new MyAsyncTask().execute();
        }else if(v.getId() == R.id.showwifi_close)
        {
            this.finish();
        }
    }

    /**
     * init dialog and show
     */
    private void showDialog() {
        progressdlg = new CustomProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if(str_Loading!=null)
        {
            if(str_Loading=="")
            {
                progressdlg.setMessage(getString(R.string.wait_moment));
            }else
            {
                progressdlg.setMessage(str_Loading);
            }

        }else {
            progressdlg.setMessage(getString(R.string.wait_moment));
        }
        progressdlg.show();
    }

    /**
     * dismiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //扫描附近WIFI信息
            result = mUtils.getScanWifiResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDismiss();
            initListViewData();
        }
    }

    private void initListViewData() {
        if (null != result && result.size() > 0) {
            wifi_lv.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.wifi_list_item,
                    R.id.ssid, result));
        } else {
            //wifi_lv.setEmptyView(findViewById(R.layout.list_empty));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        TextView tv = (TextView) arg1.findViewById(R.id.ssid);
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            /*
            Intent in = new Intent(ShowWifi.this, WifiConnectActivity.class);
            in.putExtra("ssid", tv.getText().toString());
            startActivity(in);*/
            wifiSSID=tv.getText().toString();
            showDialog();
            dealWithConnect(wifiSSID, "");
        }
    }




    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    txt_title.setText(wifiSSID);
                    String s="";
                    if(str_Con_Success==null) {
                         s = getResources().getString(R.string.wifi_did_connect);
                    }else
                    {
                        if(str_Con_Success=="")
                        {
                            s = getResources().getString(R.string.wifi_did_connect);
                        }else
                        {
                            s=str_Con_Success;
                        }
                    }
                    showToast(s);
                    finish();
                    break;
                case 1:

                    String ss="";
                    if(str_Con_Failed==null) {
                         ss=getResources().getString(R.string.wifi_not_connect);
                    }else
                    {
                        if(str_Con_Failed=="")
                        {
                            ss=getResources().getString(R.string.wifi_not_connect);
                        }else
                        {
                            ss=str_Con_Failed;
                        }
                    }
                    showToast(ss);
                    break;

            }
            progressDismiss();
        }
    };
    private void dealWithConnect(final String ssid, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                // 检验密码输入是否正确
                //WiFi type 路由器决定，1没有密码;2用wep加密;3用wpa加密
                boolean pwdSucess = mUtils.connectWifiTest(ssid, pwd,wifiType);
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pwdSucess) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }
    private void showToast(String str) {
        Toast.makeText(AmosWiFiView.this, str, Toast.LENGTH_SHORT).show();
    }
}

