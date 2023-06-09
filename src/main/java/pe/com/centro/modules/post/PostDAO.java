package pe.com.centro.modules.post;

import pe.com.centro.domain.LoginResponse;
import pe.com.centro.domain.Post;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Post's operations.
 */
public interface PostDAO {

    /**
     * Finds a list of posts by employee's father family name and other params
     *
     * @param fatherFamilyName       the employee's father family name
     * @param societyCode            the Society's code
     * @param divisionCode           the Division's code
     * @param managementId           the Management Area's id
     * @param organizationalUnitCode the Organizational Unit's code
     * @param physicalLocationCode   the Physical Location's code
     * @param costCenterCode         the Cost Center's code
     * @return a list of posts
     */
    List<Post> findAllByFatherFamilyNameAndParams(
            String fatherFamilyName,
            String societyCode,
            String divisionCode,
            Long managementId,
            String organizationalUnitCode,
            String physicalLocationCode,
            String costCenterCode,
            Long postTypeId,
            int maxSize);

    /**
     * Finds a post by its code
     *
     * @param code the Post's code
     * @return the post
     */
    List<Post> findByCode(String code);

    LoginResponse findByCodeEmployee(String email);

    /**
     * Finds a list of employees' posts in a staff requirement request
     *
     * @param id the request id
     * @return a list of codes
     */
    List<Post> findAllReplacementsByRequestId(Long id);
}
