package pe.com.centro.modules.AlldataForm;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.AllModelForm;
import pe.com.centro.utils.Serializer;


import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetAllDataForm implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a list of alldataform");

        // Business Logic Processing
        AllDataFormDAO dao = new AllDataFormDAOImpl();
        AllModelForm alldataform = dao.getAll();

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(alldataform));
    }
    
    
}
