**To get information about an input**

The following ``describe-input`` example retrieves the details of an input. ::

    aws iotevents describe-input \
        --input-name PressureInput

Output::

    {
        "input": {
            "inputConfiguration": {
                "status": "ACTIVE", 
                "inputArn": "arn:aws:iotevents:us-west-2:123456789012:input/PressureInput", 
                "lastUpdateTime": 1560795312.542, 
                "creationTime": 1560795312.542, 
                "inputName": "PressureInput", 
                "inputDescription": "Pressure readings from a motor"
            }, 
            "inputDefinition": {
                "attributes": [
                    {
                        "jsonPath": "sensorData.pressure"
                    }, 
                    {
                        "jsonPath": "motorid"
                    }
                ]
            }
        }
    }

For more information, see `DescribeInput <https://docs.aws.amazon.com/iotevents/latest/developerguide/iotevents-commands.html#api-iotevents-DescribeInput>`__ in the *AWS IoT Events Developer Guide**.
