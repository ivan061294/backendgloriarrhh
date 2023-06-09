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

public class Roles implements Serializable{

    private String bukrs;

    private String werks;

    private String btrtl;

    private String orgeh;
    
    private String plans;

    private String plstx;

    private String alcance;
}
