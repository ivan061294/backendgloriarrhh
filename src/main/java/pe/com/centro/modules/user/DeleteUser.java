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

import static pe.com.centro.utils.CognitoUtils.deleteUser;
import static pe.com.centro.utils.CognitoUtils.getAuthenticatedUser;
import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;
import static pe.com.centro.utils.Serializer.serialize;

@Slf4j
public class DeleteUser implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to delete a user or disable it");

        Map<String, String> params = input.getQueryStringParameters();

        if (params == null || !params.containsKey("option")) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("La opción no ha sido definida")));
        }

        User currentUser = getAuthenticatedUser(input, context);
        UserDAO dao = new UserDAOImpl();
        String option = params.get("option");
        String employeeCode = input.getPathParameters().get("code");

        switch (option) {
            case "user":
                if (!Role.ADMINISTRATOR.equals(currentUser.getRole())) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tienes permisos suficientes para acceder a este recurso")));
                }
                dao.delete(employeeCode);
                // TODO: delete from cognito
                deleteUser("something");
                break;
            case "hm-rule":
                if (!(Role.ADMINISTRATOR.equals(currentUser.getRole())|| Role.ORGANIZATIONAL_EFFECTIVITY.equals(currentUser.getRole()))) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tienes permisos suficientes para acceder a este recurso")));
                }
                if (!(params.containsKey("societyCode") && params.containsKey("divisionCode") && params.containsKey("subdivisionCode"))) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(400)
                            .withBody(serialize(new ErrorResponse("No se proporcionó los valores de sociedad, división y subdivisión")));
                }
                dao.deleteHM(params.get("societyCode"), params.get("divisionCode"), params.get("subdivisionCode"), employeeCode);
                break;
            default:
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(400)
                        .withBody(serialize(new ErrorResponse("Opción no válida")));
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(serialize(Map.of("message", "Created Successfully")));
    }
}
