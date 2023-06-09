version = 0.1
[default]
[default.deploy]
[default.deploy.parameters]
stack_name = "sam-app-rrhh-20221215"
s3_bucket = "aws-sam-cli-managed-default-samclisourcebucket-188w785jemoov"
s3_prefix = "sam-app-rrhh-20221215"
region = "us-east-2"
confirm_changeset = true
capabilities = "CAPABILITY_IAM"
parameter_overrides = "CodeUri=\"target/human-management-api-1.0.0.jar\" DatabaseServer=\"centro-postgres-database.ca9yjy0jz5xs.us-east-1.rds.amazonaws.com:5432\" DatabaseSchema=\"postgres\" DatabaseUser=\"postgres\" DatabasePassword=\"c3nTr02022\" DefaultCorsOrigins=\"*\""
image_repositories = []
