package wifi.custom.amos.cem.com.libamoswifi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * @ProjectName: Meterbox iScope for 380 v1.5
 * @Package: meterbox.scope380.cem.customwifi
 * @ClassName: CustomProgressDialog
 * @Description: java类作用描述
 * @Author: Amos
 * @CreateDate: 2020/4/2 17:26
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/4/2 17:26
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        fullScreenImmersive();
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
    /**
     * code by amos
     * 全屏显示 隐藏虚拟按键
     */
    private void fullScreenImmersive() {
        View gameView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            gameView.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            gameView.setSystemUiVisibility(uiOptions);
        }
    }
}
