package org.openbrr.ria.mock
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	import org.openbrr.ria.proxy.delegate.ISearch;
	
	public class MockSearchDelegate implements ISearch
	{
		private var responder:IResponder;
		private var service:HTTPService;
		
		public function MockSearchDelegate(_responder:IResponder)
		{
			responder = _responder;
			service = new HTTPService();
			service.url = "mock_data/search_data.xml";
		}
		
		public function search(searchText:String):void {
			var token:AsyncToken = service.send();
			token.addResponder(responder);
			//Alert.show("Doing Mock Search : "+searchText, "Track", Alert.OK); 
		}

	}
}