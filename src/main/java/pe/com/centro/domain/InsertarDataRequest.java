package pe.com.centro.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class InsertarDataRequest implements Serializable{
    private Integer idintegracion;
    private List<String> cabeceras;
    private List<HashMap<String, Object>> data;
}
