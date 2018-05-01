package com.artf.poloa.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;

import com.artf.poloa.R;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.view.adapter.CcMapAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.HashMap;

public class CcListFragment extends BaseFragmentMvp<CcListFragment.CcListFragmentInt> implements
        CcMapAdapter.ListItemClickListener, SwipyRefreshLayout.OnRefreshListener {


    private GridLayoutManager layoutManager;
    private CcMapAdapter ccMapAdapter;
    private TextView isSubscribe;
    private Boolean isMine = false;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_list_view;
    }

    @Override
    public void initComponents() {
        swipyRefreshLayout.setOnRefreshListener(this);
        setAdapter(new HashMap<>());
    }


    public void setAdapter(HashMap<String, TradeObject> myDataSet) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ccMapAdapter = new CcMapAdapter(myDataSet, this);
        recyclerView.setAdapter(ccMapAdapter);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            mCallback.loadData();
        } else if (direction == SwipyRefreshLayoutDirection.TOP) {
            mCallback.loadData();
        }
    }

    @Override
    public void onListItemClick(String coinName, TradeObject tradeObject) {
        mCallback.setDetails(coinName, tradeObject);
    }

    public void setRefreshing(boolean refreshing){
        swipyRefreshLayout.setRefreshing(refreshing);
    }

    public void setCcData(HashMap<String, TradeObject> ccMap){
        ccMapAdapter.clearCcList();
        ccMapAdapter.setCcList(ccMap);
        swipyRefreshLayout.setRefreshing(false);
    }


    public interface CcListFragmentInt {
        void loadData();
        void setDetails(String coinName, TradeObject tradeObject);
    }

}
