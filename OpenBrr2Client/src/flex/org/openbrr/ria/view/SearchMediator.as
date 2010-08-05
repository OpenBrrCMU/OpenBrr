package org.openbrr.ria.view
{
	import mx.collections.ArrayCollection;
	//import mx.controls.Alert;
	
	import org.openbrr.ria.ObrrFacade;
	import org.openbrr.ria.proxy.SearchProxy;
	import org.openbrr.ria.view.component.SearchResult;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.mediator.Mediator;
	
	public class SearchMediator extends Mediator
	{
		public static const NAME:String = 'SearchMediator';
		
		private var searchProxy:SearchProxy;
		private var searchComp:SearchResult;
		
		public function SearchMediator(viewComponent:SearchResult)
		{
			super(NAME, viewComponent);
			searchComp = viewComponent;
			
			searchProxy = facade.retrieveProxy(SearchProxy.NAME) as SearchProxy;
			//searchComp.addEventListener(ObrrFacade.SEARCH_EVENT, onSearchRequest);
		}
		
        /**
         * List all notifications this Mediator is interested in.
         * <P>
         * Automatically called by the framework when the mediator
         * is registered with the view.</P>
         * 
         * @return Array the list of Nofitication names
         */
        override public function listNotificationInterests():Array 
        {
            return [ ObrrFacade.SEARCH,
            		 ObrrFacade.SEARCH_SUCCESS,
					];
        }
		
        /**
         * Handle all notifications this Mediator is interested in.
         * <P>
         * Called by the framework when a notification is sent that
         * this mediator expressed an interest in when registered
         * (see <code>listNotificationInterests</code>.</P>
         * 
         * @param INotification a notification 
         */
        override public function handleNotification( note:INotification ):void 
        {
            switch ( note.getName() ) 
			{
				case ObrrFacade.SEARCH:
					//Alert.show('Handling the search notification', 'SearchMediator', Alert.OK);
					searchProxy.search(note.getBody() as String);
					break;
				case ObrrFacade.SEARCH_SUCCESS:
					searchComp.setSearchResult(note.getBody() as ArrayCollection);
					//Alert.show('Handling the search success notification', 'SearchMediator', Alert.OK);
					break;
                
            }
        }
		
		
	}
}