package com.hhwy.file.core.mapper;

import com.hhwy.file.api.domain.SysFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件详情表Mapper接口
 * 
 * @author syd
 * @date 2022-02-17
 */
@Mapper
public interface SysFileMapper {

    /**
     * 查询附件详情表列表
     * 
     * @param sysFile 附件详情表
     * @return 附件详情表集合
     */
    List<SysFile> selectSysFileList(SysFile sysFile);

    List<SysFile> selectSysFileByIds(String[] ids);

    List<SysFile> selectSysFileListBatch(String[] businessIds);

    /**
     * 新增附件详情表
     * 
     * @param sysFile 附件详情表
     * @return 结果
     */
    int insertSysFile(SysFile sysFile);

    int deleteSysFileByBusinessIds(String[] businessIds);

    /**
     * 根据附件id集合查询所有附件
     * @param businessIdList
     * @return
     */
    List<SysFile> getSysFileListByBusinessIdList(@Param("businessIdList") List<String> businessIdList);

    /**
     * 根据businessId查询附件详情表信息
     * @param businessId
     * @return
     */
    List<SysFile> selectSysFileByBusinessId(@Param("businessId") String businessId);

    /**
     * 根据businessId删除附件
     * @param businessId
     * @return
     */
    int deleteSysFilebybusinessId(@Param("businessId") String businessId);

    int deleteSysFileByIds(String[] idList);
}
