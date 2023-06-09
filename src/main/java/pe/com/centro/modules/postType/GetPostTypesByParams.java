package pe.com.centro.modules.postType;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.PostType;
import pe.com.centro.utils.Serializer;

import java.util.List;
import java.util.Map;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetPostTypesByParams implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a list of posts by query parameters");

        PostTypeDAO dao = new PostTypeDAOImpl();

        Map<String, String> parameters = input.getQueryStringParameters();

        String physicalLocationCode = null;
        String costCenterCode = null;
        String organizationalUnitCode = null;
        Long managementId = null;
        String divisionCode = null;
        String societyCode = null;

        if (parameters != null) {
            physicalLocationCode = parameters.get("physicalLocationCode");
            costCenterCode = parameters.get("costCenterCode");
            organizationalUnitCode = parameters.get("organizationalUnitCode");
            if (parameters.get("managementId") != null) {
                managementId = Long.parseLong(parameters.get("managementId"));
            }
            divisionCode = parameters.get("divisionCode");
            societyCode = parameters.get("societyCode");
        }

        List<PostType> postTypes = dao
                .findAllByParams(societyCode, divisionCode, managementId, organizationalUnitCode, costCenterCode, physicalLocationCode);

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(postTypes));
    }
}
