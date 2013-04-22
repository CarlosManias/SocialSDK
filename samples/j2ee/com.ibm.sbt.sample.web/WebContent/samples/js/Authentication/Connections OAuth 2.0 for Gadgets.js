require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
	var ep = Endpoint.find("connectionsOA2");
	if(ep.isAuthenticated){
		dom.setText("ConnOAuth2LoginStatus","You are authenticated");
		dom.byId("ConnOAuth2Login").style.display = "none";
		dom.byId("ConnOAuth2Logout").style.display = "inline";
	}else{
		dom.setText("ConnOAuth2LoginStatus","You are not authenticated");
		dom.byId("ConnOAuth2Login").style.display = "inline";
		dom.byId("ConnOAuth2Logout").style.display = "none";
	}
});

function login(loginUi) {
	require(['sbt/Endpoint',"sbt/dom","sbt/config"], function(Endpoint,dom,config) {
		config.Properties["loginUi"] = loginUi;
		Endpoint.find("connectionsOA2").xhrGet({
			serviceName : "connections",
			authType: "oauth",
	        serviceUrl : "/profiles/atom/profile.do",
	        handleAs : "text",
	        content : { 
	            userid : "%{sample.id1}"
	        },
	        load : function(response) {
	        	dom.setText("ConnOAuth2LoginStatus","You are authenticated");
				dom.byId("ConnOAuth2Login").style.display = "none";
				dom.byId("ConnOAuth2Logout").style.display = "inline";
	        },
	        error : function(error) {
	        	dom.setText("content","You need to Login to Proceed");
	        }
	    });
	});
}

function logout() {
	require(['sbt/Endpoint',"sbt/dom"], function(Endpoint,dom) {
		Endpoint.find("connectionsOA2").logout({
			load: function(response){
				dom.setText("ConnOAuth2LoginStatus","You are not authenticated");
				dom.byId("ConnOAuth2Login").style.display = "inline";
				dom.byId("ConnOAuth2Logout").style.display = "none";
			},
			error: function(){
				dom.setText("content","Failed to Logout");
			}
		});
	});
}