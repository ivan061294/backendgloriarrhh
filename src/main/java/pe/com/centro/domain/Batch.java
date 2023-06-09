package pe.com.centro.domain;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Batch implements Serializable{

    private Integer idbatch;

    private String nombreArchivo;

    private String separador;

    private String nombreTablaJob;

    private String nombreFuncion;


}
