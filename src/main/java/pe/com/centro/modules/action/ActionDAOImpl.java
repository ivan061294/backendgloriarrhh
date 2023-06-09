package pe.com.centro.modules.action;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.Action;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.RequestUserActionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ActionDAOImpl implements ActionDAO {
    private static final String FIND_ALL_BY_REQUEST_ID =
            "select rua.user_employee_code, rua.action, rua.action_date, rua.comment, " +
                    "es.employee_first_name, es.employee_father_family_name, es.employee_mother_family_name " +
                    "from request_user_actions rua " +
                    "inner join \"user\" u on rua.user_employee_code = u.employee_code " +
                    "inner join employee_sap es on es.employee_code = u.employee_code " +
                    "where request_id = ? order by 3";

    private static final String CREATE_ALL_BY_USER =
            "insert into request_user_actions(request_id, user_employee_code, action, action_date, comment)  " +
                    "values (?, ?, ?, ?, ?)";

    @Override
    public List<Action> findAllByRequestId(Long id) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Action> actions = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_BY_REQUEST_ID);
            statement.setLong(1, id);
            set = statement.executeQuery();
            while (set.next()) {
                Action action = new Action();
                action.setUserEmployeeCode(set.getString(1));
                switch (set.getInt(2)) {
                    case 0:
                        action.setActionType(RequestUserActionType.CREATED);
                        break;
                    case 1:
                        action.setActionType(RequestUserActionType.APPROVED);
                        break;
                    case 2:
                        action.setActionType(RequestUserActionType.REJECTED);
                        break;
                    case 3:
                        action.setActionType(RequestUserActionType.OBSERVED);
                        break;
                    case 4:
                        action.setActionType(RequestUserActionType.RELEASED);
                        break;
                }
                action.setDate(set.getTimestamp(3).toInstant());
                action.setComment(set.getString(4));
                action.setEmployeeFirstName(set.getString(5));
                action.setEmployeeFatherFamilyName(set.getString(6));
                action.setEmployeeMotherFamilyName(set.getString(7));
                actions.add(action);
            }
            return actions;
        } catch (SQLException e) {
            log.error("Error listing requestUserActions by request", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public void createAll(List<Action> actions, User user) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(CREATE_ALL_BY_USER);

            for (Action action : actions) {
                statement.setLong(1, action.getRequestId());
                statement.setString(2, user.getEmployeeCode());
                statement.setInt(3, action.getActionType().getValue());
                statement.setTimestamp(4, Timestamp.from(action.getDate()));
                statement.setString(5, action.getComment());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            log.debug("Error creating actions", e);
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }
}
