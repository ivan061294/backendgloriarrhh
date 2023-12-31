**To retrieves information about the status, configuration, and other settings for all the campaigns that are associated with an application**

The following ``get-campaigns`` example retrieves information about the status, configuration, and other settings for all the campaigns that are associated with an application. ::

    aws pinpoint get-campaigns \
        --application-id 6e0b7591a90841d2b5d93fa11143e5a7 \
        --region us-east-1

Output::

    {
        "CampaignsResponse": {
            "Item": [
                {
                    "AdditionalTreatments": [],
                    "ApplicationId": "6e0b7591a90841d2b5d93fa11143e5a7",
                    "Arn": "arn:aws:mobiletargeting:us-east-1:AIDACKCEVSQ6C2EXAMPLE:apps/6e0b7591a90841d2b5d93fa11143e5a7/campaigns/7e1280344c8f4a9aa40a00b006fe44f1",
                    "CreationDate": "2019-10-08T18:40:22.905Z",
                    "Description": " ",
                    "HoldoutPercent": 0,
                    "Id": "7e1280344c8f4a9aa40a00b006fe44f1",
                    "IsPaused": false,
                    "LastModifiedDate": "2019-10-08T18:40:22.905Z",
                    "Limits": {},
                    "MessageConfiguration": {
                        "EmailMessage": {
                            "FromAddress": "sender@example.com",
                            "HtmlBody": "<!DOCTYPE html>\n    <html lang=\"en\">\n    <head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n</head>\n<body>Hello</body>\n</html>",
                            "Title": "PInpointDemo Test"
                        }
                    },
                    "Name": "MyCampaign1",
                    "Schedule": {
                        "IsLocalTime": false,
                        "QuietTime": {},
                        "StartTime": "IMMEDIATE",
                        "Timezone": "UTC"
                    },
                    "SegmentId": "b66c9e42f71444b2aa2e0ffc1df28f60",
                    "SegmentVersion": 1,
                    "State": {
                        "CampaignStatus": "COMPLETED"
                    },
                    "tags": {},
                    "TemplateConfiguration": {},
                    "Version": 1
                },
                {
                    "AdditionalTreatments": [],
                    "ApplicationId": "6e0b7591a90841d2b5d93fa11143e5a7",
                    "Arn": "arn:aws:mobiletargeting:us-east-1:AIDACKCEVSQ6C2EXAMPLE:apps/6e0b7591a90841d2b5d93fa11143e5a7/campaigns/a1e63c6cc0eb43ed826ffcc3cc90b30d",
                    "CreationDate": "2019-10-08T18:40:16.581Z",
                    "Description": " ",
                    "HoldoutPercent": 0,
                    "Id": "a1e63c6cc0eb43ed826ffcc3cc90b30d",
                    "IsPaused": false,
                    "LastModifiedDate": "2019-10-08T18:40:16.581Z",
                    "Limits": {
                        "Daily": 0,
                        "MaximumDuration": 60,
                        "MessagesPerSecond": 50,
                        "Total": 0
                    },
                    "MessageConfiguration": {
                        "EmailMessage": {
                            "FromAddress": "sender@example.com",
                            "HtmlBody": "<!DOCTYPE html>\n    <html lang=\"en\">\n    <head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n</head>\n<body>Demo</body>\n</html>",
                            "Title": "PinpointDemo"
                        }
                    },
                    "Name": "MyCampaign2",
                    "Schedule": {
                        "IsLocalTime": false,
                        "StartTime": "IMMEDIATE",
                        "Timezone": "utc"
                    },
                    "SegmentId": "b66c9e42f71444b2aa2e0ffc1df28f60",
                    "SegmentVersion": 1,
                    "State": {
                        "CampaignStatus": "COMPLETED"
                    },
                    "tags": {},
                    "TemplateConfiguration": {},
                    "Version": 1
                }
            ]
        }
    }