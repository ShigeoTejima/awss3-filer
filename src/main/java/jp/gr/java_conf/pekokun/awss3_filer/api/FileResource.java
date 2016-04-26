package jp.gr.java_conf.pekokun.awss3_filer.api;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jp.gr.java_conf.pekokun.awss3_filer.api.bean.FileObjectBean;
import jp.gr.java_conf.pekokun.awss3_filer.model.FileObject;
import jp.gr.java_conf.pekokun.awss3_filer.model.User;
import jp.gr.java_conf.pekokun.awss3_filer.service.FileService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@RequestScoped
@Path("files")
public class FileResource {

    private static final String DISK_ID = "test";

    @Inject
    private User currentUser;

    @Inject
    private FileService fileService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileObjectBean> getFiles(@QueryParam("path") String path) {
        return fileService.getFiles(DISK_ID, path).stream()
                .map(fo -> new FileObjectBean(fo.getPath(), fo.getOwner(), fo.getSize(), fo.getLastModified()))
                .collect(Collectors.toList());
    }

    @Path("file")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FileObjectBean getFile(@QueryParam("path") String path) {
        FileObject fileObject = fileService.getFile(DISK_ID, path);

        return new FileObjectBean(fileObject.getPath(), fileObject.getOwner(), fileObject.getSize(), fileObject.getLastModified());
    }

    @DELETE
    public Response delete(@QueryParam("path") String path) {
        fileService.deleteFile(DISK_ID, path);
        return Response.noContent().build();
    }

    @Path("download")
    @GET
    public Response download(@QueryParam("path") String path) {
        try {
            FileObject fo =  fileService.getFileContent(DISK_ID, path);
            String downloadFileName = URLEncoder.encode(fo.getName(), "UTF-8");

            return Response.ok(fo.getContentStream(), "application/force-download")
                    .header(HttpHeaders.CONTENT_LENGTH, fo.getSize())
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", downloadFileName))
                    .build();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Path("folder")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createFolder(@FormParam("folderPath") String folderPath) {
        fileService.createFolder(folderPath, folderPath);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(
            @FormDataParam("file") InputStream file,
            @FormDataParam("file") FormDataContentDisposition fileDisposition,
            @FormDataParam("parentPath") String parentPath) {

        fileService.registerFile(DISK_ID, parentPath, fileDisposition.getFileName(), file);

        return Response.status(Response.Status.CREATED).build();
    }
}
