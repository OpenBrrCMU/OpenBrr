package org.openbrr.ria.command
{
	import org.openbrr.ria.proxy.ProjectProxy;
	import org.openbrr.ria.proxy.SearchProxy;
	import org.openbrr.ria.proxy.UsageScenarioProxy;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.command.SimpleCommand;

	public class ModelPrepCommand extends SimpleCommand
	{
		public function ModelPrepCommand()
		{
			super();
		}
		
		override public function execute(notification:INotification):void {
			facade.registerProxy(new SearchProxy())
			facade.registerProxy(new ProjectProxy());
			facade.registerProxy(new UsageScenarioProxy());
			//facade.registerProxy(new LicenseProxy());
			//facade.registerProxy(new OperatingSystemProxy());
			//facade.registerProxy(new DataStoreProxy());
			//facade.registerProxy(new AusienceProxy());
			//facade.registerProxy(new ProgramLanguageProxy());
			//facade.registerProxy(new SearchProxy());
		}
	}
}