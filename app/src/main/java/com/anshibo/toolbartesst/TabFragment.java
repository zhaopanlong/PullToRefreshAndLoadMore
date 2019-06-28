package com.anshibo.toolbartesst;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Axe on 2017/8/26.
 */

public class TabFragment extends Fragment {

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int tab = getArguments().getInt("TAB");
        View view = inflater.inflate(R.layout.fragment, container, false);
//        TextView textView = (TextView) view.findViewById(R.id.tvTitle);
//        textView.setText(tab + "");
        final PtrClassicFrameLayout ptrClassicFrameLayout = view.findViewById(R.id.ptrLayout);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.refreshComplete();
                    }
                }, 3000);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrClassicFrameLayout.refreshComplete();
                    }
                }, 3000);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecycleVewAdapter());
        return view;
    }
}
