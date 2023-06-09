package pe.com.centro.modules.postType;

import pe.com.centro.domain.PostType;

import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Post Type's operations.
 */
public interface PostTypeDAO {
    /**
     * Finds a list of post types by custom params
     *
     * @param societyCode            the Society's code
     * @param divisionCode           the Division's code
     * @param managementId           the Management Area's id
     * @param organizationalUnitCode the Organizational Unit's code
     * @param physicalLocationCode   the Physical Location's code
     * @param costCenterCode         the Cost Center's code
     * @return a list of post types
     */
    List<PostType> findAllByParams(String societyCode, String divisionCode, Long managementId, String organizationalUnitCode, String costCenterCode, String physicalLocationCode);

    /**
     * Finds a map of characteristics by post type id
     * @param id the Post's id
     * @return the map of characteristics
     */
    Map<String, List<Map<String, String>>> findCharacteristicsByPostTypeId(Long id);
}
