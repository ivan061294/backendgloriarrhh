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
public class SocSubDivision implements Serializable{
    private String bukrs;
    
    private String werks;

    private String btrtl;

    private String btext;
}
