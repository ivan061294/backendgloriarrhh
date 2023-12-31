**To update an IP set**

The following ``update-ip-set`` command updates an IPSet with an IPv4 address and deletes an IPv6 address::

 aws waf update-ip-set --ip-set-id a123fae4-b567-8e90-1234-5ab67ac8ca90 --change-token 12cs345-67cd-890b-1cd2-c3a4567d89f1 --updates Action="INSERT",IPSetDescriptor={Type="IPV4",Value="12.34.56.78/16"},Action="DELETE",IPSetDescriptor={Type="IPV6",Value="1111:0000:0000:0000:0000:0000:0000:0111/128"} 

Alternatively you can use a JSON file to specify the input. For example::

 aws waf update-ip-set --ip-set-id a123fae4-b567-8e90-1234-5ab67ac8ca90 --change-token 12cs345-67cd-890b-1cd2-c3a4567d89f1  --updates file://change.json 

Where content of the JSON file is::

 [
 { 
 "Action": "INSERT", 
 "IPSetDescriptor":
 { 
 "Type": "IPV4", 
 "Value": "12.34.56.78/16" 
 } 
 }, 
 { 
 "Action": "DELETE", 
 "IPSetDescriptor": 
 { 
 "Type": "IPV6", 
 "Value": "1111:0000:0000:0000:0000:0000:0000:0111/128" 
 } 
 }
 ]
 
For more information, see `Working with IP Match Conditions`_ in the *AWS WAF* developer guide.

.. _`Working with IP Match Conditions`: https://docs.aws.amazon.com/waf/latest/developerguide/web-acl-ip-conditions.html

