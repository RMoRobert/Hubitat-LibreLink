/**
 * ==========================  LibreLink (Linked Hub/Child App) ==========================
 *
 * DESCRIPTION:
 * Connect devices and more from multiple Hubiat Elevation or hubs. Install on same hub 
 * as primary parent app (use parent app to create child instances). See documentation
 * for more details.
 *
 * PLATFORM:
 * Hubitat 
 * 
 * TO INSTALL:
 * Documentation coming soon.
 *
 * Copyright 2021 Robert Morris
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 *
 * =======================================================================================
 *
 * Last modified: 2021-04-17
 * 
 * 1.0    - First public release
 * 1.0.1  - Fix to unsubscribe from device events if device un-selected
 * 1.0.2  - Added more information (status) to HTTP errors; tweaked version reporting for apps (may be used in future)
 * 1.0.3  - Added motion-only driver
 * 1.0.4  - Added Inovelli LZW30/31-SN drivers (should work with Hubitat driver and mostly Inovelli's but designed for RMoRobert "Advanced" driver); scene dimmer driver fixes
 * 1.0.5  - Added Inovelli LZW36 drivers (same note as LZW30/31-SN)
 * 1.0.6  - Added Dome Siren driver
 * 1.1    - New drivers (pushed/held/released buttion, motion/4-in-1, fan and shade with "level"), performance improvements
 * 2.0.0  - Added cloud connection option; new drivers (speech, music player)
 *
 */

import groovy.transform.Field

@Field static final String version = "2.0.0"

definition(
   name: "LibreLink (Linked Hub Child App)",
   namespace: "RMoRobert",
   author: "Robert Morris",
   //installOnOpen: true,
   description: "Integrate devices from multiple Hubitat hubs (child app--do not instantiate directly; use parent app)",
   category: "Utility",
   parent: "RMoRobert:LibreLink",
   documentationLink: "https://github.com/RMoRobert/Hubitat-LibreLink",      
   iconUrl: "",
   iconX2Url: "",
   iconX3Url: ""
)

String getVersion() {
   return version
}

@Field static String childNamespace = "RMoRobert"

@Field static Map linkableDevices =
[
   "Switches, Dimmers, Shades, and Bulbs": [
      "llSwitches": [capability: "capability.switch", displayName: "Switches", driver: "LibreLink Switch"],
      "llSwitchPowers": [capability: "capability.switch", displayName: "Switches with power metering", driver: "LibreLink Switch with Power"],
      "llDimmers": [capability: "capability.switchLevel", displayName: "Dimmers", driver: "LibreLink Dimmer"],
      "llSceneDimmer": [capability: "capability.switch", displayName: "Scene Dimmers (Switch/Level and Buttons)", driver: "LibreLink Scene Dimmer"],
      "llLZW30SN": [capability: "capability.switch", displayName: "Inovelli LZW30-SN Switches (with Advanced driver)", driver: "LibreLink Inovelli LZW30-SN Advanced (Switch)"],
      "llLZW31SN": [capability: "capability.switchLevel", displayName: "Inovelli LZW31-SN Dimmers (with Advanced driver)", driver: "LibreLink Inovelli LZW31-SN Advanced (Dimmer)"],
      "llRGBWBulbs": [capability: "capability.colorControl", displayName: "RGBW Bulbs", driver: "LibreLink RGBW Bulb"]
   ],
   "Fans, Shades, Blinds": [
      "llLZW36": [capability: "capability.switchLevel", displayName: "Inovelli LZW36 Fan/Lights (with Advanced driver)", driver: "LibreLink Inovelli LZW36 Advanced (Fan/Light)"],
      "llShades": [capability: "capability.windowShade", displayName: "Window shades", driver: "LibreLink Window Shade"],
      "llShadesWithLevel": [capability: "capability.windowShade", displayName: "Window shades (with Level)", driver: "LibreLink Window Shade (with Level)"],
      "llShadesWithLevel": [capability: "capability.windowBlind", displayName: "Window blinds (with Level)", driver: "LibreLink Window Blind (with Level)"],
      "llFans": [capability: "capability.fanControl", displayName: "Fans (speed only)", driver: "LibreLink Fan"],
      "llFanLevels": [capability: "capability.fanControl", displayName: "Fans (with level)", driver: "LibreLink Fan (with Level)"],
   ],
   "Sensors": [
      "llContacts": [capability: "capability.contactSensor", displayName: "Contact sensors", driver: "LibreLink Contact Sensor"],
      "llMotions": [capability: "capability.motionSensor", displayName: "Motion sensors (motion/temperature/battery)", driver: "LibreLink Motion Sensor"],
      "llMotionNoTemps": [capability: "capability.motionSensor", displayName: "Motion sensors (motion/battery)", driver: "LibreLink Motion (No Temp) Sensor"],
      "llMotionHumids": [capability: "capability.motionSensor", displayName: "Motion/humidity sensors (motion/temperature/humidity/battery)", driver: "LibreLink Motion/Humidity Sensor"],
      "llMotionLuxes": [capability: "capability.motionSensor", displayName: "Motion/lux sensors (motion/temperture/illuminance/battery)", driver: "LibreLink Motion/Lux Sensor"],
      "llMotion4in1s": [capability: "capability.motionSensor", displayName: "Motion/4-in-1 sensors (motion/temperture/humidity/illuminance/battery)", driver: "LibreLink Motion/4-in-1 Sensor"],
      "llSmokeCOs": [capability: "capability.smokeDetector", displayName: "Smoke/CO detectors", driver: "LibreLink Smoke/CO Detector"],
      "llTempHums": [capability: "capability.temperatureMeasurement", displayName: "Temperature/Humidity sensors", driver: "LibreLink Temperature/Humidity Sensor"],
      "llWaters": [capability: "capability.waterSensor", displayName: "Water sensors", driver: "LibreLink Water Sensor"]
   ],
   "Buttons, Mobile App, and Music/Speech Devices": [
      "llButtonsPHRDT": [capability: "capability.doubleTapableButton", displayName: "Buttons (push/hold/release/double-tap, temperature)", driver: "LibreLink Button (Push/Hold/Release/Double-Tap)"],
      "llButtonsPHR": [capability: "capability.pushableButton", displayName: "Buttons (push/hold/release)", driver: "LibreLink Button (Push/Hold/Release)"],
      "llMusicPlayer": [capability: "capability.musicPlayer", displayName: "Music Players (with speech)", driver: "LibreLink Music Player"],
      "llSpeechSynth": [capability: "capability.speechSynthesis", displayName: "Speech synthesis devices", driver: "LibreLink Speech Synthesizer"],
      "llPresences": [capability: "capability.presenceSensor", displayName: "Presence sensors", driver: "LibreLink Presence Sensor"],
      "llMobileAppDevs": [capability: "capability.notification", displayName: "Mobile app devices", driver: "LibreLink Mobile App Device"]
   ],
   "Garage Doors, Locks, Sirens, and Thermostats": [
      "llGarageDoors": [capability: "capability.garageDoorControl", displayName: "Garage doors", driver: "LibreLink Garage Door"],
      "llLocks": [capability: "capability.lock", displayName: "Locks", driver: "LibreLink Lock"],
      "llDomeSirens": [capability: "capability.alarm", displayName: "Dome Sirens", driver: "LibreLink Dome Siren"],
      "llThermostats": [capability: "capability.thermostat", displayName: "Thermostats", driver: "LibreLink Thermostat"]
   ]
]

