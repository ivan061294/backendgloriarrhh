package pe.com.centro.modules.action;

import pe.com.centro.domain.Action;
import pe.com.centro.domain.User;

import java.util.List;

/**
 * Data Access Object for Action's operations.
 */
public interface ActionDAO {

    /**
     * Finds a list of actions by request id
     *
     * @param id the Request's code
     * @return a list of actions
     */
    List<Action> findAllByRequestId(Long id);

    /**
     * Inserts a list of actions performed by a user
     *
     * @param actions the list of actions to be inserted
     * @param user    the user who performed the actions
     */
    void createAll(List<Action> actions, User user);
}
