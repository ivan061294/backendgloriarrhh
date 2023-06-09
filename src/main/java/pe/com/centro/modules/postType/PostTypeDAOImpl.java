package pe.com.centro.modules.postType;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.PostType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PostTypeDAOImpl implements PostTypeDAO {
    private static final String FIND_ALL_BY_PARAMS = "select distinct pt.id, pt.name " +
            "from gloria_dev_test.employee_sap es " +
            "inner join gloria_dev_test.post_type pt on es.post_type_id = pt.id " +
            /*
             * "where (? is null or es.society_code = ?) and" +
             * "(? is null or es.division_code = ?) and" +
             * "(? is null or es.management_id = ?) and" +
             * "(? is null or es.organizational_unit_code = ?) and" +
             * "(? is null or es.physical_location_code = ?) and" +
             * "(? is null or es.cost_center_code = ?)" +
             */
            "order by pt.id";

    private static final String FIND_CHARACTERISTICS_BY_POST_TYPE_ID = "select ptc.name, ptc.value " +
            "from post_type_characteristic ptc " +
            "inner join post_type pt on ptc.post_type_id = pt.id " +
            "where pt.id = ? order by ptc.name";

    @Override
    public List<PostType> findAllByParams(String societyCode, String divisionCode, Long managementId,
            String organizationalUnitCode, String costCenterCode, String physicalLocationCode) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<PostType> postTypes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_BY_PARAMS);
            /*
             * statement.setString(1, societyCode);
             * statement.setString(2, societyCode);
             * statement.setString(3, divisionCode);
             * statement.setString(4, divisionCode);
             * if (managementId == null) {
             * statement.setNull(5, Types.BIGINT);
             * statement.setNull(6, Types.BIGINT);
             * } else {
             * statement.setLong(5, managementId);
             * statement.setLong(6, managementId);
             * }
             * statement.setString(7, organizationalUnitCode);
             * statement.setString(8, organizationalUnitCode);
             * statement.setString(9, physicalLocationCode);
             * statement.setString(10, physicalLocationCode);
             * statement.setString(11, costCenterCode);
             * statement.setString(12, costCenterCode);
             */
            set = statement.executeQuery();
            while (set.next()) {
                PostType postType = new PostType();
                postType.setId(set.getLong(1));
                postType.setName(set.getString(2));
                postTypes.add(postType);
            }
            return postTypes;
        } catch (SQLException e) {
            log.error("Error listing post types by", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public Map<String, List<Map<String, String>>> findCharacteristicsByPostTypeId(Long id) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        Map<String, List<Map<String, String>>> characteristics = new HashMap<>();
        try {
            statement = connection.prepareStatement(FIND_CHARACTERISTICS_BY_POST_TYPE_ID);
            statement.setLong(1, id);
            set = statement.executeQuery();
            while (set.next()) {
                List<Map<String, String>> itemList = new ArrayList<>();
                for (String item : set.getString(2).split("&")) {
                    Map<String, String> keyAttribute = new HashMap<>();
                    for (String attribute : item.split("\\|")) {
                        String[] pair = attribute.split("=");
                        if (pair.length == 2) {
                            keyAttribute.put(pair[0], pair[1]);
                        }
                    }
                    if (!keyAttribute.isEmpty()) {
                        itemList.add(keyAttribute);
                    }
                }
                if (!itemList.isEmpty()) {
                    characteristics.put(set.getString(1), itemList);
                }
            }
            return characteristics;
        } catch (SQLException e) {
            log.error("Error listing post type characteristics", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }
}
