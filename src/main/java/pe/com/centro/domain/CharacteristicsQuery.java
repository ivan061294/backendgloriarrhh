package pe.com.centro.domain;
import java.io.Serializable;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CharacteristicsQuery implements Serializable{

    private List<JsonPostgres> idiomas;

    private List<JsonPostgres> formacion;

    private List<JsonPostgres> funciones;

    private List<JsonPostgres> habilidades;

    private List<JsonPostgres> centroestudio;

    private List<JsonPostgres> conocimientos;
}
