package pe.com.centro.modules.report;

import java.util.List;

/**
 * Data Access Object for Report's operations.
 */
public interface ReportDAO {
    /**
     * Makes a summarization of requests with {@code SENT}
     * {@link pe.com.centro.domain.enumeration.RequestStatus}
     * with additional parameters such as month and year.
     *
     * @param month a month filter
     * @param year  a year filter
     * @return the list reports per society
     */
    List<ReportBySociety> prepareReportBySocietyAndType(int month, int year);

    /**
     * Makes a summarization of requests with {@code SENT}
     * {@link pe.com.centro.domain.enumeration.RequestStatus}
     *
     * @return the list reports per year
     */
    List<ReportByYear> prepareReportByYear();
}
