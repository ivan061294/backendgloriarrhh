package pe.com.centro.modules.report;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.getInstance;
import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;
import static pe.com.centro.utils.Serializer.serialize;

@Slf4j
public class GetReportByParams implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to get a report by society and other params");

        Map<String, String> params = input.getQueryStringParameters();

        if (params == null || !params.containsKey("option")) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(400)
                    .withBody(serialize(new ErrorResponse("Debe definirse una opción válida")));
        }

        int month = getInstance().get(Calendar.MONTH) + 1;
        int year = getInstance().get(Calendar.YEAR);

        if (params.containsKey("month"))
            month = parseInt(params.get("month"));
        if (params.containsKey("year"))
            year = parseInt(params.get("year"));

        ReportDAO dao = new ReportDAOImpl();

        switch (params.get("option")) {
            case "per-society":
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(200)
                        .withBody(serialize(dao.prepareReportBySocietyAndType(month, year)));
            case "per-year":
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(200)
                        .withBody(serialize(dao.prepareReportByYear()));
            default:
                return new APIGatewayProxyResponseEvent()
                        .withHeaders(DEFAULT_HEADERS)
                        .withStatusCode(400)
                        .withBody(serialize(new ErrorResponse("La opción seleccionada no es válida")));
        }

    }
}
