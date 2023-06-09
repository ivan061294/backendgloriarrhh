package pe.com.centro.modules.society;
import pe.com.centro.utils.Serializer;


import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostConfig implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {@Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.info("imput body{}",input.getBody());
        SocietyDAO dao = new SocietyDAOImpl();
        if(!dao.update(input.getBody())){
            return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(405)
                .withBody(Serializer.serialize("error en actualizacion"));
        }
        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize("exito"));
    }
    
}
