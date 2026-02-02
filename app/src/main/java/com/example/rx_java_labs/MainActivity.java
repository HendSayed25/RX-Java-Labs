package com.example.rx_java_labs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class MainActivity extends AppCompatActivity {
    Button fromArray , fromItr;
    TextView res;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         fromArray = findViewById(R.id.from_array);
         fromItr = findViewById(R.id.from_itratable);
         res = findViewById(R.id.result);


         //Lab1

        ArrayList<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(10);
        list.add(1);
        list.add(11);


        fromArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable<ArrayList<Integer>> observable = Observable.fromArray(list);
                observable.subscribe(item -> res.append("\n"+item+" "), error -> Log.d("Error is ", error.toString()), () -> Log.d("End", "Completed"));
            }
        });

        fromItr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable<Integer> observable = Observable.fromIterable(list);
                observable.subscribe(item -> res.append(item+" "), error -> Log.d("Error is ", error.toString()), () -> Log.d("End", "Completed"));
            }
        });



        //Lab2 (Know about these observable)

        Observable.intervalRange(1, 5, 0, 2, TimeUnit.SECONDS)
                .subscribe(
                        item -> Log.d("Item", String.valueOf(item)),
                        error -> Log.d("Error", error.toString()),
                        () -> Log.d("End", "Completed")
                );


        Observable<String> observable = Observable.never();

        observable.subscribe(
                item -> Log.d("Item", item),
                error -> Log.d("Error", error.toString()),
                () -> Log.d("End", "Completed")
        );


    }
}