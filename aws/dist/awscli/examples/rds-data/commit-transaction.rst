**To commit a SQL transaction**

The following ``commit-transaction`` example ends the specified SQL transaction and commits the changes that you made as part of it. ::

    aws rds-data commit-transaction \
        --resource-arn "arn:aws:rds:us-west-2:123456789012:cluster:mydbcluster" \
        --secret-arn "arn:aws:secretsmanager:us-west-2:123456789012:secret:mysecret" \
        --transaction-id "ABC1234567890xyz"

Output::

    {
        "transactionStatus": "Transaction Committed"
    }   

For more information, see `Using the Data API for Aurora Serverless <https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/data-api.html>`__ in the *Amazon RDS User Guide*.
