**To get a CloudFront field-level encryption profile**

The following example gets the CloudFront field-level encryption profile with
ID ``PPK0UOSIF5WSV`` , including its ``ETag``::

    aws cloudfront get-field-level-encryption-profile --id PPK0UOSIF5WSV

Output::

    {
        "ETag": "E1QQG65FS2L2GC",
        "FieldLevelEncryptionProfile": {
            "Id": "PPK0UOSIF5WSV",
            "LastModifiedTime": "2019-12-10T01:03:16.537Z",
            "FieldLevelEncryptionProfileConfig": {
                "Name": "ExampleFLEProfile",
                "CallerReference": "cli-example",
                "Comment": "FLE profile for AWS CLI example",
                "EncryptionEntities": {
                    "Quantity": 1,
                    "Items": [
                        {
                            "PublicKeyId": "K2K8NC4HVFE3M0",
                            "ProviderId": "ExampleFLEProvider",
                            "FieldPatterns": {
                                "Quantity": 1,
                                "Items": [
                                    "ExampleSensitiveField"
                                ]
                            }
                        }
                    ]
                }
            }
        }
    }
