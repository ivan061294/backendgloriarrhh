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

public class CenterName implements Serializable{
    private String idinstitucion;

    private String institucion;

    private String slart;
}
