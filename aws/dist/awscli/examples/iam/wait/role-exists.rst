**To pause running until the specified role exists**

The following ``wait role-exists`` command pauses and continues only after it can confirm that the specified role exists. There is no output. ::

  aws iam wait role-exists --role-name MyRole
