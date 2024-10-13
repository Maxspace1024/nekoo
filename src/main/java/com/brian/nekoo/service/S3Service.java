package com.brian.nekoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${s3.bucket.name}")
    private String bucketName;

    private static final String PATH = "assets/";

    public String uploadFile(MultipartFile file, String uuidFileName) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(PATH + uuidFileName)
            .metadata(metadata)
            .contentType(file.getContentType())
            .build();

        S3Response response = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
        return uuidFileName;
    }

    public String uploadFileWithProgress(MultipartFile file, String uuidFileName, String progressTopic) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(PATH + uuidFileName)
            .metadata(metadata)
            .contentType(file.getContentType())
            .build();

        Consumer<Integer> progressConsumer = progress -> {
            Map<String, Object> progressMap = new HashMap<>();
            progressMap.put("progress", progress);
            messagingTemplate.convertAndSend(progressTopic, progressMap);
        };

        ProgressTrackingInputStream inputStream = new ProgressTrackingInputStream(file.getInputStream(), file.getSize(), progressConsumer);
        RequestBody requestBody = RequestBody.fromInputStream(inputStream, file.getSize());

        PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
        return uuidFileName;
    }

    public String deleteFile(String uuidFileName) throws IOException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(PATH + uuidFileName)
            .build();

        s3Client.deleteObject(deleteObjectRequest);
        return uuidFileName;
    }

    public String getFileUrl(String fileName) {
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(PATH + fileName));
        return url.toString();
    }

    static class ProgressTrackingInputStream extends InputStream {
        private final InputStream wrapped;
        private final long totalBytes;
        private long bytesRead;
        private final Consumer<Integer> progressConsumer;

        public ProgressTrackingInputStream(InputStream wrapped, long totalBytes, Consumer<Integer> progressConsumer) {
            this.wrapped = wrapped;
            this.totalBytes = totalBytes;
            this.bytesRead = 0;
            this.progressConsumer = progressConsumer;
        }

        @Override
        public int read() throws IOException {
            int b = wrapped.read();
            if (b != -1) {
                bytesRead++;
                reportProgress();
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = wrapped.read(b, off, len);
            if (bytesRead != -1) {
                this.bytesRead += bytesRead;
                reportProgress();
            }
            return bytesRead;
        }

        private void reportProgress() {
            Integer progress = (int) ((float) bytesRead / totalBytes * 100);
            progressConsumer.accept(progress);
        }
    }
}
