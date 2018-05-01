package com.artf.poloa.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artf.poloa.R;
import com.artf.poloa.data.entity.TradeObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.artf.poloa.presenter.utility.Constant.decimalFormat;

public class CcMapAdapter extends RecyclerView.Adapter<CcMapAdapter.MyViewHolder> {

    private static final String DOT = "\u2022";
    final private ListItemClickListener mOnClickListener;
    private HashMap<String, TradeObject> dataMap;
    private List<String> data;


    public CcMapAdapter(HashMap<String, TradeObject> myDataSet, ListItemClickListener listener) {
        dataMap = new HashMap<>();
        dataMap.putAll(myDataSet);
        data = new ArrayList<>();
        data.addAll(dataMap.keySet());
        mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);


    }

    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearCcList() {
        this.dataMap.clear();
        this.data.clear();
        notifyDataSetChanged();
    }

    public void setCcList(HashMap<String, TradeObject> ccMap) {
        this.dataMap.putAll(ccMap);
        this.data.addAll(dataMap.keySet());
        notifyDataSetChanged();
    }

    public List<String> getData() {
        return data;
    }

    public interface ListItemClickListener {
        void onListItemClick(String coinName, TradeObject tradeObject);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.row) LinearLayout row;
        @BindView(R.id.symbol) TextView symbol;
        @BindView(R.id.macdDay) TextView macdDay;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            row.setOnClickListener(this);
        }

        public void bind(int position) {
            String coinName = (String) getDataAtPosition(position);
            symbol.setText(coinName);
            macdDay.setText(String.valueOf(decimalFormat.format(dataMap.get(coinName).rmiValue)));
            macdDay.setSelected(false);
            if(dataMap.get(coinName).rmiValue - dataMap.get(coinName).rmiSingal > 3 && 30 > dataMap.get(coinName).rmiSingal){
                macdDay.setActivated(false);
            }else if(dataMap.get(coinName).rmiSingal - dataMap.get(coinName).rmiValue > 1 && dataMap.get(coinName).rmiValue > 70){
                macdDay.setActivated(true);
            }else{
                macdDay.setSelected(true);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String coinName = (String) getDataAtPosition(clickedPosition);
            TradeObject tradeObject = dataMap.get(coinName);
            mOnClickListener.onListItemClick(coinName, tradeObject);
        }
    }
}
