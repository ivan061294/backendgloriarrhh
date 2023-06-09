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
public class SocDivision  implements Serializable{

    private String bukrs;

    private String werks;

    private String name1;
}
