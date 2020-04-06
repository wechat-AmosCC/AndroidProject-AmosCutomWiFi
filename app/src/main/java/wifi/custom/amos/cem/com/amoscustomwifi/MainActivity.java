package wifi.custom.amos.cem.com.amoscustomwifi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import wifi.custom.amos.cem.com.libamoswifi.AmosWiFiView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Button m_btn_wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }


        m_btn_wifi=findViewById(R.id.m_btn_wifi);
        m_btn_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent starter = new Intent(MainActivity.this, AmosWiFiView.class);
                //指定WiFiType   1没有密码;2用wep加密;3用wpa加密
                starter.putExtra("wifiType",2);
                //设置为窗口模式
                starter.putExtra("isWindows",true);
                //设置高度为原高度的0.6
                starter.putExtra("z_height",0.6);
                //设置宽度为原宽度的0.6
                starter.putExtra("z_width",0.8);
                //只搜索SSID中包含CEM的WIFI  不设置则搜索所有的设备
                starter.putExtra("selectWifiSSID","CEM");
                //设置相关文字，如不设置则为默认文字
                starter.putExtra("str_Con_Success","连接成功");
                starter.putExtra("str_Con_Failed","连接失败");
                starter.putExtra("str_Loading","加载中");
                //设置搜索框文字
                starter.putExtra("str_Search","搜索WiFi");
                startActivity(starter);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else
                {
                }
                break;
        }
    }
}
