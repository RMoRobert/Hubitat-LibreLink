# LibreLink
Connect devices and more from multiple Hubitat Elevation hubs (syncs device attributes/events, sends device commands; can synchronize modes).

## Installation Instructions
Installation will soon be available via the addition of a custom repository to Hubitat Package Manager. Alternatively, you may manually install as below:
1. Install the LibreLink parent and child apps on all Hubitat hubs:
   * https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/app/librelink-parent-app.groovy
   * https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/librelink-child-linked-hub-app.groovy
2. Install the "LibreLink Hub" driver on all Hubitat hubs:
   * https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-hub.groovy
3. Install any necessary (or all) LibreLink drivers on the Hubitat hub(s) that will have linked devices of that type added to them from the other hub. (These drivers should be added to the hub with the "linked" device, not the "real" device.)
    * See the https://github.com/RMoRobert/Hubitat-LibreLink/blob/main/drivers folder

## Configuration Instructions
Go to Apps > Add User App in the Hubitat admin UI, and choose LibreLink. (Do this on both hubs.) Enter the IP address and other information requested. One hub will need to be designated as the "key authority" and the other as the "key recipient." Which hub you choose for which does not matter--you simply need one hub of each type. Each app can link only two hubs (one key authority and one key recipient); to link multiple hubs, add a new (child) app to those two hubs as well, and so on for more than two.

Use the "Test Connnection" button on both hubs *after* configuration to test the connections. If one or both fails, check the configiuration on both hubs.

To add devices from one hub to another, open the LibreLink child app on the hub with the "real" devices, go to "Link Devices," and choose the devices you want to share to the other hub. When done, press "Next," **then press "Done" on the main app page** to save your changes and create devices on the other hub.

Additional documentation coming soon.

## Support
LibreLink is provided as-is without warranty or formal support of any kind. For assistance, feel free to ask in the Hubitat Community forums.
