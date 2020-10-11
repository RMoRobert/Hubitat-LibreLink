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
 
 // TODO: does lockCodes attribute need to be converted back to JSON from String when received?

metadata {
   definition (name: "LibreLink Lock", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-lock.groovy") {
      capability "Actuator"
      capability "Sensor"
      capability "Battery"
      capability "Refresh"
		capability "Lock"
		capability "LockCodes"
      
      command "syncAttributes"
   }
      
preferences {
      input(name: "enableDebug", type: "bool", title: "Enable debug logging", defaultValue: true)
      input(name: "enableDesc", type: "bool", title: "Enable descriptionText logging", defaultValue: true)
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

def lock() {
   if (enableDebug) log.debug "lock()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "lock")
}

def unlock() {
   if (enableDebug) log.debug "unlock()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "unlock")
}

def deleteCode(codePosition) {
   if (enableDebug) log.debug "deleteCode($codePosition)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "deleteCode", [codePosition])
}

def getCodes() {
   if (enableDebug) log.debug "getCodes()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "getCodes")
}

def setCode(codePosition, pinCode, name) {
   if (enableDebug) log.debug "setCode($codePosition, $pinCode, $name)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setCode", [codePosition, pinCode, name])
}

def setCodeLength(codeLength) {
   if (enableDebug) log.debug "setCodeLength($codeLength)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setCodeLength", [codeLength])
}