preferences {
   page name: "pageFirstPage", content: "pageFirstPage"
   page name: "pageAddHub", content: "pageAddHub"
   page name: "pageManageHub", content: "pageManageHub"
   page name: "pageLinkDevices", content: "pageLinkDevices"   
   page name: "pageChooseDevices", content: "pageChooseDevices"
   page name: "pageTestConnection", content: "pageTestConnection"
}

void installed() {
   log.debug "installed()"
   initialize()
}

void uninstalled() {
   log.debug "uninstalled()"
   if (!(settings["preserveDevices"] == true)) {
      log.debug "Deleting child devices of this ${app.name} child app instance..."
      List DNIs = getChildDevices().collect { it.deviceNetworkId }
      log.debug "  Preparing to delete devices with DNIs: $DNIs"
      DNIs.each {
         deleteChildDevice(it)
      }
   }
}

void updated() {
   log.debug "update()"
   app.updateLabel("${app.name - ' Child'}" + (otherHubNickname ? " - ${otherHubNickname}" : ""))
   initialize()
   setModeSyncSettings()
   subscribeToLinkedDevices()
   requestLinkedDeviceCreation()
}

void initialize() {
   log.debug "initialize()"
   initializeAppEndpoint()
}

/* -------------------------------------------------------------------------
 *  App page definitions
 * ------------------------------------------------------------------------- */

def pageFirstPage() {
   if (app.getInstallationState() == "INCOMPLETE") {      
      dynamicPage(name: "pageFirstPage", uninstall: true, install: true) {
         section() {
            paragraph 'Please press "Done" to install, then re-open to begin linking hubs.'
         }
      }
   }
   else {
      if (state.accessToken) {
         if (settings["otherHubIP"] && state.otherHubAccessToken && state.otherHubUri) {
            return pageManageHub()
         }
         else {
            return pageAddHub()
         }
      }
      else {
         dynamicPage(name: "pageFirstPage", uninstall: true, install: true) {
            section() {
               paragraph 'OAuth is not enabled. Please enable for this app in "Apps Code" per the installation instructions, ' +
                     'then re-open and try again.'
            }
         }
      }
   }
}

