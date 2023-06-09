package pe.com.centro.modules.request;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import static pe.com.centro.utils.Constants.*;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.MailRequest;
import pe.com.centro.utils.MailService;

@Slf4j
public class PostRequest3 implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.info("enviando mail");

            MailRequest mailVariable = new MailRequest();
            mailVariable.setTo("gustavo.cueto@centro.com.pe");
            mailVariable.setRequiredPosition("Puesto Test");
            mailVariable.setEmployeeToReplace(String.format("%s - %s %s, %s",
                                                    "0000",
                                                    "ApePat",
                                                    "ApeMat",
                                                    "Nombres"));
            mailVariable.setUrl("https://d7ofeisssod1b.cloudfront.net/");

            MailService.sendMail(mailVariable);
            log.info("mail enviado con exito");
            log.info("requestid");
            return new APIGatewayProxyResponseEvent()
            .withHeaders(DEFAULT_HEADERS)
            .withStatusCode(201)
            .withBody("exito");
    }
    
}
