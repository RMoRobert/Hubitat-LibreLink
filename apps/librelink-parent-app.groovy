/**
 * ==========================  LibreLink (Parent App) ==========================
 *
 *  DESCRIPTION:
 *  Connect devices and more from multiple Hubiat Elevation hubs.
 *  See documentation for more details.
 *
 *  PLATFORM:
 *  Hubitat 
 * 
 *  TO INSTALL:
 *  Documentation coming soon.
 *
 *  Copyright 2020 Robert Morris
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
 *  Last modified: 2020-10-04
 *
 */ 
 
definition(
   name: "LibreLink",
   namespace: "RMoRobert",
   author: "Robert Morris",
   singleInstance: true,
   description: "Share devices between two (or more) Hubitat hubs. Open-source, community-created app for linking devices, etc.",
   installOnOpen: true,
   category: "Utility",
   documentationLink: "https://github.com/RMoRobert/Hubitat-LibreLink",
   iconUrl: "",
   iconX2Url: "",
   iconX3Url: ""
)

String getVersion() {
   return "0.9.0"
}

preferences {
   page(name: "mainPage", title: "LibreLink", install: true, uninstall: true) {
      section {      
         if (app.getInstallationState() == "INCOMPLETE") {
            paragraph("<b>Please press \"Done\" to finish installing this app, then re-open it to begin linking hubs.</b>")
         }
         else {
            app(name: "childApps", appName: "LibreLink (Linked Hub Child App)", namespace: "RMoRobert", title: "Add new hub linking...", multiple: true)
         }
      }
   }
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    //unsubscribe()
    initialize()
}

def initialize() {
    log.debug "Initializing; there are ${childApps.size()} child apps installed:"
    childApps.each {child ->
        log.debug "  child app: ${child.label}"
    }
}