**To create an access key for an IAM user**

The following ``create-access-key`` command creates an access key (access key ID and secret access key) for the IAM user named ``Bob``::

  aws iam create-access-key --user-name Bob

Output::

  {
      "AccessKey": {
          "UserName": "Bob",
          "Status": "Active",
          "CreateDate": "2015-03-09T18:39:23.411Z",
          "SecretAccessKey": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYzEXAMPLEKEY",
          "AccessKeyId": "AKIAIOSFODNN7EXAMPLE"
      }
  }

Store the secret access key in a secure location. If it is lost, it cannot be recovered, and you must create a new access key.

For more information, see `Managing Access Keys for IAM Users`_ in the *Using IAM* guide.

.. _`Managing Access Keys for IAM Users`: http://docs.aws.amazon.com/IAM/latest/UserGuide/ManagingCredentials.html