package wifi.custom.amos.cem.com.libamoswifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @ProjectName: Meterbox iScope for 380 v1.5
 * @Package: meterbox.scope380.cem.customwifi
 * @ClassName: WifiConnectActivity
 * @Description: java类作用描述
 * @Author: Amos
 * @CreateDate: 2020/4/2 13:54
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/2 13:54
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class WifiConnectActivity extends Activity implements View.OnClickListener {
    private Button connect_btn;
    private TextView wifi_ssid_tv;
    private EditText wifi_pwd_tv;
    private WifiUtils mUtils;
    // wifi之ssid
    private String ssid;
    private String pwd;
    private ProgressDialog progressdlg = null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    showToast("WIFI连接成功");
                    finish();
                    break;
                case 1:
                    showToast("WIFI连接失败");
                    break;

            }
            progressDismiss();
        }
    };
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mUtils = new WifiUtils(this);
        findViews();
        setLiteners();
        initDatas();
    }

    /**
     * init dialog
     */
    private void progressDialog() {
        progressdlg = new ProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.wait_moment));
        progressdlg.show();
    }

    /**
     * dissmiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }

    private void initDatas() {
        ssid = getIntent().getStringExtra("ssid");
        if (!TextUtils.isEmpty(ssid)) {
            ssid = ssid.replace("\"", "");
        }
        this.wifi_ssid_tv.setText(ssid);
    }

    private void findViews() {
        this.connect_btn = (Button) findViewById(R.id.connect_btn);
        this.wifi_ssid_tv = (TextView) findViewById(R.id.wifi_ssid_tv);
        this.wifi_pwd_tv = (EditText) findViewById(R.id.wifi_pwd_tv);
    }

    private void setLiteners() {
        connect_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connect_btn) {// 下一步操作
            pwd = wifi_pwd_tv.getText().toString();

            /*
            // 判断密码输入情况
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "请输入wifi密码", Toast.LENGTH_SHORT).show();
                return;
            }*/
            progressDialog();
            // 在子线程中处理各种业务
            dealWithConnect(ssid, pwd,2);
        }
    }

    private void dealWithConnect(final String ssid, final String pwd,final int wifiType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                // 检验密码输入是否正确
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
        Toast.makeText(WifiConnectActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
