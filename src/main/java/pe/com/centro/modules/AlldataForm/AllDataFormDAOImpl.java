package pe.com.centro.modules.AlldataForm;

import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.AgeRange;
import pe.com.centro.domain.AllModelForm;
import pe.com.centro.domain.CenterName;
import pe.com.centro.domain.CenterType;
import pe.com.centro.domain.CivilStatus;
import pe.com.centro.domain.ConfigProfile;
import pe.com.centro.domain.ContractTime;
import pe.com.centro.domain.ContractType;
import pe.com.centro.domain.CostCenter;
import pe.com.centro.domain.SocDivision;
import pe.com.centro.domain.ExperienceType;
import pe.com.centro.domain.Genre;
import pe.com.centro.domain.GradeFormation;
import pe.com.centro.domain.Languaje;
import pe.com.centro.domain.LanguajeLevel;
import pe.com.centro.domain.SocSubDivision;
import pe.com.centro.domain.OrganizationalUnit;
import pe.com.centro.domain.SearchType;
import pe.com.centro.domain.Formation;
import pe.com.centro.domain.Society;
import pe.com.centro.domain.StructNivel1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllDataFormDAOImpl implements AllDataFormDAO {

    private static final String FIND_ALL_SOCIETY = " SELECT s.bukrs, s.butxt, s.denominacion, s.codpais,cs.flgHabilitado,cs.flgPresidencia FROM maestra.sociedad s "
            +
            " join seguridad.configsociedad cs on s.bukrs=cs.bukrs order by bukrs ";

    private static final String FIND_ALL_DIVISION = "SELECT distinct bukrs, werks, name1 FROM maestra.socdivision order by werks";

    private static final String FIND_ALL_SOCSUBDIVISION = "SELECT distinct bukrs, werks, btrtl, btext FROM maestra.socsubdivision order by btrtl";

    private static final String FIND_ALL_ORGANIZTIONALUNIT = "SELECT distinct bukrs, werks, btrtl, orgeh, orget, codniv4 FROM maestra.socunidadorg order by orgeh";

    private static final String FIND_ALL_STRUCTNIVEL1 = "SELECT distinct codniv1, descniv1 FROM maestra.estructuranivel1 order by codniv1";

    private static final String FIND_ALL_COST_CENTER = "SELECT distinct bukrs, kostl, ltext, alcance FROM maestra.centrocosto order by kostl";

    private static final String FIND_ALL_SEARCHTYPE = "SELECT distinct idtipobusqueda, tipobusqueda FROM proceso.tipobusqueda order by idtipobusqueda";

    private static final String FIND_ALL_CONTRACTYPE = "SELECT distinct cttyp, cttxt FROM proceso.tipocontrato order by cttyp";

    private static final String FIND_ALL_CONTRACTTIME = "SELECT distinct idtiempocontrato, tiempocontrato FROM proceso.tiempocontrato order by idtiempocontrato";

    private static final String FIND_ALL_FORMATION = "SELECT distinct idespecialidad, especialidad FROM maestra.educacionespecialidad order by idespecialidad";

    private static final String FIND_ALL_GRADEFORMATION = "SELECT distinct ideducacion, titulo FROM maestra.educacionformal order by ideducacion";

    private static final String FIND_ALL_CENTERTYPE = "SELECT distinct slart, stext FROM maestra.tipoinstitucion order by slart";

    private static final String FIND_ALL_CENTERNAME = "SELECT distinct idinstitucion, institucion, slart FROM maestra.institucioneducativa order by idinstitucion";

    private static final String FIND_ALL_EXPERIENCETYPE = "SELECT distinct idtiempoexp, tiempoexperiencia FROM proceso.tiempoexperiencia order by idtiempoexp";

    private static final String FIND_ALL_AGERANGE = "SELECT distinct idrangoedad, rangoedad FROM proceso.rangoedad order by idrangoedad";

    private static final String FIND_ALL_GENRE = "SELECT distinct gesch, sexo FROM proceso.sexo order by gesch";

    private static final String FIND_ALL_CIVILSTATUS = "SELECT distinct famst, ftext FROM proceso.estadocivil order by famst";

    private static final String FIND_ALL_LANGUAJE = "SELECT distinct ididioma, idioma FROM proceso.idioma order by ididioma";

    private static final String FIND_ALL_LANGUAJELEVEL = "SELECT distinct ididiomanivel, idiomanivel FROM proceso.idiomanivel order by ididiomanivel";

    private static final String FIND_CONFIG_PROFILE = "select p.idperfil,cp.bukrs,cp.werks,cp.orgeh,p.perfil,cp.pernr,cp.flghabilitado from seguridad.perfil p "
            +
            " left join seguridad.configprofile cp on p.idperfil=cp.idperfil where bukrs= ? ";

    @Override
    public AllModelForm getAll() {
        AllModelForm model = new AllModelForm();
        model.setSocietys(findAllSociety());
        model.setSocdivisions(findAllSocDivision());
        model.setSocSubDivisions(findAllSocSubDivision());
        model.setOrganizationalUnits(findAllOrganizationalUnits());
        model.setStructNivel1s(findAllStructNivel1());
        model.setCostCenters(findAllCostCenter());
        model.setSearchTypes(findAllSearchType());
        model.setContractTypes(findAllContractTypes());
        model.setContractTimes(findAllContractTimes());
        model.setFormations(findAllFormations());
        model.setGradeFormations(findAllGradeFormations());
        model.setCenterTypes(findAllCenterTypes());
        model.setCenterNames(findAllCenterNames());
        model.setExperienceTypes(findAllExperienceTypes());
        model.setAgeRanges(findAllAgeRanges());
        model.setGenres(findAllGenres());
        model.setCivilStatus(findAllCivilStatus());
        model.setLanguaje(findAlllLanguajes());
        model.setLanguajeLevel(findAlllanLanguajeLevels());
        model.setConfigProfiles(findAlllConfigProfile(FIND_ALL_AGERANGE));
        return model;
    }

    public List<Society> findAllSociety() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Society> societies = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_SOCIETY);
            set = statement.executeQuery();

            while (set.next()) {
                Society society = new Society();
                society.setBukrs(set.getString(1));
                society.setButxt(set.getString(2));
                society.setDenominacion(set.getString(3));
                society.setCodpais(set.getString(4));
                society.setFlgAvailable(set.getBoolean(5));
                society.setFlgPresidencia(set.getBoolean(6));
                societies.add(society);
            }
            return societies;
        } catch (SQLException e) {
            log.error("Error listing societies", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<SocDivision> findAllSocDivision() {
        // Init variables and Connections
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<SocDivision> divisions = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_DIVISION);
            set = statement.executeQuery();

            while (set.next()) {
                SocDivision division = new SocDivision();
                division.setBukrs(set.getString(1));
                division.setWerks(set.getString(2));
                division.setName1(set.getString(3));
                divisions.add(division);
            }

            return divisions;
        } catch (SQLException e) {
            log.error("Error listing all divisions'" + '\'', e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<SocSubDivision> findAllSocSubDivision() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<SocSubDivision> managements = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_SOCSUBDIVISION);
            set = statement.executeQuery();
            while (set.next()) {
                SocSubDivision management = new SocSubDivision();
                management.setBukrs(set.getString(1));
                management.setWerks(set.getString(2));
                management.setBtrtl(set.getString(3));
                management.setBtext(set.getString(4));
                managements.add(management);
            }
            return managements;
        } catch (SQLException e) {
            log.error("Error listing all managements ");
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<OrganizationalUnit> findAllOrganizationalUnits() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<OrganizationalUnit> organizationalUnits = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_ORGANIZTIONALUNIT);
            set = statement.executeQuery();
            while (set.next()) {
                OrganizationalUnit organizationalUnit = new OrganizationalUnit();
                organizationalUnit.setBukrs(set.getString(1));
                organizationalUnit.setWerks(set.getString(2));
                organizationalUnit.setBtrtl(set.getString(3));
                organizationalUnit.setOrgeh(set.getString(4));
                organizationalUnit.setOrget(set.getString(5));
                organizationalUnit.setCodniv4(set.getString(6));
                organizationalUnits.add(organizationalUnit);
            }

            return organizationalUnits;
        } catch (SQLException e) {
            log.error("Error listing all organizational units '" + '\'', e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<StructNivel1> findAllStructNivel1() {
        // Init variables and Connections
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<StructNivel1> StructNivel1s = new ArrayList<>();

        try {
            statement = connection
                    .prepareStatement(FIND_ALL_STRUCTNIVEL1);
            set = statement.executeQuery();

            while (set.next()) {
                StructNivel1 structNivel1 = new StructNivel1();
                structNivel1.setCodniv1(set.getString(1));
                structNivel1.setDescniv1(set.getString(2));
                StructNivel1s.add(structNivel1);
            }
            return StructNivel1s;
        } catch (SQLException e) {
            log.error(
                    "Error listing all struct nivel1 locations ", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<CostCenter> findAllCostCenter() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<CostCenter> costCenters = new ArrayList<>();

        try {
            statement = connection
                    .prepareStatement(FIND_ALL_COST_CENTER);
            set = statement.executeQuery();
            while (set.next()) {
                CostCenter costCenter = new CostCenter();
                costCenter.setBukrs(set.getString(1));
                costCenter.setKostl(set.getString(2));
                costCenter.setLtext(set.getString(3));
                costCenter.setAlcance(set.getString(4));
                costCenters.add(costCenter);
            }
            return costCenters;
        } catch (SQLException e) {
            log.error(
                    "Error listing cost center ", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<SearchType> findAllSearchType() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<SearchType> SearchTypes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_SEARCHTYPE);
            set = statement.executeQuery();
            while (set.next()) {
                SearchType searchType = new SearchType();
                searchType.setIdtipobusqueda(set.getInt(1));
                searchType.setTipobusqueda(set.getString(2));
                SearchTypes.add(searchType);
            }
            return SearchTypes;
        } catch (SQLException e) {
            log.error("Error listing all searchType", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<ContractType> findAllContractTypes() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<ContractType> ContractTypes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_CONTRACTYPE);
            set = statement.executeQuery();
            while (set.next()) {
                ContractType contractType = new ContractType();
                contractType.setCttyp(set.getString(1));
                contractType.setCttxt(set.getString(2));
                ContractTypes.add(contractType);
            }
            return ContractTypes;
        } catch (SQLException e) {
            log.error("Error listing all searchType", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<ContractTime> findAllContractTimes() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<ContractTime> ContractTimes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_CONTRACTTIME);
            set = statement.executeQuery();
            while (set.next()) {
                ContractTime contractTime = new ContractTime();
                contractTime.setIdtiempocontrato(set.getInt(1));
                contractTime.setTiempocontrato(set.getString(2));
                ContractTimes.add(contractTime);
            }
            return ContractTimes;
        } catch (SQLException e) {
            log.error("Error listing all contractype", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }

    }

    public List<Formation> findAllFormations() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Formation> Formations = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_FORMATION);
            set = statement.executeQuery();
            while (set.next()) {
                Formation formation = new Formation();
                formation.setIdespecialidad(set.getString(1));
                formation.setEspecialidad(set.getString(2));
                Formations.add(formation);
            }
            return Formations;
        } catch (SQLException e) {
            log.error("Error listing all formation", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<GradeFormation> findAllGradeFormations() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<GradeFormation> GradeFormations = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_GRADEFORMATION);
            set = statement.executeQuery();
            while (set.next()) {
                GradeFormation gradeFormation = new GradeFormation();
                gradeFormation.setIdeducacion(set.getInt(1));
                gradeFormation.setTitulo(set.getString(2));
                GradeFormations.add(gradeFormation);
            }
            return GradeFormations;
        } catch (SQLException e) {
            log.error("Error listing all formation", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<CenterType> findAllCenterTypes() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<CenterType> CenterTypes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_CENTERTYPE);
            set = statement.executeQuery();
            while (set.next()) {
                CenterType centerType = new CenterType();
                centerType.setSlart(set.getString(1));
                centerType.setStext(set.getString(2));
                CenterTypes.add(centerType);
            }
            return CenterTypes;
        } catch (SQLException e) {
            log.error("Error listing all centertype", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<CenterName> findAllCenterNames() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<CenterName> CenterNames = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_CENTERNAME);
            set = statement.executeQuery();
            while (set.next()) {
                CenterName centerName = new CenterName();
                centerName.setIdinstitucion(set.getString(1));
                centerName.setInstitucion(set.getString(2));
                centerName.setSlart(set.getString(3));
                CenterNames.add(centerName);
            }
            return CenterNames;
        } catch (SQLException e) {
            log.error("Error listing all CenterName", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<ExperienceType> findAllExperienceTypes() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<ExperienceType> ExperienceTypes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_EXPERIENCETYPE);
            set = statement.executeQuery();
            while (set.next()) {
                ExperienceType experienceType = new ExperienceType();
                experienceType.setIdtiempoexp(set.getInt(1));
                experienceType.setTiempoexperiencia(set.getString(2));
                ExperienceTypes.add(experienceType);
            }
            return ExperienceTypes;
        } catch (SQLException e) {
            log.error("Error listing all experiencetype", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<AgeRange> findAllAgeRanges() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<AgeRange> AgeRanges = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_AGERANGE);
            set = statement.executeQuery();
            while (set.next()) {
                AgeRange ageRange = new AgeRange();
                ageRange.setIdrangoedad(set.getInt(1));
                ageRange.setRangoedad(set.getString(2));
                AgeRanges.add(ageRange);
            }
            return AgeRanges;
        } catch (SQLException e) {
            log.error("Error listing all AgeRange", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<Genre> findAllGenres() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Genre> Genres = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_GENRE);
            set = statement.executeQuery();
            while (set.next()) {
                Genre genre = new Genre();
                genre.setGesch(set.getString(1));
                genre.setSexo(set.getString(2));
                Genres.add(genre);
            }
            return Genres;
        } catch (SQLException e) {
            log.error("Error listing all Genre", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<CivilStatus> findAllCivilStatus() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<CivilStatus> CivilStatuss = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_CIVILSTATUS);
            set = statement.executeQuery();
            while (set.next()) {
                CivilStatus civilStatus = new CivilStatus();
                civilStatus.setFamst(set.getString(1));
                civilStatus.setFtext(set.getString(2));
                CivilStatuss.add(civilStatus);
            }
            return CivilStatuss;
        } catch (SQLException e) {
            log.error("Error listing all CivilStatus", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<Languaje> findAlllLanguajes() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<Languaje> Languajes = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_LANGUAJE);
            set = statement.executeQuery();
            while (set.next()) {
                Languaje languaje = new Languaje();
                languaje.setIdidioma(set.getString(1));
                languaje.setIdioma(set.getString(2));
                Languajes.add(languaje);
            }
            return Languajes;
        } catch (SQLException e) {
            log.error("Error listing all Languaje", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<LanguajeLevel> findAlllanLanguajeLevels() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<LanguajeLevel> LanguajeLevels = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_ALL_LANGUAJELEVEL);
            set = statement.executeQuery();
            while (set.next()) {
                LanguajeLevel languajeLevel = new LanguajeLevel();
                languajeLevel.setIdidiomanivel(set.getInt(1));
                languajeLevel.setIdiomanivel(set.getString(2));
                LanguajeLevels.add(languajeLevel);
            }
            return LanguajeLevels;
        } catch (SQLException e) {
            log.error("Error listing all LanguajeLevel", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    public List<ConfigProfile> findAlllConfigProfile(String bukrs) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<ConfigProfile> configProfiles = new ArrayList<>();

        try {
            statement = connection.prepareStatement(FIND_CONFIG_PROFILE);
            statement.setString(1 , bukrs);
            set = statement.executeQuery();
            while (set.next()) {
                ConfigProfile configProfile = new ConfigProfile();
                configProfile.setIdPerfil(set.getInt(1));
                configProfile.setBukrs(set.getString(2));
                configProfile.setWerks(set.getString(3));
                configProfile.setOrgeh(set.getString(4));
                configProfile.setPerfil(set.getString(5));
                configProfile.setPlans(set.getString(6));
                configProfile.setFlgHabilitado(set.getBoolean(7));
                configProfiles.add(configProfile);
            }
            return configProfiles;
        } catch (SQLException e) {
            log.error("Error listing all Languaje", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }


    
}
