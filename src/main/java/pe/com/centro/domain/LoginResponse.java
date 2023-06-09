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
public class LoginResponse implements Serializable{

    private Integer position;
    
    private String name;
    
    private String apPaterno;
    
    private String apMaterno;
    
    private String pern;
    
    private String plans;
    
    private String plstx;
    
    private Integer codrol;
    
    private String abkrs;
    
    private String pern_sup_code;
    
    private String alcance;
    
    private String sobid;
    
    private String stext3;

    private String bukrs;

    private String werks;

    private String btrtl;

    private String orgeh;

    private String dni;

    private String email;

    private Integer codrol_sup;

    private String kostl;
    
    
}
