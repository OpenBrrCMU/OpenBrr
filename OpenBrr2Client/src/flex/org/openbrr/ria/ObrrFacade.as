/*
 * OpenBrr
 * Copyright (c) 2009
 * License:
 */

package org.openbrr.ria
{
	import org.openbrr.ria.command.ApplicationStartupCommand;
	import org.puremvc.as3.patterns.facade.Facade;
	
    
    public class ObrrFacade extends Facade {
		
		//public static const RUN_LIVE:Boolean = false;
		
		//Notification name constants
		public static const STARTUP:String = "startup";
		public static const SHUTDOWN:String = "shutdown";
		public static const SHOW_MAIN_PAGE:String = "showMainPage";
		public static const SHOW_ADMIN_PAGE:String = "showAdminPage";
		public static const SEARCH:String = "search";
		public static const SEARCH_SUCCESS:String = "search_success";
		public static const SEARCH_ERROR:String = "search_error";
		public static const PROJECT_LOOKUP:String = "project_lookup";
		public static const PROJECT_LOOKUP_SUCCESS:String = "project_lookup_success";
		public static const PROJECT_LOOKUP_ERROR:String = "project_lookup_error";
		public static const SHOW_USAGE_SCENARIO:String = "show_usage_scenario";
		public static const USAGE_SCENARIO_SUCCESS:String = "usage_scenario_success";
		public static const USAGE_SCENARIO_ERROR:String = "usage_scenario_error";
		
		//Event name constants
		//public static const SEARCH_EVENT:String = "searchEvent";
		
		private static var instance:ObrrFacade;
		private static var mainApp:ObrrApp;
		
		public static function getInstance(): ObrrFacade {
			if(instance == null) {
				instance = new ObrrFacade();
			}
			
			return instance;
		}
		
		/**
		 * Register Commands
		 */
		override protected function initializeController():void {
			super.initializeController();
			
			//any customization
			registerCommand(STARTUP, ApplicationStartupCommand);
		}
		
		public function startUp(viewComp:ObrrApp):void {
			mainApp = viewComp;
			sendNotification(STARTUP, viewComp);
		}
		
		public function getApplication():ObrrApp {
			return mainApp;
		}
    }
}