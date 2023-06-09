package pe.com.centro.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EGenre {
    /**
     * Male genre
     */
    MALE(0),
    /**
     * Female genre
     */
    FEMALE(1),
    /**
     * Indifferent
     */
    INDIFFERENT(2);

    @Getter
    private final int value;
}
