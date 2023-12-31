**To list the faces in a collection**

The following ``list-faces`` command lists the faces in the specified collection. ::

    aws rekognition list-faces \
        --collection-id MyCollection

Output::

    {
        "FaceModelVersion": "3.0", 
        "Faces": [
            {
                "BoundingBox": {
                    "Width": 0.5216310024261475, 
                    "Top": 0.3256250023841858, 
                    "Left": 0.13394300639629364, 
                    "Height": 0.3918749988079071
                }, 
                "FaceId": "0040279c-0178-436e-b70a-e61b074e96b0", 
                "ExternalImageId": "image1.jpg", 
                "Confidence": 100.0, 
                "ImageId": "f976e487-3719-5e2d-be8b-ea2724c26991"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.5074880123138428, 
                    "Top": 0.3774999976158142, 
                    "Left": 0.18302799761295319, 
                    "Height": 0.3812499940395355
                }, 
                "FaceId": "086261e8-6deb-4bc0-ac73-ab22323cc38d", 
                "ExternalImageId": "image2.jpg", 
                "Confidence": 99.99930572509766, 
                "ImageId": "ae1593b0-a8f6-5e24-a306-abf529e276fa"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.5574039816856384, 
                    "Top": 0.37187498807907104, 
                    "Left": 0.14559100568294525, 
                    "Height": 0.4181250035762787
                }, 
                "FaceId": "11c4bd3c-19c5-4eb8-aecc-24feb93a26e1", 
                "ExternalImageId": "image3.jpg", 
                "Confidence": 99.99960327148438, 
                "ImageId": "80739b4d-883f-5b78-97cf-5124038e26b9"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.18562500178813934, 
                    "Top": 0.1618019938468933, 
                    "Left": 0.5575000047683716, 
                    "Height": 0.24770599603652954
                }, 
                "FaceId": "13692fe4-990a-4679-b14a-5ac23d135eab", 
                "ExternalImageId": "image4.jpg", 
                "Confidence": 99.99340057373047, 
                "ImageId": "8df18239-9ad1-5acd-a46a-6581ff98f51b"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.5307819843292236, 
                    "Top": 0.2862499952316284, 
                    "Left": 0.1564060002565384, 
                    "Height": 0.3987500071525574
                }, 
                "FaceId": "2eb5f3fd-e2a9-4b1c-a89f-afa0a518fe06", 
                "ExternalImageId": "image5.jpg", 
                "Confidence": 99.99970245361328, 
                "ImageId": "3c314792-197d-528d-bbb6-798ed012c150"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.5773710012435913, 
                    "Top": 0.34437501430511475, 
                    "Left": 0.12396000325679779, 
                    "Height": 0.4337500035762787
                }, 
                "FaceId": "57189455-42b0-4839-a86c-abda48b13174", 
                "ExternalImageId": "image6.jpg", 
                "Confidence": 100.0, 
                "ImageId": "0aff2f37-e7a2-5dbc-a3a3-4ef6ec18eaa0"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.5349419713020325, 
                    "Top": 0.29124999046325684, 
                    "Left": 0.16389399766921997, 
                    "Height": 0.40187498927116394
                }, 
                "FaceId": "745f7509-b1fa-44e0-8b95-367b1359638a", 
                "ExternalImageId": "image7.jpg", 
                "Confidence": 99.99979400634766, 
                "ImageId": "67a34327-48d1-5179-b042-01e52ccfeada"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.41499999165534973, 
                    "Top": 0.09187500178813934, 
                    "Left": 0.28083300590515137, 
                    "Height": 0.3112500011920929
                }, 
                "FaceId": "8d3cfc70-4ba8-4b36-9644-90fba29c2dac", 
                "ExternalImageId": "image8.jpg", 
                "Confidence": 99.99769592285156, 
                "ImageId": "a294da46-2cb1-5cc4-9045-61d7ca567662"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.48166701197624207, 
                    "Top": 0.20999999344348907, 
                    "Left": 0.21250000596046448, 
                    "Height": 0.36125001311302185
                }, 
                "FaceId": "bd4ceb4d-9acc-4ab7-8ef8-1c2d2ba0a66a", 
                "ExternalImageId": "image9.jpg", 
                "Confidence": 99.99949645996094, 
                "ImageId": "5e1a7588-e5a0-5ee3-bd00-c642518dfe3a"
            }, 
            {
                "BoundingBox": {
                    "Width": 0.18562500178813934, 
                    "Top": 0.1618019938468933, 
                    "Left": 0.5575000047683716, 
                    "Height": 0.24770599603652954
                }, 
                "FaceId": "ce7ed422-2132-4a11-ab14-06c5c410f29f", 
                "ExternalImageId": "image10.jpg", 
                "Confidence": 99.99340057373047, 
                "ImageId": "8d67061e-90d2-598f-9fbd-29c8497039c0"
            }
        ]
    }

For more information, see `Listing Faces in a Collection <https://docs.aws.amazon.com/rekognition/latest/dg/list-faces-in-collection-procedure.html>`__ in the *Amazon Rekognition Developer Guide*.
