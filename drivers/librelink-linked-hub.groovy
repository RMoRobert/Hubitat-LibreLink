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
 *  Last modified: 2020-10-11
 *
 */ 
 
metadata {
   definition (name: "LibreLink Linked Hub", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-linked-hub.groovy") {
      capability "Actuator"
      capability "PresenceSensor"
      capability "Refresh"
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
   log.debug "Updated..."
   initialize()
}

def initialize() {
   log.debug "Initializing"
   int disableMinutes = 30
   if (enableDebug) {
      log.debug "Debug logging will be automatically disabled in ${disableMinutes} minutes"
      runIn(disableMinutes*60, debugOff)
   }
}

void debugOn(Boolean autoDisable=true) {
   log.warn("Enabling debug logging...")
   int disableMinutes = 30
   if (autoDisable) {
      log.debug "Debug logging will be automatically disabled in ${disableMinutes} minutes"
      runIn(disableMinutes*60, debugOff)
   }
   device.updateSetting("enableDebug", [value:"true", type:"bool"])
}

void debugOff() {
   log.warn("Disabling debug logging")
   device.updateSetting("enableDebug", [value:"false", type:"bool"])
}

void doSendEvent(eventName, eventValue, eventUnit=null, forceStateChange=false) {
   if (enableDebug) log.debug "doSendEvent($eventName, $eventValue, $eventUnit)..."
   def descriptionText = "${device.displayName} ${eventName} is ${eventValue}${eventUnit ?: ''}"
   if (enableDesc) log.info descriptionText
   Map eventProperties = [name: eventName, value: eventValue, descriptionText: description]
   if (eventUnit != null) eventProperties["unit"] = eventUnit
   if (forceStateChange) eventProperties["isStateChange"] = true
   sendEvent(eventProperties)
}

// Probably won't happen but...
def parse(String description) {
   log.warn("Running unimplemented parse for: '${description}'")
}

/* ======== Device capability methods ======== */

def refresh() {
   // For hub device, just tries to "ping" other hub via parent app and see if online; app will
   // update the presence attribute for this device accordingly
   if (enableDebug) log.debug "refresh()"
   parent.pingOtherHub()
}