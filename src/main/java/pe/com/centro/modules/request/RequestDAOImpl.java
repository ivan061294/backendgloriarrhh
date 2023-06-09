package pe.com.centro.modules.request;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.AbilityRequirements;
import pe.com.centro.domain.AcademicFormation;
import pe.com.centro.domain.Activities;
import pe.com.centro.domain.Aprobadores;
import pe.com.centro.domain.Characteristics;
import pe.com.centro.domain.CharacteristicsQuery;
import pe.com.centro.domain.FileObject;
import pe.com.centro.domain.Languages;
import pe.com.centro.domain.MailTemplate;
import pe.com.centro.domain.Request;
import pe.com.centro.domain.RequestControl;
import pe.com.centro.domain.RequestList;
import pe.com.centro.domain.StudyCenters;
import pe.com.centro.domain.User;
import pe.com.centro.domain.knowledgeRequirements;
import pe.com.centro.domain.enumeration.*;
import pe.com.centro.utils.StorageService;
import static pe.com.centro.utils.Constants.*;

import java.sql.*;
import java.util.*;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
public class RequestDAOImpl implements RequestDAO {

    // private static final String COUNT_ALL_BY_PARAMS =
    // "select count (*), " +
    // "sum(1) filter ( where r1.request_status = 0 ), " +
    // "sum(1) filter ( where r1.request_status = 1 ), " +
    // "sum(1) filter ( where r1.request_status = 2 ), " +
    // "sum(1) filter ( where r1.request_status = 3 ), " +
    // "sum(1) filter ( where r1.request_status = 4 ), " +
    // "sum(1) filter ( where r1.request_status = 5 ), " +
    // "sum(1) filter ( where r1.request_status = 6 ), " +
    // "sum(1) filter ( where r1.request_status = 7 ), " +
    // "sum(1) filter ( where r1.request_status = 8 ), " +
    // "sum(1) filter ( where r1.request_status = 9 ) " +
    // "from ((select r.id, r.request_status " +
    // "from request r " +
    // "inner join request_user_actions rua on (r.id = rua.request_id and rua.action
    // = 0 and (? is null or r.type = ?) and (? is null or r.request_status = ?) and
    // extract(month from rua.action_date) = ? and extract(year from
    // rua.action_date) = ?) " +
    // "inner join (select request_id, post_code, row_number() over (partition by
    // request_id) index from request_post) rp on " +
    // "(r.id = rp.request_id and rp.index = 1) " +
    // "inner join employee_sap es on (es.post_code = rp.post_code and (? is null or
    // es.society_code = ?) and (? is null or es.organizational_unit_code = ?))) " +
    // "union " +
    // "(select r.id, r.request_status " +
    // "from request r " +
    // "inner join request_user_actions rua on (r.id = rua.request_id and rua.action
    // = 0 and (? is null or r.type = ?) and (? is null or r.request_status = ?) and
    // extract(month from rua.action_date) = ? and extract(year from
    // rua.action_date) = ?) " +
    // "inner join request_post_type rpt on (r.id = rpt.id and (? is null or
    // rpt.society_code = ?) and (? is null or rpt.organizational_unit_code = ?))))
    // r1";

    // private static final String FIND_ALL_BY_PARAMS =
    // "(select r.id, pt.name, s.name, d.name, ou.name, pl.name, cc.name, r.type,
    // r.vacancies, " +
    // "r.search_type, r.contract_type, r.contract_time, r.justification,
    // rua.action_date, r.request_status " +
    // "from request r " +
    // "inner join request_user_actions rua on (r.id = rua.request_id and rua.action
    // = 0 and (? is null or r.type = ?) and (? is null or r.request_status = ?) and
    // extract(month from rua.action_date) = ? and extract(year from
    // rua.action_date) = ?) " +
    // "inner join (select request_id, post_code, row_number() over (partition by
    // request_id) index from request_post) rp on (r.id = rp.request_id and rp.index
    // = 1) " +
    // "inner join employee_sap es on (es.post_code = rp.post_code and (? is null or
    // es.society_code = ?) and (? is null or es.organizational_unit_code = ?)) " +
    // "inner join post_type pt on pt.id = es.post_type_id " +
    // "inner join society s on es.society_code = s.code " +
    // "inner join division d on d.code = es.division_code " +
    // "inner join organizational_unit ou on ou.code = es.organizational_unit_code "
    // +
    // "inner join physical_location pl on es.physical_location_code = pl.code " +
    // "inner join cost_center cc on es.cost_center_code = cc.code)" +
    // "union " +
    // "(select r.id, pt.name, s.name, d.name, ou.name, pl.name, cc.name, r.type,
    // r.vacancies, " +
    // "r.search_type, r.contract_type, r.contract_time, r.justification,
    // rua.action_date, r.request_status " +
    // "from request r " +
    // "inner join request_user_actions rua on (r.id = rua.request_id and rua.action
    // = 0 and (? is null or r.type = ?) and (? is null or r.request_status = ?) and
    // extract(month from rua.action_date) = ? and extract(year from
    // rua.action_date) = ?) " +
    // "inner join request_post_type rpt on (r.id = rpt.id and (? is null or
    // rpt.society_code = ?) and (? is null or rpt.organizational_unit_code = ?)) "
    // +
    // "inner join post_type pt on pt.id = rpt.post_type_id " +
    // "inner join society s on rpt.society_code = s.code " +
    // "inner join division d on d.code = rpt.division_code " +
    // "inner join organizational_unit ou on ou.code = rpt.organizational_unit_code
    // " +
    // "inner join physical_location pl on rpt.physical_location_code = pl.code " +
    // "inner join cost_center cc on rpt.cost_center_code = cc.code)" +
    // "order by 14 desc limit ? offset ? ";

    private static final String UPDATE_REQUEST_STATUS = "update request set request_status = ? where id = ?";

