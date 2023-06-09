package pe.com.centro.modules.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.Role;

import java.util.Map;

import static pe.com.centro.utils.CognitoUtils.getAuthenticatedUser;
import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;
import static pe.com.centro.utils.Serializer.deserialize;
import static pe.com.centro.utils.Serializer.serialize;

@Slf4j
public class PostUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to post a user");

        User currentUser = getAuthenticatedUser(input, context);

        Map<String, String> params = input.getQueryStringParameters();

        if (params == null || !params.containsKey("option")) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("Es necesario definir una opción")));
        }

        String body = input.getBody();

        if (body == null) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("No se proporcionó información de usuario para la creación")));
        }

        User user = deserialize(body, User.class);
        UserDAO dao = new UserDAOImpl();

        switch (params.get("option")) {
            case "user":
                if (!Role.ADMINISTRATOR.equals(currentUser.getRole())) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tiene permisos suficientes para acceder a este recurso")));
                }
                dao.save(user);
                break;
            case "hm-rule":
                if (!(Role.ADMINISTRATOR.equals(currentUser.getRole()) || Role.ORGANIZATIONAL_EFFECTIVITY.equals(currentUser.getRole()))) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tiene permisos suficientes para acceder a este recurso")));
                }
                dao.saveHM(user);
                break;
            default:
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(400)
                        .withBody(serialize(new ErrorResponse("No se proporcionó una opción válida")));
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(201)
                .withBody(serialize(Map.of("message", "Created Successfully")));
    }
}
