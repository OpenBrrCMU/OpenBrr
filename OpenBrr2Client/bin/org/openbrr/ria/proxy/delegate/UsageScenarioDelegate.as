package org.openbrr.ria.proxy.delegate
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	import org.openbrr.GlobalConstants;
	
	public class UsageScenarioDelegate implements IUsageScenarioDelegate
	{
		private var responder:IResponder;
		private var service:HTTPService;
		
		public function UsageScenarioDelegate(_responder:IResponder)
		{
			responder = _responder;
			service = new HTTPService();
		}
		
		public function getUsageScenarios():void {
			service.url = GlobalConstants.APP_CONTEXT+"/usage_scenarios.xml";
			var token:AsyncToken = service.send();
			token.addResponder(responder);
			//Alert.show("Doing Mock Search : "+searchText, "Track", Alert.OK); 
		}

	}
}