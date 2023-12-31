**To delete a permissions policy from a repository**

The following ``delete-repository-permissions-policy`` example deletes the permission policy from a repository named test-repo. ::

    aws codeartifact delete-repository-permissions-policy \
        --domain test-domain \
        --repository test-repo

Output::

    {
        "Version": "2012-10-17",
        "Statement": [
            {
                "Effect": "Allow",
                "Principal": {
                    "AWS": "arn:aws:iam::111122223333:root"
                },
                "Action": [
                    "codeartifact:DescribePackageVersion",
                    "codeartifact:DescribeRepository",
                    "codeartifact:GetPackageVersionReadme",
                    "codeartifact:GetRepositoryEndpoint",
                    "codeartifact:ListPackages",
                    "codeartifact:ListPackageVersions",
                    "codeartifact:ListPackageVersionAssets",
                    "codeartifact:ListPackageVersionDependencies",
                    "codeartifact:ReadFromRepository"
                ],
                "Resource": "*"
            }
        ]
    }

For more information, see `Delete a policy <https://docs.aws.amazon.com/codeartifact/latest/ug/repo-policies.html#deleting-a-policy>`__ in the *AWS CodeArtifact User Guide*.