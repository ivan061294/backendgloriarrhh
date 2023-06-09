package pe.com.centro.modules.post;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.ErrorResponse;
import pe.com.centro.domain.Post;
import pe.com.centro.utils.Serializer;

import java.util.List;

import static pe.com.centro.utils.Constants.DEFAULT_HEADERS;

@Slf4j
public class GetPostByCode implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST Request to get a post by code");
        String code = input.getPathParameters().get("code");
        PostDAO dao = new PostDAOImpl();

        List<Post> post = dao.findByCode(code);

        if (post.size()==0) {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(DEFAULT_HEADERS)
                    .withStatusCode(404)
                    .withBody(Serializer.serialize(new ErrorResponse("No se encontró dicho puesto")));
        }

        return new APIGatewayProxyResponseEvent()
                .withHeaders(DEFAULT_HEADERS)
                .withStatusCode(200)
                .withBody(Serializer.serialize(post));
    }
}
