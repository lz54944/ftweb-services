package com.hhwy.file.core.service.impl;

import com.hhwy.common.core.utils.BaseEntityUtils;
import com.hhwy.common.core.utils.file.FileUploadUtils;
import com.hhwy.file.api.domain.FileParam;
import com.hhwy.file.api.domain.SysFile;
import com.hhwy.file.core.config.MinioConfig;
import com.hhwy.file.core.mapper.SysFileMapper;
import com.hhwy.file.core.service.ISysFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Minio 文件存储
 *
 * @author hhwy
 */
@Primary
@Service
public class MinioSysFileServiceImpl implements ISysFileService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    @Value("${minio.url}")
    private String minioPath;

    @Autowired
    private SysFileMapper sysFileMapper;

    /**
     * minio文件上传接口（带业务）
     *
     * @param file 上传的文件
     * @param fileParam 附件参数
     * @return 文件详情
     * @throws Exception
     */
    @Override
    public SysFile uploadFile(MultipartFile file, FileParam fileParam) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        String url = minioPath + "/" + this.minioConfig.getBucketName() + "/" + fileName;
        SysFile sysFile = new SysFile();
        sysFile.setBusinessId(fileParam.getBusinessId());
        sysFile.setBusinessName(fileParam.getBusinessName());
        sysFile.setFileUrl(fileName);
        sysFile.setFileFullUrl(url);
        sysFile.setShowFileName(fileParam.getShowFileName());
        sysFile.setActFileName(file.getOriginalFilename());
        sysFile.setFileSize(file.getSize());
        sysFile.setFileType(fileParam.getFileType());
        sysFile.setRemark(fileParam.getRemark());
        sysFile.setDeptId(fileParam.getDeptId());
        sysFile.setDeptName(fileParam.getDeptName());

        BaseEntityUtils.suppleCreateParams(sysFile);

        int i = sysFileMapper.insertSysFile(sysFile);

        if(i == 0) {
            throw new Exception("附件存储错误!");
        }

        return sysFile;
    }

    /**
     * 批量上传
     * @param files 上传的文件
     * @param fileParam 文件参数
     * @return
     */
    @Override
    public List<SysFile> uploadFiles(MultipartFile[] files, FileParam fileParam) {
        return null;
    }

    /**
     * minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName;
    }
}
