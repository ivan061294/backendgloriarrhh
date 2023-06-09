package pe.com.centro.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PostType {
    private String societyCode; 
    private String divisionCode; 
    private Long managementId; 
    private String organizationalUnitCode; 
    private String costCenterCode; 
    private String physicalLocationCode;
    private Long id;

    private String name;
}
