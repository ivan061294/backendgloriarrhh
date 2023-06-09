package pe.com.centro.modules.post;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.LoginResponse;
import pe.com.centro.domain.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostDAOImpl implements PostDAO {
    private static final String FIND_ALL_BY_FATHER_FAMILY_NAME = "select post_code, employee_code, employee_first_name, employee_father_family_name, "
            +
            "employee_mother_family_name " +
            "from gloria_dev_test.employee_sap " +
            "where (? is null or society_code = ?) and " +
            "(? is null or division_code = ?) and " +
            "(? is null or management_id = ?) and " +
            "(? is null or organizational_unit_code = ?) and " +
            "(? is null or physical_location_code = ?) and " +
            "(? is null or cost_center_code = ?) and " +
            "(? is null or post_type_id = ?) and " +
            "employee_father_family_name like upper(?) limit ?";

    private static final String FIND_BY_CODE = "select pernr,bukrs,butxt,werks,name1,werks,name1,orgeh::text,orget,btrtl, " +
            "btext,kostl,ltext,pernr,name,apepat,apemat,plans,plstx,codrol,flgavailable::integer  " +
            "from proceso.estructurapersona where bukrs=?";
    private static final String FIND_ALL_REPLACEMENTS_BY_REQUEST_ID = "select es.post_code, es.employee_code, es.employee_first_name, es.employee_father_family_name, "
            +
            "es.employee_mother_family_name " +
            "from gloria_dev_test.employee_sap es inner join gloria_dev_test.request_post rp on es.post_code = rp.post_code "
            +
            "where rp.request_id = ?";
    private static final String FIND_BY_CODE_EMPLOYEE = " select 0 as posicion, ep.name,ep.apepat,ep.apemat,ep.pernr,ep.plans,ep.plstx,ep.codrol,ep.abkrs,ep2.pernr,ep.alcance,ep.sobid,ep.stext3, "
            +
            " ep.bukrs,ep.werks,ep.btrtl,ep.orgeh::text,ep.dni,ep.email,ep2.codrol,ep.kostl " +
            " from proceso.estructurapersona ep " +
            " left join proceso.estructurapersona ep2 on ep.pernr_sup=ep2.pernr " +
            "where ep.email= ? ";

    @Override
    public List<Post> findAllByFatherFamilyNameAndParams(
            String fatherFamilyName,
            String societyCode, String divisionCode,
            Long managementId, String organizationalUnitCode,
            String physicalLocationCode, String costCenterCode,
            Long postTypeId, int maxSize) {
        Connection connection = BaseDAO.getConnection();
        List<Post> posts = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet set = null;

        try {
            statement = connection.prepareStatement(FIND_ALL_BY_FATHER_FAMILY_NAME);
            statement.setString(1, societyCode);
            statement.setString(2, societyCode);
            statement.setString(3, divisionCode);
            statement.setString(4, divisionCode);
            if (managementId == null) {
                statement.setNull(5, Types.BIGINT);
                statement.setNull(6, Types.BIGINT);
            } else {
                statement.setLong(5, managementId);
                statement.setLong(6, managementId);
            }
            statement.setString(7, organizationalUnitCode);
            statement.setString(8, organizationalUnitCode);
            statement.setString(9, physicalLocationCode);
            statement.setString(10, physicalLocationCode);
            statement.setString(11, costCenterCode);
            statement.setString(12, costCenterCode);
            if (postTypeId == null) {
                statement.setNull(13, Types.BIGINT);
                statement.setNull(14, Types.BIGINT);
            } else {
                statement.setLong(13, postTypeId);
                statement.setLong(14, postTypeId);
            }
            statement.setString(15, "%" + fatherFamilyName + "%");
            statement.setInt(16, maxSize);
            set = statement.executeQuery();
            while (set.next()) {
                Post post = new Post();
                post.setCode(set.getString(1));
                post.setEmployeeCode(set.getString(2));
                post.setEmployeeFirstName(set.getString(3));
                post.setEmployeeFatherFamilyName(set.getString(4));
                post.setEmployeeMotherFamilyName(set.getString(5));
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            log.error("Error listing posts by father family name", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public List<Post> findByCode(String code) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Post> posts = new ArrayList<>();
        try {
            statement = connection.prepareStatement(FIND_BY_CODE);
            statement.setString(1, code);
            set = statement.executeQuery();
            while (set.next()) {
                Post post = new Post();
                post.setCode(set.getString(1));
                post.setSocietyCode(set.getString(2));
                post.setSocietyName(set.getString(3));
                post.setDivisionCode(set.getString(4));
                post.setDivisionName(set.getString(5));
                post.setManagementId(set.getLong(6));
                post.setManagementName(set.getString(7));
                post.setOrganizationalUnitCode(set.getString(8));
                post.setOrganizationalUnitName(set.getString(9));
                post.setPhysicalLocationCode(set.getString(10));
                post.setPhysicalLocationName(set.getString(11));
                post.setCostCenterCode(set.getString(12));
                post.setCostCenterName(set.getString(13));
                post.setEmployeeCode(set.getString(14));
                post.setEmployeeFirstName(set.getString(15));
                post.setEmployeeFatherFamilyName(set.getString(16));
                post.setEmployeeMotherFamilyName(set.getString(17));
                post.setPostTypeId(set.getString(18));
                post.setPostTypeName(set.getString(19));
                post.setCodRol(set.getInt(20));
                post.setFlgAvailable(set.getInt(21));
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            log.error("Error getting Post by code", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public List<Post> findAllReplacementsByRequestId(Long id) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Post> posts = new ArrayList<>();
        try {
            statement = connection.prepareStatement(FIND_ALL_REPLACEMENTS_BY_REQUEST_ID);
            statement.setLong(1, id);
            set = statement.executeQuery();
            while (set.next()) {
                Post post = new Post();
                post.setCode(set.getString(1));
                post.setEmployeeCode(set.getString(2));
                post.setEmployeeFirstName(set.getString(3));
                post.setEmployeeFatherFamilyName(set.getString(4));
                post.setEmployeeMotherFamilyName(set.getString(5));
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            log.error("Error listing posts by request", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public LoginResponse findByCodeEmployee(String email) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        LoginResponse post = new LoginResponse();
        try {
            statement = connection.prepareStatement(FIND_BY_CODE_EMPLOYEE);
            statement.setString(1, email);
            log.info("statement{}", statement.toString());
            set = statement.executeQuery();
            if (set.next()) {
                post.setPosition(set.getInt(1));
                post.setName(set.getString(2));
                post.setApPaterno(set.getString(3));
                post.setApMaterno(set.getString(4));
                post.setPern(set.getString(5));
                post.setPlans(set.getString(6));
                post.setPlstx(set.getString(7));
                post.setCodrol(set.getInt(8));
                post.setAbkrs(set.getString(9));
                post.setPern_sup_code(set.getString(10));
                post.setAlcance(set.getString(11));
                post.setSobid(set.getString(12));
                post.setStext3(set.getString(13));
                post.setBukrs(set.getString(14));
                post.setWerks(set.getString(15));
                post.setBtrtl(set.getString(16));
                post.setOrgeh(set.getString(17));
                post.setDni(set.getString(18));
                post.setEmail(set.getString(19));
                post.setCodrol_sup(set.getInt(20));
                post.setKostl(set.getString(21));

            }
            log.info("postresponse", post.toString());
            return post;
        } catch (SQLException e) {
            log.error("Error getting Post by code", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }
}
