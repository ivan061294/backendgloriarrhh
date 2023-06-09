package pe.com.centro.modules.load;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.utils.Constants;

@Slf4j
public class LoadData implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event input, Context context) {

        LoadDataDao dao = new LoadDataDaoImpl();
        String bucketName = input.getRecords().get(0).getS3().getBucket().getName();
        String key = input.getRecords().get(0).getS3().getObject().getKey();
        log.info("imput s3 object {}",input);
        log.info("imput s3 string {}",input.toString());
        if (Constants.AWS_S3_BATCH_FILES_BUCKET_NAME.equals(bucketName)) {
            log.debug("Bucket's name matched");
        }
        log.debug("S3 event for load a flat file: Bucket {} - Key {}", bucketName, key);
        String archivo= dao.ObtenerArchivoEventoS3(key);
        log.info("archivodescargado", archivo);
        return "Success!";
    }
}
