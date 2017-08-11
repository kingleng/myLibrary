package com.cpsdna.app.mykotlinapplication;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by leng on 2017/4/10.
 */

public abstract class BaseRecyclerNoHeadAdapter<T> extends RecyclerView.Adapter<BaseRecyclerNoHeadAdapter.VH> {

    private ArrayList<T> data = new ArrayList<>();

    public BaseRecyclerNoHeadAdapter() {
    }

    public abstract int getLayoutId(int viewType);

    @Override
    public int getItemViewType(int getItemViewType) {
        return 1;
    }

    public ArrayList<T> getData(){
        return data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.get(parent,getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        convert(holder,data.get(position),position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract void convert(VH holder,T data,int position);

    public static class VH extends RecyclerView.ViewHolder{

        private SparseArray<View> mViews;
        private View mConvertView;

        public VH(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutId){
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
            return new VH(convertView);
        }

        public <T extends View>T getView(int id){
            View v = mViews.get(id);
            if(v == null){
                v = mConvertView.findViewById(id);
                mViews.put(id,v);
            }
            return (T)v;
        }

        public void setText(int id,String text){
            TextView textView = getView(id);
            textView.setText(text);
        }
    }
}
