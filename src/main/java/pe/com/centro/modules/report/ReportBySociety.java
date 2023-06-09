package pe.com.centro.modules.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * POJO to manage reports grouped by society.
 */
@Getter
@Setter
@ToString
public class ReportBySociety {
    private String societyName;

    private String color;

    private List<SocietySummary> summaries;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportBySociety that = (ReportBySociety) o;
        return societyName.equals(that.societyName) && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(societyName, color);
    }
}
