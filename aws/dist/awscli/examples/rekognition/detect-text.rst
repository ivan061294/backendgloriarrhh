**To detect text in an image**

The following ``detect-text`` command detects text in the specified image. ::

    aws rekognition detect-text \
        --image '{"S3Object":{"Bucket":"MyImageS3Bucket","Name":"ExamplePicture.jpg"}}'

Output::

    {
        "TextDetections": [
            {
                "Geometry": {
                    "BoundingBox": {
                        "Width": 0.24624845385551453, 
                        "Top": 0.28288066387176514, 
                        "Left": 0.391388863325119, 
                        "Height": 0.022687450051307678
                    }, 
                    "Polygon": [
                        {
                            "Y": 0.28288066387176514, 
                            "X": 0.391388863325119
                        }, 
                        {
                            "Y": 0.2826388478279114, 
                            "X": 0.6376373171806335
                        }, 
                        {
                            "Y": 0.30532628297805786, 
                            "X": 0.637677013874054
                        }, 
                        {
                            "Y": 0.305568128824234, 
                            "X": 0.39142853021621704
                        }
                    ]
                }, 
                "Confidence": 94.35709381103516, 
                "DetectedText": "ESTD 1882", 
                "Type": "LINE", 
                "Id": 0
            }, 
            {
                "Geometry": {
                    "BoundingBox": {
                        "Width": 0.33933889865875244, 
                        "Top": 0.32603850960731506, 
                        "Left": 0.34534579515457153, 
                        "Height": 0.07126858830451965
                    }, 
                    "Polygon": [
                        {
                            "Y": 0.32603850960731506, 
                            "X": 0.34534579515457153
                        }, 
                        {
                            "Y": 0.32633158564567566, 
                            "X": 0.684684693813324
                        }, 
                        {
                            "Y": 0.3976001739501953, 
                            "X": 0.684575080871582
                        }, 
                        {
                            "Y": 0.3973070979118347, 
                            "X": 0.345236212015152
                        }
                    ]
                }, 
                "Confidence": 99.95779418945312, 
                "DetectedText": "BRAINS", 
                "Type": "LINE", 
                "Id": 1
            }, 
            {
                "Confidence": 97.22098541259766, 
                "Geometry": {
                    "BoundingBox": {
                        "Width": 0.061079490929841995, 
                        "Top": 0.2843210697174072, 
                        "Left": 0.391391396522522, 
                        "Height": 0.021029088646173477
                    }, 
                    "Polygon": [
                        {
                            "Y": 0.2843210697174072, 
                            "X": 0.391391396522522
                        }, 
                        {
                            "Y": 0.2828207015991211, 
                            "X": 0.4524524509906769
                        }, 
                        {
                            "Y": 0.3038259446620941, 
                            "X": 0.4534534513950348
                        }, 
                        {
                            "Y": 0.30532634258270264, 
                            "X": 0.3923923969268799
                        }
                    ]
                }, 
                "DetectedText": "ESTD", 
                "ParentId": 0, 
                "Type": "WORD", 
                "Id": 2
            }, 
            {
                "Confidence": 91.49320983886719, 
                "Geometry": {
                    "BoundingBox": {
                        "Width": 0.07007007300853729, 
                        "Top": 0.2828207015991211, 
                        "Left": 0.5675675868988037, 
                        "Height": 0.02250562608242035
                    }, 
                    "Polygon": [
                        {
                            "Y": 0.2828207015991211, 
                            "X": 0.5675675868988037
                        }, 
                        {
                            "Y": 0.2828207015991211, 
                            "X": 0.6376376152038574
                        }, 
                        {
                            "Y": 0.30532634258270264, 
                            "X": 0.6376376152038574
                        }, 
                        {
                            "Y": 0.30532634258270264, 
                            "X": 0.5675675868988037
                        }
                    ]
                }, 
                "DetectedText": "1882", 
                "ParentId": 0, 
                "Type": "WORD", 
                "Id": 3
            }, 
            {
                "Confidence": 99.95779418945312, 
                "Geometry": {
                    "BoundingBox": {
                        "Width": 0.33933934569358826, 
                        "Top": 0.32633158564567566, 
                        "Left": 0.3453453481197357, 
                        "Height": 0.07127484679222107
                    }, 
                    "Polygon": [
                        {
                            "Y": 0.32633158564567566, 
                            "X": 0.3453453481197357
                        }, 
                        {
                            "Y": 0.32633158564567566, 
                            "X": 0.684684693813324
                        }, 
                        {
                            "Y": 0.39759939908981323, 
                            "X": 0.6836836934089661
                        }, 
                        {
                            "Y": 0.39684921503067017, 
                            "X": 0.3453453481197357
                        }
                    ]
                }, 
                "DetectedText": "BRAINS", 
                "ParentId": 1, 
                "Type": "WORD", 
                "Id": 4
            }
        ]
    }
