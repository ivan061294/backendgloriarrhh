package pe.com.centro.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Profiles implements Serializable{

    private Integer idPerfil;

    private String perfil;

    private boolean flgAnulado;
    
}
