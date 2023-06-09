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
public class Society implements Serializable{
    private String bukrs;

    private String butxt;

    private String denominacion;

    private String codpais;

    private boolean flgAvailable;

    private boolean flgPresidencia;
}