def pageAddHub() {
   dynamicPage(name: "pageAddHub", uninstall: true, install: false, nextPage: pageManageHub) {
      section(styleSection("Link with other hub")) {
         input name: "hubType", type: "enum", title: "Other hub connection type:", required: true, submitOnChange: true,
            options: [["hubitat-local": "Hubitat (LAN/local)"], ["hubitat-cloud": "Hubitat (cloud)"]], defaultValue: "hubitat-local"
         input name: "otherHubIP", type: "text", title: "Other hub IP address:", required: (hubType != "hubitat-cloud"), submitOnChange: true
         if (settings["hubType"] == "hubitat-cloud") {
            paragraph: "NOTE: IP address is optional for Hubitat cloud connection. (You may provide local/LAN IP address of remote hub if you wish. It is not currently used at all.)"
         }
         input name: "otherHubNickname", type: "text", title: "\"Nickname\" for other hub (optional; will be used as part of child app name):"
         input name: "enumRole", type: "enum", title: "LibreLink hub key exchange role:", required: true, submitOnChange: true,
            options: [["keyAuthority": "Linking key authority"], ["keyRecipient": "Linking key recipient"]]
         if (enumRole == "keyAuthority") {
            if (settings["otherHubIP"] || settings["hubType"] == "hubitat-cloud") {
               // Generate map of settings to be "received" by other hub. "hubType" is reserved for future expansion possibilities.
               Map keyMap = [:]
               if (settings["hubType"] == "hubitat-cloud") {
                  keyMap = [token: state.accessToken, uri: getFullApiServerUrl(), hubType: "hubitat-cloud"]
               }
               else {
                  keyMap = [token: state.accessToken, uri: getFullLocalApiServerUrl(), hubType: "hubitat-local"]
               }
               String encodedParams = URLEncoder.encode((new groovy.json.JsonBuilder(keyMap)).toString().bytes.encodeBase64().toString(), "utf-8")
               paragraph '<b>Copy/paste the key below to the other hub.</b> Choose the "Linking key recipient" role for the other hub.'
               paragraph "<textarea id=\"linkKeyForOtherHub\" rows=\"5\" cols=\"50\" onclick=\"this.select()\">$encodedParams</textarea>"
            }
            href name: "hrefTestConnection", title: "Test connection",
               description: "Test communication between hubs after you have configured both hubs (recommendation: test both hubs)", page: "pageTestConnection"
            if ((settings["otherHubIP"] || settings["hubType"] == "hubitat-cloud") && state.otherHubAccessToken && state.otherHubUri) {
               paragraph "Connection information from other hub received."
            }
            else {
               paragraph "No connection to other hub. Ensure \"key recipient\" hub has key, and try testing the connection above."
            }
         }
         else if (enumRole == "keyRecipient") {
            input name: "linkingKey", type: "string", title: "Linking key from key authority hub:",
               description: "Copy/paste linking key from other hub here", required: true, submitOnChange: true
            if ((settings["otherHubIP"] || settings["hubType"] == "hubitat-cloud") && settings["linkingKey"]) {
               Map linkingMap = [:]
               try {
                  linkingMap = parseJson(new String(URLDecoder.decode(linkingKey, "utf-8").decodeBase64()))
                  log.trace "linkingMap = $linkingMap"
                  state.otherHubAccessToken = linkingMap.token
                  state.otherHubUri = linkingMap.uri
                  // May use this key in the future, but for now just check and continue anyway:
                  if (enableDebug) log.debug "Linking map = $linkingMap"
                  if (!(linkingMap.hubType == "hubitat-local" || linkingMap.hubType == "hubitat-cloud")) log.warn "Hub connection type not specified as local or cloud Hubitat. Ensure settings on other hub are correct."
                  if (state.otherHubAccessToken && (state.otherHubUri || linkingMap.hubType == "hubitat-cloud")) {
                     paragraph "<b>Press the button below</b> to establish a link between the two hubs, then use \"Test Connection\" on both hubs to ensure it is working:"
                     if (linkingMap.hubType == "hubitat-local" && !(state.otherHubUri?.contains(otherHubIP))) {
                        // Not the most foolproof way to test, but should catch most cases:
                        paragraph "Warning: the linking key IP address does not match the address entered for the other hub. Please verify both are correct and continue only if certain."
                     }
                     // else: no check currently if remote hub IP matches for cloud connections, but shouldn't matter if all is correct
                     input name: "btnLink", type: "button", title: "Link!"
                  }
                  else {
                     paragraph "Invalid linking key. Please verify correct copy/paste from authority hub, and try again."
                     state.remove("otherHubAccessToken")
                     state.remove("otherHubUri")
                  }
               }
               catch (ex) {
                  paragraph "Could not decode linking key. Please ensure it was copied correctly from the main hub."
                  log.error ex
                  state.remove("otherHubAccessToken")
                  state.remove("otherHubUri")
               }
            }
            href name: "hrefTestConnection", title: "Test connection",
               description: "Test communication between hubs after you have configured both hubs", page: "pageTestConnection"
         }
         else {
            paragraph '<strong>Please choose a key exchange role for this hub above.</strong>  When connecting two hubs, one must be chosen '  +
            'as they "key authority" and the other as the "key recipient." <em>Which hub you choose for which is arbitrary; you just need one of each</em>. ' +
            '(Note: to connect more than one remote hub, an additional authority/receiver app pair will need to be created. Each app ' +
            'can connect to only one other hub.)'
         }
      }
      section(styleSection("Link maintenance")) {
         paragraph "Clicking the button below will require providing a new key to the other hub to reconnect. Use with caution."
         input name: "btnClearLink", type: "button", title: "Disconnect", submitOnChange: true
      }
      section(styleSection("Logging")) {
         input name: "enableDebug", type: "bool", title: "Enable debug logging (for app)", submitOnChange: true
      }
   }
}

