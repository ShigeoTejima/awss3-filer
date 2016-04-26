package jp.gr.java_conf.pekokun.awss3_filer.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import jp.gr.java_conf.pekokun.awss3_filer.model.FileObject;

@Dependent
@Named
public class AwsS3Service {

    public boolean doesBucketExist(String name) {
        return getAmazonS3().doesBucketExist(name);
    }

    public List<FileObject> getFiles(String bucketName, String path) {
        AmazonS3 amazonS3 = getAmazonS3();

        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(bucketName);
        request.setPrefix(path);
        request.setDelimiter("/");

        ObjectListing objectListing = amazonS3.listObjects(request);

        List<FileObject> fileObjects = new ArrayList<>();
        fileObjects.addAll(
            objectListing.getCommonPrefixes().stream()
                .filter(dir -> !Objects.equals(dir, path))
                .map(dir -> new FileObject(bucketName, dir))
                .collect(Collectors.toList())
        );
        fileObjects.addAll(
            objectListing.getObjectSummaries().stream()
               .filter(so -> !Objects.equals(so.getKey(), path))
               .map(so ->
                       new FileObject(
                           bucketName,
                           so.getKey(),
                           null,
                           so.getSize(),
                           LocalDateTime.ofInstant(so.getLastModified().toInstant(), ZoneOffset.systemDefault())))
               .collect(Collectors.toList())
        );

        return fileObjects;
    }

    public FileObject getFile(String bucketName, String path) {
        AmazonS3 amazonS3 = getAmazonS3();
        S3Object s3object = amazonS3.getObject(bucketName, path);
        return new FileObject(bucketName,
                              s3object.getKey(),
                              s3object.getObjectMetadata().getUserMetaDataOf("owner"),
                              s3object.getObjectMetadata().getContentLength(),
                              LocalDateTime.ofInstant(s3object.getObjectMetadata().getLastModified().toInstant(), ZoneOffset.systemDefault())
        );
    }

    public void deleteFile(String bucketName, String path) {
        getAmazonS3().deleteObject(bucketName, path);
    }

    public FileObject getFileContent(String bucketName, String path) {
        S3Object s3object = getAmazonS3().getObject(bucketName, path);
        return new FileObject(bucketName,
                              s3object.getKey(),
                              s3object.getObjectMetadata().getUserMetaDataOf("owner"),
                              s3object.getObjectMetadata().getContentLength(),
                              LocalDateTime.ofInstant(s3object.getObjectMetadata().getLastModified().toInstant(), ZoneOffset.systemDefault()),
                              s3object.getObjectContent());
    }

    public void registerFile(String bucketName, FileObject fileObject) {
        AmazonS3 amazonS3 = getAmazonS3();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileObject.getSize());
        objectMetadata.addUserMetadata("owner", fileObject.getOwner());
        amazonS3.putObject(bucketName, fileObject.getPath(), fileObject.getContentStream(), objectMetadata);
    }

    private AmazonS3 getAmazonS3() {
        String accessKey = " ... "; // TODO get config
        String accessKeySecret = " ... "; // TODO get config
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, accessKeySecret);
        return new AmazonS3Client(credentials);
    }
}
