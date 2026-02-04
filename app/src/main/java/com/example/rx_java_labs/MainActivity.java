package com.example.rx_java_labs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    EditText etNames, etAges;
    TextView tvRes;
    Button btnAction;
    List<String> names, ages;

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


        lab1Task();

        lab2Task();

        lab3Task();

    }

    @SuppressLint("CheckResult")
    private void lab1Task() {
        Observable<Integer> myObserve = Observable.create(source -> {
            Log.d("TAG", "Hello MAD");
            source.onNext(1);
            source.onNext(2);
            source.onNext(3);
            source.onNext(4);
            source.onNext(5);
        });

        myObserve.subscribeOn(Schedulers.io())
                .doOnNext(Integer -> System.out.println("Emitting Item " + Integer + " On " + Thread.currentThread().getName()))
                .observeOn(Schedulers.newThread())
                .doOnNext(Integer -> System.out.println("After Emitting Item " + Integer + " On " + Thread.currentThread().getName()))
                .observeOn(Schedulers.computation())
                .doOnNext(Integer -> System.out.println("After Computation Thread Emitting Item " + Integer + " On " + Thread.currentThread().getName()))
                .subscribe(Integer -> System.out.println("Consuming Item " + Integer + " On " + Thread.currentThread().getName()));

    }

    @SuppressLint("CheckResult")
    private void lab2Task() {
        etNames = findViewById(R.id.et_names);
        etAges = findViewById(R.id.et_ages);
        tvRes = findViewById(R.id.tv_res);
        btnAction = findViewById(R.id.btn_action);
        names = new ArrayList<>();
        ages = new ArrayList<>();

        btnAction.setOnClickListener(v -> {
            names = Arrays.asList(etNames.getText().toString().split("\n"));
            ages = Arrays.asList(etAges.getText().toString().split("\n"));

            tvRes.setText("");

            Iterator<String> ageIterator = ages.iterator();

            Observable.fromIterable(names)
                    .subscribeOn(Schedulers.io())
                    .flatMap(name -> {
                        if (!ageIterator.hasNext()) return Observable.empty();

                        String age = ageIterator.next().trim();
                        String cleanName = name.trim();

                        if (cleanName.isEmpty() || age.isEmpty()) return Observable.empty();

                        return Observable.just(cleanName + " -> " + age);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            pair -> tvRes.append(pair + "\n"),
                            throwable -> tvRes.append("\nError!"),
                            () -> tvRes.append("\nCompleted!")
                    );
        });
    }

    @SuppressLint("CheckResult")
    private void lab3Task() {

        // distinctUntilChanged operator --> emits an item from the Observable only if it is different from the previous emitted item.
        Observable<Integer> source = Observable.just(1, 1, 2, 2, 2, 3, 1, 1);

        source.distinctUntilChanged().subscribe(System.out::println);


        // Compose operator ---> Useful for reusing a chain of operators across multiple Observables.

        ObservableTransformer<Integer, String> transformer = upstream -> upstream
                .filter(i -> i % 2 == 0)
                .map(i -> "Even: " + i);

        source.compose(transformer).subscribe(System.out::println);
    }
}