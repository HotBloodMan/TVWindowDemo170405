package com.example.a1.tvwindowdemo170405;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCreate;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private View view;
    private Button btnAdd;
    private Button btnUpdate;
    private Button btnRemove;

    private TimerTask task;
    private int time = 10;
    private Timer timer;
    private TextView tvRW;
    private EditText etMain;
    private TextView tvShowTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.remote_window,null);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnRemove = (Button) findViewById(R.id.btn_removelayout);
        tvShowTime = (TextView) findViewById(R.id.tvshowtime);
        etMain = (EditText) findViewById(R.id.et_main);
        tvRW = (TextView) view.findViewById(R.id.tv_rw);
        initListener();

    }

    private void initListener() {
        btnCreate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

    }
public Handler mHandler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    eixt();
                    break;
                case 2:
                    tvRW.setText(time+getApplicationContext().getResources().getString(R.string.close_voice_remote_control_string));
                    mWindowManager.updateViewLayout(view, mLayout);
                    break;
            }
            super.handleMessage(msg);
        }
};

    protected void eixt() {
        if(task!=null){
            task.cancel();
        }
        mWindowManager.removeView(view);
    }


    /**
     * 设置WindowManager
     */
    private void createWindowManager() {
        // 取得系统窗体
//        mWindowManager = (WindowManager) getApplicationContext()
//                .getSystemService("window");
        mWindowManager = (WindowManager) getApplicationContext()
                .getSystemService(WINDOW_SERVICE);

        // 窗体的布局样式
        mLayout = new WindowManager.LayoutParams();

        // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
        mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE+WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        // 设置显示的模式
        mLayout.format = PixelFormat.RGBA_8888;

        // 设置对齐的方法
        mLayout.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        // 设置窗体宽度和高度
        mLayout.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create:
                createWindowManager();
                mWindowManager.addView(view,mLayout);
            break;
            case R.id.btn_add:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        do{
                            time--;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            tvShowTime.setText("剩余时间："+time);
                        }while (time==1);

                        if(time==1){
                            Log.d("TAG","aaa-------->>>>>countDown ");
                            countDown();
                            return;
                        }
                    }
                }.start();
            break;
            case R.id.btn_update:
                String s = etMain.getText().toString();
                tvRW.setText("现在的内容是："+s.toString());
                mWindowManager.updateViewLayout(view,mLayout);
                break;
            case R.id.btn_removelayout:
                countDown();

            break;
        }
    }

    private void countDown() {
        if(timer ==null){
            timer = new Timer();
        }
        if(task==null){
            task = new TimerTask()
            {
                public void run()
                {
//                 runOnUiThread(new Runnable()
//                 {
//                     public void run()
//                     {
                    if (time <= 1)
                    {
//                             getVerifyCodeBtn.setEnabled(true);
//                             getVerifyCodeBtn.setText("获取验证码");
                        mHandler.sendEmptyMessage(1);
                    }else {
                        time = (-1 + time);
                        mHandler.sendEmptyMessage(2);

                    }
//                     }
//                 });
                }
            };
            timer.schedule(task, 0L, 1000L);
        }

    }

}
