package com.hhwy.file.core.service;

import com.hhwy.file.api.domain.FileParam;
import com.hhwy.file.api.domain.SysFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传接口
 *
 * @author hhwy
 */
public interface ISysFileService {

    /**
     * 文件上传接口（带业务）
     *
     * @param file 上传的文件
     * @param fileParam 文件参数
     * @return 访问地址
     * @throws Exception
     */
    public SysFile uploadFile(MultipartFile file, FileParam fileParam) throws Exception;

    /**
     * 批量文件上传接口
     *
     * @param files 上传的文件
     * @param fileParam 文件参数
     * @return 访问地址
     * @throws Exception
     */
    List<SysFile> uploadFiles(MultipartFile[] files, FileParam fileParam);

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception;

}
