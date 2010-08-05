package org.openbrr.ria.view
{
	import org.openbrr.ria.ObrrFacade;
	import org.openbrr.ria.proxy.UsageScenarioProxy;
	import org.openbrr.ria.view.component.UsageScenario;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.mediator.Mediator;
	
	public class UsageScenarioMediator extends Mediator
	{
		public static const NAME:String = 'UsageScenarioMediator';
		
		private var usageScenarioProxy:UsageScenarioProxy;
		private var usageScenarioView:UsageScenario;
		
		public function UsageScenarioMediator(viewComponent:UsageScenario)
		{
			super(NAME, viewComponent);
			usageScenarioView = viewComponent;
			
			usageScenarioProxy = facade.retrieveProxy(UsageScenarioProxy.NAME) as UsageScenarioProxy;
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
            return [ ObrrFacade.SHOW_USAGE_SCENARIO,
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
				case ObrrFacade.SHOW_USAGE_SCENARIO:
					//Alert.show('Handling the serach notification', 'SearchMediator', Alert.OK);
					usageScenarioProxy.lookupUsageScenarios();
					break;
				case ObrrFacade.USAGE_SCENARIO_SUCCESS:
					//Alert.show('Handling the serach notification', 'SearchMediator', Alert.OK);
					usageScenarioView.setUsageScenarios(usageScenarioProxy.getUsageScenario());
					//usageScenarioProxy.lookupUsageScenarios();
					break;
            }
        }
		
		
	}
}