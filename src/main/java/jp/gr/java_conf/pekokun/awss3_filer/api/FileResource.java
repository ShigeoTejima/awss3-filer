package jp.gr.java_conf.pekokun.awss3_filer.api;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jp.gr.java_conf.pekokun.awss3_filer.api.bean.FileObject;
import jp.gr.java_conf.pekokun.awss3_filer.service.AwsS3Service;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@RequestScoped
@Path("files")
public class FileResource {

    private static final String BUCKET_NAME = "shigeo.tejima-test";

    @Inject
    private AwsS3Service awsS3Service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileObject> getFiles(@QueryParam("path") String path) {
        System.out.println(String.format("FileResource#getFiles(String) - path: [%s]", path));

        return awsS3Service.getFiles(BUCKET_NAME, path).stream()
                .map(fo -> new FileObject(fo.getPath(), fo.getOwner(), fo.getSize(), fo.getLastModified()))
                .collect(Collectors.toList());
    }

    @Path("file")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FileObject getFile(@QueryParam("path") String path) {
        jp.gr.java_conf.pekokun.awss3_filer.model.FileObject fo = awsS3Service.getFile(BUCKET_NAME, path);

        return new FileObject(fo.getPath(), fo.getOwner(), fo.getSize(), fo.getLastModified());
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("path") String path) {
        System.out.println(String.format("FileResource#delete(String) - path: [%s]", path));

        awsS3Service.deleteFile(BUCKET_NAME, path);

        return Response.noContent().build();
    }

    @Path("download")
    @GET
//    @Produces("application/force-download")
    public Response download(@QueryParam("path") String path) {
        jp.gr.java_conf.pekokun.awss3_filer.model.FileObject fo =  awsS3Service.getFileContent(BUCKET_NAME, path);

        return Response.ok(fo.getContentStream(), "application/force-download")
                .header(HttpHeaders.CONTENT_LENGTH, fo.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fo.getName()))
                .build();

    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(
            @FormDataParam("file") InputStream file,
            @FormDataParam("file") FormDataContentDisposition fileDisposition,
            @FormDataParam("path") String path,
            @FormDataParam("filename") String filename) {

        jp.gr.java_conf.pekokun.awss3_filer.model.FileObject fileObject = new jp.gr.java_conf.pekokun.awss3_filer.model.FileObject(BUCKET_NAME, path, "owner");
        fileObject.setContentStream(file);

        awsS3Service.registerFile(BUCKET_NAME, fileObject);

        return Response.status(Response.Status.CREATED).build();
    }
}
