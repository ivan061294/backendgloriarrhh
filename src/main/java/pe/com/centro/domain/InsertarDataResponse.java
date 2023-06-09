package pe.com.centro.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class InsertarDataResponse implements Serializable{
    private Integer status;
    private String message;
}
