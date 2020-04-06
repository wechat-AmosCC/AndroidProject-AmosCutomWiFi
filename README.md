# AmosWiFi
## Android下自定义的WiFi搜索功能 ##


### AmosCustomWiFi

- WiFiType--1没有密码;2用wep加密;3用wpa加密
- isWindows--是否为窗口模式
- z_height--高度比列
- z_width--宽度比列
- selectWifiSSID--是否筛选WIFI不设置则显示所有搜索到的WIFI
- str_Con_Success--连接成功返回的文字
- str_Con_Failed--连接失败返回的文字
- str_Loading--连接中显示的文字
- str_Search--搜索框的文字


### how to use

 - **Add it in your root build.gradle at the end of repositories:**

```
	    allprojects {
		   repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	    }
```

 -  **Add the dependency**
```sh
	 implementation 'com.github.wechat-AmosCC:AndroidProject-AmosCutomWiFi:1.0'
	


```

 -  **java**
```java
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
```




