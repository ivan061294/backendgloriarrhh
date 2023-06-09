package pe.com.centro.modules.society;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.AllModelForm;
import pe.com.centro.domain.Society;
import pe.com.centro.utils.Serializer;


import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

import java.util.Map;

@Slf4j
public class GetSocieties implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a list of alldata");
        Map<String, String> parameters = input.getQueryStringParameters();
        log.info("parameters get all data form {}",parameters);
        String bukrs="";
        
        // if (parameters == null) {
        //     return new APIGatewayProxyResponseEvent()
        //         .withHeaders(DEFAULT_HEADERS)
        //         .withStatusCode(400)
        //         .withBody(Serializer.serialize("Society Code no detectado!!"));
        // }
            bukrs = parameters==null?"":parameters.get("societyCode")==null?"":parameters.get("societyCode");
            log.info("bukrs {}", bukrs);
            // if (bukrs==null||bukrs.isEmpty()) {
            //     return new APIGatewayProxyResponseEvent()
            //     .withHeaders(DEFAULT_HEADERS)
            //     .withStatusCode(400)
            //     .withBody(Serializer.serialize("Society Code no detectado!!"));
            // }
        

        // Business Logic Processing
        SocietyDAO dao = new SocietyDAOImpl();
        AllModelForm societies = dao.getAll(bukrs);
        log.info("socities normal: \n" +  societies.toString());
        log.info("serializado:    \n"+ Serializer.serialize(societies));

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(societies));
    }
}
