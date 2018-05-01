package com.artf.poloa.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artf.poloa.R;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseFragmentMvp<T> extends Fragment {

    @BindView(R.id.swipeRefreshLayout)
    public SwipyRefreshLayout swipyRefreshLayout;
    @Nullable
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    protected View rootView;
    protected T mCallback;
    protected SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (T) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentLayout(), container, false);
        ButterKnife.bind(this, rootView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initComponents();
        return rootView;
    }

    public abstract int getContentLayout();

    public abstract void initComponents();

}
