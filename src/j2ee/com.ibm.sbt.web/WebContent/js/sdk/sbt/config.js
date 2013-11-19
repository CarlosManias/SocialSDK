/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

/**
 * Definition of config module.
 * 
	 * @module sbt.config
 */
define([
    'sbt/declare',
    'sbt/lang',
    'sbt/ErrorTransport',
    'sbt/Endpoint',
    'sbt/Proxy',
    'sbt/_bridge/Transport'
].concat(typeof extraDeps !== "undefined"?extraDeps:[]), function(declare, lang, ErrorTransport, Endpoint, Proxy, Transport) {
    return declare("sbt.config", null, {

        endpoints: null,
        Properties: null,
        depObjs : Array.prototype.slice.apply(arguments).slice(6),

        constructor: function(args){
            this.inherited(arguments);
            var _callback = lang.hitch(this, function(sbtConfig) {
                    this.Properties = sbtConfig["properties"];
                    this.endpoints = {};
                    this._populateEndpoints(sbtConfig["endpoints"]);
                });
            require(['sbt/sbtConfig'], _callback);
        },
        
        findEndpoint: function(endpointName) {
            return this.endpoints[endpointName];
        },

        _populateEndpoints: function(sbtConfigEndpoints) {
            for (var i=0;i<sbtConfigEndpoints.length;i++) {
                var endpointInfo = sbtConfigEndpoints[i];
                var endpoint = this._newEndpointInstance(endpointInfo);
                this.endpoints[endpointInfo["name"]] = endpoint;
            }
        },
        
        _newEndpointInstance: function(objInfo) {
            var ctorParams = this._getParams(objInfo["params"]);
            var className = objInfo["instanceOf"];
            switch(className){
                case "ErrorTransport":
                    return new ErrorTransport(ctorParams);
                case "Endpoint":
                    return new Endpoint(ctorParams);
                case "Proxy":
                    return new Proxy(ctorParams);
                case "Transport":
                    return new Transport(ctorParams);
                default:
                	for (var i=0;i<extraDeps.length;i++){
                		if (extraDeps[i].indexOf(className)>-1){
                			return this.depObjs[i].apply(this, ctorParams);
                		}
                	}
                	break;
            }
            console.error("Unable to find an object of type " + objInfo["instanceOf"] + " to create in _newEndpointInstance");
        },

        _getParams: function(params) {
            if (params.hasOwnProperty("instanceOf")) { 
                return this._newEndpointInstance(params);
            }
            var endpointParams = {};
            for (var key in params) {      
                 if (params.hasOwnProperty(key)) {
                    var param = params[key];
                    if (typeof param == "string") {
                        endpointParams[key] = param;
                    } else if (typeof param == "object") {
                        endpointParams[key] = this._getParams(param);
                    }
                 }
            }
            return endpointParams;
        }
        
    })();

});