package com.hhwy.system.core.utils;

import com.hhwy.system.core.domain.SysDictType;
import java.util.ArrayList;
import java.util.List;

public class DictTypeTreeUtils {
    private List<SysDictType> dictTypeCommon;
    private List<SysDictType> list = new ArrayList<>();

    public List<SysDictType> dictTypeList(List<SysDictType> dictType){
        this.dictTypeCommon = dictType;
        for (SysDictType x : dictType) {
            if(x.getParentId() == null || x.getParentId() == 0L){
                x.setChildren(menuChild(x.getDictId()));
                list.add(x);
            }
        }
        return list;
    }

    private List<SysDictType> menuChild(Long id) {
        List<SysDictType> lists = new ArrayList<>();
        for (SysDictType x : dictTypeCommon) {
            if (id.equals(x.getParentId())) {
                x.setChildren(menuChild(x.getDictId()));
                lists.add(x);
            }
        }
        return lists;
    }
}
