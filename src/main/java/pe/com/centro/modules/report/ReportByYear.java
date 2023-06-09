package pe.com.centro.modules.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * POJO to manage reports grouped by year.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ReportByYear {
    private int year;

    private List<YearSummary> summaries;
}
