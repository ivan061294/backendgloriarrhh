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

public class ExperienceType implements Serializable{
    private Integer idtiempoexp;

    private String tiempoexperiencia;
}