    private static final String FIND_REQUEST_BY_SOCIETY = "select * from proceso.fn_get_requerimiento_persona( ?, ? )";
    
    private static final String FIND_REQUEST_BY_SOCIETY_ON_COURSE = "select * from proceso.fn_get_requerimiento_persona_on_course( ?, ? )";

    private static final String FIND_BY_ID = "select distinct req.idsolicitud,s.butxt,req.cantidad,sorg.orget,co.ltext,req.fec_crea::text ,"
            +
            "case when tiposolicitud=1 then 'Nuevo Planificado' when tiposolicitud=2 then 'Nuevo no Planificado' when tiposolicitud=3 then 'Reemplazo' end typerequest, "
            +
            "req.plstx,sd.btext " +
            "FROM proceso.requerimientopersona req " +
            "join maestra.sociedad s on req.bukrs=s.bukrs " +
            "join maestra.socunidadorg sorg on req.orgeh=sorg.orgeh " +
            "join maestra.centrocosto co on req.kostl=co.kostl " +
            "join maestra.socsubdivision sd on req.btrtl=sd.btrtl";

    private static final String CREATE_REQUEST = "INSERT INTO proceso.requerimientopersona" +
            "(tiposolicitud, pernr_basado, bukrs, werks, btrtl, orgeh, kostl, ltext, email, abkrs," +
            "dni, pernr_sup, sobid, stext3,alcance, pernr_solicitante, name_solicitante, plans_solicitante, plstx_solicitante, plans,"
            +
            "plstx, cantidad,  flgplanficado, cttyp, idtiempocontrato, pernr_reemplazo, name_reemplazo, plans_reemplazo, plstx_reemplazo, justificacion,"
            +
            "idtiempoexp, idrangoedad, gesch, famst,idtipobusqueda, idsolestado,flganulado, begda ,gbdat," +
            "fecingestimada, fec_crea, usu_crea, host_crea,idsolicitud) " +
            "VALUES ( ?, ?, ?, ?," +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
            "?, ?, ?, ?, ?, ?, ?, ?, ?::bit, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?::bit, now()::date, now()::date, now()::date, now()::date, ?, ?, ?)";

    private static final String CREATE_REQUEST_FUNCION = "INSERT INTO proceso.requerimientofuncion" +
            "(idsolicitud, funcion, fec_crea, usu_crea, host_crea)" +
            " VALUES (?, ?, now()::date, ?, ?)";

    private static final String CREATE_REQUEST_REEMPLAZO = "INSERT INTO proceso.requerimientoreemplazo " +
            "(idsolicitud, pernr, apepat, apemat, name, plans, plstx, fec_crea, usu_crea, host_crea) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, now()::date, ?, ?) ";

    private static final String CREATE_REQUEST_CONOCIMIENTO = "INSERT INTO proceso.requerimientoconocimiento" +
            "(idsolicitud, conocimiento, fec_crea, usu_crea, host_crea)" +
            "VALUES (?, ?, now()::date, ?, ?)";

    private static final String CREATE_REQUEST_HABILIDADES = "INSERT INTO proceso.requerimientohabilidad " +
            "(idsolicitud,  habilidad, fec_crea, usu_crea, host_crea)" +
            " VALUES (?, ?, now()::date, ?, ?)";

    private static final String CREATE_REQUEST_FOMACION = "INSERT INTO proceso.requerimientoformacion" +
            "(idsolicitud, idespecialidad, ideducacion, fec_crea, usu_crea, host_crea) " +
            " VALUES (?, ?, ?, now()::date, ?, ? )";

    private static final String CREATE_REQUEST_CENTROESTUDIO = "INSERT INTO proceso.requerimientocentroestudio " +
            "(idsolicitud, idinstitucion, slart, fec_crea, usu_crea, host_crea) " +
            " VALUES (?, ?, ?, now()::date, ?, ?)";

    private static final String CREATE_REQUEST_IDIOMA = "INSERT INTO proceso.requerimientoidioma " +
            "(idsolicitud, ididioma, ididiomanivel, fec_crea, usu_crea, host_crea) " +
            " VALUES (?, ?, ?, now()::date, ?, ?) ";

    private static final String CREATE_REQ_HIST_FLUJO = "INSERT INTO proceso.requerimientohistflujo " +
            "( idsolicitud, idsolestado, pernr, idrol, sec, observacion, fec_crea, usu_crea, host_crea ) " +
            "VALUES (?, 1, ?, ?, ?, ?, now()::date, ?, ?)";

    private static final String CREATE_REQ_HIST_FLUJO_SUPERIOR = "INSERT INTO proceso.requerimientohistflujo " +
            "( idsolicitud, idsolestado, pernr, idrol, sec, observacion, fec_crea, usu_crea, host_crea ) " +
            "VALUES (?, 0, ?, ?, ?, ?, now()::date, ?, ?)";

    private static final String CREATE_REQ_FILE = "INSERT INTO proceso.requerimientofile " +
            "(idsolicitud, file, nombre, ruta, fec_crea, usu_crea, host_crea) " +
            "VALUES (?, ?, ?, ?, now()::date, ?, ?)";

    private static final String GET_NIVEL_APROBACION = "SELECT code_nivel_aprobacion from proceso.fn_validar_req_por_puesto(?)";

    private static final String UPDATE_REQ = "SELECT code,mensaje from proceso.fn_update_requerimiento_persona(? ,? ,? ,? ,? ,? ,? ,? ,?, ?)";

    private static final String GET_ID_REQ = "select nextval('proceso.request_id_seq'::regclass)";

    private static final String GET_RUTA_FILE = "SELECT rf.ruta,rf.nombre FROM proceso.requerimientofile rf where rf.idsolicitud=?";

    private static final String GET_MAX_ORGEH = "select (max(orgeh)::integer+1)::text  FROM maestra.socunidadorg";

    private static final String INSERT_UO = "INSERT INTO maestra.socunidadorg( bukrs, werks, btrtl, orgeh, orget) VALUES (?, ?, ?, ?, ?) ";

