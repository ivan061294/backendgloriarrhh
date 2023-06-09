package pe.com.centro.modules.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.Role;
import pe.com.centro.utils.Page;

import java.util.HashMap;
import java.util.Map;

import static pe.com.centro.utils.CognitoUtils.getAuthenticatedUser;
import static pe.com.centro.utils.Constants.*;
import static pe.com.centro.utils.Serializer.serialize;

@Slf4j
public class GetUsersByParams implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a page of users");

        User user = getAuthenticatedUser(input, context);
        Map<String, String> params = input.getQueryStringParameters();

        int pageNumber = DEFAULT_PAGE_NUMBER;
        int pageSize = DEFAULT_PAGE_SIZE;
        Role role = null;

        if (params == null || !params.containsKey("option")) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("La opción no ha sido definida")));
        }

        String option = params.get("option");
        String fatherFamilyName = params.get("fatherFamilyName");
        String employeeCode = params.get("employeeCode");
        if (params.containsKey("page"))
            pageNumber = Integer.parseInt(params.get("page"));
        if (params.containsKey("size"))
            pageSize = Integer.parseInt(params.get("size"));
        if (params.containsKey("role"))
            role = Role.valueOf(params.get("role"));

        UserDAO dao = new UserDAOImpl();
        Page<User> page;

        switch (option) {
            case "all":
                if (!Role.ADMINISTRATOR.equals(user.getRole())) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tienes permisos suficientes para acceder a este recurso")));
                }
                page = dao.findAll(fatherFamilyName, employeeCode, role, pageNumber, pageSize);
                break;
            case "hm-rules":
                if (!(Role.ADMINISTRATOR.equals(user.getRole()) || Role.ORGANIZATIONAL_EFFECTIVITY.equals(user.getRole()))) {
                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(401)
                            .withBody(serialize(new ErrorResponse("No tienes permisos suficientes para acceder a este recurso")));
                }
                page = dao.findHM(fatherFamilyName, employeeCode, pageNumber, pageSize);
                break;
            default:
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(400)
                        .withBody(serialize(new ErrorResponse("Opción no válida")));
        }

        Map<String, String> headers = new HashMap<>(DEFAULT_HEADERS);
        headers.put("X-Total-Count", Integer.toString(page.getRows()));

        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(200)
                .withBody(serialize(page.getContent()));
    }
}
