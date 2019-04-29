package com.demo;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ALIOSSTest {
    public static void main(String[] args) {
        try {
            // Endpoint以杭州为例，其它Region请按实际情况填写。
            String endpoint = "http://oss-cn-beijing.aliyuncs.com";
            // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
            String accessKeyId = "accessKeyId";
            String accessKeySecret = "accessKeySecret";
            String bucketName = "bucketName";
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 创建存储空间。
            // 关闭OSSClient。
            String objectName = "transfer_test/";
            String Prefix = "cloud_tray/";
            ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMaxKeys(1000).withPrefix(Prefix));
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary s : sums) {
                String name = s.getKey().toString();
                System.out.println(name);
                if (!name.endsWith("/")) {
                    String[] paths = StringUtils.split(name, "/");
                    String fileName = paths[paths.length - 1];
                    StringBuffer buf = new StringBuffer();
                    StringBuffer bufPath = new StringBuffer();
                    for (int i = 0; i < paths.length - 1; i++) {
                        if (i != paths.length - 2) {
                            bufPath.append(paths[i]).append("/");
                        } else {
                            bufPath.append(paths[i]);
                        }
                    }
                    File file = new File("D:/data/" + bufPath.toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File downFIle = new File("D:/data/" + bufPath.toString() + "/" + paths[paths.length - 1]);

                    GetObjectRequest request = new GetObjectRequest(bucketName, name);
                    // 设置限定条件。
                    request.setModifiedSinceConstraint(new Date());
                    // 下载OSS文件到本地文件。
                    ossClient.getObject(new GetObjectRequest(bucketName, name), downFIle);
                }
            }
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}