def pageManageHub() {
   // Create "other hub" device if does not exist
   com.hubitat.app.DeviceWrapper hub = getChildDevices()?.find({it.getDeviceNetworkId() == "LL/${app.id}/LinkedHub"})
   if (hub == null) {
      log.debug "Other hub device does not exist; creating"
      def devDriver = "LibreLink Linked Hub"
      def devDNI = "LL/${app.id}/LinkedHub"
      def devProps = [name: "LibreLink Linked Hub " + (otherHubNickname ? "(${otherHubNickname})" : "(${app.id})")]
      addChildDevice(childNamespace, devDriver, devDNI, null, devProps)
   }
   // Page:
   dynamicPage(name: "pageManageHub", uninstall: true, install: true) {
      if (!((settings["otherHubIP"] || settings["hubType"] == "hubitat-cloud") && state.otherHubAccessToken && state.otherHubUri)) {
         section(styleSection("Oops!")) {
            paragraph "It seems your hubs are not yet linked. Please re-open the app to re-attempt setup."
         }
      }
      else {
         section(styleSection("Manage linked devices")) {
            href name: "hrefLinkDevices", title: "Link devices", description: "Choose devices to link from this hub to other hub",
               page: "pageLinkDevices"
            input name: "modeSyncMethod", type: "enum", title: "Synchronize modes between hubs?",
               options: [["no": "No (default)"], ["this": "Push mode changes from this hub to other hub"],
                         ["other": "Push mode changes from other hub to this hub"]], defaultValue: "no", required: true
         }
      }
      section(styleSection("Link maintenace")) {
         href name: "hrefTestConnection", title: "Test connection",
            description: "Test communication between hubs after you have configured both hubs", page: "pageTestConnection"
         href name: "hrefReAddHub", title: "Re-configure hub connection", description: "Change other hub IP address association or generate new key", page: "pageAddHub"
      }
      section(styleSection("Logging")) {
         input name: "enableDebug", type: "bool", title: "Enable debug logging (for app)", submitOnChange: true
      }
   }
}

def pageLinkDevices() {
   dynamicPage(name: "pageLinkDevices", uninstall: false, install: false, nextPage: "pageFirstPage") {
      /*section(styleSection("Choose devices to link")) {
         paragraph "Create devices on linked hub for these devices on this hub:"
         href name: "hrefDevices.XYZ", title: "Select XYZ devices",
            description: "Select devices of type XYZ", page: "pageChooseDevices"
      }*/
      section(styleSection("Choose devices to link")) {
         paragraph "Create linked devices on other hub for these devices on this hub:"
         linkableDevices.each { category ->
            paragraph ""
            paragraph(styleSection("${category.key}"))
            category.value.each { inputName, properties ->
               input name: inputName, type: properties.capability, title: properties.displayName, multiple: true
            }
         }
      }
      section(styleSection("Other options")) {
         input name: "preserveDevices", type: "bool", title: "Attempt to preserve devices (on this hub) from linked hub if app uninstalled",
            defaultValue: false
         // TODO: Option for pruning un-selected devices on remote up with update, too?
      }
   }
}

// Probably won't need anymore since are doing the above instead...
def pageChooseDevices() {
   dynamicPage(name: "pageChooseDevices", uninstall: false, install: false, nextPage: "pageLinkDevices") {
      section(styleSection("Choose devices of certain capabilities")) {
         paragraph "Coming soon!"
      }
   }
}

def pageTestConnection() {
   dynamicPage(name: "pageTestConnection", uninstall: false, install: false, nextPage: "pageFirstPage") {
      section(styleSection("Test connection")) {
         if (testOtherHubConnection()) {
            paragraph "<b>Connection succesful!</b> Be sure to test connection from both hubs."
         }
         else {
            paragraph "<b>Connection failed.</b> Ensure the key was copied correctly, that both hubs are online, and try again. " +
               "To re-test connection, you can re-load this page."
         }
      }
   }
}


/* -------------------------------------------------------------------------
 *  General methods specific to app functionality
 * ------------------------------------------------------------------------- */

/**
 * Intended to be called the first time app is run or if user requests re-set of tokens
 */
private void initializeAppEndpoint(Boolean forceNewAccessToken=false) {
   if (enableDebug) log.debug "initializeAppEndpoint()"
   if (!state.accessToken || forceNewAccessToken) {
      try {
         log.debug "Creating access token..."
         createAccessToken()
      } 
      catch(Exception ex) {
         log.warn "Failed to generate access token: $ex"
         state.accessToken = null
      }
   }
}

/**
 * Gets URI for specified path on remote hub.
 * @param path Absolute remote path; example = "/libreLinkHubPing"
 * @return String suitable for use in Hubitat HTTP method calls as uri
 */
private String getRemoteUri(String path) {
   return "${state.otherHubUri}${path}?access_token=${state.otherHubAccessToken}"
}

/**
 * Reads mode sync setting on this hub and sets other hub to complement. Subscribes/unsubsribes from
 * location mode events when needed.
 */
