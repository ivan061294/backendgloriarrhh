package pe.com.centro.modules.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.Role;
import pe.com.centro.utils.Serializer;

import java.util.Map;

import static pe.com.centro.utils.CognitoUtils.getAuthenticatedUser;
import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;
import static pe.com.centro.utils.Serializer.serialize;

@Slf4j
public class PatchUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to update an user role");

        User currentUser = getAuthenticatedUser(input, context);

        Map<String, String> params = input.getQueryStringParameters();
        if (params == null || !params.containsKey("option")) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("No se proporcionó una opción")));
        }

        String body = input.getBody();

        User user = Serializer.deserialize(body, User.class);
        UserDAO dao = new UserDAOImpl();

        if (body == null) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("No se proporcionó información de usuario para la actualización")));
        }

        switch (params.get("option")) {
            case "user":
                if (!Role.ADMINISTRATOR.equals(currentUser.getRole())) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tiene permisos suficientes para acceder a este recurso")));
                }
                dao.update(user);
                break;
            case "hm-rule":
                if (!(Role.ADMINISTRATOR.equals(currentUser.getRole()) || Role.ORGANIZATIONAL_EFFECTIVITY.equals(currentUser.getRole()))) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tiene permisos suficientes para acceder a este recurso")));
                }
                if (!(params.containsKey("prevSociety") && params.containsKey("prevDivision") && params.containsKey("prevSubdivision"))) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(400)
                            .withBody(serialize(new ErrorResponse("No se proporcionó los valores de sociedad, división y subdivisión previos a la actualización")));
                }
                dao.updateHM(user, params.get("prevSociety"), params.get("prevDivision"), params.get("prevSubdivision"));
                break;
            default:
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(400)
                        .withBody(serialize(new ErrorResponse("La opción proveída no es válida")));
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(serialize(Map.of("message", "Updated Successfully")));
    }
}
