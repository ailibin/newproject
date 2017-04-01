package com.splant.smartgarden.baseModel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.splant.smartgarden.listenerModel.OnHeaderClickListener;
import com.splant.smartgarden.listenerModel.OnMyItemClickListener;
import com.splant.smartgarden.listenerModel.OnMyItemLongClickListener;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.ButterKnife;

/**
 * Created by aifengbin on 2017/3/15.
 * ViewHolder的基类
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private OnMyItemClickListener onMyItemClickListener;
    private OnMyItemLongClickListener onMyItemLongClickListener;
    private OnHeaderClickListener onHeaderClickListener;
    private int mHeadCount;//头部的个数
    private int mFootCount;//尾部的个数

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        AutoUtils.autoSize(itemView); //屏幕适配
    }

    public BaseViewHolder(View itemView, OnHeaderClickListener onHeaderClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        AutoUtils.autoSize(itemView);
        this.onHeaderClickListener = onHeaderClickListener;
        itemView.setOnClickListener(this);
    }

    public BaseViewHolder(View itemView, OnMyItemClickListener onMyItemClickListener, int headCount) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        AutoUtils.autoSize(itemView);
        this.mHeadCount = headCount;
        itemView.setOnClickListener(this);
        this.onMyItemClickListener = onMyItemClickListener;
    }


    public BaseViewHolder(View itemView, OnMyItemLongClickListener onMyItemLongClickListener, int headCount) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        AutoUtils.autoSize(itemView);
        this.mHeadCount = headCount;
        itemView.setOnLongClickListener(this);
        this.onMyItemLongClickListener = onMyItemLongClickListener;
    }

    public BaseViewHolder(View itemView, OnMyItemClickListener onMyItemClickListener, OnMyItemLongClickListener onMyItemLongClickListener, int headCount) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        AutoUtils.autoSize(itemView);
        this.mHeadCount = headCount;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.onMyItemClickListener = onMyItemClickListener;
        this.onMyItemLongClickListener = onMyItemLongClickListener;
    }

    @Override
    public void onClick(View v) {

        if (onHeaderClickListener != null) {
            onHeaderClickListener.onRecycleHeadClick(v);
        }
        if (onMyItemClickListener != null && mHeadCount == 0) {
            onMyItemClickListener.onItemClick(v, getAdapterPosition());
        } else if (onMyItemClickListener != null && mHeadCount > 0) {
            onMyItemClickListener.onItemClick(v, getAdapterPosition() - mHeadCount);
        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (onMyItemLongClickListener != null && mHeadCount == 0) {
            onMyItemLongClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        } else if (onMyItemLongClickListener != null && mHeadCount > 0) {
            onMyItemLongClickListener.onItemLongClick(v, getAdapterPosition() - mHeadCount);
        }
        return false;
    }
}
