package pe.com.centro.modules.society;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.Society;
import pe.com.centro.utils.Serializer;

import java.util.Map;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

public class PatchSociety implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        String body = input.getBody();
        if (body == null) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(Serializer.serialize(new ErrorResponse("No se encontró un cuerpo en la petición.")));
        }

        Society society = Serializer.deserialize(body, Society.class);

        SocietyDAO dao = new SocietyDAOImpl();


        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(Map.of("message", "Updated Successfully.")));
    }
}
