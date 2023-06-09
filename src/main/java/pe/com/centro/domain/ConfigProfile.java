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
public class ConfigProfile implements Serializable{
    private Integer idPerfil;

    private String bukrs;

    private String werks;

    private String orgeh;

    private String perfil;

    private String plans;

    private boolean flgHabilitado;
}
