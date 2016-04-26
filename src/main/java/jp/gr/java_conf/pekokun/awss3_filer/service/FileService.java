package jp.gr.java_conf.pekokun.awss3_filer.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import jp.gr.java_conf.pekokun.awss3_filer.model.FileObject;

@Dependent
@Named
public class FileService {

    @Inject
    private AwsS3Service awsS3Service;

    public List<FileObject> getFiles(String diskId, String path) {
        if (!FileObject.isFolder(path)) {
            throw new IllegalArgumentException("path must be folder.");
        }
        String bucketName = getBucketName(diskId);
        if (Objects.isNull(bucketName) || bucketName.isEmpty()) {
            throw new IllegalArgumentException("not specified bucketName.");
        }

        return awsS3Service.getFiles(bucketName, path);
    }

    public FileObject getFile(String diskId, String path) {
        if (Objects.isNull(path) || path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty.");
        }
        String bucketName = getBucketName(diskId);
        if (Objects.isNull(bucketName) || bucketName.isEmpty()) {
            throw new IllegalArgumentException("not specified bucketName.");
        }

        return awsS3Service.getFile(bucketName, path);
    }

    public void deleteFile(String diskId, String path) {
        if (Objects.isNull(path) || path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty.");
        }
        String bucketName = getBucketName(diskId);
        if (Objects.isNull(bucketName) || bucketName.isEmpty()) {
            throw new IllegalArgumentException("not specified bucketName.");
        }

        awsS3Service.deleteFile(bucketName, path);
    }

    public FileObject getFileContent(String diskId, String path) {
        if (Objects.isNull(path) || path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty.");
        }
        String bucketName = getBucketName(diskId);
        if (Objects.isNull(bucketName) || bucketName.isEmpty()) {
            throw new IllegalArgumentException("not specified bucketName.");
        }

        return awsS3Service.getFileContent(bucketName, path);
    }

    public void createFolder(String diskId, String path) {
        if (!FileObject.isFolder(path)) {
            throw new IllegalArgumentException("path must be folder.");
        }
        String bucketName = getBucketName(diskId);
        if (Objects.isNull(bucketName) || bucketName.isEmpty()) {
            throw new IllegalArgumentException("not specified bucketName.");
        }

        
        FileObject folder = new FileObject(bucketName, path, "owner", 0, LocalDateTime.now(), new ByteArrayInputStream(new byte[0]));
        awsS3Service.registerFile(bucketName, folder);
    }

    public void registerFile(String diskId, String parentPath, String fileName, InputStream contentStream) {
        String bucketName = getBucketName(diskId);
        if (Objects.isNull(bucketName) || bucketName.isEmpty()) {
            throw new IllegalArgumentException("not specified bucketName.");
        }
        if (!FileObject.isFolder(parentPath)) {
            throw new IllegalArgumentException("parentPath must be folder.");
        }
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException("fileName must not be empty.");
        }
        if (Objects.isNull(contentStream)) {
            throw new IllegalArgumentException("contentStream must not be empty.");
        }

        Content content = toContent(contentStream);
        FileObject fileObject = new FileObject(bucketName, parentPath.concat(fileName), "owner", content.size, LocalDateTime.now(), content.stream);
        awsS3Service.registerFile(bucketName, fileObject);
    }

    // TODO specify from mapping config.
    private String getBucketName(String diskId) {
        return "shigeo.tejima-test";
    }

    private Content toContent(InputStream inputStream) {
        ByteArrayInputStream stream = toByteArrayInputStream(inputStream);
        long size = stream.skip(Long.MAX_VALUE);
        stream.reset();

        return new Content(size, stream);
    }

    private ByteArrayInputStream toByteArrayInputStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length = 0;
        byte[] buff = new byte[4096];
        try {
            while((length = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, length);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static class Content {
        long size;
        InputStream stream;

        Content(long size, InputStream stream) {
            this.size = size;
            this.stream = stream;
        }

    }
}