void setModeSyncSettings() {
   if (enableDebug) log.debug "Setting mode sync settings (to: '${modeSyncMethod}')"
   Map<String,String> otherHubSyncMethod = [:]
   switch (settings["modeSyncMethod"]) {
      case "this":
         if (enableDebug) log.debug "Configured to push mode to other hub. Modifying settings on both hubs to accomodate..."
         otherHubSyncMethod = [modeSyncMethod: "other"]
         break
      case "other":
         if (enableDebug) log.debug "Configured to receive mode from other hub.  Modifying settings on both hubs to accomodate..."
         otherHubSyncMethod = [modeSyncMethod: "this"]
         break
      default:
         if (enableDebug) log.debug "Not configured to sync mode"
         otherHubSyncMethod = [modeSyncMethod: "no"]
         break
   }
   updateModeSubscriptions()
   if (enableDebug) log.debug "Sending new mode sync method to other hub..."
   asynchttpPut(handleGenericHTTPResponse, [uri: getRemoteUri("/modeSyncMethod"),
      contentType: "application/json", timeout: 15, body: otherHubSyncMethod], [fromMethod: 'setModeSyncSettings']) 
}

/**
 * Reads list of user-selected devices on this hub to link to other hub. Sends request to other hub
 * to create them.
 */
void requestLinkedDeviceCreation() {
   Map<Map> devsToLink = [:]

   linkableDevices.each { category ->
      category.value.each { inputName, properties ->         
         if (settings[inputName]) {
            Map devsOfType = [:]
            settings[inputName].each {
               devsOfType[it.id] = it.displayName
            }
            //log.trace "devsOfType = $devsOfType"
            devsToLink << [(inputName): devsOfType]
         }
      }
   }
   if (enableDebug) log.debug "Creating devices for: $devsToLink"
   asynchttpPut(handleGenericHTTPResponse, [uri: getRemoteUri("/device/createFromList"),
      contentType: "application/json", timeout: 15, body: [devicesToLink: devsToLink]], [fromMethod: 'requestLinkedDevicesCreation']) 
}

/**
 * Reads list of user-selected devices on this hub and subscribes to all device attributes
 * so can send to linked hub when needed
 */
void subscribeToLinkedDevices() {
   unsubscribe("handleDeviceEvent")
   linkableDevices.each { category -> 
      category.value.each { inputName, properties ->
         if (settings[inputName]) {
            settings[inputName].each { dev ->
               dev.getSupportedAttributes().each { attr ->
                  subscribe(dev, attr.name, handleDeviceEvent)
                  // In the future, could make list of all attributes and filter out before subscription. For now I will just:
                  // Filter out undesired attributes for specific devices (right now just switches not selected as "switch with power"):
                  if (inputName == "llSwitches") {
                     unsubscribe(dev, "power", handleDeviceEvent)
                     unsubscribe(dev, "energy", handleDeviceEvent)
                     unsubscribe(dev, "voltage", handleDeviceEvent)
                  }
               }
            }
         }
      }
   }
}

void updateModeSubscriptions(String modeSyncMethod = settings["modeSyncMethod"]) {
   if (enableDebug) log.debug "updateModeSubscriptions()"
   // Update subscriptions, etc. now so user doesn't have to click "Done":
   switch (modeSyncMethod) {
      case "this":
         if (enableDebug) log.debug "modeSyncMethod = this; subscribing to mode changes on this hub"
         subscribe(location, "mode", handleModeChange)
         break
      case "other":
         if (enableDebug) log.debug "modeSyncMethod = other; unsubscribing from mode changes on this hub"
         unsubscribe(location, "mode")
         break
      default:
         if (enableDebug) log.debug "modeSyncMethod = no; unsubscribing from mode changes on this hub"
         unsubscribe(location, "mode")
         break
   }
}

/**
 * Can be called from child device/driver to populate attributes with values from "real" device.
 * Available on-demand from syncAttributes() command on child device. Should also be called when
 * device created.
 * @param childDNI Device Network ID of child device (used to extract other hub's device ID for this device)
 */
void syncAttributesToChildDevice(String childDNI) {
   if (enableDebug) log.debug "syncAttributesToChildDevice(childDNI = $childDNI)"
   String otherHubDevID = childDNI.split("/")[2]
   asynchttpGet(handleGenericHTTPResponse, [uri: getRemoteUri("/device/syncAttributesRequest/${otherHubDevID}"),
      contentType: "application/json", timeout: 15], [fromMethod: 'syncAttributesToChildDevice'])
}

/**
 * Is called from child device/driver when command is run. Routes to parent app (here), which
 * then passes along to "real" device on other hub.
 * @param childDNI Device Network ID of child device (used to extract other hub's device ID for this device)
 * @commandName The name of the command (e.g., "off" or "refresh")
 * @parameters List of any necessary parameters; not required for commands witout parameters
 */
void sendCommandFromChildDevice(String childDNI, String commandName, List parameters=[]) {
   String otherHubDevID = childDNI.split("/")[2]
   Map commandMap = [command: commandName, parameters: parameters]
   asynchttpPut(handleGenericHTTPResponse, [uri: getRemoteUri("/device/command/${otherHubDevID}"),
      contentType: "application/json", timeout: 15, body: commandMap], [fromMethod: 'sendCommandFromChildDevice'])
}

/* -------------------------------------------------------------------------
 *  "This hub" methods that call endpoints on other hub
 * ------------------------------------------------------------------------- */

 /**
  * Sends async ping request (GET) to to other hub. Could be used to periodically montior connection.
  */
