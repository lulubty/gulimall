package com.ljf.gulimall.thirdParty;


import com.aliyun.oss.OSS;
        import com.aliyun.oss.OSSClient;
        import com.aliyun.oss.OSSClientBuilder;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTest {



    OSSClient ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-hongkong.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI5tKqD9sr12gWJNcZ6Usj";
        String accessKeySecret = "eTbXWrCqg1cCo4vIwaAmk0Omn5txwP";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //上传文件流。
        InputStream inputStream = new FileInputStream("D:\\BaiduNetdiskDownload\\Guli Mall\\分布式基础\\资源\\pics\\7ae0120ec27dc3a7.jpg");
        ossClient.putObject("ljf-gulimall--images", "time3.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功.");
    }

}