package pe.com.centro.modules.request;

import com.amazonaws.ResetException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.util.Base64;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.MultipartStream;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.FileObject;
import pe.com.centro.domain.MailRequest;
import pe.com.centro.domain.MailTemplate;
import pe.com.centro.domain.Request;
import pe.com.centro.domain.RequestControl;
import pe.com.centro.domain.RequestList;
import pe.com.centro.domain.RequirementResponse;
import pe.com.centro.domain.User;
import pe.com.centro.domain.enumeration.Role;
import pe.com.centro.utils.MailService;
import pe.com.centro.utils.Parser;
import pe.com.centro.utils.Serializer;
import pe.com.centro.utils.StorageService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static pe.com.centro.utils.CognitoUtils.getAuthenticatedUser;
import static pe.com.centro.utils.Serializer.deserialize;
import static pe.com.centro.utils.Constants.*;

import static java.lang.System.getenv;

@Slf4j
public class PostRequest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String WEB_URL = getenv("AWS_CLOUDFRONT_WEB_URL");
    String rowTemplateConcat="";
    Request requirement = null;
    RequestList requirementList = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = processMPFBMT(input,context);
       
        return apiGatewayProxyResponseEvent;
    }

    // private APIGatewayProxyResponseEvent processNoBMT(APIGatewayProxyRequestEvent input, Context context)
    // {

    //     try {

            
    //         log.debug("REST Request to post a new requirement request");
    //         log.info("headers {}", input.getHeaders());
    //         //log.info("input {}",input.getBody());
    //         //log.info(new String(input.getBody().getBytes(),StandardCharsets.ISO_8859_1));
            
    
    //         String forwarderHeader = input.getHeaders().get("X-Forwarded-For");
    //         String ipHost = forwarderHeader;
    //         String contentType = (input.getHeaders().get(Headers.CONTENT_TYPE)) != null
    //                 ? input.getHeaders().get(Headers.CONTENT_TYPE)
    //                 : input.getHeaders().get("content-type");
    //         if (forwarderHeader.contains(",")) {
    //             String[] ipSplit = forwarderHeader.split(",");
    //             ipHost = ipSplit[0];
    //         }
    //         ;
    //         log.info("forwarderHeader: {}",forwarderHeader);
    //         log.info("ipHost: {}", ipHost);
    //         log.info("contentType: {}", contentType);
    //         log.info("input size: {}",input.getBody().getBytes().length);
    
    //         // Get user from Cognito*/
    //         User user = getAuthenticatedUser(input, context);
    
    //         if (!Role.REQUESTER.equals(user.getRole())) {
    //             return new APIGatewayProxyResponseEvent()
    //                     .withHeaders(DEFAULT_HEADERS)
    //                     .withStatusCode(401)
    //                     .withBody(Serializer.serialize(new ErrorResponse(
    //                             "Solo un usuario con rol de Solicitante puede solicitar requerimientos")));
    //         }
    //         RequestDAO dao = new RequestDAOImpl();
    //         Request request = null;
    
    //         if (contentType.equalsIgnoreCase("application/json")) {
    //             RequestList lstrequest = deserialize(input.getBody(), RequestList.class);
    //             lstrequest.getRequest().forEach(e -> log.info("request-control {}", e.toString()));
    //             //Update request
    //             List<RequestControl> response = dao.updateRequest(lstrequest, ipHost);
    
    //             if (response == null || response.size() == 0) {
    //                 log.info("if (response == null || response.size() == 0) -> true");
    //                 return new APIGatewayProxyResponseEvent()
    //                         .withHeaders(DEFAULT_HEADERS)
    //                         .withStatusCode(500)
    //                         .withBody(Serializer.serialize(Map.of("message", "updated error")));
    //             }
    //             // verify send mail
    //             lstrequest.getRequest().forEach(e -> {
    //                 if (e.isIsmail()) {
    //                     log.info("sendmail type {}", e.getPostType());
    //                     //String to = dao.getNextMailApprove(e.getCodeSolicitante());
    //                     String to = "gustavo.cueto@centro.com.pe";
    //                     switch (e.getPostType()) {
    //                         case 3:
    //                             //MailService.sendMailForApprovedRequirement(to);
    //                             break;
    //                         case 5:
    //                             //MailService.sendMailForRejectRequirement(to);
    //                             break;
    //                         case 6:
    //                             //MailService.sendMailForObservedRequirement(to);
    //                             break;
    
    //                     }
    //                 }
    //             });
    //             // Response Parsing
    //             log.info("APIGatewayProxyResponseEvent :201");
    //             return new APIGatewayProxyResponseEvent()
    //                     .withHeaders(DEFAULT_HEADERS)
    //                     .withStatusCode(201)
    //                     .withBody(Serializer.serialize(response));
    //         }
    //         long RequestId = dao.getRequestId();
    
    //         request = this.parseFormBody(input.getBody().getBytes(), contentType, RequestId);
    //         request.setIpHost(ipHost);
    
    //         // Store the request into db
    //         log.info("request");
    //         log.info(request.toString());
    //         List<RequestControl> requestId = dao.create(request, user, RequestId);
    //         //TODO:send next user
    //         String mailSup = dao.getNextMailApprove(request.getPernSolicitante());
    //         log.info("enviando mail");
    //         //MailService.sendMailForNewRequirement(mailSup);
    //         log.info("mail enviado con exito");
    //         log.info("requestid");
    //         log.info(String.valueOf(requestId));
    
    //         // Response Parsing
    //         return new APIGatewayProxyResponseEvent()
    //                 .withHeaders(DEFAULT_HEADERS)
    //                 .withStatusCode(201)
    //                 .withBody(Serializer.serialize(requestId));

    //     } catch (IOException | InvocationTargetException | IllegalAccessException e) {
    //         return new APIGatewayProxyResponseEvent()
    //                 .withHeaders(DEFAULT_HEADERS)
    //                 .withStatusCode(400)
    //                 .withBody(Serializer.serialize(new ErrorResponse("No se encontró contenido")));
    //     }

    // }


    private APIGatewayProxyResponseEvent processMPFBMT(APIGatewayProxyRequestEvent input, Context context)
    {

        try {

            
            log.debug("REST Request to post a new requirement request");
            log.info("headers {}", input.getHeaders());
            //log.info("input {}",input.getBody());
            //log.info(new String(input.getBody().getBytes(),StandardCharsets.ISO_8859_1));
            
    
            String forwarderHeader = input.getHeaders().get("X-Forwarded-For");
            String ipHost = forwarderHeader;
            String contentType = (input.getHeaders().get(Headers.CONTENT_TYPE)) != null
                    ? input.getHeaders().get(Headers.CONTENT_TYPE)
                    : input.getHeaders().get("content-type");
            if (forwarderHeader.contains(",")) {
                String[] ipSplit = forwarderHeader.split(",");
                ipHost = ipSplit[0];
            }
            ;
            log.info("forwarderHeader: {}",forwarderHeader);
            log.info("ipHost: {}", ipHost);
            log.info("contentType: {}", contentType);
            log.info("input size: {}",input.getBody().getBytes().length);
    
            // Get user from Cognito*/
            User user = getAuthenticatedUser(input, context);
    
            if (!Role.REQUESTER.equals(user.getRole())) {
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(401)
                        .withBody(Serializer.serialize(new ErrorResponse(
                                "Solo un usuario con rol de Solicitante puede solicitar requerimientos")));
            }
            
            RequestDAO dao = new RequestDAOImpl();
            Request request = null; //! Variable para obtener el tipo de requerimiento (1 = NUEVO PLANIFICADO, 2 = NUEVO NO PLANIFICADO, 3 = REEMPLAZO)
            Integer codpositionRequest = 0; //! Variable para obtener el codigo de la posicion del requerimiento a traves del metodo findByPositionCodeNivelApprove por medio del ID
            Integer nivel_aprobacion = 0; //! Variable para obtener el nivel de aprobacion segun el cargo/posicion del requerimiento en cuestion


            if (contentType.equalsIgnoreCase("application/json")) {

                RequestList lstrequest = deserialize(input.getBody(), RequestList.class);                
                lstrequest.getRequest().forEach(e -> log.info("request-control {}", e.toString()));

                //Update request
                List<RequestControl> response = dao.updateRequest(lstrequest, ipHost);            

                if (response == null || response.size() == 0) {
                    log.info("if (response == null || response.size() == 0) -> true");

                    return new APIGatewayProxyResponseEvent()
                            .withHeaders(DEFAULT_HEADERS)
                            .withStatusCode(500)
                            .withBody(Serializer.serialize(Map.of("message", "updated error")));
                }

                // FIXME:verify send mail massive

                log.info("lstrequest : {}", lstrequest.getRequest().size());

                //! Aprobaciones de manera individual
                if(lstrequest.getRequest().size()==1)
            {

                if (lstrequest.getRequest().get(0).isIsmail()) {                        
                        
                    //! Obtener el tipo de requerimiento con request.getType() == 1 PLANIFICADO, 2 NO PLANIFICADO, 3 REEMPLAZO
                    request = dao.findById(Long.parseLong(lstrequest.getRequest().get(0).getId()));
                    
                    //! Obtener el codigo de la persona requerida e identificar de qué cargo es (1 = CARGO SUPERIOR, 2 = CARGO INFERIOR (ANALISTA a OBRERO))
                    codpositionRequest = dao.findByPositionCodeNivelApprove(Integer.parseInt(lstrequest.getRequest().get(0).getId()));
                    nivel_aprobacion = Integer.parseInt(dao.getNivelApprove(String.valueOf(codpositionRequest)));
                    
                    log.info("codpositionRequest : {}", codpositionRequest);                                             
                    log.info("nivel_aprobacion : {}", nivel_aprobacion);                                                                                         
                    
                    
                    //! EVALUAR LOS ESCENARIOS DEL FLUJO COMPLETO

                    //Si aprueba presidencia (Ejemplo: JORGE COLUMBO) con rol = 6 así sea PLANIFICADO, NO PLANIFICADO Y REEMPLAZO
                    if(lstrequest.getRequest().get(0).getRolSolicitante()==6)
                    {

                        mailFactory(   
                            lstrequest
                        );

                    }

                    //! DE LO CONTRARIO, aplicar las restrincciones que se soliciten
                    else    
                    {
                        //! PRIMER REQUERIEMIENTO => Req. Tipo Reemplazo(3) / G.G rol 3 
                        //! y que esté aprobando getPostType()=3
                        if (lstrequest.getRequest().get(0).getRolSolicitante() == 3 && 
                            request.getType() == 3 && lstrequest.getRequest().get(0).getPostType()==3) {

                            log.info("ENTRO AL PRIMER REQUERIMIENTO : {}", 1);

                            mailFactory(
                                lstrequest
                            );
                        }
                        

                        //! SEGUNDO REQUERIMIENTO(1) => Req. Tipo Vacantes Planificada(1) / G.G con rol 3
                        //! y que esté aprobando getPostType()=3
                        else if(lstrequest.getRequest().get(0).getRolSolicitante() == 3 && 
                                request.getType() == 1 && lstrequest.getRequest().get(0).getPostType()==3){
            
                            log.info("ENTRO AL SEGUNDO REQUERIMIENTO - Vacantes Nuevas Planificadas Plan 2023 : {}", 2);

                            mailFactory(
                                lstrequest
                            ); 
                        }


                        //! SEGUNDO REQUERIMIENTO(2) => Req. Tipo Vacantes No Planificada(2) / G.G con rol 3 
                        //! y de cargo: ANALISTA a OBRERO o sea nivel_aprobacion = 2 segun tabla creada agrupacion_puesto 
                        //! y que esté aprobando getPostType()=3                           
                        else if(lstrequest.getRequest().get(0).getRolSolicitante() == 3 && 
                            request.getType() == 2 && nivel_aprobacion==2 &&
                            lstrequest.getRequest().get(0).getPostType() == 3){
                            
                            log.info("ENTRO AL SEGUNDO REQUERIMIENTO - No Planificadas de Analistas a Obreros : {}", 2);

                            mailFactory(
                                lstrequest
                            ); 

                        }

                
                
                        //! TERCER REQUERIMIENTO => Req. Vacantes no planificadas(2) 
                        //! y de cargo: ESPECIALISTA A MAYOR CARGO (director/G.G) o sea nivel_aprobacion = 1 segun tabla creada agrupacion_puesto
                        //! Pero con el flujo normal, o sea que llegue hasta PRESIDENCIA DE JORGE RODRIGUEZ COLUMBO
                        //! y que esté aprobando getPostType()=3                           
                        else if(lstrequest.getRequest().get(0).getRolSolicitante() == 6 && 
                            request.getType() == 2  && nivel_aprobacion==1 &&
                            lstrequest.getRequest().get(0).getPostType()==3 )
                        {
                        
                            log.info("ENTRO AL TERCER REQUERIMIENTO : {}", 3);
                            
                            mailFactory(
                                lstrequest
                                ); 
                        }


                        //!SINO, EN ESTE CASO SE RECHAZA
                        else if(lstrequest.getRequest().get(0).getPostType()==5)
                        {
                            //TODO: Respuesta rechazo
                            mailFactory(   
                                lstrequest
                            );
                        }


                        //! SI NO SE CUMPLE NINGUNA DE LAS ANTERIORES RESTRICCIONES, ENTONCES
                        //! ENTRA EL FLUJO NORMAL SIN NINGUNA RESTRICCIÓN
                        else {

                            log.info("ENTRO AL FLUJO NORMAL EXISTENTE : {}", 0);
                            log.info("sendmail type {}", lstrequest.getRequest().get(0).getPostType());                                
                            Integer tipoplantilla = 1; //add global var                                
                            mailFactory(   lstrequest.getRequest().get(0).getPostType(),
                                            Long.parseLong(lstrequest.getRequest().get(0).getId()),
                                            lstrequest.getRequest().get(0).getCodeSolicitante(),
                                            tipoplantilla
                                        );
                            }                       
                                                    
                    }
                }

            }




                //! Aprobaciones de manera masiva
                else if(lstrequest.getRequest().size()>1){
                    
                    
                    if (lstrequest.getRequest().get(0).isIsmail()) {

                       //! Obtener el tipo de requerimiento con request.getType() == 1 PLANIFICADO, 2 NO PLANIFICADO, 3 REEMPLAZO
                       request = dao.findById(Long.parseLong(lstrequest.getRequest().get(0).getId()));
                       //! Obtener el codigo de la persona requerida e identificar de qué cargo es (1 = CARGO SUPERIOR, 2 = CARGO INFERIOR (ANALISTA a OBRERO))
                       codpositionRequest = dao.findByPositionCodeNivelApprove(Integer.parseInt(lstrequest.getRequest().get(0).getId()));
                       nivel_aprobacion = Integer.parseInt(dao.getNivelApprove(String.valueOf(codpositionRequest)));
                       
                       log.info("codpositionRequest : {}", codpositionRequest);                                             
                       log.info("nivel_aprobacion : {}", nivel_aprobacion);   



                        //! EVALUAR LOS ESCENARIOS DEL FLUJO COMPLETO

                        //Si aprueba presidencia (Ejemplo: JORGE COLUMBO) con rol = 6 así sea PLANIFICADO, NO PLANIFICADO Y REEMPLAZO
                        if(lstrequest.getRequest().get(0).getRolSolicitante()==6)
                        {

                            Integer tipoplantilla = 3; //add global var
                            
                            mailFactory(   
                                            lstrequest
                                        );
                        }
                        
                        //! DE LO CONTRARIO, aplicar las restrincciones que se soliciten
                        else
                        {


                            // ! PRIMER REQUERIEMIENTO => Req. Tipo Reemplazo(3) / G.G rol 3
                            // ! y que esté aprobando getPostType()=3
                            if (lstrequest.getRequest().get(0).getRolSolicitante() == 3 &&
                                    request.getType() == 3 && lstrequest.getRequest().get(0).getPostType() == 3)
                            {

                                log.info("ENTRO AL PRIMER REQUERIMIENTO CON APROBACION DE MANERA MASIVA: {}", 1);

                                mailFactory(
                                        lstrequest);

                            }




                            // ! SEGUNDO REQUERIMIENTO(1) => Req. Tipo Vacantes Planificada(1) / G.G con rol
                            // 3
                            // ! y que esté aprobando getPostType()=3
                            else if (lstrequest.getRequest().get(0).getRolSolicitante() == 3 &&
                                    request.getType() == 1 && lstrequest.getRequest().get(0).getPostType() == 3) 
                            {

                                log.info("ENTRO AL SEGUNDO REQUERIMIENTO CON APROBACION DE MANERA MASIVA - Vacantes Nuevas Planificadas Plan 2023 : {}",
                                        2);

                                mailFactory(
                                        lstrequest);
                            }




                            // ! SEGUNDO REQUERIMIENTO(2) => Req. Tipo Vacantes No Planificada(2) / G.G con
                            // rol 3
                            // ! y de cargo: ANALISTA a OBRERO o sea nivel_aprobacion = 2 segun tabla creada
                            // agrupacion_puesto
                            // ! y que esté aprobando getPostType()=3
                            else if (lstrequest.getRequest().get(0).getRolSolicitante() == 3 &&
                                    request.getType() == 2 && nivel_aprobacion == 2 &&
                                    lstrequest.getRequest().get(0).getPostType() == 3) 
                            {

                                log.info("ENTRO AL SEGUNDO REQUERIMIENTO CON APROBACION DE MANERA MASIVA - No Planificadas de Analistas a Obreros : {}",
                                        2);

                                mailFactory(
                                        lstrequest);

                            }



                            //! TERCER REQUERIMIENTO => Req. Vacantes no planificadas(2) 
                            //! y de cargo: ESPECIALISTA A MAYOR CARGO (director/G.G) o sea nivel_aprobacion = 1 segun tabla creada agrupacion_puesto
                            //! Pero con el flujo normal, o sea que llegue hasta PRESIDENCIA DE JORGE RODRIGUEZ COLUMBO
                            //! y que esté aprobando getPostType()=3                           
                            else if(lstrequest.getRequest().get(0).getRolSolicitante() == 6 && 
                                request.getType() == 2  && nivel_aprobacion==1 &&
                                lstrequest.getRequest().get(0).getPostType()==3)
                            {
                            
                                log.info("ENTRO AL TERCER REQUERIMIENTO CON APROBACION DE MANERA MASIVA : {}", 3);
                                
                                mailFactory(
                                    lstrequest
                                ); 
                            }



                            //!SINO, EN ESTE CASO SE RECHAZA
                            else if(lstrequest.getRequest().get(0).getPostType()==5)
                            {
                                //TODO: Respuesta rechazo
                                mailFactory(   
                                    lstrequest
                                );
                            }



                        //! SI NO SE CUMPLE NINGUNA DE LAS ANTERIORES RESTRICCIONES, ENTONCES
                        //! ENTRA EL FLUJO NORMAL SIN NINGUNA RESTRICCIÓN APROBANDO MASIVAMENTE
                        else {

                            log.info("ENTRO AL FLUJO NORMAL EXISTENTE CON APROBACION DE MANERA MASIVA : {}", 0);
                            log.info("sendmail type {}", lstrequest.getRequest().get(0).getPostType());                                
                            Integer tipoplantilla = 2; //add global var                                
                            mailFactory(   lstrequest.getRequest().get(0).getPostType(),
                                            Long.parseLong(lstrequest.getRequest().get(0).getId()),
                                            lstrequest.getRequest().get(0).getCodeSolicitante(),
                                            tipoplantilla
                                        );
                            }
                            
                        }
                    } 
                    
                    
                }
                
                
                // if (lstrequest.getRequest().get(0).getRolSolicitante() == 3 && 
                // request.getType() == 3)



                // lstrequest.getRequest().forEach(e -> {
                //     if (e.isIsmail()) {
                //         log.info("sendmail type {}", e.getPostType());
                //         enviarCorreo(e.getPostType(),Long.parseLong(e.getId()),e.getCodeSolicitante());
                //     }
                // });


                // Response Parsing
                log.info("APIGatewayProxyResponseEvent :201");
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(201)
                        .withBody(Serializer.serialize(response));
            }
            long RequestId = dao.getRequestId();
    
            request = this.parseFormBodyV2(input.getBody().getBytes(), contentType, RequestId);
            request.setIpHost(ipHost);
            
            // Store the request into db
            log.info("request");
            log.info(request.toString());
            List<RequestControl> requestId = dao.create(request, user, RequestId);


            //FIXME: Envío de correo para creación,

            // MailTemplate mailTemplate = dao.getTemplate(    String.valueOf(request.getPostType()), 
            // String.valueOf(user.getRole().getValue()),
            // e.getId());

            //TODO:Obtener datos faltantes / Considerar una aprobación incremental (*)
            
            

            //

            // MailDetailFlow mailDetailFlow = new MailDetailFlow();
            //             mailDetailFlow.setTo(user.getEmail());//
            //             mailDetailFlow.setPosition(request.getPlstx());
            //             mailDetailFlow.setSubstitution(String.format("%s - %s %s, %s",
            //                                                     "",//codigo de trabajador
            //                                                     request.getApePatReemplazo(),
            //                                                     request.getApeMatReemplazo(),
            //                                                     request.getNameReemplazo()));
            //             mailDetailFlow.setUrl(WEB_URL);
            //             // mailDetailFlow.setMailTamplete(mailTemplate);//

            //             mailDetailFlow.setAction(request.getType());
            //             mailDetailFlow.setTargetRole(request.getRolSolicitante());
            //             mailDetailFlow.setRequirementStatus(request.getPlans());//desde BD
            //             mailDetailFlow.setRequirementType(request.get());//desde BD
            //Integer tipoplantilla = 1; //add global var
            mailFactory(Integer.valueOf(String.valueOf(request.getType())+3),RequestId,request.getPernSolicitante(),1);
            mailFactory(request.getType(),RequestId,1);
            // //String to = dao.getNextMailApprove(request.getPernSolicitante());
            // String to = "gustavo.cueto@centro.com.pe";
            // //carga de mailVariable

            // MailDetailFlow mailVariable = new MailDetailFlow();
            // mailVariable.setTo(to);
            // mailVariable.setPuesto(request.getPlstx());
            // mailVariable.setReemplazo(String.format("%s - %s %s, %s",
            //                                         request.getPernReemplazo(),
            //                                         request.getApePatReemplazo(),
            //                                         request.getApeMatReemplazo(),
            //                                         request.getNameReemplazo()));
            // mailVariable.setUrl(WEB_URL);

            // log.info("mailVariable : {}",mailVariable);

            // log.info("enviando mail");
            // MailService.sendMailForNewRequirementPlanAndNoPlan(mailVariable);
            // log.info("mail enviado con exito");
            // log.info("requestid");
            // log.info(String.valueOf(requestId));
    
            // Response Parsing
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(201)
                    .withBody(Serializer.serialize(requestId));

        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(Serializer.serialize(new ErrorResponse("No se encontró contenido")));
        }

    }
    

    private void mailFactory(RequestList lstrequest) 
    {
        log.info("mailFactory presidencia & Rechazos");
        RequestDAO dao = new RequestDAOImpl();
        Request request = new Request();
        request = dao.findById(Long.parseLong(lstrequest.getRequest().get(0).getId()));

        
        Integer accion = lstrequest.getRequest().get(0).getPostType();
        Integer tipoRequerimiento = 0; // es para identificar los requerimientos de presidencia
        Integer estadoRequerimiento = request.getStatus();

        Integer tipoPlantillaBase = 3;
        Integer tipoPlantillaRow = 4;

        

        //MailRequest mailRequest = new MailRequest();

        //armar bloques de correo (3 tipos)
      

        // obtener lista de requerimientos - rol - correo
        
        List<Request> requestToSendList = dao.getRequestToPresidencyRespond();

        log.info("requestToSendList : {}",requestToSendList.toString());

        requestToSendList.forEach(e->{
            log.info("id : {}", e.getId());
        });

        Set<String> idRequestList = lstrequest.getRequest()
        .stream()
        .map(RequestControl::getId)
        .collect(Collectors.toSet());

        idRequestList.forEach(e -> log.info("requestToSend : {}",e));

        List<Request> requestToPresidencyResponseFilter = requestToSendList
                                                        .stream()
                                                        .filter(r -> idRequestList.contains(r.getId().toString()))
                                                        .collect(Collectors.toList());


        log.info("requestToPresidencyResponseFilter : {}", requestToPresidencyResponseFilter.toString());                                      
        //agrugar requerimientos, segun correo

        List<RequirementResponse> requestToPresidencyResponse = new ArrayList<>();

        Map<String,List<Request>> collectRequestMap = requestToPresidencyResponseFilter
                                                        .stream()
                                                        .collect(Collectors.groupingBy(Request::getEmailSolicitante));

        collectRequestMap.entrySet().forEach(e ->
        {
            log.info("collectRequestMap - e.getKey() : {}",e.getKey());
            log.info("collectRequestMap - e.getValue() : {}",e.getValue());
            
            List<Request> requestsGroupByMail = e.getValue()
                                                    .stream()
                                                    .collect(Collectors.toList());

            Request requirement = e.getValue().get(0);
            requestToPresidencyResponse.add(new RequirementResponse(){{
                setEmail(requirement.getEmailSolicitante());
                setEname(requirement.getEname());
                setButxt(requirement.getButxt());
                setOrget(requirement.getOrget());
                setRequestList(requestsGroupByMail);
            }});


        });

        log.info("requestToPresidencyResponse : {}", requestToPresidencyResponse.toString());


        requestToPresidencyResponse.forEach(e->
        {

            log.info("requestToPresidencyResponse : {}",e.toString());
            log.info("requestToPresidencyResponse : {}",e.getEmail());
            log.info("requestToPresidencyResponse : {}",e.getRequestList().toString());
            

            MailTemplate mailTemplateBase = new MailTemplate();
            mailTemplateBase = dao.getMailTemplate(tipoRequerimiento,accion,estadoRequerimiento,tipoPlantillaBase);
            //MailTemplate mailTemplateRow = new MailTemplate();
            MailTemplate mailRowTemplate = dao.getMailTemplate(tipoRequerimiento,accion,estadoRequerimiento,tipoPlantillaRow);

            MailTemplate baseTemplate = mailTemplateBase;
            
            e.getRequestList().forEach(r -> {

                log.info("e.getRequestList() : {}", r.toString());

                String rowTemplate = mailRowTemplate.getContentTemplate();  

                rowTemplateConcat += rowTemplate.replace("@@empresa", r.getButxt())
                            .replace("@@id",String.valueOf(r.getId()))
                            .replace("@@position", r.getPlstx())
                            .replace("@@unidadOrg", r.getOrget());
                
                log.info("rowTemplate : {}",rowTemplate);
            });

            log.info("rowTemplateConcat : {}",rowTemplateConcat);

            String contentBaseTemplate = baseTemplate.getContentTemplate()  .replace("@@rowRequest",rowTemplateConcat)
                                                                            .replace("@@usuarioDestino", e.getEname());
            rowTemplateConcat="";
            log.info("contentBaseTemplate : {}",contentBaseTemplate);

            baseTemplate.setContentTemplate(contentBaseTemplate);

            //Enviar correo 
            //RequestDAO dao = new RequestDAOImpl();
            MailRequest mailRequest = new MailRequest();
            //User nextUserToApprove = dao.getNextMailApprove(requestingPersonId);
            //String to = nextUserToApprove.getEmail();
            String to = "gustavo.cueto@centro.com.pe";
            //Integer roleCode = nextUserToApprove.getRoleCode();

            mailRequest.setTo(to);
            //Request request = new Request();
            //request = dao.findById(requestId);
            //obtener plantilla
            //MailTemplate mailTemplate = new MailTemplate();
            //mailTemplate = dao.getMailTemplate(request.getType(),action,request.getStatus(),tipoplantilla);
            mailRequest.setMailTamplete(baseTemplate);
            mailRequest.setRequiredPosition("");
            mailRequest.setUrl(WEB_URL);
            mailRequest.setEmployeeToReplace("");
            // mailRequest.setEmployeeToReplace(String.format("%s - %s",
            //                                         request.getPernReemplazo(),
            //                                         request.getNameReemplazo()));

            // obtener cantidad de requerimientos pendientes para usuario superior masivo.
            //Integer numberOfRequirements = dao.getNumberOfRequirements(roleCode);

            mailRequest.setNumberofRequirements("0");
            mailRequest.setDestinationUserName("");

            MailService.sendMail(mailRequest);



        });


        //recorrer correr y armar plantilla
        //Enviar
        




        // lstrequest.getRequest()
        //     .stream()
        //     .sorted((request1,request2) -> request1.getDivision().compareTo(request2.getDivision()))
        //     .sorted((request1,request2) -> request1.getId().compareTo(request2.getId()));

        
        // //TODO: filtrar los requerimientos por rol, crear lista para cada tipo de correo.
        
        // List<Integer> roles = requestToPresidencyResponseFilter
        //                         .stream()
        //                         .map(Request::getRolSolicitante)
        //                         .distinct()
        //                         .sorted((rolFirst,rolLast)->rolFirst.compareTo(rolLast))
        //                         .collect(Collectors.toList());


        // //GG EO | EO - Lista de requerimientos a enviar

        // roles.forEach(rol -> {
        //     //TODO rol = 3
        //     if(rol == 3){
        //         //empresas x rol

        //         List<Request> requestsRolList = requestToPresidencyResponseFilter
        //                                         .stream()
        //                                         //.map(Request::getBukrs)
        //                                         .distinct()
        //                                         .filter(requestsRol -> rol.equals(requestsRol.getRolSolicitante()))
        //                                         .sorted((requestFisrt,requestLast)->requestFisrt.getBukrs().compareTo(requestLast.getBukrs()))
        //                                         .collect(Collectors.toList());

                
        //         List<String> codigoEmpresas = requestsRolList
        //                                         .stream()
        //                                         .map(Request::getBukrs)
        //                                         .distinct()
        //                                         .sorted((empresaFisrt,empresaLast)->empresaFisrt.compareTo(empresaLast))
        //                                         .collect(Collectors.toList());

        //         codigoEmpresas.forEach(codigoEmpresa ->
        //         {
                    
        //             List<Long> requestsIdList = requestToPresidencyResponseFilter
        //                                                 .stream()
        //                                                 .distinct()
        //                                                 .filter(requestsEmpresa -> codigoEmpresa.equals(requestsEmpresa.getBukrs()))
        //                                                 .map(Request::getId)
        //                                                 .sorted((requestIdFisrt,requestIdLast)->requestIdFisrt.compareTo(requestIdLast))
        //                                                 .collect(Collectors.toList());
                    
                                                    
        //             List<RequestControl> requestsToSendMailList = lstrequest.getRequest()
        //                                                         .stream()
        //                                                         .filter(requestsToSendMail -> requestsIdList.contains(requestsToSendMail.getId()))
        //                                                         .collect(Collectors.toList());
                    
                    
        //             MailTemplate mailTemplateBase = new MailTemplate();
        //             mailTemplateBase = dao.getMailTemplate(tipoRequerimiento,accion,estadoRequerimiento,tipoPlantillaBase);
        //             MailTemplate mailRowTemplate = dao.getMailTemplate(tipoRequerimiento,accion,estadoRequerimiento,tipoPlantillaRow);
                                                        

        //             MailTemplate baseTemplate = mailTemplateBase;
        //             String contentBaseTemplate = baseTemplate.getContentTemplate();
        //             requestsToSendMailList.forEach(e -> {

        //             String rowTemplate = mailRowTemplate.getContentTemplate();
        
        //             //Estructura de correo |division|requestid|getPlstxSolicitante|orgUnit|
        
        //             rowTemplate.replace("@@empresa", e.getDivision())
        //                         .replace("@@idRequerimiento",e.getId())
        //                         .replace("@@posicionSolicitada", e.getPosition())
        //                         .replace("@@unidadOrganizativa", e.getOrgUnit());
        
        //             rowTemplateConcat += rowTemplate;
                    
        //             });

        //             contentBaseTemplate.replace("@@rowRequest",rowTemplateConcat);

        //             baseTemplate.setContentTemplate(contentBaseTemplate);

        //             //Enviar correo 

        //             MailRequest mailRequest = new MailRequest();

        //             String to = "gustavo.cueto@centro.com.pe";

        //             mailRequest.setTo(to);
        //             mailRequest.setMailTamplete(baseTemplate);
        //             mailRequest.setUrl(WEB_URL);

        //             MailService.sendMail(mailRequest);





        //         });
                

                
        //     }
             
        // });
        
        


        
        
        // // lstrequest.getRequest().forEach(e -> {

        // //     String rowTemplate = mailRowTemplate.getContentTemplate();

        // //     //Estructura de correo |division|requestid|getPlstxSolicitante|orgUnit|

        // //     rowTemplate.replace("@@empresa", e.getDivision())
        // //                 .replace("@@idRequerimiento",e.getId())
        // //                 .replace("@@posicionSolicitada", e.getPosition())
        // //                 .replace("@@unidadOrganizativa", e.getOrgUnit());

        // //     rowTemplateConcat += rowTemplate;
            
        // // });



        // // mailTemplateBase.getContentTemplate().replace("@@rowRequest",rowTemplateConcat);


        // //Correo tipo 1 GG EO / EO
        // //Cargar los correos con la data de requerimientos

        // //enviar correo según destinatario.
        // //List<User> users = dao.getUsersListToApproved();



    }

    private void mailFactory(Integer action,Long requestId,String requestingPersonId, Integer tipoplantilla) 
    {

            log.info("action,requestId,requestingPersonId : {} {} {}",action, requestId, requestingPersonId);

            RequestDAO dao = new RequestDAOImpl();
            MailRequest mailRequest = new MailRequest();
            //TODO: sigeuinte aprobador depende de la acción, si es aprobado va a supeior, si es rechazado va al anterior

            User nextUserToApprove = dao.getNextMailApprove(requestingPersonId);
            
            //String to = nextUserToApprove.getEmail();
            String to = "gustavo.cueto@centro.com.pe";
            Integer roleCode = nextUserToApprove.getRoleCode();
            String destinationUserName = nextUserToApprove.getFullName();

            mailRequest.setTo(to);
            Request request = new Request();
            request = dao.findById(requestId);
            //obtener plantilla
            MailTemplate mailTemplate = new MailTemplate();
            mailTemplate = dao.getMailTemplate(request.getType(),action,request.getStatus(),tipoplantilla);
            mailRequest.setMailTamplete(mailTemplate);
            mailRequest.setRequiredPosition(request.getPlstx());
            mailRequest.setUrl(WEB_URL);
            mailRequest.setEmployeeToReplace(String.format("%s - %s",
                                                    request.getPernReemplazo(),
                                                    request.getNameReemplazo()));

            // obtener cantidad de requerimientos pendientes para usuario superior masivo.
            Integer numberOfRequirements = dao.getNumberOfRequirements(roleCode);

            mailRequest.setNumberofRequirements(String.valueOf(numberOfRequirements));
            mailRequest.setDestinationUserName(destinationUserName);

            MailService.sendMail(mailRequest);
    }

    private void mailFactory(Integer action,Long requestId, Integer tipoplantilla) 
    {

            log.info("action,requestId : {} {}",action, requestId);

            RequestDAO dao = new RequestDAOImpl();
            MailRequest mailRequest = new MailRequest();

            Request request = new Request();
            request = dao.findById(requestId);
            //String to = request.getEmailSolicitante();
            String to = "gustavo.cueto@centro.com.pe";
            mailRequest.setTo(to);
            //obtener plantilla
            MailTemplate mailTemplate = new MailTemplate();
            mailTemplate = dao.getMailTemplate(request.getType(),action,request.getStatus(),tipoplantilla);
            mailRequest.setMailTamplete(mailTemplate);
            mailRequest.setRequiredPosition(request.getPlstx());
            mailRequest.setUrl(WEB_URL);
            mailRequest.setEmployeeToReplace(String.format("%s - %s",
                                                    request.getPernReemplazo(),
                                                    request.getNameReemplazo()));

            mailRequest.setNumberofRequirements("");
            mailRequest.setDestinationUserName("");

            MailService.sendMail(mailRequest);


    }


    private Request parseFormBody(byte[] bodyBytes, String contentType, long RequestId)
            throws IOException, InvocationTargetException, IllegalAccessException {
        // Check typical form-data body: https://stackoverflow.com/a/23517227/14978149
        Request request = new Request();
        // Content-Type: multipart/form-data; boundary=xxx
        log.info(contentType.split("=")[1]);
        byte[] boundary = contentType.substring(contentType.indexOf("boundary=") + 9).getBytes();

        //byte[] bI = java.util.Base64.getDecoder().decode(bodyBytes.toString().getBytes());

        //byte[] bI = Base64.decode(bodyBytes.toString().getBytes());


        log.info("bodyBytes.length");
        log.info(String.valueOf(bodyBytes.length));

        MultipartStream ms = new MultipartStream(
                                                    new ByteArrayInputStream(bodyBytes),
                                                    boundary,
                                                    bodyBytes.length,
                                            null);
        boolean next = ms.skipPreamble();
        List<FileObject> files = new ArrayList<>();
        StorageService storageService = new StorageService(
                AWS_S3_BATCH_FILES_BUCKET_NAME);

        while (next) {
            // Content-Disposition: form-data; name="customFileFormField";
            // filename="customFile.txt"
            // Content-Type: <application/pdf|text/plain|...>
            String headers = ms.readHeaders();
            int start = headers.indexOf("Content-Type: ") + 14;

            log.info("ms header   " + headers);
            log.info("header index   " + headers.indexOf("Content-Type: "));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (start == 13) {
                int fieldNameStart = headers.indexOf("name=\"") + 6;
                String fieldName = headers.substring(fieldNameStart, headers.indexOf("\"", fieldNameStart));
                ms.readBodyData(baos);
                String fieldContent = new String(new ByteArrayInputStream(baos.toByteArray()).readAllBytes(),
                        StandardCharsets.UTF_8);

                Parser.setProperty(request, fieldName, fieldContent);

                log.info("File Content: {}",fieldContent);

            } else {
                log.debug("File Field found!");
                log.info("file headers {}", headers);
                int fieldFilenameStart = headers.indexOf("filename=\"") + 10;
                String fieldFilename = headers.substring(fieldFilenameStart, headers.indexOf("\"", fieldFilenameStart));
                String fileContentType = headers.substring(start, headers.indexOf("\n", start));
                log.debug("File Content Type: {}", fileContentType);

                ms.readBodyData(baos);
                log.info("fieldname");
                log.info(fieldFilename);
                log.info("fielcontent");
                log.info("File Size :" +String.valueOf(baos.toByteArray().length));

                try {
                    // log.info("baos {}", baos);
                    String keyFile = storageService.upload3(
                            new ByteArrayInputStream(baos.toByteArray()),
                            fieldFilename.split("\\.")[1],
                            fileContentType, fieldFilename, RequestId, baos.toByteArray().length);
                    FileObject file = new FileObject();
                    file.setNameArchivo(fieldFilename);
                    file.setRutaArchivo(keyFile);
                    files.add(file);

                    log.info("archivo subido  key {}", keyFile);

                } catch (Exception e) {
                    log.info("Archivo no subido error {}", e);
                }

            }

            next = ms.readBoundary();
        }
        request.setFilesObject(files);
        return request;
    }

    private Request parseFormBodyV2(byte[] bodyBytes, String contentType, long RequestId)
            throws IOException, InvocationTargetException, IllegalAccessException {
        // Check typical form-data body: https://stackoverflow.com/a/23517227/14978149
        Request request = new Request();
        // Content-Type: multipart/form-data; boundary=xxx
        log.info(contentType.split("=")[1]);
        byte[] boundary = contentType.substring(contentType.indexOf("boundary=") + 9).getBytes();

        //byte[] bI = java.util.Base64.getDecoder().decode(bodyBytes.toString().getBytes());

        //byte[] bI = Base64.decode(bodyBytes.toString().getBytes());

            // java.util.Base64.Encoder enc = java.util.Base64.getEncoder();
            // byte[] encbytes = enc.encode(bodyBytes);
            // for (int i = 0; i < encbytes.length; i++)
            // {
            //     System.out.printf("%c", (char) encbytes[i]);
            //     if (i != 0 && i % 4 == 0)
            //         System.out.print(' ');
            // }
            java.util.Base64.Decoder dec = java.util.Base64.getDecoder();
            byte[] barray2 = dec.decode(bodyBytes);
            InputStream fis2 = new ByteArrayInputStream(barray2);


        log.info("bodyBytes.length");
        log.info(String.valueOf(barray2.length));

        MultipartStream ms = new MultipartStream(
                                                    new ByteArrayInputStream(barray2),
                                                    boundary,
                                                    barray2.length,
                                            null);
        boolean next = ms.skipPreamble();
        List<FileObject> files = new ArrayList<>();
        StorageService storageService = new StorageService(
                AWS_S3_BATCH_FILES_BUCKET_NAME);

        while (next) {
            // Content-Disposition: form-data; name="customFileFormField";
            // filename="customFile.txt"
            // Content-Type: <application/pdf|text/plain|...>
            String headers = ms.readHeaders();
            int start = headers.indexOf("Content-Type: ") + 14;

            log.info("ms header   " + headers);
            log.info("header index   " + headers.indexOf("Content-Type: "));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (start == 13) {
                int fieldNameStart = headers.indexOf("name=\"") + 6;
                String fieldName = headers.substring(fieldNameStart, headers.indexOf("\"", fieldNameStart));
                ms.readBodyData(baos);
                String fieldContent = new String(new ByteArrayInputStream(baos.toByteArray()).readAllBytes(),
                        StandardCharsets.UTF_8);

                Parser.setProperty(request, fieldName, fieldContent);

                log.info("File Content: {}",fieldContent);

            } else {
                log.debug("File Field found!");
                log.info("file headers {}", headers);
                int fieldFilenameStart = headers.indexOf("filename=\"") + 10;
                String fieldFilename = headers.substring(fieldFilenameStart, headers.indexOf("\"", fieldFilenameStart));
                String fileContentType = headers.substring(start, headers.indexOf("\n", start));
                log.debug("File Content Type: {}", fileContentType);

                ms.readBodyData(baos);
                log.info("fieldname");
                log.info(fieldFilename);
                log.info("fielcontent");
                log.info("File Size :" +String.valueOf(baos.toByteArray().length));

                try {
                    // log.info("baos {}", baos);
                    String keyFile = storageService.upload3(
                            new ByteArrayInputStream(baos.toByteArray()),
                            fieldFilename.split("\\.")[1],
                            fileContentType, fieldFilename, RequestId, baos.toByteArray().length);
                    FileObject file = new FileObject();
                    file.setNameArchivo(fieldFilename);
                    file.setRutaArchivo(keyFile);
                    files.add(file);

                    log.info("archivo subido  key {}", keyFile);

                } catch (Exception e) {
                    log.info("Archivo no subido error {}", e);
                }

            }

            next = ms.readBoundary();
        }
        request.setFilesObject(files);
        return request;
    }


}
