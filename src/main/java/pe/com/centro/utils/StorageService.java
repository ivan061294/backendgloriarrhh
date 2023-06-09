package pe.com.centro.utils;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.http.entity.ContentType;

/**
 * Storage Service for AWS S3
 * <br/>
 * This is only used for store files, not processing
 * <pre>Note: Set appropriate Lambda Role to get access to S3 operations</pre>
 */
@Slf4j
public class StorageService {
    private final String bucketName;

    // 200 MB for max upload file size
    private static final long AWS_S3_MAX_THRESHOLD = 200 * 1024 * 1025;

    // By default, the max upload threads is 10, but we use 5 instead
    private static final int AWS_MAX_UPLOAD_THREADS = 5;

    // The expiration time is 1 hour
    private static final long EXPIRATION_TIME_MILLISECONDS = 1000 * 60 * 60;


    private final AmazonS3 s3Client;

    private final TransferManager transferManager;

    /**
     * Creates an instance of storage service
     *
     * @param bucketName the bucket that is going to be managed
     */
    public StorageService(String bucketName) {
        this.bucketName = bucketName;
        //BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIA4AMNYPKU6KIQZIWT", "wA5Yteoj88in7MO4i8Gh4uRha53qrCOs14iUJ2pn");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIA4AMNYPKU6KIQZIWT", "wA5Yteoj88in7MO4i8Gh4uRha53qrCOs14iUJ2pn");
        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(this.s3Client)
                .withMultipartUploadThreshold(AWS_S3_MAX_THRESHOLD)
                .withExecutorFactory(() -> Executors.newFixedThreadPool(AWS_MAX_UPLOAD_THREADS))
                .build();
    }

    /**
     * Uploads a file to Amazon S3
     *
     * @param fis         the file input stream
     * @param extension   the file extension (e.g. '.txt', '.html', etc.)
     * @param contentType the file content type (e.g. 'text/plain', 'text/html', etc.)
     * @return the file key to be stored in database
     */
    public String upload(ByteArrayInputStream fis, String extension, String contentType,String nameFile,long requestId,int contentLength) {
        log.debug("Request to upload a file");
        
        String fileKey =  "archivosrequerimientos/requerimiento#"+requestId+"/"+nameFile;
      
               try {
       ObjectMetadata metadata = new ObjectMetadata();
       log.info("filekey {}",fileKey);
       log.info("bucket {}",bucketName);
       log.info("content type {} , content length {}", contentType,contentLength);
       //metadata.setContentType("application/mspowerpoint");
       //metadata.setContentLength(contentLength);
       //metadata.setContentLength(fis.available());
      // metadata.setCacheControl("public, max-age=31536000");
       PutObjectRequest request = new PutObjectRequest(bucketName, fileKey, fis, metadata);
           this.s3Client.putObject(request);
           log.info("subiendo archivo upload {}",request.toString());
             
          } catch (Exception e) {
              log.error("Error uploading file", e);
         }

       return fileKey;
    }


    public String upload3(ByteArrayInputStream fis, String extension, String contentType,String nameFile,long requestId,int contentLength) {
        log.debug("Request to upload a file");
        
        String fileKey =  "archivosrequerimientos/requerimiento#"+requestId+"/"+nameFile;
      
        try {

            // Base64.Encoder enc = Base64.getEncoder();
            // byte[] encbytes = enc.encode(fis.readAllBytes());
            // for (int i = 0; i < encbytes.length; i++)
            // {
            //     System.out.printf("%c", (char) encbytes[i]);
            //     if (i != 0 && i % 4 == 0)
            //         System.out.print(' ');
            // }
            // Base64.Decoder dec = Base64.getDecoder();
            // byte[] barray2 = dec.decode(encbytes);
            // InputStream fis2 = new ByteArrayInputStream(barray2);


            ObjectMetadata metadata = new ObjectMetadata();
            log.info("filekey {}",fileKey);
            log.info("bucket {}",bucketName);
            log.info("content type {} , content length {}", contentType,contentLength);
            //metadata.setContentType("application/mspowerpoint");
            metadata.setContentLength(contentLength);
            //metadata.setContentLength(fis.available());
            // metadata.setCacheControl("public, max-age=31536000");


            PutObjectRequest request = new PutObjectRequest(bucketName, fileKey, fis, metadata);
                this.s3Client.putObject(request);
                log.info("subiendo archivo upload {}",request.toString());
                
        } catch (Exception e) {
            log.error("Error uploading file", e);
        }

       return fileKey;
    }


    public String upload2(InputStream fis, String extension, String contentType,String nameFile,long requestId,int contentLength) {
        log.debug("Request to upload a file");
        
         String fileKey =  "archivosrequerimientos/requerimiento#"+requestId+"/"+nameFile;
       
                try {
        
        log.info("filekey {}",fileKey);
        log.info("bucket {}",bucketName);
        log.info("content type {} , content length {}", contentType,contentLength);
        ObjectMetadata metadata = new ObjectMetadata();
        //metadata.setContentType(contentType.replace(" ", ""));
        metadata.setContentLength(contentLength);
        //metadata.setCacheControl("public, max-age=31536000");
        PutObjectRequest request = new PutObjectRequest(bucketName, fileKey, fis, metadata);
            //this.s3Client.putObject(request); //new ByteArrayInputStream(Base64.getDecoder().decode(fis.readAllBytes()))
            TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3Client)
            .withMultipartCopyThreshold((long)(5*1024*1025)).build();
            log.info("subiendo archivo upload {}",request.toString());
            Upload upload=tm.upload(request);
            upload.waitForCompletion();
            log.info("Archivo subido wait");

              
           } catch (Exception e) {
               log.error("Error uploading file", e);
               fileKey=e.getMessage();
          }

        return fileKey;
    }

    public String getFileUri(String fileKey) {
        // TODO: to download or use a pre-signed url?

        // Example of pre-signed URL
        log.info("fileKey {}",fileKey);
        Date expiration = new Date();
        long expirationLimit = expiration.getTime() + EXPIRATION_TIME_MILLISECONDS;
        expiration.setTime(expirationLimit);
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(this.bucketName, fileKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        URL preSignedURL = this.s3Client.generatePresignedUrl(request);
        log.info("presignedUrl {}", preSignedURL);

        return preSignedURL.toString();
    }

    public InputStream descargarArchivo(String claveArchivo) {

        S3Object s3Object = null;
        InputStream readResponse=null;

        try {
            // Get an object and print its contents.
            System.out.println("Descargando el archivo CVS");
            s3Object = this.s3Client.getObject(new GetObjectRequest(this.bucketName, claveArchivo));
            log.info("Tamaño de archivo a descargar (bytes): "
                    + String.valueOf(s3Object.getObjectMetadata().getContentLength()));
            //
            readResponse= s3Object.getObjectContent();
            //
        } catch (Exception e) {
            log.error("ERROR: ",e);
        }
        return readResponse;
    }
}
