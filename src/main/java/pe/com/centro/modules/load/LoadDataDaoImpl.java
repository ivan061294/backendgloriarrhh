package pe.com.centro.modules.load;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.configuration.BaseDAO;
import pe.com.centro.domain.Batch;
import pe.com.centro.domain.Bloque;
import pe.com.centro.domain.InsertarDataRequest;
import pe.com.centro.domain.InsertarDataResponse;
import pe.com.centro.utils.Constants;
import pe.com.centro.utils.StorageService;

@Slf4j
public class LoadDataDaoImpl implements LoadDataDao {

	BufferedWriter writer = null;
	BufferedReader reader = null;
	File archivoDetalles = null;

	private static final String FN_OBTENER_BATCHFILE = "SELECT * FROM integracion.obtener_batch( ? )";

	public Batch getFlujoFile(String keyFile) {
		Connection connection = BaseDAO.getConnection();
		PreparedStatement statement = null;
		ResultSet res = null;
		Batch batch = null;
		try {
			statement = connection.prepareStatement(FN_OBTENER_BATCHFILE);
			statement.setString(1, keyFile);
			log.info("statement get file batch {}", statement.toString());
			res = statement.executeQuery();
			if (res.next()) {
				batch = new Batch();
				batch.setIdbatch(res.getInt(1));
				batch.setNombreArchivo(res.getString(2));
				batch.setSeparador(res.getString(3));
				batch.setNombreTablaJob(res.getString(4));
				batch.setNombreFuncion(res.getString(5));
			}
		} catch (SQLException e) {
			log.error("Error updating request status", e);
			throw new RuntimeException();
		} finally {
			if (statement != null)
				BaseDAO.close(statement);
		}
		return batch;
	}

	public Boolean JobIntegracionTable(String key) {
		Connection connection = BaseDAO.getConnection();
		PreparedStatement statement = null;
		ResultSet res = null;
		int estado=1;
		String mensajeError="";
		Boolean funRes = true;
		try {
			statement = connection.prepareStatement( "SELECT * FROM "+key);
			log.info("statement process integra {}", statement.toString());
			res = statement.executeQuery();
			if (res.next()) {
				estado=res.getInt(1);
				mensajeError=res.getString(2);
			}
			if (estado==1) {
				funRes=false;
				log.info("mensajeError integra {}", mensajeError);
			}
		} catch (SQLException e) {
			log.error("Error updating request status", e);
			throw new RuntimeException();
		} finally {
			if (statement != null)
				BaseDAO.close(statement);
		}
		return funRes;
	}

	@Override
	public String ObtenerArchivoEventoS3(String keyFile) {

		StorageService storage = new StorageService(Constants.AWS_S3_BATCH_FILES_BUCKET_NAME);
		try {
			if (!(keyFile.contains("/")) && !(keyFile.split("/")[0].equalsIgnoreCase("sap"))) {
				return "ruta archivo sin integracion :" + keyFile;
			}
			Batch batch = getFlujoFile(keyFile);
			
			if (batch == null) {
				return "No existe tabla de integracion para el archivo :" + keyFile;
			}
			log.info("batch{}", batch.toString());
			InputStream readResponse = storage.descargarArchivo(keyFile);
			String nametable = batch.getNombreTablaJob();
			reader = new BufferedReader(new InputStreamReader(readResponse, StandardCharsets.UTF_8));
			List<String> cabeceras = null;
			char comillasdobles = '"';
			cabeceras = Arrays.asList(
					reader.readLine().replace(String.valueOf(comillasdobles), " ").replace(",", ";").split("\\" + ";"));
			for (String cabecera : cabeceras) {
				log.info("  -  " + "cabecera :" + cabecera);

			}

			// ACTIVIDAD 8: Se validar e inserta la data en la tabla de trabajo, se insertan
			// también los detalles
			Integer leidos = 0;
			Integer correctos = 0;
			Integer incorrectos = 0;

			// Recorremos filas
			int offset = 1;
			while (true) {
				Bloque bloque = _obtenerBloque(reader,
						cabeceras, batch.getSeparador(),
						offset, 5000);
				// Actualizamos contadores
				leidos += bloque.getLeidos();
				correctos += bloque.getCorrectos();
				incorrectos += bloque.getIncorrectos();
				log.info("leidos{} , correctos = {}, incorrectos {}", leidos, correctos, incorrectos);
				//
				if (bloque.getLeidos() > 0) {
					// Si se leyeron filas
					if (bloque.getCorrectos() > 0) {
						InsertarDataRequest insertarDataRequest = new InsertarDataRequest();
						insertarDataRequest.setIdintegracion(1);
						insertarDataRequest.setCabeceras(cabeceras);
						insertarDataRequest.setData(bloque.getData());
						//
						InsertarDataResponse insertarDataResponse = insertarData(insertarDataRequest, nametable);
						// Verificamos si se creó correctamente
						if (insertarDataResponse.getStatus() == 1) {
							log.info("Error en insertar error {}", insertarDataResponse.getMessage());
							return "Error en insertar data";
						}
					}

					// Actualizamos offset
					offset += bloque.getLeidos();
				} else {
					break;
				}
			}
			if(!JobIntegracionTable(batch.getNombreFuncion())){
				return "error en integracion";

			}

		} catch (Exception e) {
			log.error("  -  " + "Error en obtener archivo   Error:" + e);
		}

		return "archivo2";
	}

