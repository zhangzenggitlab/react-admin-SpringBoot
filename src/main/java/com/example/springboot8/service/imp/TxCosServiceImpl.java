package com.example.springboot8.service.imp;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.service.TxCosService;
import com.example.springboot8.utils.GetUserIdByToken;
import com.example.springboot8.utils.TXCosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class TxCosServiceImpl implements TxCosService {

    @Autowired
    private TXCosUtil txCosUtil;

    @Autowired
    private GetUserIdByToken getUserIdByToken;

    @Value("${TxCos.url}")
    private String url;

    @Value("${TxCos.uploadPath}")
    private String uploadPath;

    @Value("${TxCos.zipUploadPath}")
    private String zipUploadPath;

    @Override
    public ResponseResult upload(MultipartFile file) {
        return new ResponseResult(200, "上传成功", txCosUtil.upload(file));
    }

    public ResponseResult uploadModelZip(MultipartFile file) throws IOException {
        try {
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf(".") + 1);
            // 生成唯一文件名，当前时间 + 随机UUID + 文件类型
            String fileName = UUID.randomUUID() + "." + suffix;

            // 创建一个文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.close();

            // 解压缩zip文件
            ZipFile zipFile = new ZipFile(fileName);
            Enumeration zipFileEntries = zipFile.entries();

            String path = txCosUtil.modelUpload(file);

            String modelFile = "";                                          // 模型路径
            String zipUrl = zipUploadPath + "/" + getUserIdByToken.getUserIdByToken() + "/";                                             // zip压缩文件
            List<String> list = new ArrayList<>();                          // zip解压的文件

            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String name = entry.getName();

                // 读取文件
                if (name.contains("glb") || name.contains("gltf") || name.contains("stl") || name.contains("obj")) {
                    modelFile = entry.getName();

                }
                list.add(uploadPath + "/" + path + "/" + name);
            }

            zipFile.close();
            // 删除zip文件
            Files.delete(Paths.get(fileName));

            // 没有符合条件的数据，直接拒绝上传
            if ("".equals(modelFile)) {
                return new ResponseResult(400, "没有符合的模型文件", null);
            }

            // 返回结果
            Map<String, Object> map = new HashMap<>();
            map.put("file",uploadPath + "/" + path + "/" + modelFile);
            map.put("multiplefiles",list);
            map.put("zipUrl",zipUrl + path + ".zip");

            return new ResponseResult(200, "上传成功", map);
        } catch (IOException e) {
            System.out.println("异常" + e);

        }


        return new ResponseResult(200, "上传成功", 1);
    }

}
