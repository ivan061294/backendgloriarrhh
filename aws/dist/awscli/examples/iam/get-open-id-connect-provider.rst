**To return information about the specified OpenID Connect provider**

This example returns details about the OpenID Connect provider whose ARN is ``arn:aws:iam::123456789012:oidc-provider/server.example.com``::

  aws iam get-open-id-connect-provider --open-id-connect-provider-arn arn:aws:iam::123456789012:oidc-provider/server.example.com

Output::

  {
      "Url": "server.example.com"
          "CreateDate": "2015-06-16T19:41:48Z",
          "ThumbprintList": [
			"12345abcdefghijk67890lmnopqrst987example"
		  ],
          "ClientIDList": [
			"example-application-ID"
		  ]
  }

For more information, see `Using OpenID Connect Identity Providers`_ in the *Using IAM* guide.

.. _`Using OpenID Connect Identity Providers`: http://docs.aws.amazon.com/IAM/latest/UserGuide/identity-providers-oidc.html