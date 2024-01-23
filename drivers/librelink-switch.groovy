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

 #include RMoRobert.LibreLink-General
 #include RMoRobert.LibreLink-Switch
 #include RMoRobert.LibreLink-Flash
 
metadata {
   definition (name: "LibreLink Switch", namespace: "RMoRobert", author: "Robert Morris", importUrl: "https://raw.githubusercontent.com/RMoRobert/Hubitat-LibreLink/main/drivers/librelink-switch.groovy") {
      capability "Actuator"
      capability "Switch"
      //capability "Flash"  // required command is implemented; can uncomment if needed for any device(s)
      capability "Refresh"
      
      command "syncAttributes"
   }
      
preferences {
      input(name: "enableDebug", type: "bool", title: "Enable debug logging", defaultValue: true)
      input(name: "enableDesc", type: "bool", title: "Enable descriptionText logging", defaultValue: true)
   }
}

// Probably won't happen but...
void parse(String description) {
   log.warn "parse() not implemented: '${description}'"
}

