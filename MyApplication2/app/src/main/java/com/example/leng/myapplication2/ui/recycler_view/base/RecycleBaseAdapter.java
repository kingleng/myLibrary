package com.example.leng.myapplication2.ui.recycler_view.base;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class RecycleBaseAdapter<C extends RecycleBaseCell> extends
        RecyclerView.Adapter<RecycleBaseViewHolder> {

    public static final String TAG = "RecycleBaseAdapter";
    protected List<C> mDataList;

    public RecycleBaseAdapter() {
        mDataList = new ArrayList<>();
    }

    public List<C> getData() {
        return mDataList;
    }

    public void setData(List<C> data) {
        addAll(data);
//        notifyDataSetChanged();
    }

    public void setDataRefresh(List<C> data) {
        mDataList.clear();
        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecycleBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < getItemCount(); i++) {
            if (viewType == mDataList.get(i).getItemType()) {
                return mDataList.get(i).onCreateViewHolder(parent, viewType);
            }
        }

        throw new RuntimeException("wrong viewType");
    }

    @Override
    public void onBindViewHolder(RecycleBaseViewHolder holder, int position) {
        mDataList.get(position).onBindViewHolder(holder, position);
    }

    @Override
    public void onViewAttachedToWindow(RecycleBaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.e(TAG, "onViewAttachedToWindow invoke...");
        //初始化资源
        int position = holder.getAdapterPosition();
        //越界检查
        if (position < 0 || position >= mDataList.size()) {
            return;
        }
        mDataList.get(position).initResource();
    }

    @Override
    public void onViewDetachedFromWindow(RecycleBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.e(TAG, "onViewDetachedFromWindow invoke...");
        //释放资源
        int position = holder.getAdapterPosition();
        //越界检查
        if (position < 0 || position >= mDataList.size()) {
            return;
        }
        mDataList.get(position).releaseResource();
    }


    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getItemType();
    }

    /**
     * add one itemcell
     */
    public void add(C cell) {
        mDataList.add(cell);
        int index = mDataList.indexOf(cell);
        notifyItemChanged(index);
    }

    public void add(int index, C cell) {
        mDataList.add(index, cell);
        notifyItemChanged(index);
    }

    /**
     * remove a cell
     */
    public void remove(C cell) {
        int indexOfCell = mDataList.indexOf(cell);
        remove(indexOfCell);
    }

    public void remove(int index) {
        if (index > -1) {
            mDataList.remove(index);
            notifyItemRemoved(index);
        } else {
            Log.e(TAG, "remove cell: this cell  not found");
        }
    }

    /**
     * @param start
     * @param count
     */
    public void remove(int start, int count) {
        if ((start + count) > mDataList.size()) {
            return;
        }

        mDataList.subList(start, start + count).clear();
        notifyItemRangeRemoved(start, count);
    }

    /**
     * add a cell list
     */
    public void addAll(List<C> cells) {
        if (cells == null || cells.size() == 0) {
            return;
        }
        Log.e(TAG, "addAll cell size:" + cells.size());
        mDataList.addAll(cells);
        notifyItemRangeChanged(mDataList.size() - cells.size(), mDataList.size());
    }

    public void addAll(int index, List<C> cells) {
        if (cells == null || cells.size() == 0) {
            return;
        }
        mDataList.addAll(index, cells);
        notifyItemRangeChanged(index, index + cells.size());
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

}
