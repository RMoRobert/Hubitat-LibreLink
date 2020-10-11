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
   definition (name: "LibreLink Thermostat", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-thermostat.groovy") {
      capability "Actuator"
      capability "Sensor"
      capability "Battery"
      capability "Refresh"
      capability "TemperatureMeasurement"
      capability "Thermostat"
      capability "ThermostatMode"
      capability "ThermostatFanMode"
      capability "ThermostatSetpoint"
      capability "ThermostatCoolingSetpoint"
      capability "ThermostatHeatingSetpoint"
      capability "ThermostatOperatingState"
      capability "RelativeHumidityMeasurement"  // can comment out if don't want (attribute-only capability; no commands)
      capability "PowerSource"  // can comment out if don't want (attribute-only capability; no commands)

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

def auto() {
   if (enableDebug) log.debug "auto()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "auto")
}

def cool() {
   if (enableDebug) log.debug "cool()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "cool")
}

def emergencyHeat() {
   if (enableDebug) log.debug "emergencyHeat()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "emergencyHeat")
}

def fanAuto() {
   if (enableDebug) log.debug "fanAuto()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "fanAuto")
}

def fanCirculate() {
   if (enableDebug) log.debug "fanCirculate()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "fanCirculate")
}

def fanOn() {
   if (enableDebug) log.debug "fanOn()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "fanOn")
}

def heat() {
   if (enableDebug) log.debug "heat()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "heat")
}

def off() {
   if (enableDebug) log.debug "off()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "off")
}

def setCoolingSetpoint(temperature) {
   if (enableDebug) log.debug "setCoolingSetpoint($temperature)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setCoolingSetpoint", [temperature])
}

def setHeatingSetpoint(temperature) {
   if (enableDebug) log.debug "setHeatingSetpoint($temperature)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setHeatingSetpoint", [temperature])
}

def setSchedule(scheduleJson) {
   if (enableDebug) log.debug "setSchedule($scheduleJson)"
   // TODO: Test if schedule needs to be converted to String or re-converted to JSON/map on receiving hub?
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setSchedule", [scheduleJson])
}

def setThermostatFanMode(fanMode) {
   if (enableDebug) log.debug "setThermostatFanMode($fanMode)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setThermostatFanMode", [fanMode])
}

def setThermostatMode(thermostatMode) {
   if (enableDebug) log.debug "setThermostatMode($thermostatMode)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setThermostatMode", [thermostatMode])
}