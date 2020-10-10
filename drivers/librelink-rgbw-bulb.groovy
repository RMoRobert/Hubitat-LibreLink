/**
 * =======================================================================================
 *  Copyright 2020 Robert Morris
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * =======================================================================================
 *
 *  Last modified: 2020-10-10
 *
 */ 
 
metadata {
   definition (name: "LibreLink RGBW Bulb", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-rgbw-bulb.groovy") {
      capability "Actuator"
      capability "Refresh"
      capability "Switch"
      capability "SwitchLevel"
      capability "Light"
      capability "ColorControl"
      capability "ColorTemperature"
      capability "ChangeLevel"
      capability "ColorMode"
      //capability "LightEffects"  // Can uncomment if needed, but probably need to find a way to re-JSON-ify "lightEffects" attributes to be technically correct. Commands shoulds should still work.
      
      command "syncAttributes"
   }
      
preferences {
      input(name: "transitionTime", type: "enum", title: "Transition time", options:
         [[0:"ASAP"],[400:"400ms"],[500:"500ms"],[1000:"1s"],[1500:"1.5s"],[2000:"2s"],[5000:"5s"]], defaultValue: 400)
      input name: "enableDebug", type: "bool", title: "Enable debug logging", defaultValue: true
      input name: "enableDesc", type: "bool", title: "Enable descriptionText logging", defaultValue: true
   }
}

/* ======== General device and convenience methods ======== */

def installed(){
   log.debug "Installed..."
   initialize()
}

def updated() {
   if (enableDebug) log.debug "Updated..."
   initialize()
}

def initialize() {
   if (enableDebug) log.debug "Initializing"
   int disableMinutes = 30
   if (enableDebug) {
      log.debug "Debug logging will be automatically disabled in ${disableMinutes} minutes"
      runIn(disableMinutes*60, debugOff)
   }
}

void debugOn(Boolean autoDisable=true) {
   log.warn "Enabling debug logging..."
   int disableMinutes = 30
   if (autoDisable) {
      log.debug "Debug logging will be automatically disabled in ${disableMinutes} minutes"
      runIn(disableMinutes*60, debugOff)
   }
   device.updateSetting("enableDebug", [value:"true", type:"bool"])
}

void debugOff() {
   log.warn "Disabling debug logging"
   device.updateSetting("enableDebug", [value:"false", type:"bool"])
}

void doSendEvent(Map eventData, Boolean forceStateChange=false) {
   if (enableDebug) log.debug("doSendEvent(${eventData}...")
   def descriptionText = "${device.displayName} ${eventData.name} is ${eventData.value}${eventData.unit ?: ''}"
   if (enableDesc && (device.currentValue(eventData.name) != eventData.value || eventData.isStateChange)) log.info(descriptionText)
   Map eventProperties = [name: eventData.name, value: eventData.value, descriptionText: descriptionText,
      unit: eventData.unit, phyiscal: eventData.physical, digital: eventData.digital,
      isStateChange: eventData.isStateChange]
   if (forceStateChange) eventProperties["isStateChange"] = true
   sendEvent(eventProperties)
}

// Probably won't happen but...
def parse(String description) {
   log.warn "parse() not implemented: '${description}'"
}

/* ======== LibreLink-specific methods ======== */

void syncAttributes() {
   if (enableDebug) log.debug "syncAttributes()"
   parent.syncAttributesToChildDevice(device.deviceNetworkId)
}

/* ======== Device capability methods ======== */

def refresh() {
   if (enableDebug) log.debug "refresh()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "refresh")
}

def on() {
   if (enableDebug) log.debug "on()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "on")
}

def off() {
   if (enableDebug) log.debug "off()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "off")
}

def setLevel(level, duration=null) {
   if (enableDebug) log.debug "setLevel($level, $transitionTime)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId,
      "setLevel", [level, (duration != null ? duration.toBigDecimal() : 1)])
}

def setColor(color) {
   if (enableDebug) log.debug "setColor($color)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setColor", [color])
}

def setHue(hue) {
   if (enableDebug) log.debug "setHue($hue)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setHue", [hue])
}

def setSaturation(sat) {
   if (enableDebug) log.debug "setSaturation($sat)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setSaturation", [sat])
}

def setColorTemperature(ct) {
   if (enableDebug) log.debug "setColorTemperature($ct)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setColorTemperature", [ct])
}

def startLevelChange(direction) {
   if (enableDebug) log.debug "startLevelChange($direction)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "startLevelChange", [direction])
}

def stopLevelChange() {
   if (enableDebug) log.debug "stopLevelChange()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "stopLevelChange")
}

def setEffect(effectNum) {
   if (enableDebug) log.debug "setEffect($effectNum)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setEffect", [effectNum])
}

def setNextEffect() {
   if (enableDebug) log.debug "setNextEffect()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setNextEffect")
}

def setPreviousEffect() {
   if (enableDebug) log.debug "setPreviousEffect()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setPreviousEffect")
}