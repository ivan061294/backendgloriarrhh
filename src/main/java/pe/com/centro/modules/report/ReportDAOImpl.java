package pe.com.centro.modules.report;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.enumeration.RequestType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
public class ReportDAOImpl implements ReportDAO {
    private static final String PREPARE_BY_SOCIETY_AND_TYPE_0_1 =
            "select s.code, s.name, s.color, r.type, count(r.*) from society s " +
                    "inner join request_post_type rpt on s.code = rpt.society_code " +
                    "inner join request r on r.id = rpt.id " +
                    "inner join request_user_actions rua on (r.id = rua.request_id and rua.action = 4) " +
                    "where r.request_status = 9 and extract(month from rua.action_date) = ? and extract(year from rua.action_date) = ? " +
                    "group by s.code, r.type";

    private static final String PREPARE_BY_SOCIETY_AND_TYPE_2 =
            "select s.code, s.name, s.color, r.type, count(r.*) from request_post rp " +
                    "inner join employee_sap es on rp.post_code = es.post_code " +
                    "inner join society s on s.code = es.society_code " +
                    "inner join request r on r.id = rp.request_id " +
                    "inner join request_user_actions rua on (r.id = rua.request_id and rua.action = 4) " +
                    "where r.request_status = 9 and extract(month from rua.action_date) = ? and extract(year from rua.action_date) = ? " +
                    "group by s.code, r.type";

    private static final String PREPARE_BY_YEAR_AND_MONTH =
            "select extract(year from rua.action_date), extract(month from rua.action_date), count(r.*) " +
                    "from request r " +
                    "inner join request_user_actions rua on (r.id = rua.request_id and rua.action = 4) " +
                    "where r.request_status = 9 " +
                    "group by extract(year from rua.action_date), extract(month from rua.action_date)";

    @Override
    public List<ReportBySociety> prepareReportBySocietyAndType(int month, int year) {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statementNotReplacement = null;
        PreparedStatement statementReplacement = null;
        ResultSet setNotReplacement = null;
        ResultSet setReplacement = null;
        List<ReportBySociety> reportBySocieties = new ArrayList<>();

        try {
            statementNotReplacement = connection.prepareStatement(PREPARE_BY_SOCIETY_AND_TYPE_0_1);
            statementNotReplacement.setInt(1, month);
            statementNotReplacement.setInt(2, year);
            statementReplacement = connection.prepareStatement(PREPARE_BY_SOCIETY_AND_TYPE_2);
            statementReplacement.setInt(1, month);
            statementReplacement.setInt(2, year);
            setNotReplacement = statementNotReplacement.executeQuery();
            setReplacement = statementReplacement.executeQuery();
            reportBySocieties.addAll(this.mapSocietyReports(setNotReplacement));
            reportBySocieties.addAll(this.mapSocietyReports(setReplacement));
            reportBySocieties = this.mergeReports(reportBySocieties);
            return reportBySocieties;
        } catch (SQLException e) {
            log.error("Error preparing report by society and request type", e);
            throw new RuntimeException(e);
        } finally {
            if (setNotReplacement != null)
                BaseDAO.close(setNotReplacement);
            if (statementNotReplacement != null)
                BaseDAO.close(statementNotReplacement);
            if (setReplacement != null)
                BaseDAO.close(setReplacement);
            if (statementReplacement != null)
                BaseDAO.close(statementReplacement);
        }
    }

    @Override
    public List<ReportByYear> prepareReportByYear() {
        Connection connection = BaseDAO.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        List<ReportByYear> reportByYears = new ArrayList<>();

        try {
            statement = connection.prepareStatement(PREPARE_BY_YEAR_AND_MONTH);
            set = statement.executeQuery();
            ReportByYear reportByYear = null;
            int year = 0;
            List<YearSummary> summaries = null;
            while (set.next()) {
                if (set.getInt(1) != year) {
                    if (reportByYear != null) {
                        reportByYear.setSummaries(summaries);
                        reportByYears.add(reportByYear);
                    }
                    year = set.getInt(1);
                    reportByYear = new ReportByYear();
                    reportByYear.setYear(set.getInt(1));
                    summaries = new ArrayList<>();
                }
                YearSummary yearSummary = new YearSummary();
                yearSummary.setMonth(set.getInt(2));
                yearSummary.setQuantity(set.getInt(3));
                assert summaries != null;
                summaries.add(yearSummary);
            }
            if (reportByYear != null) {
                reportByYear.setSummaries(summaries);
                reportByYears.add(reportByYear);
            }
            return reportByYears;
        } catch (SQLException e) {
            log.error("Error preparing report by year, month", e);
            throw new RuntimeException(e);
        } finally {
            if (set != null)
                BaseDAO.close(set);
            if (statement != null)
                BaseDAO.close(statement);
        }
    }

    private List<ReportBySociety> mergeReports(List<ReportBySociety> reportBySocieties) {
        var noDuplicates = new LinkedHashSet<>(reportBySocieties);
        noDuplicates.forEach(nd -> {
            List<SocietySummary> summaries = new ArrayList<>();
            reportBySocieties.forEach(rbs -> {
                if (nd.equals(rbs)) {
                    summaries.addAll(rbs.getSummaries());
                }
            });
            nd.setSummaries(summaries);
        });
        return new ArrayList<>(noDuplicates);
    }


    private List<ReportBySociety> mapSocietyReports(ResultSet set) throws SQLException {
        List<ReportBySociety> reportBySocieties = new ArrayList<>();
        ReportBySociety reportBySociety = null;
        String societyCode = null;
        List<SocietySummary> summaries = null;
        while (set.next()) {
            if (!set.getString(1).equals(societyCode)) {
                if (reportBySociety != null) {
                    reportBySociety.setSummaries(summaries);
                    reportBySocieties.add(reportBySociety);
                }
                societyCode = set.getString(1);
                reportBySociety = new ReportBySociety();
                reportBySociety.setSocietyName(set.getString(2));
                reportBySociety.setColor(set.getString(3));
                summaries = new ArrayList<>();
            }
            SocietySummary societySummary = new SocietySummary();
            switch (set.getInt(4)) {
                case 0:
                    societySummary.setType(RequestType.NEW_PLANNED);
                    break;
                case 1:
                    societySummary.setType(RequestType.NEW_NOT_PLANNED);
                    break;
                case 2:
                    societySummary.setType(RequestType.REPLACEMENT);
                    break;
            }
            societySummary.setQuantity(set.getInt(5));
            summaries.add(societySummary);
        }
        if (reportBySociety != null) {
            reportBySociety.setSummaries(summaries);
            reportBySocieties.add(reportBySociety);
        }
        return reportBySocieties;
    }
}
