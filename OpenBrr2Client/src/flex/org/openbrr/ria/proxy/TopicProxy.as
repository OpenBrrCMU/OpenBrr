package org.openbrr.ria.proxy
{
	import mx.collections.ArrayCollection;
	//import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	import org.openbrr.ria.mock.MockSearchDelegate;
	import org.openbrr.ria.util.URL;
	import org.puremvc.as3.interfaces.IProxy;
	import org.puremvc.as3.patterns.proxy.Proxy;

	public class TopicProxy extends Proxy implements IProxy, IResponder
	{
		public static const NAME:String = "TopicProxy";
		
		public function TopicProxy(data:Object=null)
		{
			super(NAME, data);
		}
		
		public function search(searchText:String):void {
			var mockSearch:MockSearchDelegate = new MockSearchDelegate(this);
			mockSearch.search(searchText);
		}
		
		public function result(data:Object):void {
			//Alert.show(""+data, "Result of search", Alert.OK);
		}

		public function fault(data:Object):void {
			//Alert.show(""+data, "Error in search", Alert.OK);
		}

		/*
		override public function getProxyName():String
		{
			return null;
		}
		
		override public function setData(data:Object):void
		{
		}
		
		override public function getData():Object
		{
			return null;
		}
		
		override public function onRegister():void
		{
		}
		
		override public function onRemove():void
		{
		}
		
		public function searchToken():ArrayCollection {
			var service:HTTPService = new HTTPService();
			service.url = URL.SEARCH;
			service.resultFormat = "e4x";
			//service.showBusyCursor = true;
			var token:AsyncToken = service.send();
			token.addResponder(this);
			
			return null;
		}
		
		*/
	}
}