package org.openbrr.ria.view
{
	//import mx.controls.Alert;
	
	import org.openbrr.ria.ObrrFacade;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.mediator.Mediator;
	
	public class ObrrMediator extends Mediator
	{
		private var obrrApp:ObrrApp;
		private var NAME:String = "ObrrMediator";
		
		public function ObrrMediator(app:ObrrApp)
		{
			super(NAME, app);
			obrrApp = app;
			
			//trying to use a single mediator for both view components
			//facade.registerMediator(new SearchMediator(obrrApp.content.searchMain));
			facade.registerMediator(new SearchMediator(obrrApp.content.searchResult));
			facade.registerMediator(new ProjectMediator(obrrApp.content.projectDetail));
			facade.registerMediator(new UsageScenarioMediator(obrrApp.content.usageScenario));
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
            return [ ObrrFacade.SEARCH_SUCCESS,
            		 ObrrFacade.PROJECT_LOOKUP_SUCCESS,
            		 ObrrFacade.SHOW_MAIN_PAGE,
            		 ObrrFacade.SHOW_ADMIN_PAGE,
            		 ObrrFacade.USAGE_SCENARIO_SUCCESS
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
				case ObrrFacade.SEARCH_SUCCESS:
					obrrApp.content.selectedChild = obrrApp.content.searchResult;
					//Alert.show('Handling the search success notification', 'ObrrMediator', Alert.OK);
					break;
				case ObrrFacade.PROJECT_LOOKUP_SUCCESS:
					obrrApp.content.selectedChild = obrrApp.content.projectDetail;
					break;
				case ObrrFacade.SHOW_MAIN_PAGE:
					obrrApp.content.selectedChild = obrrApp.content.searchMain;
                	break;
				case ObrrFacade.SHOW_ADMIN_PAGE:
					obrrApp.content.selectedChild = obrrApp.content.admin;
                	break;
				case ObrrFacade.USAGE_SCENARIO_SUCCESS:
					obrrApp.content.selectedChild = obrrApp.content.usageScenario;
                	break;
            }
        }
	}
}