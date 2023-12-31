**To list the IAM groups for the current account**

The following ``list-groups`` command lists the IAM groups in the current account::

  aws iam list-groups

Output::

  "Groups": [
    {
        "Path": "/",
        "CreateDate": "2013-06-04T20:27:27.972Z",
        "GroupId": "AIDACKCEVSQ6C2EXAMPLE",
        "Arn": "arn:aws:iam::123456789012:group/Admins",
        "GroupName": "Admins"
    },
    {
        "Path": "/",
        "CreateDate": "2013-04-16T20:30:42Z",
        "GroupId": "AIDGPMS9RO4H3FEXAMPLE",
        "Arn": "arn:aws:iam::123456789012:group/S3-Admins",
        "GroupName": "S3-Admins"
    }
  ]

For more information, see `Creating and Listing Groups`_ in the *Using IAM* guide.

.. _`Creating and Listing Groups`: http://docs.aws.amazon.com/IAM/latest/UserGuide/Using_CreatingAndListingGroups.html

