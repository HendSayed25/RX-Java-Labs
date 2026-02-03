package com.example.rx_java_labs.lab4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rx_java_labs.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchActivity extends AppCompatActivity {

    private EditText etText;
    private RecyclerView recycler;
    private NameAdapter adapter;
    private List<String> names;
    private Disposable dis;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Observable<String> obser = Observable.create(emit -> {
            etText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @SuppressLint("CheckResult")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emit.onNext(s.toString());
                }
            });
        });

        dis = obser.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    List<String> ress = names.stream().filter(n -> n.contains(res)).collect(Collectors.toList());
                    adapter.setList(ress);
                }, Throwable::printStackTrace);
    }

    void init() {
        etText = findViewById(R.id.editText);
        recycler = findViewById(R.id.recycler);
        adapter = new NameAdapter();
        names = Arrays.asList("Hend", "Esraa", "Alaa", "Fatema", "Yomna", "Mona", "Ahmed", "Mohamed", "Maher", "Omar", "Ali", "Ashraf");
        adapter.setList(names);
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dis.dispose();
    }
}