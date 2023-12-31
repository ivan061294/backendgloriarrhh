**To retrieve information about a connector definition**

The following ``get-connector-definition`` example retrieves information about the specified connector definition. To retrieve the IDs of your connector definitions, use the ``list-connector-definitions`` command. ::

    aws greengrass get-connector-definition \
        --connector-definition-id "b5c4ebfd-f672-49a3-83cd-31c7216a7bb8"

Output::

    {
        "Arn": "arn:aws:greengrass:us-west-2:123456789012:/greengrass/definition/connectors/b5c4ebfd-f672-49a3-83cd-31c7216a7bb8",
        "CreationTimestamp": "2019-06-19T19:30:01.300Z",
        "Id": "b5c4ebfd-f672-49a3-83cd-31c7216a7bb8",
        "LastUpdatedTimestamp": "2019-06-19T19:30:01.300Z",
        "LatestVersion": "63c57963-c7c2-4a26-a7e2-7bf478ea2623",
        "LatestVersionArn": "arn:aws:greengrass:us-west-2:123456789012:/greengrass/definition/connectors/b5c4ebfd-f672-49a3-83cd-31c7216a7bb8/versions/63c57963-c7c2-4a26-a7e2-7bf478ea2623",
        "Name": "MySNSConnector",
        "tags": {}
    }

For more information, see `Integrate with Services and Protocols Using Greengrass Connectors <https://docs.aws.amazon.com/greengrass/latest/developerguide/connectors.html>`__ in the **AWS IoT Greengrass Developer Guide**.
