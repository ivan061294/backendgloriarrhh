**To retrieve details about a bot**

The following ``get-bot`` example displays the details for the specified bot. ::

    aws chime get-bot \
        --account-id 12a3456b-7c89-012d-3456-78901e23fg45 \
        --bot-id 123abcd4-5ef6-789g-0h12-34j56789012k

Output::

    {
        "Bot": {
            "BotId": "123abcd4-5ef6-789g-0h12-34j56789012k",
            "UserId": "123abcd4-5ef6-789g-0h12-34j56789012k",
            "DisplayName": "myBot (Bot)",
            "BotType": "ChatBot",
            "Disabled": false,
            "CreatedTimestamp": "2019-09-09T18:05:56.749Z",
            "UpdatedTimestamp": "2019-09-09T18:05:56.749Z",
            "BotEmail": "myBot-chimebot@example.com",
            "SecurityToken": "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
        }
    }

For more information, see `Update Chat Bots <https://docs.aws.amazon.com/chime/latest/dg/update-bots.html>`__ in the *Amazon Chime Developer Guide*.
