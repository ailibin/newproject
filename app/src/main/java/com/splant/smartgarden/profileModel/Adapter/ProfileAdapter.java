package com.splant.smartgarden.profileModel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseRecyclerAdapter;
import com.splant.smartgarden.baseModel.BaseViewHolder;
import com.splant.smartgarden.beanModel.Entity.ProfileItem;
import com.splant.smartgarden.listenerModel.OnHeaderClickListener;
import com.splant.smartgarden.listenerModel.OnMyItemClickListener;

import java.util.List;

import butterknife.Bind;

/**
 * Created by aifengbin on 2017/3/9.
 */
public class ProfileAdapter extends BaseRecyclerAdapter<ProfileItem,BaseViewHolder> {

    private Context mContext;
    private List<ProfileItem> mProfileItems;
    private final int TYPE_HEADER = 0;
    private final int TYPE_CONTENT = 1;
    private final int HEARDER_COUNT = 1;
    private OnMyItemClickListener onItemClickListener;
    private OnHeaderClickListener onHeaderClickListener;
    private LayoutInflater mLayoutInflater;

    public ProfileAdapter(Context context, List<ProfileItem> profileItems) {
        super(context,profileItems);
        mContext = context;
        mProfileItems = profileItems;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.gaiaa_fragment_profile_header, parent, false), onHeaderClickListener);
        }
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.gaiaa_profile_item, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindData();
        } else {
            ((ContentViewHolder) holder).bind(mProfileItems, position-HEARDER_COUNT);
        }
    }

    @Override
    public int getItemCount() {
        return mProfileItems == null ? 0 : mProfileItems.size() + HEARDER_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //返回头布局
            return TYPE_HEADER;
        }
        return TYPE_CONTENT;
    }

    class ContentViewHolder extends BaseViewHolder{

        @Bind(R.id.icon)
        ImageView mImageView;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.descript)
        TextView descript;

        public ContentViewHolder(View itemView, OnMyItemClickListener onItemClickListener) {
            super(itemView,onItemClickListener,HEARDER_COUNT);
        }

        public void bind(List<ProfileItem> mProfileItems, int position) {
            ProfileItem item = mProfileItems.get(position);
            mImageView.setImageResource(item.getIconResId());
            title.setText(item.getTitle());
            descript.setText(item.getDescript());
        }
    }

    class HeaderViewHolder extends BaseViewHolder{

        @Bind(R.id.logo)
        ImageView logo;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.locale)
        TextView locale;
        @Bind(R.id.profile_header)
        RelativeLayout profileHeader;

        public HeaderViewHolder(View itemView, OnHeaderClickListener listener) {
            super(itemView,listener);
        }

        public void bindData() {
            //这里等会再处理
            logo.setImageResource(R.mipmap.ic_avator);
            name.setText("天使");
            locale.setText("烟台");
        }
    }
}