void pingOtherHub() {
   if (enableDebug) log.debug "Sending ping to other hub..."
   Map params = [uri: getRemoteUri("/libreLinkHubPing"), contentType: "application/json", timeout: 10]
   if (enableDebug) log.debug "Sent ping to other hub with parameters: $params"
   asynchttpGet(handlePingOtherHubResponse, params)
}

/**
 * Intended to be called on "receiver" hub to give its key to "authority" hub.
 * Synchronus PUT (no callback method).
 */
void attemptConnection() {
   String uri = getRemoteUri("/otherHubKey")
   Map bodyMap = [:]
   if (settings["hubType"] == "hubitat-cloud") {
      bodyMap = [token: state.accessToken, uri: getFullApiServerUrl()]
   }
   else {
      bodyMap = [token: state.accessToken, uri: getFullLocalApiServerUrl()]
   }
   log.debug "Attempting connection to other hub..."
   httpPutJson([uri: uri, timeout: 10, body: bodyMap]) { resp ->
      if (enableDebug) { log.debug "Connection attempt response:"; resp.properties.each { log.trace it } }
   }
}

/* -------------------------------------------------------------------------
 *  Endpoint mappings and handlers for endpoints on this hub
 * ------------------------------------------------------------------------- */

mappings {
   path("/libreLinkHubPing") {action: [GET: "handlePingRequest"]}
   path("/otherHubKey") {action: [PUT: "handleReceiveOtherHubKey"]}
   path("/modeSyncMethod") {action: [PUT: "handleReceiveOtherHubModeSyncMethod"]}
   path("/mode") {action: [PUT: "handleReceiveOtherHubMode"]}
   path("/device/createFromList") {action: [PUT: "handleCreateLinkedDevices"]}
   path("/device/command/:deviceId") {action: [PUT: "handleReceiveOtherHubDeviceCommand"]}
   path("/device/event/:deviceId") {action: [PUT: "handleReceiveOtherHubDeviceEvent"]}
   path("/device/syncAttributesRequest/:deviceId") {action: [GET: "handleReceiveOtherHubDeviceSyncRequest"]}
   path("/device/syncAttributesSend/:deviceId") {action: [PUT: "handleReceiveOtherHubDeviceSync"]}
   
}

Map handleReceiveOtherHubKey() {
   log.debug "handleReceiveOtherHubKey()..."
   String token = request?.JSON?.token
   String uri = request?.JSON?.uri
   if (token && uri) {
      log.debug "Token and ID found in data from remote hub. Success!"
      state.otherHubAccessToken = token
      state.otherHubUri = uri
   }
   else {
      log.warn "Token and/or ID not found in data from remote hub. Please try again."
      state.remove("otherHubAccessToken")
      state.remove("otherHubUri")
   }
   return [otherHubToken: state.otherHubAccessToken]
}

/**
 * Handles ping request from other hub to this one
 * @return Map with key/value pair "pingTime" and current time; if receiving hubs sees this is present,
 *         can consider ping successful (ideally may want to check response time)
 */
Map handlePingRequest() {
   if (enableDebug) log.debug "Ping received from other hub"
   return ["pingTime": now()]
}

/**
 * Handler for async pingOtherHub() response
 */
void handlePingOtherHubResponse(response, data) {
   if (enableDebug) log.debug "Handling ping response from other hub..."
   com.hubitat.app.DeviceWrapper hub = getChildDevices()?.find({it.getDeviceNetworkId() == "LL/${app.id}/LinkedHub"})
   Boolean success = (response?.json ? response.json.pingTime : false)
   if (enableDebug) log.debug "Ping was ${success ? 'successful' : 'unsuccessful'}"
   if (hub) {
      hub.doSendEvent("presence", success ? "present" : "not present")
   }
   else {
      log.warn "Device for other (remote) hub not found on this hub"
   }
}

/**
 * Handler for HTTP calls to other hub that can be used if do not care to verify
 * response
 */
void handleGenericHTTPResponse(response, data) {
   //response?.properties.each { log.warn it }
   if (response?.headers && response.status < 300) {
      if (enableDebug) log.debug "handleGenericHTTPResponse: response = ${response?.data}; data = \n ${data}"
   }
   else {
      log.error "Error encountered by handleGenericHTTPResponse (data: $data). Error: ${response?.errorMessage}. HTTP ${response.status}"
   }
}

/**
 * Method of testing connection to other hub--similar to ping, but synchronous; waits for response,
 * and returns true if successful and false if not.
 */
Boolean testOtherHubConnection(Integer timeoutInSeconds=10) {
   if (enableDebug) log.debug "Sending test ping to other hub..."
   Boolean successful = false
   Map params = [uri: getRemoteUri("/libreLinkHubPing"), contentType: "application/json", timeout: timeoutInSeconds]
   try {
      httpGet(params) { response ->
         successful = (response?.data ? response.data.pingTime : false)
      }
   }
   catch (Exception ex) {
      log.error "Error when attempting to test connection: $ex"
   }
   if (enableDebug) log.debug "Ping test was ${successful ? '' : 'not '}successful"
   return successful
}

/**
 * Handles request from other hub to set this hub's mode sync method (user can change from any hub, and other hub
 * will change its setting to complement as needed)
 */
