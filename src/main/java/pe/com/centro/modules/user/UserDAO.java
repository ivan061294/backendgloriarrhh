package pe.com.centro.modules.user;

import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.RequestType;
import pe.com.centro.domain.enumeration.Role;
import pe.com.centro.utils.Page;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for User Management operations.
 */
public interface UserDAO {

    /**
     * Finds the superior of the supplied employee "user"
     *
     * @param employeeCode the employee code
     * @return a found (or not found) superior employee "user"
     */
    Optional<User> findSuperiorByEmployeeCode(String employeeCode);

    /**
     * Finds a list of human management users who manage specific types of requests
     * (society, division, physical location) by request's id
     *
     * @param requestId   the Request's id
     * @param requestType the {@link pe.com.centro.domain.enumeration.RequestType}
     * @return a list of human management rules
     */
    List<User> findHMByRequestIdAndRequestType(Long requestId, RequestType requestType);

    /**
     * Finds users by father family name and other params
     *
     * @param fatherFamilyName the user's father family name
     * @param employeeCode     the employee's code
     * @param role             the user's role
     * @param pageNumber       the page number
     * @param pageSize         the page size
     * @return a page of users
     */
    Page<User> findAll(String fatherFamilyName, String employeeCode, Role role, int pageNumber, int pageSize);

    /**
     * Updates a user
     *
     * @param user entity that contains the new user's information (also its id)
     */
    void update(User user);

    /**
     * Updates a human management rule
     *
     * @param user            with {@code HUMAN_MANAGEMENT} {@link pe.com.centro.domain.enumeration.Role}
     * @param prevSociety     the previous society where it was assigned
     * @param prevDivision    the previous division where it was assigned
     * @param prevSubdivision the previous sub-division or physical location where it was assigned
     */
    void updateHM(User user, String prevSociety, String prevDivision, String prevSubdivision);

    /**
     * Saves a new user
     *
     * @param user the entity to be stored
     */
    void save(User user);

    /**
     * Finds human management rules (Society, Division, Physical Location and User assignment)
     *
     * @param fatherFamilyName the employee's father family name
     * @param employeeCode     the employee's code
     * @param pageNumber       the page number
     * @param pageSize         the page size
     * @return a page of users
     */
    Page<User> findHM(String fatherFamilyName, String employeeCode, int pageNumber, int pageSize);

    /**
     * Saves human management rule
     *
     * @param user the human management rule to be assigned
     */
    void saveHM(User user);

    /**
     * Deletes a user by its employee code
     *
     * @param employeeCode the User's employee code
     */
    void delete(String employeeCode);

    /**
     * Deletes a human management rule
     *
     * @param societyCode     the Society's code
     * @param divisionCode    the Division's code
     * @param subdivisionCode the Sub-division's code
     * @param employeeCode    the User's employee code
     */
    void deleteHM(String societyCode, String divisionCode, String subdivisionCode, String employeeCode);

    /**
     * Finds users who made an action to a specific request and are under Organizational Effectivity level
     *
     * @param requestId the Request's id
     * @return a list of users
     */
    List<User> findLowerRequestersByRequestId(Long requestId);
}
