**To list all of the resources in a resource group**

The following ``list-resource-groups`` example lists all of the resources that are part of the specified resource group. ::

    aws resource-groups list-group-resources \ 
        --group-name tbq-WebServer

Output::

    {
        "ResourceIdentifiers": [
            {
                "ResourceArn": "arn:aws:ec2:us-west-2:123456789012:instance/i-09f77fa38c12345ab",
                "ResourceType": "AWS::EC2::Instance"
            }
        ]
    }
