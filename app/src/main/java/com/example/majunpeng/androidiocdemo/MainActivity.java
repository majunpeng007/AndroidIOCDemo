package com.example.majunpeng.androidiocdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity{

    @ViewInject(value = R.id.btn1)
    private Button mBtn1;

    @ViewInject(value = R.id.btn2)
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewInjectUtils.inject(this);

//        mBtn1.setOnClickListener(this);
//        mBtn2.setOnClickListener(this);
    }

    @OnClick({R.id.btn1,R.id.btn2})
    public void clickBtnInvoked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Toast.makeText(MainActivity.this, "Why do you click me ?",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn2:
                Toast.makeText(MainActivity.this, "I am sleeping !!!",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId())
//        {
//            case R.id.btn1:
//                Toast.makeText(MainActivity.this, "Why do you click me ?",
//                        Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.btn2:
//                Toast.makeText(MainActivity.this, "I am sleeping !!!",
//                        Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
}
