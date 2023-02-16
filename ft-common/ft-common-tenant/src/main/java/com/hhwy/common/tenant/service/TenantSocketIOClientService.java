package com.hhwy.common.tenant.service;

import com.alibaba.fastjson.JSON;
import com.hhwy.common.datasource.utils.DataSourceUtils;
import com.hhwy.common.socket.service.SocketIOClientServiceAdapter;
import com.hhwy.common.tenant.utils.TenantDataSourceUtils;
import com.hhwy.system.api.domain.SysTenant;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 案例
 */
@Slf4j
public class TenantSocketIOClientService extends SocketIOClientServiceAdapter {
    private String genDbType;
    private Boolean dropIfExists;
    private Boolean needCreateDb;

    public Boolean getNeedCreateDb() {
        return needCreateDb;
    }

    public String getGenDbType() {
        return genDbType;
    }

    public Boolean getDropIfExists() {
        return dropIfExists;
    }

    public TenantSocketIOClientService(String genDbType, Boolean dropIfExists) {
        this.genDbType = genDbType;
        this.dropIfExists = dropIfExists;
        this.needCreateDb = true;
    }

    public TenantSocketIOClientService(String genDbType, Boolean dropIfExists, Boolean needCreateDb) {
        this.genDbType = genDbType;
        this.dropIfExists = dropIfExists;
        this.needCreateDb = needCreateDb;
    }

    @Override
    public void publishEvents(Socket socket) {

        socket.on("tenant_created_event", objects -> {
            log.debug("租户被添加，数据为:" + objects[0].toString());
            // 添加数据源
            if (needCreateDb) {
                JSONObject object = (JSONObject) (objects[0]);
                SysTenant sysTenant = JSON.parseObject(object.toString(), SysTenant.class);
                TenantDataSourceUtils.genTenantDataSource(sysTenant,genDbType,dropIfExists);
            }
        });

        //租户修改不需要做任何处理
        socket.on("tenant_edited_event",  objects -> {
            log.debug("租户被修改，数据为：" + objects[0].toString());
            //修改数据源
            if (needCreateDb) {
                JSONObject object = (JSONObject) (objects[0]);
                SysTenant sysTenant = JSON.parseObject(object.toString(), SysTenant.class);
                TenantDataSourceUtils.editTenantDataSource(sysTenant, genDbType, dropIfExists);
            }
        });

        socket.on("tenant_deleted_event", objects -> {
            for (Object object : objects) {
                JSONArray jsonArr= (JSONArray) object;
                for (int i = 0; i < jsonArr.length(); i++) {
                    try {
                        log.debug("租户被删除，数据为：" + jsonArr.get(i).toString());
                        // 删除数据源
                        if (needCreateDb) {
                            JSONObject obj = (JSONObject)jsonArr.get(i);
                            SysTenant sysTenant = JSON.parseObject(obj.toString(), SysTenant.class);
                            DataSourceUtils.removeDataSource(TenantDataSourceUtils.getDataSourceNameByTenantKey(sysTenant.getTenantKey()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