    private static final String GET_MAX_FORMACION_ID = " SELECT (MAX(idespecialidad)::integer+10)::text,MAX(valpos)::integer+1 FROM maestra.educacionespecialidad ";

    private static final String INSERT_FORMACION = "INSERT INTO maestra.educacionespecialidad(idespecialidad, especialidad, valpos) VALUES (?, ?, ?)";

    //private static final String GET_EMAIL_NEXT_APPROVE = "SELECT email from proceso.estructurapersona where pernr = (select pernr_sup from proceso.estructurapersona where pernr=?)";

    private static final String GET_EMAIL_NEXT_APPROVE = "SELECT codrol_sup_out, fullname_out, email_sup_out from proceso.fn_get_pernr_sup(?)";

    private static final String GET_EMAIL_TEMPLATE = "SELECT subject_out,contentplantillacorreo_out FROM proceso.fn_get_plantilla_correo(?,?,?,?)";

    private static final String GET_REQUEST_BY_ID = "SELECT out_idsolicitud,out_tiposolicitud,out_idsolestado,out_plstx,out_pernr_reemplazo,out_name_reemplazo, out_plans from proceso.fn_get_requerimiento_persona_por_id(?)";

    private static final String GET_CODE_POSITION_REQUEST_BY_ID = "SELECT code_position_sol from proceso.fn_validar_code_position_sol(?)";

    private static final String GET_NUMBER_OF_REQUIREMENTS = "select * from proceso.fn_get_number_of_requirements(?)";

    private static final String GET_REQUESTS_TO_PRESIDENCY_RESPONSE = "SELECT idsolestado_out,idrol_out,email_out,ename_out,butxt_out,idsolicitud_out,plstx_out,orget_out from proceso.fn_get_requestlist_to_presidency_response()";

