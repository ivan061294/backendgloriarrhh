package pe.com.centro.domain;

import java.io.Serializable;
import java.util.List;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RequestControl implements Serializable{
      private String id;

      private String state;

      private String society;

      private String position;

      private String typeOfVacant;

      private String codOfVacant;

      private String orgUnit;

      private String centerOfCost;

      private String physicLocation;

      private String category;

      private Integer quantity;

      private String typeOfContract;

      private String timeOfContract;

      private String justify;

      private String division;

      private String tiempoExperiencia;

      private String rangoEdad;

      private String sexo;

      private String statusCivil;

      private String codeSolicitante;

      private String codeSolicitanteSuperior;

      private Integer rolSolicitanteSuperior;

      private String nameSolicitante;

      private Integer rolSolicitante;

      private Integer positionSolicitante;

      private String cargoSolicitante;

      private String apePatReemplazo;

      private String apeMatReemplazo;

      private String nameReemplazo;

      private String plansReemplazo;

      private String plstxReemplazo;

      private CharacteristicsQuery characteristics;

      private String observation;

      private int postType;

      private String dateState;

      private String status;

      private boolean check;

      private boolean ismail;

      private List<FileObject> files;

      private Aprobadores aprobadores;

      private String typeRequirement;

}
