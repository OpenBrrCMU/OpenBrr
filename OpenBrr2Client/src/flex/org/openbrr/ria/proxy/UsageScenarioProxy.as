package org.openbrr.ria.proxy
{
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.remoting.RemoteObject;
	
	import org.openbrr.GlobalConstants;
	import org.openbrr.ria.ObrrFacade;
	import org.openbrr.ria.mock.MockUsageScenarioDelegate;
	import org.openbrr.ria.proxy.delegate.IUsageScenarioDelegate;
	import org.openbrr.ria.proxy.delegate.UsageScenarioDelegate;
	import org.puremvc.as3.interfaces.IProxy;
	import org.puremvc.as3.patterns.proxy.Proxy;

	public class UsageScenarioProxy extends Proxy implements IProxy, IResponder
	{
		public static const NAME:String = "UsageScenarioProxy";
		
		private var usageScenarioList:ArrayCollection;
		
		public function UsageScenarioProxy(data:Object=null)
		{
			super(NAME, data);
		}
		
		public function lookupUsageScenarios_old():void {
			var delegate:IUsageScenarioDelegate = null;

			//var mockProject:MockUsageScenarioDelegate = new MockUsageScenarioDelegate(this);
			//mockProject.lookupUsageScenarios();
			
			if(GlobalConstants.RUN_LIVE) {
				delegate = new UsageScenarioDelegate(this);
			} else {
				delegate = new MockUsageScenarioDelegate(this);
			}
			delegate.getUsageScenarios();
			
		}
		
		public function lookupUsageScenarios():void {
			
			var ro:RemoteObject = new RemoteObject();
			ro.destination = "UsageScenarioService";
			ro.getAllUsageScenarios.addEventListener("result", result);
			ro.addEventListener("fault", fault);
		}
		
		public function result(rpcEvent:Object):void {
			usageScenarioList = rpcEvent.result as ArrayCollection;
			sendNotification(ObrrFacade.USAGE_SCENARIO_SUCCESS, usageScenarioList);
		}

		public function fault(rpcEvent:Object):void {
			Alert.show(""+rpcEvent, "Error in Project Lookup", Alert.OK);
		}
		
		public function getUsageScenario() : ArrayCollection {
			return usageScenarioList;
		} 

	}
}