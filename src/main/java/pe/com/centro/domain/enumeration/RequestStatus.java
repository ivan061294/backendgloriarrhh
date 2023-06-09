package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RequestStatus {
    /**
     * When the request is created.
     * <br/>
     * The superior will look up for this kind of requests.
     */
    CREATED(0),
    /**
     * When the request is approved by Human Management.
     */
    APPROVED_BY_HM(1),
    /**
     * When the request is approved by General Management.
     */
    APPROVED_BY_GM(2),
    /**
     * When the request is rejected by the General Manager or Human Management Unit
     */
    NOT_APPROVED(3),
    /**
     * When Org. Effectivity or other level above it observe the request.
     */
    OBSERVED(4),
    /**
     * When Org. Effectivity or other level above it reject the request.
     */
    REJECTED(5),
    /**
     * When Org. Effectivity approves the request.
     */
    APPROVED_BY_OE(6),
    /**
     * When Corporate Management approves the request.
     */
    APPROVED_BY_CM(7),
    /**
     * When Org. Effectivity or other level above it approves the request.
     */
    APPROVED_BY_PRESIDENCY(8),
    /**
     * When Org. Effectivity clears and closes the request.
     */
    SENT(9);

    @Getter
    private final int value;
}