Map handleReceiveOtherHubModeSyncMethod() {
   String newModeSyncMethod = request?.JSON?.modeSyncMethod
   if (newModeSyncMethod != null && (newModeSyncMethod == "no" || newModeSyncMethod == "this" || newModeSyncMethod == "other")) {
      if (enableDebug) log.debug "newModeSyncMethod is \"${newModeSyncMethod}\"; configuring this hub appropriately"
      app.updateSetting("modeSyncMethod", [type: "enum", value: newModeSyncMethod])
   }
   else {
      log.warn "Invalid modeSyncMethod received from other hub (value = $newModeSyncMethod); not changing settings on this hub"
   }
   updateModeSubscriptions()
   return [modeSyncMethod: "${settings['modeSyncMethod']}"]
}

/**
 * Handles mode change sent by other hub (should only happen if other hub configured to send and this to receive)
 * @return Hub mode after change (should match if successful)
 */
Map handleReceiveOtherHubMode() {
   String newMode = request?.JSON?.mode
   if (newMode != null) {
      setLocationMode(newMode)
   }
   else {
      log.warn "Invalid mode received from other hub (mode = ${newMode}); not changing."
   }
   return [mode: location.currentMode.name]
}

/**
 * Handles request from other hub to create linked devices on this hub (device information in request body)
 * @return Map/JSON with success: true or success: false (false only for major failures; logs may indicate nuances)
 */
Map handleCreateLinkedDevices() {
   if (enableDebug) "Creating linked devices on this hub per request from other hub, if needed..."
   if (enableDebug) "Devices = request?.JSON?.devicesToLink"
   Boolean success = true
   if (!(request?.JSON?.devicesToLink)) success = false
   request?.JSON?.devicesToLink?.each { devType, devs ->
      String driverName = null
      linkableDevices.each { category, selectorMaps ->
         if (driverName != null) return
         driverName = selectorMaps.find { it.key == devType }?.value?.driver
      }
      if (driverName != null) {
         devs.each {
            if (enableDebug) log.debug "Creating device for ${it.value} (ID: ${it.key})..."
            String dni = "LL/${app.id}/${it.key}"
            if (getChildDevices().find { it.getDeviceNetworkId() == dni}) {
               if (enableDebug) log.debug "Skipping device creation for ${it.value} (${it.key}) because already found on this hub"
            }
            else {
               try {
                  com.hubitat.app.DeviceWrapper dev = addChildDevice(childNamespace, driverName, dni, null, [name: it.value])
                  pauseExecution(50) // dirty fix for: device sometimes isn't available yet for next step...maybe driver should do instead on install?
                  dev?.syncAttributes()
               }
               catch (com.hubitat.app.exception.UnknownDeviceTypeException ex) {
                  log.error "Error while creating hcild device for ${it.value}. Make sure the \"${driverName}\" driver is installed " +
                     "on this hub, and try again. (Error: ${ex})"
               }
               catch (Exception ex) {
                  log.error "Error while creating child device for ${it.value}: ${ex}"
               }
            }
         }
      }
      else {
         log.error "Could not create device because no driver specified (devices: $devType = $devs)"
         success = false
      }
   }
   return [success: success]
}

/**
 * Handles device command initiated/sent by other hub
 * @return Map/JSON with success: true or success: false
 */
Map handleReceiveOtherHubDeviceCommand() {
   if (enableDebug) log.debug "handleReceiveOtherHubDeviceCommand(deviceId = ${params.deviceId})"
   com.hubitat.app.DeviceWrapper dev = findDeviceById(params.deviceId)
   Boolean success = false
   if (dev != null) {
      if (enableDebug) "Found device on this hub for ID ${params.deviceId}: ${dev.displayName}"
      String cmd = request?.JSON?.command
      List parameters = request?.JSON?.parameters
      //log.trace "cmd = $cmd"
      //log.trace "parameters = $parameters"
      if (dev.hasCommand(cmd)) {
         if (parameters) dev."$cmd"(*parameters)
         else dev."$cmd"()
         success = true
      }
      else {
         log.warn "Device ${dev.displayName} does not support command \"${cmd}\" send by remote hub"
      }
   }
   else {
      log.warn "No device found on this hub for ID ${params.deviceId}"
   }
   return [success: success]
}

/**
 * Handles device command initiated/sent by other hub
 * @return Map/JSON with success: true or success: false
 */
Map handleReceiveOtherHubDeviceEvent() {
   if (enableDebug) log.debug "handleReceiveOtherHubDeviceEvent(deviceId = ${params.deviceId})"
   com.hubitat.app.DeviceWrapper dev = getChildDevices().find { it.getDeviceNetworkId().split("/")[2] == params.deviceId }
   Boolean success = false
   if (dev != null) {
      if (enableDebug) "Found device on this hub for ID ${params.deviceId}: ${dev.displayName}"
      Map eventAttributes = request?.JSON
      if (enableDebug) log.debug "eventAttributes = $eventAttributes"
      dev.doSendEvent(eventAttributes)
      success = true
   }
   else {
      log.warn "No device found on this hub for ID ${params.deviceId}"
   }
   return [success: success]
}

/**
 * Handles device sync request initiated/sent by other hub
 * @return Map/JSON with success: true or success: false
 */
