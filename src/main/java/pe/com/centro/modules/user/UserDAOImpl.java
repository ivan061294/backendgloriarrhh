package pe.com.centro.modules.user;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.RequestType;
import pe.com.centro.domain.enumeration.Role;
import pe.com.centro.utils.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDAOImpl implements UserDAO {
    private static final String FIND_SUPERIOR_BY_EMPLOYEE_CODE =
            "select sup.employee_code, sup.employee_email, sup_u.role " +
                    "from gloria_dev_test.employee_sap es " +
                    "inner join gloria_dev_test.employee_sap sup on es.superior_employee_code = sup.employee_code " +
                    "inner join gloria_dev_test.user sup_u on es.employee_code = sup_u.employee_code " +
                    "where es.employee_code = ?";

    private static final String FIND_HM_BY_POST_CODE =
            "select e.employee_code, e.employee_email " +
                    "from request_post rp " +
                    "inner join gloria_dev_test.employee_sap es on es.post_code = rp.post_code " +
                    "inner join gloria_dev_test.human_management_rule hmr on (es.society_code = hmr.society_code and es.division_code = hmr.division_code and es.physical_location_code = hmr.physical_location_code) " +
                    "inner join gloria_dev_test.employee_sap e on e.employee_code = hmr.user_employee_code " +
                    "where rp.post_code = ? limit 1";

    private static final String FIND_HM_BY_POST_TYPE_ID =
            "select es.employee_code, es.employee_email " +
                    "from request_post_type rpt " +
                    "inner join gloria_dev_test.human_management_rule hmr on (rpt.society_code = hmr.society_code and rpt.division_code = hmr.division_code and rpt.physical_location_code = hmr.physical_location_code) " +
                    "inner join gloria_dev_test.employee_sap es on hmr.user_employee_code = es.employee_code " +
                    "where rpt.id = ?";

    private static final String FIND_HM_BY_REQUEST_ID_POST =
            "select e.employee_code, e.employee_email " +
                    "from request_post rp " +
                    "inner join gloria_dev_test.request r on r.id = rp.request_id " +
                    "inner join gloria_dev_test.employee_sap es on es.post_code = rp.post_code " +
                    "inner join gloria_dev_test.human_management_rule hmr on (es.society_code = hmr.society_code and es.division_code = hmr.division_code and es.physical_location_code = hmr.physical_location_code) " +
                    "inner join gloria_dev_test.employee_sap e on e.employee_code = hmr.user_employee_code " +
                    "where r.id = ? limit 1";

    private static final String COUNT_ALL = "select count(*) from gloria_dev_test.user u " +
            "inner join gloria_dev_test.employee_sap es on es.employee_code = u.employee_code " +
            "where ((? is null or es.employee_father_family_name like upper(?)) " +
            "or (? is null or es.employee_code like ?)) " +
            "and (? is null or u.role = ?)";

    private static final String FIND_ALL =
            "select es.employee_code, es.employee_email, es.employee_first_name, es.employee_father_family_name, es.employee_mother_family_name, u.role " +
                    "from gloria_dev_test.user u " +
                    "inner join gloria_dev_test.employee_sap es on es.employee_code = u.employee_code " +
                    "where ((? is null or es.employee_father_family_name like upper(?)) " +
                    "or (? is null or es.employee_code like ?)) " +
                    "and (? is null or u.role = ?) " +
                    "limit ? offset ?";

    private static final String UPDATE_USER_ROLE = "update gloria_dev_test.user set role = ? where employee_code = ?";

    private static final String CREATE_USER = "insert into gloria_dev_test.user(employee_code, role) values(?, ?)";

    private static final String FIND_HM =
            "select u.employee_code, es.employee_first_name, es.employee_father_family_name, es.employee_mother_family_name, s.code, s.name, d.code, d.name, pl.code, pl.name " +
                    "from gloria_dev_test.user u " +
                    "inner join gloria_dev_test.employee_sap es on (u.employee_code = es.employee_code and u.role = 1) " +
                    "inner join gloria_dev_test.human_management_rule hmr on u.employee_code = hmr.user_employee_code " +
                    "inner join gloria_dev_test.society s on s.code = hmr.society_code " +
                    "inner join gloria_dev_test.division d on d.code = hmr.division_code " +
                    "inner join gloria_dev_test.physical_location pl on pl.code = hmr.physical_location_code " +
                    "where (? is null or es.employee_father_family_name like upper(?)) " +
                    "or (? is null or es.employee_code like ?) " +
                    "limit ? offset ?";

    private static final String COUNT_HM =
            "select count(*) from human_management_rule hmr " +
                    "inner join gloria_dev_test.user u on u.employee_code = hmr.user_employee_code " +
                    "inner join gloria_dev_test.employee_sap es on es.employee_code = u.employee_code " +
                    "where (? is null or es.employee_father_family_name like upper(?)) " +
                    "or (? is null or es.employee_code like ?)";

    private static final String CREATE_HM_RULE = "insert into gloria_dev_test.human_management_rule(society_code, division_code, physical_location_code, user_employee_code) " +
            "values (?, ?, ?, ?)";

    private static final String UPDATE_HM = "update gloria_dev_test.human_management_rule set society_code = ?, division_code = ?, physical_location_code = ? " +
            "where society_code = ? and division_code = ? and physical_location_code = ? and user_employee_code = ?";

    private static final String DELETE_USER = "delete from gloria_dev_test.user where employee_code = ?";

    private static final String DELETE_HM = "delete from gloria_dev_test.human_management_rule where society_code = ? and division_code = ? and physical_location_code = ? and user_employee_code = ?";

    private static final String FIND_ALL_LOWER_BY_REQUEST_ID =
            "select es.employee_code, es.employee_email " +
                    "from gloria_dev_test.request_user_actions rua " +
                    "inner join gloria_dev_test.user u on (u.employee_code = rua.user_employee_code and (rua.action = 0 or u.role in (1, 2))) " +
                    "inner join gloria_dev_test.employee_sap es on u.employee_code = es.employee_code " +
                    "where rua.request_id = ?";

    public Optional<User> findSuperiorByEmployeeCode(String employeeCode) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        User user = null;

        try {
            statement = connection.prepareStatement(FIND_SUPERIOR_BY_EMPLOYEE_CODE);
            statement.setString(1, employeeCode);
            set = statement.executeQuery();
            if (set.next()) {
                user = new User();
                user.setEmployeeCode(set.getString(1));
                user.setEmail(set.getString(2));
                switch (set.getInt(3)) {
                    case 0:
                        user.setRole(Role.REQUESTER);
                        break;
                    case 1:
                        user.setRole(Role.HUMAN_MANAGEMENT);
                        break;
                    case 2:
                        user.setRole(Role.GENERAL_MANAGER);
                        break;
                    case 3:
                        user.setRole(Role.CORPORATE_DIRECTORATE);
                        break;
                    case 4:
                        user.setRole(Role.ORGANIZATIONAL_EFFECTIVITY);
                        break;
                    case 5:
                        user.setRole(Role.CORPORATE_MANAGEMENT);
                        break;
                    case 6:
                        user.setRole(Role.PRESIDENCY);
                        break;
                    case 7:
                        user.setRole(Role.ADMINISTRATOR);
                        break;
                }
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            log.error("Error getting superior user", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public List<User> findHMByRequestIdAndRequestType(Long requestId, RequestType requestType) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<User> users = new ArrayList<>();

        try {
            if (RequestType.REPLACEMENT.equals(requestType)) {
                statement = connection.prepareStatement(FIND_HM_BY_REQUEST_ID_POST);
            } else {
                statement = connection.prepareStatement(FIND_HM_BY_POST_TYPE_ID);
            }
            statement.setLong(1, requestId);
            set = statement.executeQuery();
            while (set.next()) {
                User user = new User();
                user.setEmployeeCode(set.getString(1));
                user.setEmail(set.getString(2));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            log.debug("Error listing Human management employees by RequestId and RequestType", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public Page<User> findAll(String fatherFamilyName, String employeeCode, Role role, int pageNumber, int pageSize) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement countStatement = null;
        ResultSet countSet = null;
        PreparedStatement listStatement = null;
        ResultSet listSet = null;
        Page<User> page = new Page<>();
        List<User> users = new ArrayList<>();

        try {
            countStatement = connection.prepareStatement(COUNT_ALL);
            countStatement.setString(1, fatherFamilyName == null ? null : "%" + fatherFamilyName + "%");
            countStatement.setString(2, fatherFamilyName == null ? null : "%" + fatherFamilyName + "%");
            countStatement.setString(3, employeeCode == null ? null : "%" + employeeCode + "%");
            countStatement.setString(4, employeeCode == null ? null : "%" + employeeCode + "%");
            if (role == null) {
                countStatement.setNull(5, Types.INTEGER);
                countStatement.setNull(6, Types.INTEGER);
            } else {
                countStatement.setInt(5, role.getValue());
                countStatement.setInt(6, role.getValue());
            }
            countSet = countStatement.executeQuery();
            if (countSet.next()) {
                page.setRows(countSet.getInt(1));
            }

            listStatement = connection.prepareStatement(FIND_ALL);
            listStatement.setString(1, fatherFamilyName == null ? null : "%" + fatherFamilyName + "%");
            listStatement.setString(2, fatherFamilyName == null ? null : "%" + fatherFamilyName + "%");
            listStatement.setString(3, employeeCode == null ? null : "%" + employeeCode + "%");
            listStatement.setString(4, employeeCode == null ? null : "%" + employeeCode + "%");
            if (role == null) {
                listStatement.setNull(5, Types.INTEGER);
                listStatement.setNull(6, Types.INTEGER);
            } else {
                listStatement.setInt(5, role.getValue());
                listStatement.setInt(6, role.getValue());
            }
            listStatement.setInt(7, pageSize);
            listStatement.setInt(8, pageNumber * pageSize);
            listSet = listStatement.executeQuery();
            while (listSet.next()) {
                User user = new User();
                user.setEmployeeCode(listSet.getString(1));
                user.setEmail(listSet.getString(2));
                user.setEmployeeFirstName(listSet.getString(3));
                user.setEmployeeFatherFamilyName(listSet.getString(4));
                user.setEmployeeMotherFamilyName(listSet.getString(5));
                switch (listSet.getInt(6)) {
                    case 0:
                        user.setRole(Role.REQUESTER);
                        break;
                    case 1:
                        user.setRole(Role.HUMAN_MANAGEMENT);
                        break;
                    case 2:
                        user.setRole(Role.GENERAL_MANAGER);
                        break;
                    case 3:
                        user.setRole(Role.CORPORATE_DIRECTORATE);
                        break;
                    case 4:
                        user.setRole(Role.ORGANIZATIONAL_EFFECTIVITY);
                        break;
                    case 5:
                        user.setRole(Role.CORPORATE_MANAGEMENT);
                        break;
                    case 6:
                        user.setRole(Role.PRESIDENCY);
                        break;
                    case 7:
                        user.setRole(Role.ADMINISTRATOR);
                        break;
                }
                users.add(user);
            }
            page.setContent(users);
            return page;
        } catch (SQLException e) {
            log.error("Error listing users", e);
            throw new RuntimeException(e);
        } finally {
            if (listSet != null)
                BaseDAO.close(listSet);
            if (listStatement != null)
                BaseDAO.close(listStatement);
            if (countSet != null)
                BaseDAO.close(countSet);
            if (countStatement != null)
                BaseDAO.close(countStatement);
        }
    }

    @Override
    public void update(User user) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UPDATE_USER_ROLE);
            statement.setInt(1, user.getRole().getValue());
            statement.setString(2, user.getEmployeeCode());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating user role");
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public void updateHM(User user, String prevSociety, String prevDivision, String prevSubdivision) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UPDATE_HM);
            statement.setString(1, user.getSocietyCode());
            statement.setString(2, user.getDivisionCode());
            statement.setString(3, user.getPhysicalLocationCode());
            statement.setString(4, prevSociety);
            statement.setString(5, prevDivision);
            statement.setString(6, prevSubdivision);
            statement.setString(7, user.getEmployeeCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating human management rule");
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public void save(User user) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(CREATE_USER);
            statement.setString(1, user.getEmployeeCode());
            statement.setInt(2, user.getRole().getValue());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error saving user role");
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public Page<User> findHM(String fatherFamilyName, String employeeCode, int pageNumber, int pageSize) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement countStatement = null;
        ResultSet countSet = null;
        PreparedStatement listStatement = null;
        ResultSet listSet = null;
        Page<User> page = new Page<>();
        List<User> users = new ArrayList<>();

        try {
            countStatement = connection.prepareStatement(COUNT_HM);
            countStatement.setString(1, fatherFamilyName != null ? "%" + fatherFamilyName + "%" : null);
            countStatement.setString(2, fatherFamilyName != null ? "%" + fatherFamilyName + "%" : null);
            countStatement.setString(3, employeeCode != null ? "%" + employeeCode + "%" : null);
            countStatement.setString(4, employeeCode != null ? "%" + employeeCode + "%" : null);
            countSet = countStatement.executeQuery();

            if (countSet.next()) {
                page.setRows(countSet.getInt(1));
            }

            listStatement = connection.prepareStatement(FIND_HM);
            listStatement.setString(1, fatherFamilyName != null ? "%" + fatherFamilyName + "%" : null);
            listStatement.setString(2, fatherFamilyName != null ? "%" + fatherFamilyName + "%" : null);
            listStatement.setString(3, employeeCode != null ? "%" + employeeCode + "%" : null);
            listStatement.setString(4, employeeCode != null ? "%" + employeeCode + "%" : null);
            listStatement.setInt(5, pageSize);
            listStatement.setInt(6, pageNumber * pageSize);
            listSet = listStatement.executeQuery();

            while (listSet.next()) {
                User user = new User();
                user.setEmployeeCode(listSet.getString(1));
                user.setEmployeeFirstName(listSet.getString(2));
                user.setEmployeeFatherFamilyName(listSet.getString(3));
                user.setEmployeeMotherFamilyName(listSet.getString(4));
                user.setSocietyCode(listSet.getString(5));
                user.setSocietyName(listSet.getString(6));
                user.setDivisionCode(listSet.getString(7));
                user.setDivisionName(listSet.getString(8));
                user.setPhysicalLocationCode(listSet.getString(9));
                user.setPhysicalLocationName(listSet.getString(10));
                users.add(user);
            }
            page.setContent(users);
            return page;
        } catch (SQLException e) {
            log.error("Error listing human management users", e);
            throw new RuntimeException(e);
        } finally {
            if (countSet != null)
                BaseDAO.close(countSet);
            if (countStatement != null)
                BaseDAO.close(countStatement);
            if (listSet != null)
                BaseDAO.close(listSet);
            if (listStatement != null)
                BaseDAO.close(listStatement);
        }
    }

    @Override
    public void saveHM(User user) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(CREATE_HM_RULE);
            statement.setString(1, user.getSocietyCode());
            statement.setString(2, user.getDivisionCode());
            statement.setString(3, user.getPhysicalLocationCode());
            statement.setString(4, user.getEmployeeCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error saving a human management rule", e);
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public void delete(String employeeCode) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(DELETE_USER);
            statement.setString(1, employeeCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting user", e);
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public void deleteHM(String societyCode, String divisionCode, String subdivisionCode, String employeeCode) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(DELETE_HM);
            statement.setString(1, societyCode);
            statement.setString(2, divisionCode);
            statement.setString(3, subdivisionCode);
            statement.setString(4, employeeCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error deleting human management rule", e);
            throw new RuntimeException(e);
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public List<User> findLowerRequestersByRequestId(Long requestId) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<User> users = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_LOWER_BY_REQUEST_ID);
            statement.setLong(1, requestId);

            set = statement.executeQuery();

            while (set.next()) {
                User user = new User();
                user.setEmployeeCode(set.getString(1));
                user.setEmail(set.getString(2));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            log.error("Error listing all lower users of a request", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }
}
