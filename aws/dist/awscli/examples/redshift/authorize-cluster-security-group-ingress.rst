Authorizing Access to an EC2 Security Group
-------------------------------------------

This example authorizes access to a named Amazon EC2 security group.

Command::

   aws redshift authorize-cluster-security-group-ingress --cluster-security-group-name mysecuritygroup --ec2-security-group-name myec2securitygroup --ec2-security-group-owner-id 123445677890

Authorizing Access to a CIDR range
----------------------------------

This example authorizes access to a CIDR range.

Command::

   aws redshift authorize-cluster-security-group-ingress --cluster-security-group-name mysecuritygroup --cidrip 192.168.100.100/32


