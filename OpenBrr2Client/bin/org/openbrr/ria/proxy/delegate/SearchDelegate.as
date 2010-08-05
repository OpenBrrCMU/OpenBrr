package org.openbrr.ria.proxy.delegate
{
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	import org.openbrr.GlobalConstants;
	
	public class SearchDelegate implements ISearch
	{
		private var responder:IResponder;
		private var service:HTTPService;
		
		public function SearchDelegate(_responder:IResponder)
		{
			responder = _responder;
			service = new HTTPService();
		}
		
		public function search(searchText:String):void {
			service.url = GlobalConstants.APP_CONTEXT+"/search?format=xml&query="+searchText;
			var token:AsyncToken = service.send();
			token.addResponder(responder);
			//Alert.show("Doing Mock Search : "+searchText, "Track", Alert.OK); 
		}

	}
}