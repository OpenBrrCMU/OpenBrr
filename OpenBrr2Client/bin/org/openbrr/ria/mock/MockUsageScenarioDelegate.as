package org.openbrr.ria.mock
{
	//import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	import org.openbrr.ria.proxy.delegate.IUsageScenarioDelegate;
	
	public class MockUsageScenarioDelegate implements IUsageScenarioDelegate
	{
		private var responder:IResponder;
		private var service:HTTPService;
		
		public function MockUsageScenarioDelegate(_responder:IResponder)
		{
			responder = _responder;
			service = new HTTPService();
			service.url = "mock_data/usage_scenario.xml";
		}
		
		public function getUsageScenarios():void {
			var token:AsyncToken = service.send();
			token.addResponder(responder);
			//Alert.show("Find Mock Project : "+id, "Track", Alert.OK); 
		}

	}
}