Map handleReceiveOtherHubDeviceSyncRequest() {
   if (enableDebug) log.debug "handleReceiveOtherHubDeviceSyncRequest(deviceId = ${params.deviceId})"
   com.hubitat.app.DeviceWrapper dev = findDeviceById(params.deviceId)
   Boolean success = false
   if (dev != null) {
      if (enableDebug) "Found device on this hub for ID ${params.deviceId}: ${dev.displayName}"
      List allAttributes = []
      dev.getSupportedAttributes().each { attr ->
         def currVal = dev.currentValue(attr.name)
         if (currVal != null) {
            Map thisAttr = [:]
            thisAttr["name"] = attr.name
            thisAttr["value"] = currVal
            allAttributes.add(thisAttr)
            thisAttr = null
         }
      }
      asynchttpPut(handleGenericHTTPResponse, [uri: getRemoteUri("/device/syncAttributesSend/${params.deviceId}"),
         contentType: "application/json", timeout: 15, body: [allAttributes: allAttributes]], [fromMethod: 'handleReceiveOtherHubDeviceSyncRequest'])
      success = true
   }
   else {
      log.warn "No device found on this hub for ID ${params.deviceId}"
   }
   return [success: success]
}

/**
 * Handles receipt of device sync (attribute) information from other hub
 * @return Map/JSON with success: true or success: false
 */
Map handleReceiveOtherHubDeviceSync() {
   if (enableDebug) log.debug "handleReceiveOtherHubDeviceSync(deviceId = ${params.deviceId})"
   com.hubitat.app.DeviceWrapper dev = getChildDevices().find { it.getDeviceNetworkId().split("/")[2] == params.deviceId }
   Boolean success = false
   if (dev != null) {
      if (enableDebug) "Found device on this hub for ID ${params.deviceId}: ${dev.displayName}"
      List eventAttributes = request?.JSON?.allAttributes
      if (eventAttributes != null) {
         eventAttributes.each { attr ->
            dev.doSendEvent([name: attr.name, value: attr.value])
         }
         success = true
      }
      else {
         if (enableDebug) "No attributes found in response from remote hub"
      }
   }
   else {
      log.warn "No device found on this hub for ID ${params.deviceId}"
   }
   return [success: success]
}

/* -------------------------------------------------------------------------
 *  Regular app/device/location event handlers and other methods
 * ------------------------------------------------------------------------- */

/**
 * Should be handler for mode-change subscription iff this hub is configured to push mode
 * changes to other hub.
 */
void handleModeChange(evt) {
   if (enableDebug) log.debug "handleModeChange(); new mode = ${evt.value}"
   String newMode = evt.value
   if (settings["modeSyncMethod"] == "this") {
      if (enableDebug) log.debug "Hub configured to send mode change to other hub. Sending..."
      asynchttpPut(handleGenericHTTPResponse, [uri: getRemoteUri("/mode"),
         contentType: "application/json", timeout: 15, body: [mode: newMode]], [fromMethod: 'handleModeChange']) 
   }
   else if (settings["modeSyncMethod"] == other) {
      if (enableDebug) log.debug "Hub configured to send mode change to other hub. Ignoring mode chage (and this app should not be subscribed to this event)."
   }
   else {
      if (enableDebug) log.warn "Hub not configured to send/receive mode, but app is still subscribed to mode changes. Not doing anything."
   }
}

/**
 * Handles events/attribute changes on this hub so can send event to other hub's linked device
 */
void handleDeviceEvent(evt) {
   if (enableDebug) log.debug "handleDeviceEvent: $evt.device $evt.name is $evt.value"
   Map eventMap = [name: evt.name, value: evt.value, unit: evt.unit, descriptionText: evt.descriptionText,
      isStateChange: evt.isStateChange, displayName: evt.displayName]
   // TODO: remove when know working for other types
   //log.trace "evt.value = ${evt.value}"
   //log.trace "evt.numericValue = ${evt.numericValue}"
   if (evt.type) eventMap["type"] = evt.type
   //if (evt.digital) eventMap["type"] = "digital"
   asynchttpPut(handleGenericHTTPResponse, [uri: getRemoteUri("/device/event/${evt.deviceId}"),
      contentType: "application/json", timeout: 15, body: eventMap], [fromMethod: 'handleDeviceEvent'])
}

/**
 * Finds user-selected device on this hub by device ID (Hubitat-assigned ID as exctracted from linked hub device DNI, not "real" DNI on this hub)
 * @returns DeviceWrapper for device, or null if not found
 */
com.hubitat.app.DeviceWrapper findDeviceById(String id) {
   com.hubitat.app.DeviceWrapper dev
   linkableDevices.each { category ->
      if (dev != null) return
      category.value.each { inputName, properties ->
         if (dev != null) return
         dev = settings[inputName]?.find { it.id == id }
      }
   }
   return dev
}

String styleSection(String sectionTitle) {
   return """<span style="font-weight: bold; font-size: 110%">$sectionTitle</span>"""
}

void appButtonHandler(btn) {
   switch (btn) {
      case "btnLink":
         if (settings["enumRole"] == "keyRecipient") {
            attemptConnection()
         }
         else {
            log.debug "not key recipient hub; establish connection from recipient"
         }
         break
      case "btnClearLink":
         state.remove("otherHubAccessToken")
         state.remove("otherHubUri")
         state.remove("accessToken")
         initializeAppEndpoint(true)
      default:
         break
   }
}