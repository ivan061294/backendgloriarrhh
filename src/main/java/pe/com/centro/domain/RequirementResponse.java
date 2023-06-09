package pe.com.centro.domain;

import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@EqualsAndHashCode

public class RequirementResponse implements Serializable{
    private String email;
    private String ename;
    private String idRol;
    private String butxt;
    //private String id;
    private String plstx;
    private String orget;
    private String typeRequirement;
    private List<Request> requestList;
    private List<RequestControl> requestListControl;
}
