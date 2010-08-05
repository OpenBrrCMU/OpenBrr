package org.openbrr.ria.proxy
{
	import mx.collections.ArrayCollection;
	import mx.rpc.IResponder;
	
	import org.openbrr.GlobalConstants;
	import org.openbrr.ria.ObrrFacade;
	import org.openbrr.ria.mock.MockSearchDelegate;
	import org.openbrr.ria.proxy.delegate.ISearch;
	import org.openbrr.ria.proxy.delegate.SearchDelegate;
	import org.puremvc.as3.interfaces.IProxy;
	import org.puremvc.as3.patterns.proxy.Proxy;

	public class SearchProxy extends Proxy implements IProxy, IResponder
	{
		public static const NAME:String = "SearchProxy";
		//public static const RUN_LIVE:Boolean = true;
		
		private var searchData:ArrayCollection;
		 
		public function SearchProxy(data:Object=null)
		{
			super(NAME, data);
		}
		
		public function search(searchText:String):void {
			
			var searchDelegate:ISearch = null;
			
			if(GlobalConstants.RUN_LIVE) {
				searchDelegate = new SearchDelegate(this);
			} else {
				searchDelegate = new MockSearchDelegate(this);
			}
			searchDelegate.search(searchText);
		}
		
		public function result(rpcEvent:Object):void {
			//Alert.show("Data: "+rpcEvent.result, "Result of search", Alert.OK);
			searchData = rpcEvent.result.projects.project as ArrayCollection
			sendNotification(ObrrFacade.SEARCH_SUCCESS, searchData);
			
		}

		public function fault(data:Object):void {
			//Alert.show(""+data, "Error in search", Alert.OK);
		}

		public function getLastSearchResult():ArrayCollection {
			return searchData;
		}
	}
}