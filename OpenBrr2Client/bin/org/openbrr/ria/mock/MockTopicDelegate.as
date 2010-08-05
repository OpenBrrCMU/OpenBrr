package org.openbrr.ria.mock
{
	//import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	public class MockTokenDelegate
	{
		private var responder:IResponder;
		private var service:HTTPService;
		
		public function MockSearchDelegate(_responder:IResponder)
		{
			responder = _responder;
			service = new HTTPService();
			service.url = "mock_data/search_data.xml";
			
			responder = _responder
		}
		
		public function search(searchText:String):void {
			var token:AsyncToken = service.send();
			token.addResponder(responder);
			//Alert.show("Doing Mock Search : "+searchText, "Track", Alert.OK); 
		}

	}
}