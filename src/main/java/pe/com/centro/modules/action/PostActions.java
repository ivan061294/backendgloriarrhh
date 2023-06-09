package pe.com.centro.modules.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.Action;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.RequestStatus;
import pe.com.centro.domain.enumeration.RequestUserActionType;
import pe.com.centro.domain.enumeration.Role;
import pe.com.centro.modules.request.RequestDAO;
import pe.com.centro.modules.request.RequestDAOImpl;
import pe.com.centro.modules.user.UserDAO;
import pe.com.centro.modules.user.UserDAOImpl;
import pe.com.centro.utils.Constants;
import pe.com.centro.utils.Serializer;

import java.util.List;
import java.util.Map;

import static pe.com.centro.utils.CognitoUtils.getAuthenticatedUser;
// import static pe.com.centro.utils.MailService.sendMailForApprovedRequirementPlanAndNoPlan;
// import static pe.com.centro.utils.MailService.sendMailForReleasedRequirementPlanAndNoPlan;
// import static pe.com.centro.utils.Parser.parseEmails;
import static pe.com.centro.utils.Serializer.deserialize;

@Slf4j
public class PostActions implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to post a list of actions");
        User user = getAuthenticatedUser(input, context);

        List<Action> actions = deserialize(input.getBody(), new TypeReference<>() {
        });
        ActionDAO dao = new ActionDAOImpl();

        this.manageActionsByUser(user, actions, dao);

        return new APIGatewayProxyResponseEvent()
                .withHeaders(Constants.DEFAULT_HEADERS)
                .withStatusCode(201)
                .withBody(Serializer.serialize(Map.of("message", "Created successfully")));
    }

    private void manageActionsByUser(User user, List<Action> actions, ActionDAO dao) {
        RequestDAO requestDAO = new RequestDAOImpl();
        dao.createAll(actions, user);

        switch (user.getRole()) {
            case REQUESTER:
                UserDAO userDAO = new UserDAOImpl();
                userDAO.findSuperiorByEmployeeCode(user.getEmployeeCode()).ifPresent(superior -> {
                    if (superior.getRole().equals(Role.GENERAL_MANAGER)) {
                        for (Action action : actions) {
                            if (!RequestUserActionType.REJECTED.equals(action.getActionType())) {
                                List<User> users = userDAO.findHMByRequestIdAndRequestType(action.getRequestId(), action.getRequestType());
                                // Send emails to multiple users
                                //sendMailForApprovedRequirement(parseEmails(users));
                            }
                        }
                    } else {
                        for (Action action : actions) {
                            if (!RequestUserActionType.REJECTED.equals(action.getActionType())) {
                                // Send emails to one user
                                //sendMailForApprovedRequirement(superior.getEmail());
                            }
                        }
                    }
                });
                break;
            case HUMAN_MANAGEMENT:
                for (Action action : actions) {
                    switch (action.getActionType()) {
                        case APPROVED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.APPROVED_BY_HM);
                            break;
                        case REJECTED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.NOT_APPROVED);
                            break;
                    }
                }
                break;
            case GENERAL_MANAGER:
            case CORPORATE_DIRECTORATE:
                for (Action action : actions) {
                    switch (action.getActionType()) {
                        case APPROVED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.APPROVED_BY_GM);
                            break;
                        case REJECTED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.NOT_APPROVED);
                            break;
                    }
                }
                break;
            case ORGANIZATIONAL_EFFECTIVITY:
                for (Action action : actions) {
                    switch (action.getActionType()) {
                        case APPROVED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.APPROVED_BY_OE);
                            break;
                        case REJECTED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.REJECTED);
                            break;
                        case OBSERVED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.OBSERVED);
                            break;
                        case RELEASED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.SENT);
                            this.manageSendEmailForReleaseAction(action.getRequestId());
                            break;
                    }
                }
                break;
            case CORPORATE_MANAGEMENT:
            case PRESIDENCY:
                for (Action action : actions) {
                    switch (action.getActionType()) {
                        case APPROVED:
                            requestDAO.updateRequestStatus(action.getRequestId(), user.getRole().equals(Role.CORPORATE_MANAGEMENT) ? RequestStatus.APPROVED_BY_CM : RequestStatus.APPROVED_BY_PRESIDENCY);
                            break;
                        case REJECTED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.REJECTED);
                            break;
                        case OBSERVED:
                            requestDAO.updateRequestStatus(action.getRequestId(), RequestStatus.OBSERVED);
                            break;
                    }
                }
                break;
        }
    }

    private void manageSendEmailForReleaseAction(Long requestId) {
        UserDAO dao = new UserDAOImpl();
        List<User> users = dao.findLowerRequestersByRequestId(requestId);
        //sendMailForReleasedRequirement(parseEmails(users));
    }
}
