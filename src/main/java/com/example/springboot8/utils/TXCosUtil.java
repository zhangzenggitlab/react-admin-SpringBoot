package com.example.springboot8.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 腾讯云cos工具类
 */

@Configuration
public class TXCosUtil {
    @Value("${TxCos.secretId}")
    private String secretId;
    @Value("${TxCos.secretKey}")
    private String secretKey;
    @Value("${TxCos.bucket}")
    private String bucket;
    @Value("${TxCos.region}")
    private String region;

    @Value("${TxCos.url}")
    private String url;

    @Value("${TxCos.zipUploadPath}")
    private String zipUploadPath;

    @Value("${TxCos.uploadPath}")
    private String uploadPath;

    @Autowired
    private GetUserIdByToken getUserIdByToken;

    public String upload(MultipartFile file) {

        // 初始化cos客户端
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, clientConfig);
        try {
            String path = uploadPath + "/" + getUserIdByToken.getUserIdByToken() + "/";
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf(".") + 1);
            // 生成唯一文件名，当前时间 + 随机UUID + 文件类型
            String fileName = path + UUID.randomUUID() + "." + suffix;

            // 上传文件到cos
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, metadata);
            cosClient.putObject(putObjectRequest);
            inputStream.close();

            // 返回文件在cos上的访问url
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("上传失败");
        } finally {
            // 关闭cos客户端
            cosClient.shutdown();
        }
    }

    public String modelUpload(MultipartFile file) {

        // 初始化cos客户端
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {

            String path = zipUploadPath + "/" + getUserIdByToken.getUserIdByToken() + "/";
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf(".") + 1);
            // 生成唯一文件名，当前时间 + 随机UUID + 文件类型

            UUID uuid = UUID.randomUUID();
            String fileName = path + uuid + "." + suffix;

            // 上传文件到cos
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            InputStream inputStream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, metadata);
            cosClient.putObject(putObjectRequest);
            inputStream.close();

            // 返回文件在cos上的访问url
            return uuid.toString();


        } catch (IOException e) {
            throw new RuntimeException("上传失败");
        } finally {
            // 关闭cos客户端
            cosClient.shutdown();
        }
    }


}
