package com.splant.smartgarden.listenerModel;


import com.splant.smartgarden.daoModel.Entity.UnitSingleModel;

/**
 * Created by aifengbin on 2017/3/16.
 */

public interface UnitOnClickListener {
    void onItemClick(int position, UnitSingleModel unitInfo);
}
