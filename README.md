# LibreLink
Connect devices and more from multiple Hubitat Elevation hubs (syncs device attributes/events, sends device commands; can synchronize modes).

## Installation Instructions
To install via Hubitat Package Manager, add a custom package (Install > From a URL). The custom package repository URL is: https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/hpm-libreLinkPackageManifest.json

Alternatively, you may you may manually install as below:
1. Install the LibreLink parent and child apps on all Hubitat hubs:
   * https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/app/librelink-parent-app.groovy
   * https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/librelink-child-linked-hub-app.groovy
2. Enable OAuth in the "Libre Link (Linked Hub Child)" app: while this app is opened in the Apps Code editor, clck the "OAuth" button in the upper right, press the "Enable OAuth in App" button, press "Update", button, press "Close" to exit the popup, then hit "Save" in the code editor again and proceeed.
3. Install the "LibreLink Hub" driver on all Hubitat hubs:
   * https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-hub.groovy
4. Install any necessary (or all) LibreLink drivers on the Hubitat hub(s) that will have linked devices of that type added to them from the other hub. (These drivers should be added to the hub with the "linked" device, not the "real" device.)
    * See the https://github.com/RMoRobert/Hubitat-LibreLink/blob/main/drivers folder

## Configuration Instructions
Go to Apps > Add User App in the Hubitat admin UI, and choose LibreLink. (Do this on both hubs.) Enter the IP address and other information requested. One hub will need to be designated as the "key authority" and the other as the "key recipient." Which hub you choose for which does not matter--you simply need one hub of each type. Each app can link only two hubs (one key authority and one key recipient); to link multiple hubs, add a new (child) app to those two hubs as well, and so on for more than two.

Use the "Test Connnection" button on both hubs *after* configuration to test the connections. If one or both fails, check the configiuration on both hubs.

To add devices from one hub to another, open the LibreLink child app on the hub with the "real" devices, go to "Link Devices," and choose the devices you want to share to the other hub. When done, press "Next," **then press "Done" on the main app page** to save your changes and create devices on the other hub.

Additional documentation coming soon.

## Support
For assistance, feel free to ask in the Hubitat Community forums.

All LibreLink apps and drivers are licensed under the Apache 2.0 license as stated. LibreLink is provided as-is without warranty and without formal or guaranteed support of any kind.
