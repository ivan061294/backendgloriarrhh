package pe.com.centro.domain;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class Bloque {
    ArrayList<HashMap<String, Object>> data;
    Integer leidos;
    Integer correctos;
    Integer incorrectos;
}
