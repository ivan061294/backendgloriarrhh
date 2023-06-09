package pe.com.centro.modules.request;

import pe.com.centro.domain.FileObject;
import pe.com.centro.domain.MailRequest;
import pe.com.centro.domain.MailTemplate;
import pe.com.centro.domain.Request;
import pe.com.centro.domain.RequestControl;
import pe.com.centro.domain.RequestList;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.RequestStatus;

import java.util.List;

/**
 * Data Access Object for Request's operations.
 */
public interface RequestDAO {

     long getRequestId();
    List<RequestControl> create(Request request, User user,long RequestId);

    List<RequestControl> findAllByParams(String pern,String bukrs);//mostrar todas las solicitudes

    List<RequestControl> findAllByParamsOnCourse(String pern,String bukrs);//mostrar solicitudes en curso

    List<RequestControl> updateRequest(RequestList requestlist,String host);

    /**
     * Updates request's status
     *
     * @param requestId the Request's id.
     * @param status    the new status.
     */
    void updateRequestStatus(Long requestId, RequestStatus status);

    /**
     * Finds a request by id
     *
     * @param id the Request's id
     * @return the found (or not found) request
     */
    List<Request> findById();

    /**
     * Finds a request by id
     *
     * @param id the Request's id
     * @return the found (or not found) request
     */
    Request findById(Long id);

    List<FileObject> getRutaFile(long requestId);

    /**
     * Finds a next mail user approve
     *
     * 
     * 
     */

    User getNextMailApprove(String userID);

    MailTemplate getMailTemplate(Integer tiporequerimiento, Integer accion,Integer estadorequerimiento,Integer tipoplantilla);
    Integer getNumberOfRequirements(Integer roleCode);
    List<Request> getRequestToPresidencyRespond();
    String getNivelApprove(String posicion_code);
    Integer findByPositionCodeNivelApprove(Integer id);
    void findRequestReemplCreatedRepeat(String plans, String name_reemplazo, Integer idsolestado);
       
}

