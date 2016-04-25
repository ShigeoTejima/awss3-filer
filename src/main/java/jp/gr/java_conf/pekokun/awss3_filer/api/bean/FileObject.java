package jp.gr.java_conf.pekokun.awss3_filer.api.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FileObject {

    private String path;
    private String owner;
    private long size;
    private LocalDateTime lastModifiedAt;

    public FileObject() {
    }

    public FileObject(String path, String owner) {
        this(path, owner, 0);
    }

    public FileObject(String path, String owner, long size) {
        this(path, owner, size, LocalDateTime.now());
    }

    public FileObject(String path, String owner, long size, LocalDateTime lastModifiedAt) {
        this.path = path;
        this.owner = owner;
        this.size = size;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getPath() {
        return path;
    }

    public String getOwner() {
        return owner;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

}
