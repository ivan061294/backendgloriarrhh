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
public class Characteristics implements Serializable{
    private List<Activities> activities;
    private List<knowledgeRequirements>knowledgeRequirements;
    private List<AbilityRequirements>abilityRequirements;
    private List<AcademicFormation>academicFormation;
    private List<StudyCenters>studyCenters;
    private List<Languages>languages;
    
}
