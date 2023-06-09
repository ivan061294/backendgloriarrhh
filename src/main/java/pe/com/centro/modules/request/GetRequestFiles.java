package pe.com.centro.modules.request;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.FileObject;
import pe.com.centro.utils.Serializer;

import java.util.List;
import java.util.Map;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetRequestFiles implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a request by id");
        Map<String, String> params = input.getQueryStringParameters();
        RequestDAO dao = new RequestDAOImpl();
        String id=params.get("idrequest");
        log.info("idrequest:{}",id);
        long idrequest=Long.valueOf(id);
        List<FileObject> res=dao.getRutaFile(idrequest);
        log.info("res {}",res.toString());

        if (res.isEmpty()) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(404)
                    .withBody(Serializer.serialize(new ErrorResponse("No se encontró la solicitud")));
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(res));
    }
}