package pe.com.centro.modules.request;

import lombok.Getter;
import lombok.Setter;
import pe.com.centro.utils.Page;

@Getter
@Setter
public class DetailedPage<T> extends Page<T> {
    private int createdRows;
    private int approvedByHMRows;
    private int approvedByGMRows;
    private int notApprovedRows;
    private int observedRows;
    private int rejectedRows;
    private int approvedByOERows;
    private int approvedByCMRows;
    private int approvedByPresidencyRows;
    private int sentRows;
}
