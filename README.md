# Centro RR.HH. Staff Requirement

## Requirements

- Java Version 11 (Oracle JDK or OpenJDK).
- AWS SAM (Serverless Application Model).
- AWS Account (With necessary permissions).
- Maven _(until AWS solves "building in container")_

## Build, Development and Deploy

### For Local Development

To init a local aws stack + database, run:

**Note**: Use it only to test S3 events. For API test, use SAM instead.

```shell
$ docker-compose -f src/main/docker/localstack.yml up -d
```

Check `local-test-files/localstack-s3-test.sh` for more details.

To init only a local database, run:

```shell
$ docker-compose -f src/main/docker/localstack.yml up -d centro-postgres-database
```

To build the project, run (using local maven):

**Note**: For some reason building project in a container doesn't work, so you have to install maven locally.

```shell
$ sam build
```

To run it:

```shell
$ sam deployer start-api
```

### For Cloud Deployment

To build and deploy the project in development profile, run:

```shell
$ sam build --config-env dev
$ sam deploy --config-env dev
```

To build the project in production profile, run:

```shell
$ sam build --config-env prod
# unfortunately there's no way to pass maven params through SAM,
# so you have to do it manually
$ mvn clean package -Pprod
$ sam deploy --config-env prod
```

For SAM's environment configuration check: `samconfig.toml` in project's root directory.

## Handlers

### Handlers' Nomenclature

[Method]-[Entity(s)].java

The method could be:

- Get
- Post
- Put
- Delete
- Patch

The entities are from the domain package.

### Handlers basic structure

```java
package pe.com.centro.modules.custom;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import pe.com.centro.utils.Serializer;
import pe.com.centro.domain.MyCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GetCustoms implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.debug("REST request to ...");
        // Request Parsing
        MyCustom myCustom = Serializer.deserialize(input.getBody(), MyCustom.class);

        // Business Logic Processing
        List<MyCustom> myCustoms = new ArrayList<>();

        // Response Parsing
        APIGatewayProxyResponseEvent output = new APIGatewayProxyResponseEvent();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Powered-By", "AWS Lambda & Serverless");

        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(200)
                .withBody(Serializer.serialize(myCustom));
    }
}
```

1. **Request Parsing**: Parse the request body if it exists. and retrieve other kind of data like headers, params, etc.

2. **Business Logic Processing**: Manage your business logic.

3. **Response Parsing**: Parse the response and prepare headers, status codes, etc.

## Events Configuration:

### For Local Development:

You can configure `local-template.yml` to manage an emulated API Gateway or any other event (Using SAM).

- Check
  [template.yml](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification-template-anatomy.html)
  for full configuration.

Here's an example:

```yaml
Resources:
    GetCustoms:
        Type: AWS::Serverless::Function
        Properties:
            CodeUri: .
            Handler: pe.com.centro.modules.custom.GetCustoms
            Runtime: java11
            Description: Get Customs
            MemorySize: 512
            Timeout: 100
            Events:
                HttpPost:
                    Type: Api
                    Properties:
                        Path: /customs
                        Method: get
```

### For Cloud Deployment

You can configure `template.yml` to auto associate AWS API Gateway, RDS Databases, Security Groups, etc. or other
service with your Lambda functions.

## Maven Profiles

There are only two profiles: Development (_dev_) and Production (_prod_).

They change:

- **ROOT Logging Level** (_DEBUG_ to _INFO_)

See:

- [Introduction to Maven Profiles](https://maven.apache.org/guides/introduction/introduction-to-profiles.html) for full
  configuration.

## Logging

The application uses `log4j2` to manage logs but also uses `slf4j` which is pretty easy to use. You can configure log's
display, colors, etc. in `log4j2.xml` file in `resources` folder.

- You can also check [Log4j Configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html) for more
  details.

## Git Management

The pattern solution (model) used to deal with changes is the following.
![](https://nvie.com/img/git-model@2x.png)

### Create Branch

To create a new branch, from your current branch run:

```shell
$ git checkout -b new_branch current_branch
```

To delete a branch, run:

```shell
$ git branch -d my_branch
```

To merge a branch and don't delete it, run: (previously you have to commit your changes)

```shell
$ git checkout dev # switch to development branch
$ git merge --no-ff my_branch
```

To push changes

```shell
$ git push origin branch_to_push_in
```

#### Feature Branch's Nomenclature

For feature branches the name convention is `account_name/feature-feature_name`.
E.g. `enriqueavilac/feature-new_wonderful_feature`

For more details check [A successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/)

## Additional Details

If you are using MFA authentication in your AWS Account don't forget to use it too when configuring AWS CLI credentials.

Here's an example:

This command is going to return you 3 tokens.

```shell
$ aws sts get-session-token --serial-number arn:aws:iam::<account-id>:mfa/<account-username> --token-code <mfa-code>
```

After that, update your `.aws/credentials` file (it is usually in `~` user's root directory in Linux Distros)

```
[my_profile]
aws_access_key_id = XXXXXXXXXXXXXXXXXXXX
aws_secret_access_key = xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
aws_session_token = xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

Then use `--profile my_profile` in your AWS CLI commands or SAM commands if it isn't `default` profile.# centro-human-resources-lambda-dev
