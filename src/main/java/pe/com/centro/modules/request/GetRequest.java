package pe.com.centro.modules.request;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.Request;
import pe.com.centro.utils.Serializer;

import java.util.List;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a request by id");

        Long id = Long.parseLong(input.getPathParameters().get("id"));

        RequestDAO dao = new RequestDAOImpl();

        List<Request> request = dao.findById();

        if (request.isEmpty()) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(404)
                    .withBody(Serializer.serialize(new ErrorResponse("No se encontró la solicitud")));
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(request));
    }
}
