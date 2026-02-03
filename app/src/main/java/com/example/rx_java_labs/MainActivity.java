package com.example.rx_java_labs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;

public class MainActivity extends AppCompatActivity {

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

        //Lab1 (Example of filter and count)
        List<String> names = List.of("Patrick Ross", "Kelly Wood", "James Moore", "Janice Coleman", "Mary Carter");

        long count = names.stream().filter(item -> item.length() < 12 && item.charAt(0) == 'J').count();
        Log.e("TAG", "Lab1-->  " + count);


        //Lab2 (Example of anyMatch)
        Stream<String> stream = Stream.of("One", "Two", "Three", "Four");
        boolean match = stream.anyMatch(s -> s.contains("Four"));
        Log.e("TAG", "Lab2--> " + match);


        //Lab3 (Understanding debounce + non terminal) (flatMap don't save the order while concatMap save)

        Observable.just("H", "He", "Hell", "Hello")
                .concatMap(item ->
                        Observable.just(item)
                                .delay(
                                        item.equals("H") ? 100 :
                                                item.equals("He") ? 300 :
                                                        item.equals("Hell") ? 100 :
                                                                item.equals("Hello") ? 600 : 1000,
                                        TimeUnit.MILLISECONDS
                                ))
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> Log.e("TAG", "Lab 3--> " + item),
                        Throwable::printStackTrace,
                        () -> Log.e("TAG", "Lab 3--> Complete")
                );
    }
}