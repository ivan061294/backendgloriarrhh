package pe.com.centro.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * POJO to manage pages
 * @param <T> the type of the content
 */
@Getter
@Setter
public class Page<T> {
    private int rows;

    private List<T> content;
}
