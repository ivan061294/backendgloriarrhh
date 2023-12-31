**To disassociate a link**

The following ``disassociate-link`` example disassociates the specified link from device ``device-07f6fd08867abc123`` in the specified global network. ::

    aws networkmanager disassociate-link \
        --global-network-id global-network-01231231231231231 \
        --device-id device-07f6fd08867abc123 \
        --link-id link-11112222aaaabbbb1 \
        --region us-west-2

Output::

    {
        "LinkAssociation": {
            "GlobalNetworkId": "global-network-01231231231231231",
            "DeviceId": "device-07f6fd08867abc123",
            "LinkId": "link-11112222aaaabbbb1",
            "LinkAssociationState": "DELETING"
        }
    }

For more information, see `Device and Link Associations <https://docs.aws.amazon.com/vpc/latest/tgw/on-premises-networks.html#device-link-association>`__ in the *Transit Gateway Network Manager Guide*.