    @Override
    public long getRequestId() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet res = null;
        long requestIdGenerate = 0;
        try {
            statement = connection.prepareStatement(GET_ID_REQ);
            res = statement.executeQuery();
            if (res.next()) {
                requestIdGenerate = res.getLong(1);
            }
        } catch (SQLException e) {
            log.error("Error updating request status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return requestIdGenerate;
    }

    @Override
    public List<RequestControl> create(Request request, User user, long requestId) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement requestStatement = null;
        PreparedStatement requestFuncionStatement = null;
        PreparedStatement requestnewplans = null;
        PreparedStatement requestConocimientoStatement = null;
        PreparedStatement requestHabilidadesStatement = null;
        PreparedStatement requestFormacionStatement = null;
        PreparedStatement requestGetFormacionStatement = null;
        PreparedStatement requestInsertFormacionStatement = null;
        PreparedStatement requestGetUOStatement = null;
        PreparedStatement requestInsertUOStatement = null;
        PreparedStatement requestCentroEstudioStatement = null;
        PreparedStatement requestIdiomaStatement = null;
        PreparedStatement requestHistFlujoStatement = null;
        PreparedStatement requestHistFlujoStatementNext = null;
        PreparedStatement requestFileStatement = null;
        PreparedStatement requestReemplazoStatement = null;
        ResultSet setRequestId = null;
        ResultSet setPostTypeId = null;
        ResultSet setNewPlanscode = null;

        try {

            /* format object json to object java class */
            Characteristics characteristics = null;
            String characteristicsString=request.getCharacteristics().toString();

            try {
                if(characteristicsString.length() > 0){
                    ObjectMapper om = new ObjectMapper();
                    characteristics = om.readValue(request.getCharacteristics().toString(), Characteristics.class);
                }
            } catch (Exception e) {
                log.error("error en convertir string json en object java", e);
            }

            // if (characteristics == null) {
            //     throw new Exception("error en convertir data json a objeto");
            // }
            log.info("characteristicas");
            // log.info(characteristics.toString());
            String plansCode = request.getPlans();
            // nuevo plans
            if (request.getNewplans() > 0) {
                requestnewplans = connection.prepareStatement(
                        "select (max(plans)::integer +1)::character(8) codeplams from proceso.estructurapersona where plans<>'99999999'");
                setNewPlanscode = requestnewplans.executeQuery();
                if (setNewPlanscode.next()) {
                    plansCode = setNewPlanscode.getString(1);
                }

            }
            String newUOId = request.getOrganizationalUnitCode();
            if (request.isNewOrganizationalUnitCode()) {
                requestGetUOStatement = connection.prepareStatement(GET_MAX_ORGEH);
                ResultSet res = requestGetUOStatement.executeQuery();
                if (res.next()) {
                    newUOId = res.getString(1);
                }
                requestInsertUOStatement = connection.prepareStatement(INSERT_UO);
                requestInsertUOStatement.setString(1, request.getSocietyCode());
                requestInsertUOStatement.setString(2, request.getDivisionCode());
                requestInsertUOStatement.setString(3, request.getPhysicalLocationCode());
                requestInsertUOStatement.setString(4, newUOId);
                requestInsertUOStatement.setString(5, request.getOrganizationalUnitCode());
                requestInsertUOStatement.executeUpdate();
            }
            // create request
            requestStatement = connection.prepareStatement(CREATE_REQUEST);
            requestStatement.setInt(1, request.getType());
            requestStatement.setString(2, "preguntar");
            requestStatement.setString(3, request.getSocietyCode());
            requestStatement.setString(4, request.getDivisionCode());
            requestStatement.setString(5, request.getPhysicalLocationCode());
            requestStatement.setString(6, newUOId);
            requestStatement.setString(7, request.getCostCenterCode());
            requestStatement.setString(8, request.getCostCenterdesc());
            requestStatement.setString(9, request.getEmailSolicitante());/* email */
            requestStatement.setString(10, request.getAbkrs());
            requestStatement.setString(11, request.getDni());
            requestStatement.setString(12, request.getPernSup());
            requestStatement.setString(13, request.getSobid());
            requestStatement.setString(14, request.getStext3());
            requestStatement.setString(15, request.getAlcance());// alcance
            requestStatement.setString(16, request.getPernSolicitante());
            requestStatement.setString(17, request.getNameSolicitante());
            requestStatement.setString(18, request.getPlansSolicitante());
            requestStatement.setString(19, request.getPlstxSolicitante());
            requestStatement.setString(20, plansCode);
            requestStatement.setString(21, request.getPlstx());
            requestStatement.setInt(22, request.getQuantity());/* arreglar desde fromt */
            requestStatement.setInt(23, request.getFlagplanificado());/* arreglar desde fromt */
            requestStatement.setString(24, request.getContractType());
            requestStatement.setInt(25, request.getContractTime());/* idtiempocontrato */
            requestStatement.setString(26, request.getPernReemplazo());
            requestStatement.setString(27, request.getNameReemplazo());
            requestStatement.setString(28, request.getPlansReemplazo());
            requestStatement.setString(29, request.getPlstxReemplazo());
            requestStatement.setString(30, request.getJustification());

            requestStatement.setInt(31, request.getExperienceTime());
            requestStatement.setInt(32, request.getAgeRange());
            requestStatement.setString(33, request.getGenre());
            requestStatement.setString(34, request.getCivilStatus());

            requestStatement.setInt(35, request.getSearchType());
            requestStatement.setInt(36, 1);// creado
            requestStatement.setInt(37, 0);
            requestStatement.setString(38, request.getUsuarioCreacion());
            requestStatement.setString(39, request.getIpHost());
            requestStatement.setLong(40, requestId);
            
            log.info("request insert");
            log.info(requestStatement.toString());
            requestStatement.executeUpdate();

            // get request id
            // setRequestId = requestStatement.getGeneratedKeys();
            // setRequestId.next();
            // requestId = setRequestId.getLong(1);
            log.info("requestid");
            log.info(String.valueOf(requestId));

            if (request.getType() == 3) {
                // 1 nuevo , 2 nuevo no planificado , 3 reemplazo
                // validar si es uno a uno o en cascada los reemplazos
                log.info("insert reemplazo");
                requestReemplazoStatement = connection.prepareStatement(CREATE_REQUEST_REEMPLAZO);
                requestReemplazoStatement.setLong(1, requestId);
                requestReemplazoStatement.setString(2, request.getPernReemplazo());
                requestReemplazoStatement.setString(3, request.getApePatReemplazo());
                requestReemplazoStatement.setString(4, request.getApeMatReemplazo());
                requestReemplazoStatement.setString(5, request.getNameReemplazo());
                requestReemplazoStatement.setString(6, request.getPlansReemplazo());
                requestReemplazoStatement.setString(7, request.getPlstxReemplazo());
                requestReemplazoStatement.setString(8, request.getUsuarioCreacion());
                requestReemplazoStatement.setString(9, request.getIpHost());
                requestReemplazoStatement.executeUpdate();
            }

        if(characteristics != null) { 

            if (characteristics.getActivities().size() > 0) {
                log.info("insert funcion");

                for (Activities objeto : characteristics.getActivities()) {
                    requestFuncionStatement = connection.prepareStatement(CREATE_REQUEST_FUNCION);
                    requestFuncionStatement.setLong(1, requestId);
                    requestFuncionStatement.setString(2, objeto.getActivity());
                    requestFuncionStatement.setString(3, request.getUsuarioCreacion());
                    requestFuncionStatement.setString(4, request.getIpHost());
                    requestFuncionStatement.executeUpdate();
                }
            }
            if (characteristics.getKnowledgeRequirements().size() > 0) {
                log.info("insert conocimiento");
                for (knowledgeRequirements objeto : characteristics.getKnowledgeRequirements()) {
                    requestConocimientoStatement = connection.prepareStatement(CREATE_REQUEST_CONOCIMIENTO);
                    requestConocimientoStatement.setLong(1, requestId);
                    requestConocimientoStatement.setString(2, objeto.getKnowledgeRequirement());
                    requestConocimientoStatement.setString(3, request.getUsuarioCreacion());
                    requestConocimientoStatement.setString(4, request.getIpHost());
                    requestConocimientoStatement.executeUpdate();
                }
            }
            if (characteristics.getAbilityRequirements().size() > 0) {
                log.info("insert habilidades");
                for (AbilityRequirements objeto : characteristics.getAbilityRequirements()) {
                    requestHabilidadesStatement = connection.prepareStatement(CREATE_REQUEST_HABILIDADES);
                    requestHabilidadesStatement.setLong(1, requestId);
                    requestHabilidadesStatement.setString(2, objeto.getAbilityRequirement());
                    requestHabilidadesStatement.setString(3, request.getUsuarioCreacion());
                    requestHabilidadesStatement.setString(4, request.getIpHost());
                    requestHabilidadesStatement.executeUpdate();
                }
            }
            if (characteristics.getAcademicFormation().size() > 0) {
                /*
                 * school idespecialidad
                 * grade ideducacion
                 */
                log.info("insert formacion");
                for (AcademicFormation objeto : characteristics.getAcademicFormation()) {
                    String SchoolId = objeto.getSchool();
                    Integer valpos = 0;
                    if (objeto.isBoolInsert()) {
                        requestGetFormacionStatement = connection.prepareStatement(GET_MAX_FORMACION_ID);
                        ResultSet res = requestGetFormacionStatement.executeQuery();
                        if (res.next()) {
                            SchoolId = res.getString(1);
                            valpos = res.getInt(2);
                        }
                        requestInsertFormacionStatement = connection.prepareStatement(INSERT_FORMACION);
                        requestInsertFormacionStatement.setString(1, SchoolId);
                        requestInsertFormacionStatement.setString(2, objeto.getSchool());
                        requestInsertFormacionStatement.setInt(3, valpos);
                        requestInsertFormacionStatement.executeUpdate();
                    }
                    // implementar funcion nueva formacion
                    requestFormacionStatement = connection.prepareStatement(CREATE_REQUEST_FOMACION);
                    requestFormacionStatement.setLong(1, requestId);
                    requestFormacionStatement.setString(2, SchoolId);
                    requestFormacionStatement.setInt(3, Integer.valueOf(objeto.getGrade()));
                    requestFormacionStatement.setString(4, request.getUsuarioCreacion());
                    requestFormacionStatement.setString(5, request.getIpHost());
                    requestFormacionStatement.executeUpdate();
                }
            }

            if (characteristics.getStudyCenters().size() > 0) {
                /*
                 * centertyoe slart
                 * name idinstitucion
                 */
                log.info("insert centro estudio");
                for (StudyCenters objeto : characteristics.getStudyCenters()) {
                    requestCentroEstudioStatement = connection.prepareStatement(CREATE_REQUEST_CENTROESTUDIO);
                    requestCentroEstudioStatement.setLong(1, requestId);
                    requestCentroEstudioStatement.setString(2, objeto.getName());/* idinstitucion arreglar en front */
                    requestCentroEstudioStatement.setString(3, objeto.getCenterType());
                    requestCentroEstudioStatement.setString(4, request.getUsuarioCreacion());
                    requestCentroEstudioStatement.setString(5, request.getIpHost());
                    requestCentroEstudioStatement.executeUpdate();
                }
            }
            if (characteristics.getLanguages().size() > 0) {
                /*
                 * centertyoe slart
                 * name idinstitucion
                 */
                log.info("insert lenguajes");
                for (Languages objeto : characteristics.getLanguages()) {
                    requestCentroEstudioStatement = connection.prepareStatement(CREATE_REQUEST_IDIOMA);
                    requestCentroEstudioStatement.setLong(1, requestId);
                    requestCentroEstudioStatement.setString(2, objeto.getLanguage());
                    requestCentroEstudioStatement.setInt(3, Integer.valueOf(objeto.getLevel()));
                    requestCentroEstudioStatement.setString(4, request.getUsuarioCreacion());
                    requestCentroEstudioStatement.setString(5, request.getIpHost());
                    requestCentroEstudioStatement.executeUpdate();
                }
            }
            
        }

            if (request.getFilesObject().size() > 0) {
                log.info("insert files deploying s3");
                for (FileObject object : request.getFilesObject()) {
                    requestFileStatement = connection.prepareStatement(CREATE_REQ_FILE);
                    requestFileStatement.setLong(1, requestId);
                    requestFileStatement.setString(2, object.getNameArchivo());
                    requestFileStatement.setString(3, object.getNameArchivo());
                    requestFileStatement.setString(4, object.getRutaArchivo());
                    requestFileStatement.setString(5, request.getUsuarioCreacion());
                    requestFileStatement.setString(6, request.getIpHost());
                    requestFileStatement.executeUpdate();
                }
            }
            //TODO: Modify new request approve sup 

            //new

            log.info("requestId : {}",requestId);
            log.info("request.getObservation() : {}",request.getObservation());
            log.info("estado Req : {}",1);
            log.info("request.getPernSup() : {}",request.getPernSup());
            log.info("request.getRolSolicitante() : {}",request.getRolSolicitante());
            log.info("request.getPernSolicitante() : {}",request.getPernSolicitante());
            log.info("request.getPlstxSolicitante() : {}",request.getPlstxSolicitante());
            log.info("request.getNameSolicitante() : {}",request.getNameSolicitante());
            log.info("request.getIpHost() : {}",request.getIpHost());
            log.info("request.getType() : {}",request.getType());


            

            requestHistFlujoStatement = connection.prepareStatement(UPDATE_REQ);
            requestHistFlujoStatement.setInt(1, Math.toIntExact(requestId));
            requestHistFlujoStatement.setString(2, request.getObservation());
            requestHistFlujoStatement.setInt(3, 1);
            requestHistFlujoStatement.setString(4, request.getPernSup());
            requestHistFlujoStatement.setInt(5, request.getRolSolicitante());
            requestHistFlujoStatement.setString(6, request.getPernSolicitante());
            requestHistFlujoStatement.setString(7, request.getPlstxSolicitante());
            requestHistFlujoStatement.setString(8, request.getNameSolicitante());
            requestHistFlujoStatement.setString(9, request.getIpHost());
            requestHistFlujoStatement.setString(10, String.valueOf(request.getType()));
            requestHistFlujoStatement.executeQuery();

            //old

            // log.info("insert req hist flujo");
            // requestHistFlujoStatement = connection.prepareStatement(CREATE_REQ_HIST_FLUJO);
            // requestHistFlujoStatement.setLong(1, requestId);
            // requestHistFlujoStatement.setString(2, request.getPernSolicitante());
            // requestHistFlujoStatement.setLong(3, request.getRolSolicitante());
            // requestHistFlujoStatement.setLong(4, 0);
            // requestHistFlujoStatement.setString(5, "");
            // requestHistFlujoStatement.setString(6, request.getUsuarioCreacion());
            // requestHistFlujoStatement.setString(7, request.getIpHost());
            // requestHistFlujoStatement.executeUpdate();

            // log.info("insert req hist siguiente flujo ");
            // if (request.getPernSup() != null) {
            //     requestHistFlujoStatementNext = connection.prepareStatement(CREATE_REQ_HIST_FLUJO_SUPERIOR);
            //     requestHistFlujoStatementNext.setLong(1, requestId);
            //     requestHistFlujoStatementNext.setString(2, request.getPernSup());
            //     requestHistFlujoStatementNext.setLong(3, request.getRolSolicitanteSuperior());
            //     requestHistFlujoStatementNext.setLong(4, 0);
            //     requestHistFlujoStatementNext.setString(5, "");
            //     requestHistFlujoStatementNext.setString(6, request.getUsuarioCreacion());
            //     requestHistFlujoStatementNext.setString(7, request.getIpHost());
            //     requestHistFlujoStatementNext.executeUpdate();
            // }



            
            return findAllByParamsOnCourse(request.getPernSolicitante(), request.getSocietyCode());
        } catch (Exception e) {
            log.error("Error creating a new request", e);
            throw new RuntimeException(e);
        } finally {
            if (setPostTypeId != null)
                BaseDAO.close(setPostTypeId);
            if (setRequestId != null)
                BaseDAO.close(setRequestId);
            if (requestCentroEstudioStatement != null)
                BaseDAO.close(requestCentroEstudioStatement);
            if (requestIdiomaStatement != null)
                BaseDAO.close(requestIdiomaStatement);
            if (requestHabilidadesStatement != null)
                BaseDAO.close(requestHabilidadesStatement);
            if (requestFormacionStatement != null)
                BaseDAO.close(requestFormacionStatement);
            if (requestConocimientoStatement != null)
                BaseDAO.close(requestConocimientoStatement);
            if (requestFuncionStatement != null)
                BaseDAO.close(requestFuncionStatement);
            if (requestHistFlujoStatement != null)
                BaseDAO.close(requestHistFlujoStatement);
            // if (requestHistFlujoStatement != null)
            //     BaseDAO.close(requestHistFlujoStatementNext);
            if (requestFileStatement != null)
                BaseDAO.close(requestFileStatement);
            if (requestStatement != null)
                BaseDAO.close(requestStatement);
            if (requestInsertFormacionStatement != null)
                BaseDAO.close(requestInsertFormacionStatement);
            if (requestGetFormacionStatement != null)
                BaseDAO.close(requestGetFormacionStatement);
            if (requestGetUOStatement != null)
                BaseDAO.close(requestGetUOStatement);
            if (requestInsertUOStatement != null)
                BaseDAO.close(requestInsertUOStatement);

        }
    }

