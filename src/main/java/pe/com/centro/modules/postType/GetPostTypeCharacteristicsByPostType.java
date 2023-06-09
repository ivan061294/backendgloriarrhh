package pe.com.centro.modules.postType;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.utils.Serializer;

import java.util.List;
import java.util.Map;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetPostTypeCharacteristicsByPostType implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST Request to get a list of characteristics by post type");
        Long postTypeId = Long.parseLong(input.getPathParameters().get("id"));

        PostTypeDAO dao = new PostTypeDAOImpl();
        Map<String, List<Map<String, String>>> postTypeCharacteristics = dao
                .findCharacteristicsByPostTypeId(postTypeId);

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(postTypeCharacteristics));
    }
}
