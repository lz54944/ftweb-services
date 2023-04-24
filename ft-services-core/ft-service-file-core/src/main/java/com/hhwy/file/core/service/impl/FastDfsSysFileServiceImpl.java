package com.hhwy.file.core.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hhwy.file.api.domain.FileParam;
import com.hhwy.file.api.domain.SysFile;
import com.hhwy.file.core.service.ISysFileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FastDFS 文件存储
 *
 * @author hhwy
 */
@Service
public class FastDfsSysFileServiceImpl implements ISysFileService {
    /**
     * 域名或本机访问地址
     */
    @Value("${fdfs.domain}")
    public String domain;

    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public SysFile uploadFile(MultipartFile file, FileParam fileParam) throws Exception {
        return null;
    }

    @Override
    public List<SysFile> uploadFiles(MultipartFile[] files, FileParam fileParam) {
        return null;
    }

    /**
     * FastDfs文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return domain + "/" + storePath.getFullPath();
    }
}
