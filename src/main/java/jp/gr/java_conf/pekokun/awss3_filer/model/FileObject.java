package jp.gr.java_conf.pekokun.awss3_filer.model;

import java.io.InputStream;
import java.time.LocalDateTime;

public class FileObject {

    private String bucketName;
    private String path;
    private String owner;
    private long size;
    private LocalDateTime lastModified;

    private InputStream contentStream;

    public static boolean isFolder(String path) {
        return (path != null && path.endsWith("/"));
    }

    public FileObject(String bucketName, String path) {
        this(bucketName, path, null, 0, LocalDateTime.now());
    }

    public FileObject(String bucketName, String path, String owner) {
        this(bucketName, path, owner, 0, LocalDateTime.now());
    }

    public FileObject(String bucketName, String path, String owner, long size, LocalDateTime lastModified) {
        this(bucketName, path, owner, size, lastModified, null);
    }

    public FileObject(String bucketName, String path, String owner, long size, LocalDateTime lastModified, InputStream contentStream) {
        this.bucketName = bucketName;
        this.path = path;
        this.owner = owner;
        this.size = size;
        this.lastModified = lastModified;
        this.contentStream = contentStream;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public InputStream getContentStream() {
        return contentStream;
    }

    public void setContentStream(InputStream contentStream) {
        this.contentStream = contentStream;
    }

    public String getName() {
        if (this.path == null || this.path.isEmpty()) {
            return "";
        }

        int endIndex = isFolder() ? this.path.length() - 1 : this.path.length();
        int index = this.path.lastIndexOf("/", endIndex - 1);
        return this.path.substring(index + 1, endIndex);
    }

    public boolean isFolder() {
        return isFolder(this.path);
    }

}
