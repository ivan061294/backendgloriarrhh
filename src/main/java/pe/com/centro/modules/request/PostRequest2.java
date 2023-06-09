package pe.com.centro.modules.request;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.Headers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.IOUtils;

import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.FileObject;
import pe.com.centro.utils.MailService;
import pe.com.centro.utils.Serializer;
import pe.com.centro.utils.StorageService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import static pe.com.centro.utils.Constants.*;

@Slf4j
public class PostRequest2 implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST Request to post a new requirement request");

        log.info("input");
        log.info(input.getBody());
        log.info("headers {}", input.getHeaders());
        String contentType = (input.getHeaders().get(Headers.CONTENT_TYPE)) != null
                ? input.getHeaders().get(Headers.CONTENT_TYPE)
                : input.getHeaders().get("content-type");

        try {


            this.parseFormBody(input.getBody().getBytes(), contentType,333);

            
            

            // Store the request into db

            // Response Parsing
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(201)
                    .withBody("exito");
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(Serializer.serialize(new ErrorResponse("No se encontró contenido")));
        }
    }

    private String parseFormBody(byte[] bodyBytes, String contentType,long RequestId)
            throws IOException, InvocationTargetException, IllegalAccessException {
        // Check typical form-data body: https://stackoverflow.com/a/23517227/14978149
        //Request request = new Request();
        // Content-Type: multipart/form-data; boundary=xxx
        MultipartStream ms = new MultipartStream(
                new ByteArrayInputStream(bodyBytes),
                contentType.split("=")[1].getBytes(),
                bodyBytes.length,
                null);
        boolean next = ms.skipPreamble();
        List<FileObject> files = new ArrayList<>();
        StorageService storageService = new StorageService(
                    AWS_S3_BATCH_FILES_BUCKET_NAME);

        while (next) {
            // Content-Disposition: form-data; name="customFileFormField";
            // filename="customFile.txt"
            // Content-Type: <application/pdf|text/plain|...>
            String headers = ms.readHeaders();
            int start = headers.indexOf("Content-Type: ") + 14;

            log.info("ms header   " + headers);
            log.info("header index   " + headers.indexOf("Content-Type: "));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (start == 13) {
                int fieldNameStart = headers.indexOf("name=\"") + 6;
                String fieldName = headers.substring(fieldNameStart, headers.indexOf("\"", fieldNameStart));
                ms.readBodyData(baos);
                String fieldContent = new String(new ByteArrayInputStream(baos.toByteArray()).readAllBytes(),
                        StandardCharsets.UTF_8);
                        log.info("fielname {},fieldContent {}",fieldName,fieldContent);

                //Parser.setProperty(request, fieldName, fieldContent);
            } else {
                log.debug("File Field found!");
                log.info("file headers {}",headers);
                int fieldFilenameStart = headers.indexOf("filename=\"") + 10;
                String fieldFilename = headers.substring(fieldFilenameStart, headers.indexOf("\"", fieldFilenameStart));
                String fileContentType = headers.substring(start, headers.indexOf("\n", start));
               
                ms.readBodyData(baos);
                String fieldContent = new String(new ByteArrayInputStream(baos.toByteArray()).readAllBytes(),
                StandardCharsets.UTF_8);
                log.debug("File Content Type: {},fieldcontentlenthstring {} ,fielcontent {}", fileContentType,fieldContent.length(),fieldContent);
                InputStream imput = IOUtils.toInputStream(fieldContent);

                log.info("fieldname");
                log.info(fieldFilename);
                log.info("fielcontent");

                try {
                    // log.info("baos {}", baos);
                    String keyFile = storageService.upload2(
                            imput,
                            fieldFilename.split("\\.")[1],
                            fileContentType,fieldFilename,RequestId,baos.toByteArray().length);
                    FileObject file = new FileObject();
                    file.setNameArchivo(fieldFilename);
                    file.setRutaArchivo(keyFile);
                    files.add(file);

                    log.info("archivo subido  key {}", keyFile);

                } catch (Exception e) {
                    log.info("Archivo no subido error {}", e);
                }

            }

            next = ms.readBoundary();
        }
        //request.setFilesObject(files);
        return "Exito";
    }
}
