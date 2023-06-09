package pe.com.centro.domain;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Request implements Serializable{
     private Long id;

     private int type;

     private int status;

     private String typeDesc;

     private String btext;

     private int searchType;

     private String plans;

     private String plstx;

    private String societyCode;

     private String divisionCode;

     private String managementId;

     private boolean newOrganizationalUnitCode;

     private String organizationalUnitCode;

     private String physicalLocationCode;

     private String costCenterCode;

     private String costCenterdesc;

     private Integer vacancies;

     private Integer quantity;

     private Integer flagplanificado;

     private LocalDate estimatedEntryDate;

     private boolean consideredInStaffPlan;

     private String contractType;

     private int contractTime;

     private String requestDate;

     private String justification;

     private int experienceTime;

     private int ageRange;

     private String genre;

     private int newplans;

     private String civilStatus;

     private Instant requestedAt;

     private String pernSolicitante;

     private String nameSolicitante;

     private String plansSolicitante;

     private String plstxSolicitante;

     private String emailSolicitante;

     private String ename;

     private Integer rolSolicitante;

     private Integer rolSolicitanteSuperior;

     private String abkrs;

     private String dni;

     private String pernSup;

     private String sobid;

     private String stext3;

     private String alcance;

     private String pernReemplazo;

     private String nameReemplazo;

     private String apePatReemplazo;

     private String apeMatReemplazo;

     private String plansReemplazo;

     private String plstxReemplazo;

     private String characteristics;

     private String usuarioCreacion;

     private String usuarioModifica;

     private String ipHost;

     private String observation;

     private String bukrs;

     private String butxt;

     private String orgeh;

     private String orget;

     private List<FileObject> filesObject;
}