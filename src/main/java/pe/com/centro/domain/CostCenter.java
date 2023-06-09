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
public class CostCenter implements Serializable{

    private String bukrs; 
    private String kostl; 
    private String ltext; 
    private String alcance;
}