	private Bloque _obtenerBloque(BufferedReader reader, List<String> cabeceras, String separador, Integer offset,
			Integer limite) throws IOException {
		String linea = null;
		Integer leidos = 0;
		Integer correctos = 0;
		Integer incorrectos = 0;
		//
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		int numerofila = offset;

		do {
			linea = reader.readLine();

			if (linea == null) {
				break;
			} else {
				leidos++;
				numerofila++;
			}

			// Obtenemos las cabeceras .replace(String.valueOf(comillasdobles), "
			// ").replace(",", ";").split("\\" + ";")
			char comillasdobles = '"';
			String[] celdas = linea.replace(String.valueOf(comillasdobles), "").replace(",", ";")
					.split("\\" + separador, -1);

			//
			if (celdas.length != cabeceras.size()) {
				log.info("celdas length{} , cabeceras length{}", celdas.length, cabeceras.size());
				log.info("linea leida {}", linea);
				List<String> celdaseroneas = new ArrayList<>();
				for (int i = 0; i < celdas.length; i++) {
					celdaseroneas.add(celdas[i]);
				}
				log.info("celdaincorrecta{}", celdaseroneas.toString());
				log.info("cabeceras correctas {}", cabeceras.toString());

				incorrectos++;
			} else {

				// Obteemos fila
				HashMap<String, String> fila = _obtenerFila(cabeceras, celdas);
				HashMap<String, Object> valoresSql = _validarFila(numerofila, cabeceras, fila);
				//
				if (valoresSql != null) {
					data.add(valoresSql);
					correctos++;
				} else {
					incorrectos++;
				}
			}

		} while (leidos < limite);

		Bloque bloque = new Bloque();
		bloque.setData(data);
		bloque.setLeidos(leidos);
		bloque.setCorrectos(correctos);
		bloque.setIncorrectos(incorrectos);

		return bloque;
	}

	private HashMap<String, Object> _validarFila(Integer numerofila,
			List<String> cabeceras, HashMap<String, String> fila) throws IOException {
		// Valores
		HashMap<String, Object> valores = new HashMap<String, Object>();
		// Recorremos según metas
		for (String cabecera : cabeceras) {
			String valor = fila.get(cabecera);
			valores.put(cabecera, valor);
		}
		//
		return valores;
	}

	private HashMap<String, String> _obtenerFila(List<String> cabeceras, String[] celdas) {
		HashMap<String, String> fila = new HashMap<String, String>();
		// Recorremos
		for (int j = 0; j < cabeceras.size(); j++) {
			String campo = cabeceras.get(j).toString();
			String valor = celdas[j].toString();
			fila.put(campo, valor);
		}

		return fila;
	}

	public InsertarDataResponse insertarData(InsertarDataRequest request, String nametable) {
		InsertarDataResponse response = new InsertarDataResponse();
		long tiempoInicio = System.currentTimeMillis();
		Connection conexion = null;
		PreparedStatement preparedStatement = null;
		try {
			log.info(" - " + "[INICIO] - METODO: [insertarData] ");

			String sql = "INSERT INTO "+ nametable 
					+ " (idcarga,fechaproceso, "
					+ String.join(", ", request.getCabeceras()) + ") VALUES "
					+ _generarData(request.getIdintegracion(), request.getCabeceras(), request.getData());

			// log.info(sql);

			conexion = BaseDAO.getConnection();
			StringBuffer funcion = new StringBuffer();
			funcion.append(sql);
			// log.info( " - " + "[DATABASE] - FUNCTIONS ");
			log.info(funcion.toString());
			preparedStatement = conexion.prepareStatement(funcion.toString());
			preparedStatement.execute();
			response.setStatus(0);
			response.setMessage("OK");
		} catch (Exception e) {
			response.setStatus(1);
			response.setMessage(e.getMessage());
			e.printStackTrace();
			log.info("  -  " + "ERROR: " + e);
		} finally {
			if (preparedStatement != null)
				try {
					BaseDAO.close(preparedStatement);
					;
				} catch (Exception e) {
					e.printStackTrace();
					log.info("  -  " + "ERROR: " + e);
				}

			log.info("  -  " + "Tiempo Transacurrido (ms): ["
					+ (System.currentTimeMillis() - tiempoInicio) + "]");
			log.info("  -  " + "[FIN] - METODO: [insertarData] ");
		}
		return response;
	}

	private String _generarData(Integer idIntegracion, List<String> cabeceras, List<HashMap<String, Object>> data) {

		ArrayList<String> partes = new ArrayList<String>();

		for (HashMap<String, Object> item : data) {

			ArrayList<String> celdas = new ArrayList<String>();
			for (String cabecera : cabeceras) {

				Object valor = item.get(cabecera);

				String valorsql = null;

				if (valor != null) {

					if (Boolean.class.isInstance(valor)) {
						valorsql = (Boolean) valor ? "true" : "false";
					} else if (Integer.class.isInstance(valor)) {
						valorsql = valor.toString();
					} else if (Double.class.isInstance(valor)) {
						valorsql = valor.toString();
					} else {
						valorsql = valor.toString().equalsIgnoreCase("null") ? "null" : "'" + valor.toString() + "'";
					}
				} else {
					valorsql = "null";
				}

				celdas.add(valorsql);
			}

			partes.add("(nextval('temporal.tempcargas3secuencia" + "') "
					+ ",current_date, "
					+ String.join(", ", celdas) + ")");
		}

		return String.join(",\n", partes);
	}
}
