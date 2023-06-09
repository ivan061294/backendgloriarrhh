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
public class AllModelForm implements Serializable{

    private List<Society> societys;

    private List<SocDivision> socdivisions;

    private List<SocSubDivision> socSubDivisions;

    private List<OrganizationalUnit> organizationalUnits;

    private List<StructNivel1> structNivel1s;

    private List<CostCenter>costCenters;

    private List<SearchType>searchTypes;

    private List<Employee>employees;

    private List<ContractType> contractTypes;

    private List<ContractTime> contractTimes;

    private List<Formation> formations;

    private List<GradeFormation> gradeFormations;

    private List<CenterType> centerTypes;

    private List<CenterName> centerNames;

    private List<ExperienceType> experienceTypes;

    private List<AgeRange> ageRanges;

    private List<Genre> genres;

    private List<CivilStatus>civilStatus;

    private List<Languaje> languaje;

    private List<LanguajeLevel> languajeLevel;

    /* 
    private List<PhysicalLocation> pLocation;

    private List<Post> post;
*/
    private List<PostType> postTypes;

    private List<ConfigProfile> configProfiles;

    private List<Profiles> profiles;

    private List<Roles> rolesEmpresa;

    private List<Roles> rolesRRHH;

    private List<Roles> rolesCorporativo;

    
    
}
