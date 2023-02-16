package com.hhwy.common.core.utils;

import java.util.ArrayList;
import java.util.List;
import com.github.pagehelper.util.MetaObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

/**
 * <br>描 述： 将记录list转化为树形list
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/6/30 11:00
 * <br>修改备注：案例 SysMenuController.treeList()
 * <br>版本：1.0.0
 */
public class TreeUtils {

    /**
     * <br>方法描述：将List<SysMenu>转换为tree结构
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/5 15:53
     * <br>备注：无
     */
    /*public static <T> List<T> formatMenuTree(List<T> list, Boolean flag) {
        return formatTree(list, flag, "menuId", "parentId", "children");
    }*/

    /**
     * <br>方法描述：将List<DictType>转换为tree结构
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/5 15:53
     * <br>备注：无
     */
    /*public static <T> List<T> formatDictTypeTree(List<T> list, Boolean flag) {
        return formatTree(list, flag, "dictId", "parentId", "children");
    }*/

    /**
     * <br>方法描述：将List<SysDept>转换为tree结构
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/5 15:53
     * <br>备注：无
     */
    /*public static <T> List<T> formatDeptTree(List<T> list, Boolean flag) {
        return formatTree(list, flag, "deptId", "parentId", "children");
    }*/


    public static <T> List<T> formatTree(List<T> list, Boolean flag, String idPropertyName, String parentIdPropertyName, String childrenPropertyName) {
        return formatTree(list, flag, idPropertyName, parentIdPropertyName, childrenPropertyName, "leaf", "expanded");
    }

    public static <T> List<T> formatTree(List<T> list, Boolean flag, String idPropertyName, String parentIdPropertyName,
                                             String childrenPropertyName, String leafPropertyName, String expandedPropertyName) {
        if (StringUtils.isBlank(leafPropertyName)) leafPropertyName = "leaf";
        if (StringUtils.isBlank(expandedPropertyName)) expandedPropertyName = "expanded";
        List<T> nodeList = new ArrayList<T>();
        for (T node1 : list) {
            Object node1ParentId = getParamValue(node1, parentIdPropertyName, false);
            boolean mark = false;
            for (T node2 : list) {
                Object node2MenuId = getParamValue(node2, idPropertyName, false);
                List node2Children = (List) getParamValue(node2, childrenPropertyName, false);

                if (node1ParentId != null && node1ParentId.equals(node2MenuId)) {
                    setParamValue(node2, leafPropertyName, false, false);
                    mark = true;
                    if (node2Children == null) {
                        setParamValue(node2, childrenPropertyName, new ArrayList<>(), false);
                    }
                    node2Children.add(node1);
                    if (flag) {
                        //默认已经全部展开
                    } else {
                        setParamValue(node2, expandedPropertyName, false, false);
                    }
                    break;
                }
            }
            if (!mark) {
                nodeList.add(node1);
                if (flag) {
                    //默认已经全部展开
                } else {
                    setParamValue(node1, expandedPropertyName, false, false);
                }
            }
        }
        return nodeList;
    }

    protected static Object getParamValue(Object obj, String paramName, boolean required) {
        MetaObject paramsObject = MetaObjectUtil.forObject(obj);
        Object value = null;
        if (paramsObject.hasGetter(paramName)) {
            value = paramsObject.getValue(paramName);
        }
        if (value != null && value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            if (values.length == 0) {
                value = null;
            } else {
                value = values[0];
            }
        }
        if (required && value == null) {
            throw new RuntimeException("封装tree json 缺少必要的参数:" + paramName);
        }
        return value;
    }

    protected static Object setParamValue(Object obj, String paramName, Object paramValue, boolean required) {
        MetaObject paramsObject = MetaObjectUtil.forObject(obj);
        Object value = null;
        if (paramsObject.hasSetter(paramName)) {
            paramsObject.setValue(paramName, paramValue);
        }
        if (required && value == null) {
            throw new RuntimeException("缺少必要的参数:" + paramName);
        }
        return value;
    }

}
