package pe.com.centro.modules.report;

import lombok.Getter;
import lombok.Setter;
import pe.com.centro.domain.enumeration.RequestType;

/**
 * Summary of {@code SENT} {@link pe.com.centro.domain.enumeration.RequestStatus}
 * requests for specific {@link pe.com.centro.domain.enumeration.RequestType}
 */
@Getter
@Setter
public class SocietySummary {
    private RequestType type;

    private int quantity;
}
