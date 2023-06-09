package pe.com.centro.modules.society;

import pe.com.centro.domain.AllModelForm;
import pe.com.centro.domain.Society;
import java.util.Optional;

/**
 * Data Access Object for Society's operations.
 */
public interface SocietyDAO {

    /**
     * Finds a list of societies
     */
    AllModelForm getAll(String bukrs);

    /**
     * Updates a society
     *
     * @param society entity that contains the new society's attributes (also its id)
     */
     boolean update(String body);

    /**
     * Finds a society by post type or post code.
     * <br/>
     * <i>Note: only one must be defined</i>
     *
     * @param postCode   the Post's code
     * @param postTypeId the Post Type's id
     * @return the found (or not found) society
     */
    Optional<Society> findByPost(String postCode, Long postTypeId);
}
