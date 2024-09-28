package com.brian.nekoo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Response;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String uuidFileName) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(uuidFileName)
            .metadata(metadata)
            .contentType(file.getContentType())
            .build();

        S3Response response = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
        return uuidFileName;
    }

    public String deleteFile(String uuidFileName) throws IOException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(uuidFileName)
            .build();

        s3Client.deleteObject(deleteObjectRequest);
        return uuidFileName;
    }

    public String getFileUrl(String fileName) {
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName));
        return url.toString();
    }
}
