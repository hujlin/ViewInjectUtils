package com.example.che.annocationdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.che.annocationdemo.anno.ContentView;
import com.example.che.annocationdemo.anno.OnClick;
import com.example.che.annocationdemo.anno.ViewInject;
import com.example.che.annocationdemo.utils.ViewInjectUtils;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.text)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        ViewInjectUtils.inject(this);

        textView.setText("自定义注解");
    }

    @OnClick(value = {R.id.button,R.id.button2})
    public void onClick(View view){
       switch (view.getId()){
           case R.id.button:
               Toast.makeText(this,"button1",Toast.LENGTH_SHORT).show();
               break;
           case R.id.button2:
               Toast.makeText(this,"button2",Toast.LENGTH_SHORT).show();
               break;
       }
    }
}
