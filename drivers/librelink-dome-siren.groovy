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
 *  Last modified: 2020-11-19
 *
 */ 
 
metadata {
   definition (name: "LibreLink Dome Siren", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-dome-siren.groovy") {
      capability "Actuator"
      capability "Switch"
      capability "Refresh"
      capability "Tone"
      capability "Alarm"
      capability "Chime"
      
      // testSound1, etc. commands not included as can be done with standard playSound(soundNumber) command

      command "syncAttributes"
   }
      
preferences {
      input(name: "enableDebug", type: "bool", title: "Enable debug logging", defaultValue: true)
      input(name: "enableDesc", type: "bool", title: "Enable descriptionText logging", defaultValue: true)
   }
}

/* ======== General device and convenience methods ======== */

void installed(){
   log.debug "Installed..."
   initialize()
}

void updated() {
   if (enableDebug) log.debug "Updated..."
   initialize()
}

void initialize() {
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

void refresh() {
   if (enableDebug) log.debug "refresh()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "refresh")
}

void on() {
   if (enableDebug) log.debug "on()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "on")
}

void off() {
   if (enableDebug) log.debug "off()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "off")
}

void beep() {
   if (enableDebug) log.debug "beep()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "beep")
}

void both() {
   if (enableDebug) log.debug "both()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "both")
}

void playSound(soundNumber) {
   if (enableDebug) log.debug "playSound($btsoundNumberNum)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "playSound", [soundNumber])
}

void siren() {
   if (enableDebug) log.debug "siren()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "siren")
}

void stop() {
   if (enableDebug) log.debug "stop()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "stop")
}

void strobe() {
   if (enableDebug) log.debug "strobe()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "strobe")
}