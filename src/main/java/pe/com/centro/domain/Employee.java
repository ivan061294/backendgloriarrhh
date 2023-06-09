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
public class Employee implements Serializable{

    private String bukrs;

    private String btrtl;

    private String werks;

    private String pernr;

    private String name1;

    private String butxt;

    private String btext;

    
}
