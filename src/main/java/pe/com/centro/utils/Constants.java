package pe.com.centro.utils;

import java.util.Map;

import static java.lang.System.getenv;

/**
 * Utils for constants
 */
public final class Constants {
    /**
     * Default headers.
     * <ul>
     *     <li>Content-Type: application/json</li>
     *     <li>X-Powered-By: AWS Lambda & Serverless</li>
     *     <li>Access-Control-Allow-Headers: Authorization</li>
     *     <li>Access-Control-Expose-Headers: X-Total-Count</li>
     *     <li>Access-Control-Allow-Origin: http://localhost:4200</li>
     * </ul>
     */
    public static final Map<String, String> DEFAULT_HEADERS = Map.of(
            "Content-Type", "application/json",
            //"X-Powered-By", "AWS Lambda & Serverless",
            //"Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key",
            "Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With",
            //"Access-Control-Expose-Headers", "X-Total-Count",
            //"Access-Control-Allow-Methods", "OPTIONS,GET,PATCH",
            "Access-Control-Allow-Methods", "OPTIONS,POST,GET,PUT,DELETE,PATCH",
            //"Access-Control-Allow-Origin", "https://main.d27mkt4ykx9f1v.amplifyapp.com"
            "Access-Control-Allow-Origin", getenv("AWS_DEFAULT_CORS_ORIGINS")
    );

    //TODO: Completar para los otros tipos de requerimientos. 
    public static final Map<String, String> HTML_MESSAGES = Map.of(
        "New-Requirement-Plan-NoPlan",
                "<body>" +
                        "<h4>Se ha registrado el requerimiento por la posición <b>@@puesto</b> satisfactoriamente</h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a><p>"+
                "</body>",

        "Requirement-Pending-Approved-Plan-NoPlan",
                "<body>" +
                        "<h4>Te encuentras pendiente de aprobación del requerimiento por la posición <b>@@puesto</b> </h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a></p>" +
                "</body>",

        "Requirement-Reject-Plan-NoPlan",
                "<body>" +
                        "<h4>Se ha rechazado el requerimiento por la posición <b>@@puesto</b>.</h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a></p>" +
                "</body>",

        "Requirement-Approved-Plan-NoPlan",
                "<body>" +
                        "<h4>Se realizó la aprobación de un requerimiento de personal para la posición @@puesto</h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a></p>" +
                "</body>",
        //Reemplazo
        "New-Requirement-Reemplazo",
                "<body>" +
                        "<h4>Se ha registrado el requerimiento por la posición <b>@@puesto</b> satisfactoriamente</h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a><p>"+
                "</body>",

        "Requirement-Pending-Approved-Reemplazo",
                "<body>" +
                        "<h4>Te encuentras pendiente de aprobación del requerimiento por la posición <b>@@puesto</b> </h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a></p>" +
                "</body>",

        "Requirement-Reject-Reemplazo",
                "<body>" +
                        "<h4>Se ha rechazado el requerimiento por la posición <b>@@puesto</b>. Para mayor detalle, entrar a la plataforma digital de requerimientos</h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a></p>" +
                "</body>",

        "Requirement-Approved-Reemplazo",
                "<body>" +
                        "<h4>Se realizó la aprobación de un requerimiento de personal para la posición @@puesto</h4>" +
                        "<p>Para mayor detalle, entrar a la <a href=\"@@url\">Plataforma digital de requerimientos</a></p>" +
                "</body>"
    );

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;

    private static final String CREATED_COUNT_HEADER = "X-Total-Created-Count";
    private static final String APPROVED_BY_HM_COUNT_HEADER = "X-Total-Approved-HM-Count";
    private static final String APPROVED_BY_GM_COUNT_HEADER = "X-Total-Approved-GM-Count";
    private static final String NOT_APPROVED_COUNT_HEADER = "X-Total-Not-Approved-Count";
    private static final String OBSERVED_COUNT_HEADER = "X-Total-Observed-Count";
    private static final String REJECTED_COUNT_HEADER = "X-Total-Rejected-Count";
    private static final String APPROVED_BY_OE_COUNT_HEADER = "X-Total-Approved-OE-Count";
    private static final String APPROVED_BY_CM_COUNT_HEADER = "X-Total-Approved-CM-Count";
    private static final String APPROVED_BY_PRESIDENCY_COUNT_HEADER = "X-Total-Approved-Presidency-Count";
    private static final String SENT_COUNT_HEADER = "X-Total-Sent-Count";

    public static final String[] DETAILED_HEADERS = {
            CREATED_COUNT_HEADER,
            APPROVED_BY_HM_COUNT_HEADER,
            APPROVED_BY_GM_COUNT_HEADER,
            NOT_APPROVED_COUNT_HEADER,
            OBSERVED_COUNT_HEADER,
            REJECTED_COUNT_HEADER,
            APPROVED_BY_OE_COUNT_HEADER,
            APPROVED_BY_CM_COUNT_HEADER,
            APPROVED_BY_PRESIDENCY_COUNT_HEADER,
            SENT_COUNT_HEADER
    };

    public static final String AWS_S3_FILES_BUCKET_NAME = getenv("AWS_S3_FILES_BUCKET_NAME");

    public static final String AWS_S3_BATCH_FILES_BUCKET_NAME = getenv("AWS_S3_BATCH_FILES_BUCKET_NAME");
}
