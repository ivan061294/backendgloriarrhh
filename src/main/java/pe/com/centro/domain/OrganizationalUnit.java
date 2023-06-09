package pe.com.centro.domain;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrganizationalUnit implements Serializable{

    private String bukrs;

    private String werks;

    private  String btrtl;
    
    private String orgeh;

    private String orget;

    private String codniv4;
}
