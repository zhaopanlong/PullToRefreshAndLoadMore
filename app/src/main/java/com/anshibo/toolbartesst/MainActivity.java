package com.anshibo.toolbartesst;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    RecyclerView recyclerView;
    PtrClassicFrameLayout ptrLayout;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlexStatusBarUtils.setTransparentStatusBar(this);
        AlexStatusBarUtils.StatusBarLightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecycleVewAdapter());

        ptrLayout= findViewById(R.id.ptrLayout);
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                ptrLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrLayout.refreshComplete();
                    }
                },3000);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrLayout.refreshComplete();
                    }
                },3000);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop, menu);
        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                break;
            case R.id.two:
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                break;
            case R.id.three:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
                case R.id.foure:
                    recyclerView.setLayoutManager(new GridLayoutManager(this,3));
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
}
