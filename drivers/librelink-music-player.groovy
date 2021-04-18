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
   definition (name: "LibreLink Music Player", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-music-player.groovy") {
      capability "Actuator"
      capability "MusicPlayer"
      capability "SpeechSynthesis" // can comment out if don't need
      capability "Refresh"  // can comment out if don't need

      // Custom Echo Speaks command; uncomment if need to use for that integration (are already implemented below):
      /*
      command "playAnnouncement", [[name: "Message to Announce*", type: "STRING", description: "Message to announce"],[name: "Announcement Title", type: "STRING", description: "This displays a title above message on devices with display"], [name: "Set Volume", type: "NUMBER", description: "Sets the volume before playing the message"],[name: "Restore Volume", type: "NUMBER", description: "Restores the volume after playing the message"]]
      command "playAnnouncementAll", [[name: "Message to Announce*", type: "STRING", description: "Message to announce"],[name: "Announcement Title", type: "STRING", description: "This displays a title above message on devices with display"]]
      */
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

void mute() {
   if (enableDebug) log.debug "mute()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "mute")
}

void nextTrack() {
   if (enableDebug) log.debug "nextTrack()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "nextTrack")
}

void pause() {
   if (enableDebug) log.debug "pause()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "pause")
}

void play() {
   if (enableDebug) log.debug "play()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "play")
}

void playText(text) {
   if (enableDebug) log.debug "playText()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "playText", [text])
}

void playTrack(trackuri) {
   if (enableDebug) log.debug "playTrack()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "playTrack", [trackuri])
}

void previousTrack() {
   if (enableDebug) log.debug "previousTrack()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "previousTrack")
}

void restoreTrack(trackuri) {
   if (enableDebug) log.debug "restoreTrack()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "restoreTrack", [trackuri])
}

void resumeTrack(trackuri) {
   if (enableDebug) log.debug "resumeTrack()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "resumeTrack", [trackuri])
}

void setLevel(level) {
   if (enableDebug) log.debug "setLevel($level)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setLevel", [level])
}

void setTrack(trackuri) {
   if (enableDebug) log.debug "setTrack($trackuri)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "setTrack", [trackuri])
}

void stop() {
   if (enableDebug) log.debug "stop()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "stop")
}

void unmute() {
   if (enableDebug) log.debug "unmute()"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "unmute")
}

void speak(text, volume, voice) {
   if (enableDebug) log.debug "speak($text, $volume, $voice)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "speak", [text, volume, voice])
}

void playAnnouncement(message, title=null, volume=null, restoreVolume=null) {
   if (enableDebug) log.debug "playAnnouncement($message, $title, $volume, $restoreVolume)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "playAnnouncement", [message, title, volume, restoreVolume])
}

void playAnnouncementAll(message, title=null) {
   if (enableDebug) log.debug "playAnnouncementAll($message, $title)"
   parent.sendCommandFromChildDevice(device.deviceNetworkId, "playAnnouncement", [message, title])
}