    @Override
    public void updateRequestStatus(Long requestId, RequestStatus status) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UPDATE_REQUEST_STATUS);
            statement.setInt(1, status.getValue());
            statement.setLong(2, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error updating request status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    @Override
    public List<Request> findById() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Request> lstrequest = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_BY_ID);
            // statement.setLong(1, id);
            // statement.setLong(2, id);
            set = statement.executeQuery();
            while (set.next()) {
                Request request = new Request();
                request.setId(set.getLong(1));
                request.setSocietyCode(set.getString(2));
                request.setQuantity(set.getInt(3));
                request.setOrganizationalUnitCode(set.getString(4));
                request.setCostCenterCode(set.getString(5));
                request.setRequestDate(set.getString(6));
                request.setTypeDesc(set.getString(7));
                request.setPlstx(set.getString(8));
                request.setBtext(set.getString(9));

                lstrequest.add(request);
            }
        } catch (SQLException e) {
            log.error("Error getting a request by id", e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }

        return lstrequest;
    }

    @Override
    public List<FileObject> getRutaFile(long requestId) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet res = null;
        String rutafile = "";
        List<FileObject> files = new ArrayList<>();
        StorageService storageService = new StorageService(
                AWS_S3_BATCH_FILES_BUCKET_NAME);
        try {
            statement = connection.prepareStatement(GET_RUTA_FILE);
            statement.setLong(1, requestId);
            res = statement.executeQuery();
            while (res.next()) {
                FileObject file = new FileObject();
                rutafile = res.getString(1);
                file.setRutaArchivo(storageService.getFileUri(rutafile));
                file.setNameArchivo(res.getString(2));
                log.info("fileobject{}", file);
                files.add(file);
            }
            log.info("fileslist{}", files);
        } catch (SQLException e) {
            log.error("Error updating request status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return files;
    }

    @Override
    public List<RequestControl> findAllByParams(String pern, String bukrs) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<RequestControl> lstrequest = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_REQUEST_BY_SOCIETY);
            statement.setString(1, pern);
            statement.setString(2, bukrs);
            log.info("requestall {}", statement.toString());
            // statement.setLong(1, id);
            // statement.setLong(2, id);
            set = statement.executeQuery();
            while (set.next()) {

                CharacteristicsQuery characteristics = null;
                Aprobadores aprobadores = null;
                try {
                    log.info("charcteristicas {}", set.getString(30));
                    ObjectMapper om = new ObjectMapper();
                    characteristics = om.readValue(set.getString(30), CharacteristicsQuery.class);
                    aprobadores = om.readValue(set.getString(31), Aprobadores.class);
                } catch (Exception e) {
                    log.error("error en convertir string json en object java", e);
                }
                log.info(characteristics.toString());

                RequestControl request = new RequestControl();
                request.setId(String.valueOf(set.getInt(1)));
                request.setState(set.getString(2));
                request.setSociety(set.getString(3));
                request.setPosition(set.getString(4));
                request.setTypeOfVacant(set.getString(5));
                request.setCodOfVacant(String.valueOf(set.getInt(6)));
                request.setOrgUnit(set.getString(7));
                request.setCenterOfCost(set.getString(8));
                request.setPhysicLocation(set.getString(9));
                request.setCategory(set.getString(10));
                request.setTypeOfContract(set.getString(11));
                request.setTimeOfContract(set.getString(12));
                request.setJustify(set.getString(13));
                request.setObservation(set.getString(14));
                request.setDateState(set.getString(15));
                request.setCheck(set.getBoolean(16));
                request.setQuantity(set.getInt(17));
                request.setDivision(set.getString(18));
                request.setTiempoExperiencia(set.getString(19));
                request.setRangoEdad(set.getString(20));
                request.setSexo(set.getString(21));
                request.setStatusCivil(set.getString(22));
                request.setNameSolicitante(set.getString(23));
                request.setCargoSolicitante(set.getString(24));
                request.setApePatReemplazo(set.getString(25));
                request.setApeMatReemplazo(set.getString(26));
                request.setNameReemplazo(set.getString(27));
                request.setPlansReemplazo(set.getString(28));
                request.setPlstxReemplazo(set.getString(29));
                request.setCharacteristics(characteristics);
                // request.setFiles(getRutaFile(set.getInt(1)));
                request.setAprobadores(aprobadores);
                log.info("requests{}", request);
                lstrequest.add(request);
            }
        } catch (SQLException e) {
            log.error("Error getting a request by id", e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }

        return lstrequest;
    }

    @Override
    public List<RequestControl> findAllByParamsOnCourse(String pern, String bukrs) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<RequestControl> lstrequest = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_REQUEST_BY_SOCIETY_ON_COURSE);
            statement.setString(1, pern);
            statement.setString(2, bukrs);
            log.info("requestall {}", statement.toString());
            // statement.setLong(1, id);
            // statement.setLong(2, id);
            set = statement.executeQuery();
            while (set.next()) {

                CharacteristicsQuery characteristics = null;
                Aprobadores aprobadores = null;
                try {
                    log.info("charcteristicas {}", set.getString(30));
                    ObjectMapper om = new ObjectMapper();
                    characteristics = om.readValue(set.getString(30), CharacteristicsQuery.class);
                    aprobadores = om.readValue(set.getString(31), Aprobadores.class);
                } catch (Exception e) {
                    log.error("error en convertir string json en object java", e);
                }
                log.info(characteristics.toString());

                RequestControl request = new RequestControl();
                request.setId(String.valueOf(set.getInt(1)));
                request.setState(set.getString(2));
                request.setSociety(set.getString(3));
                request.setPosition(set.getString(4));
                request.setTypeOfVacant(set.getString(5));
                request.setCodOfVacant(String.valueOf(set.getInt(6)));
                request.setOrgUnit(set.getString(7));
                request.setCenterOfCost(set.getString(8));
                request.setPhysicLocation(set.getString(9));
                request.setCategory(set.getString(10));
                request.setTypeOfContract(set.getString(11));
                request.setTimeOfContract(set.getString(12));
                request.setJustify(set.getString(13));
                request.setObservation(set.getString(14));
                request.setDateState(set.getString(15));
                request.setCheck(set.getBoolean(16));
                request.setQuantity(set.getInt(17));
                request.setDivision(set.getString(18));
                request.setTiempoExperiencia(set.getString(19));
                request.setRangoEdad(set.getString(20));
                request.setSexo(set.getString(21));
                request.setStatusCivil(set.getString(22));
                request.setNameSolicitante(set.getString(23));
                request.setCargoSolicitante(set.getString(24));
                request.setApePatReemplazo(set.getString(25));
                request.setApeMatReemplazo(set.getString(26));
                request.setNameReemplazo(set.getString(27));
                request.setPlansReemplazo(set.getString(28));
                request.setPlstxReemplazo(set.getString(29));
                request.setCharacteristics(characteristics);
                // request.setFiles(getRutaFile(set.getInt(1)));
                request.setAprobadores(aprobadores);
                log.info("requests{}", request);
                lstrequest.add(request);
            }
        } catch (SQLException e) {
            log.error("Error getting a request by id", e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }

        return lstrequest;
    }


    @Override
    public List<RequestControl> updateRequest(RequestList requestlist, String host) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        int code = 1;
        int codeError = 1;
        String messageError = "";
        List<RequestControl> response = null;
        String perns = "";
        String bukrs = "";

        try {
            for (RequestControl request : requestlist.getRequest()) {

                log.info("request.getId(): {}",request.getId());
                log.info("request.getObservation(): {}",request.getObservation());
                log.info("request.getPostType(): {}",request.getPostType());
                log.info("request.getCodeSolicitanteSuperior(): {}",request.getCodeSolicitanteSuperior());
                log.info("request.getRolSolicitante(): {}",request.getRolSolicitante());
                log.info("request.getCodeSolicitante(): {}",request.getCodeSolicitante());
                log.info("request.getCargoSolicitante(): {}",request.getCargoSolicitante());
                log.info("request.getNameSolicitante(): {}",request.getNameSolicitante());
                log.info("host: {}",host);
                log.info("request.getTypeRequirement(): {}", request.getTypeRequirement());

                perns = request.getCodeSolicitante();
                statement = connection.prepareStatement(UPDATE_REQ);
                statement.setInt(1, Integer.valueOf(request.getId()));
                statement.setString(2, request.getObservation());
                statement.setInt(3, request.getPostType());
                statement.setString(4, request.getCodeSolicitanteSuperior());
                statement.setInt(5, request.getRolSolicitante());
                statement.setString(6, request.getCodeSolicitante());
                statement.setString(7, request.getCargoSolicitante());
                statement.setString(8, request.getNameSolicitante());
                statement.setString(9, host);
                statement.setString(10, request.getTypeRequirement());
                                
                set = statement.executeQuery();
                if (set.next()) {
                    code = set.getInt(1);
                    codeError = code == 0 ? 0 : 1;
                    messageError = set.getString(2);

                }
            }
            if (codeError != 0) {
                log.info("error en actualizar error: {}", messageError);
                throw new Exception("Error en actualizar requerimiento: " + messageError);
            }
            response = findAllByParamsOnCourse(perns, bukrs);// por ahora trae todo en login se implementara codigo

        } catch (Exception e) {
            log.error("Error updating request status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return response;
    }

    public String getPernsSuperior(String pernr) {
        return "";
    }

    public String calcularWorflow(Request request) {
        return "";
    }

    @Override
    public User getNextMailApprove(String userID) {

        log.info(userID);

        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        //int code = 1;
        // int codeError = 1;
        // String messageError = "";

        User user = new User();

        try {
                statement = connection.prepareStatement(GET_EMAIL_NEXT_APPROVE);
                statement.setString(1, userID);
                set = statement.executeQuery();
                if (set.next()) {
                    user.setEmail(set.getString("email_sup_out"));
                    user.setRoleCode(set.getInt("codrol_sup_out"));
                    user.setFullName(set.getString("fullname_out"));
                    //codeError = code == 0 ? 0 : 1;
                    //messageError = set.getString(2);

                }
            
            // if (codeError != 0) {
            //     log.info("error en lectura error: {}", messageError);
            //     throw new Exception("Error en lectura: " + messageError);
            // }
            

        } catch (Exception e) {
            log.error("Error en lectura status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return user;
    }

    @Override
    public MailTemplate getMailTemplate(Integer tiporequerimiento, Integer accion,Integer estadorequerimiento,Integer tipoplantilla) {
        // TODO Auto-generated method stub

        MailTemplate mailTemplate = new MailTemplate();
        log.info("MailTemplate getMailTemplate(tiporequerimiento:{},accion:{},estadorequerimiento:{},tipoplantilla:{})",tiporequerimiento,accion,estadorequerimiento,tipoplantilla);

        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;

        try {
                statement = connection.prepareStatement(GET_EMAIL_TEMPLATE);
                statement.setInt(1, tiporequerimiento);
                statement.setInt(2, accion);
                statement.setInt(3, estadorequerimiento);
                statement.setInt(4, tipoplantilla);
                set = statement.executeQuery();
                if (set.next()) {
                    mailTemplate.setSubject(set.getString(1));
                    mailTemplate.setContentTemplate(set.getString(2));
                }
            
            

        } catch (Exception e) {
            log.error("Error en lectura status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return mailTemplate;
    }

    @Override
    public Request findById(Long id) {
        // TODO Auto-generated method stub
        Request request = new Request();

        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;

        try {
                statement = connection.prepareStatement(GET_REQUEST_BY_ID);
                statement.setLong(1, id);
                set = statement.executeQuery();
                if (set.next()) {
                    request.setId(set.getLong("out_idsolicitud"));
                    request.setType(set.getInt("out_tiposolicitud"));
                    request.setStatus(set.getInt("out_idsolestado"));
                    request.setPlstx(set.getString("out_plstx"));
                    request.setPernReemplazo(set.getString("out_pernr_reemplazo"));
                    request.setNameReemplazo(set.getString("out_name_reemplazo"));
                    request.setPlans(set.getString("out_plans"));
                }
        } catch (Exception e) {
            log.error("Error en lectura :", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return request;
    }


    @Override
    public Integer findByPositionCodeNivelApprove(Integer id){

         Connection connection = BaseDAO.getConnection();
         PreparedStatement statement = null;
         ResultSet set = null;
         Integer codePositionRequest = 0;
 
         try {
                 statement = connection.prepareStatement(GET_CODE_POSITION_REQUEST_BY_ID);
                 statement.setInt(1, id);
                 set = statement.executeQuery();
                 if (set.next()) {
                    codePositionRequest = set.getInt("code_position_sol");
                 }
         } catch (Exception e) {
             log.error("Error en lectura :", e);
             throw new RuntimeException();
         } finally {
             if (statement != null)
                 BaseDAO.close(statement);
         }
         return codePositionRequest;
    }
    
    @Override 
    public void findRequestReemplCreatedRepeat(String plans, String name_reemplazo, Integer idsolestado){
        
    }

    @Override
    public String getNivelApprove(String posicion_code) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet res = null;
        String requestNivelPosition = "";

        try {
            statement = connection.prepareStatement(GET_NIVEL_APROBACION);
            statement.setString(1, posicion_code);
            res=statement.executeQuery();
            if (res.next()) {
                requestNivelPosition = res.getString("code_nivel_aprobacion");
            }
        } catch (SQLException e) {
            log.error("Error al mapear nivel de aprobacion", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return requestNivelPosition;        
    }
    
   
    @Override
    public Integer getNumberOfRequirements(Integer roleCode) {
        
        log.info("{}",roleCode);

        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        //int code = 1;
        // int codeError = 1;
        // String messageError = "";

        Integer numberOfRequirements = 0;

        try {
                statement = connection.prepareStatement(GET_NUMBER_OF_REQUIREMENTS);
                statement.setInt(1, roleCode);
                set = statement.executeQuery();
                if (set.next()) {
                    numberOfRequirements= set.getInt("out_number_of_requirements");
                    //codeError = code == 0 ? 0 : 1;
                    //messageError = set.getString(2);

                }
            
            // if (codeError != 0) {
            //     log.info("error en lectura error: {}", messageError);
            //     throw new Exception("Error en lectura: " + messageError);
            // }
            

        } catch (Exception e) {
            log.error("Error en lectura status", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return numberOfRequirements;
    }

    @Override
    public List<Request> getRequestToPresidencyRespond() {

        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        //int code = 1;
        // int codeError = 1;
        // String messageError = "";

        List<Request> requestList = new ArrayList<>();

        try {
                statement = connection.prepareStatement(GET_REQUESTS_TO_PRESIDENCY_RESPONSE);
                set = statement.executeQuery();
                while (set.next()) {

                    log.info("id : {}", set.getLong("idsolicitud_out"));
                    Request request = new Request();

                    request.setStatus(set.getInt("idsolestado_out"));
                    request.setRolSolicitante(set.getInt("idrol_out"));
                    request.setEmailSolicitante(set.getString("email_out"));
                    request.setEname(set.getString("ename_out"));
                    request.setButxt(set.getString("butxt_out"));
                    request.setId(set.getLong("idsolicitud_out"));
                    request.setPlstx(set.getString("plstx_out"));
                    request.setOrget(set.getString("orget_out"));

                    requestList.add(request);
                    //codeError = code == 0 ? 0 : 1;
                    //messageError = set.getString(2);
                }



        } catch (Exception e) {
            log.error("Error en lectura", e);
            throw new RuntimeException();
        } finally {
            if (statement != null)
                BaseDAO.close(statement);
        }
        return requestList;
    }
}
