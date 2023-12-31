**To get the details about a chat room**

The following ``get-room`` example displays details about the specified chat room. ::

    aws chime get-room \
        --account-id 12a3456b-7c89-012d-3456-78901e23fg45 \
        --room-id abcd1e2d-3e45-6789-01f2-3g45h67i890j

Output::

    {
        "Room": {
            "RoomId": "abcd1e2d-3e45-6789-01f2-3g45h67i890j",
            "Name": "chatRoom",
            "AccountId": "12a3456b-7c89-012d-3456-78901e23fg45",
            "CreatedBy": "arn:aws:iam::111122223333:user/alejandro",
            "CreatedTimestamp": "2019-12-02T22:29:31.549Z",
            "UpdatedTimestamp": "2019-12-02T22:29:31.549Z"
        }
    }

For more information, see `Creating a Chat Room <https://docs.aws.amazon.com/chime/latest/ug/chime-chat-room.html>`__ in the *Amazon Chime User Guide*.
