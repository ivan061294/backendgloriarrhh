package pe.com.centro.utils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.Role;

/**
 * Utils for Cognito operations
 */
@Slf4j
public final class CognitoUtils {

    /**
     * Gets the authenticated user from the http request
     *
     * @param input   the API request event
     * @param context the context of the request
     * @return the user information (employee's code and {@link pe.com.centro.domain.enumeration.Role})
     */
    public static User getAuthenticatedUser(APIGatewayProxyRequestEvent input, Context context) {
        // TODO: see https://stackoverflow.com/q/51142695/14978149
        //       check cognito from Authorization header, Cognito Pool and Identity ID
        String authorization = input.getHeaders().get("Authorization");
        String code = authorization.split(";")[0];
        String role = authorization.split(";")[1];
        log.debug("UserCode='{}' and role='{}'", code, role);
        // String identityId = context.getIdentity().getIdentityId();
        // String identityPoolId = context.getIdentity().getIdentityPoolId();
        User user = new User();
        user.setEmployeeCode(code);
        user.setRole(Role.valueOf(role));
        return user;
    }

    public static void deleteUser(String attribute) {
        // TODO: delete cognito user using CognitoAdminAPI
    }
}
