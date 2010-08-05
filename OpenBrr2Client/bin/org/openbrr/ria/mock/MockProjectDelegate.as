package org.openbrr.ria.mock
{
	//import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	public class MockProjectDelegate
	{
		private var responder:IResponder;
		private var service:HTTPService;
		
		public function MockProjectDelegate(_responder:IResponder)
		{
			responder = _responder;
			service = new HTTPService();
			service.url = "mock_data/project_data.xml";
		}
		
		public function find(id:int):void {
			var token:AsyncToken = service.send();
			token.addResponder(responder);
			//Alert.show("Find Mock Project : "+id, "Track", Alert.OK); 
		}

	}
}