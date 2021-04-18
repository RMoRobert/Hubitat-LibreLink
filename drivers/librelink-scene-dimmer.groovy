/**
 * =======================================================================================
 *  Copyright 2021 Robert Morris
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
 *  Last modified: 2021-04-17
 *
 */ 
 
metadata {
   definition (name: "LibreLink Scene Dimmer", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-scene-dimmer.groovy") {
      capability "Actuator"
      capability "Refresh"
      capability "Switch"
      capability "SwitchLevel"
      capability "ChangeLevel" // can comment out if don't want or devices don't support
      capability "LevelPreset"
      //capability "Flash" // can uncomment if needed; required command implemented
      capability "Light"
      capability "PushableButton"
      capability "HoldableButton"
      capability "DoubleTapableButton"
      capability "ReleasableButton"

      // Can comment out if don't want this custom command:
      command "setIndicator", [[name:"paramValue", type: "NUMBER", description: "Calculated indicator/notification LED value (intended only for Inovelli or other devices with this command)" ]]
      
      command "syncAttributes"
   }
      
preferences {
      input name: "enableDebug", type: "bool", title: "Enable debug logging", defaultValue: true
      input name: "enableDesc", type: "bool", title: "Enable descriptionText logging", defaultValue: true
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
   String descriptionText = "${device.displayName} ${eventData.name} is ${eventData.value}${eventData.unit ?: ''}"
   if (enableDesc && (device.currentValue(eventData.name) != eventData.value || eventData.isStateChange)) log.info(descriptionText)
   Map eventProperties = [name: eventData.name, value: eventData.value, descriptionText: descriptionText,
      unit: eventData.unit, phyiscal: eventData.physical, digital: eventData.digital,
      isStateChange: eventData.isStateChange]
   if (forceStateChange) eventProperties["isStateChange"] = true
   sendEvent(eventProperties)
}

// Probably won't happen but...
void parse(String description) {
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

void flash() {
   if (enableDebug) log.debug "flash()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "flash")
}

void setLevel(level) {
   if (enableDebug) log.debug "setLevel($level)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId,
      "setLevel", [level])
}

void setLevel(level, duration) {
   if (enableDebug) log.debug "setLevel($level, $transitionTime)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId,
      "setLevel", [level, (duration != null ? duration.toBigDecimal() : 1)])
}

void presetLevel(level) {
   if (enableDebug) log.debug "presetLevel($level, $transitionTime)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId,
      "presetLevel", [level])
}

void startLevelChange(direction) {
   if (enableDebug) log.debug "startLevelChange($direction)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "startLevelChange", [direction])
}

void stopLevelChange() {
   if (enableDebug) log.debug "stopLevelChange()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "stopLevelChange")
}

void push(btnNum) {
   if (enableDebug) log.debug "push($btnNum)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "push", [btnNum])
}

void hold(btnNum) {
   if (enableDebug) log.debug "hold($btnNum)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "hold", [btnNum])
}

void release(btnNum) {
   if (enableDebug) log.debug "release($btnNum)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "release", [btnNum])
}

void doubleTap(btnNum) {
   if (enableDebug) log.debug "doubleTap($btnNum)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "doubleTap", [btnNum])
}

void setIndicator(paramValue) {
   if (enableDebug) log.debug "setIndicator($paramValue)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setIndicator", [paramValue])
}