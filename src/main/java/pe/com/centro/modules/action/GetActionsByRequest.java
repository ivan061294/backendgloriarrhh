package pe.com.centro.modules.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.Action;
import pe.com.centro.utils.Serializer;

import java.util.List;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetActionsByRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a list of actions by request");
        Long id = Long.parseLong(input.getPathParameters().get("id"));

        ActionDAO dao = new ActionDAOImpl();
        List<Action> actions = dao.findAllByRequestId(id);

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(actions));
    }
}
