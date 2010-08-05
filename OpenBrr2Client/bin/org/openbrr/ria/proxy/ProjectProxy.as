package org.openbrr.ria.proxy
{
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	
	import org.openbrr.ria.ObrrFacade;
	import org.openbrr.ria.mock.MockProjectDelegate;
	import org.openbrr.ria.model.Project;
	import org.puremvc.as3.interfaces.IProxy;
	import org.puremvc.as3.patterns.proxy.Proxy;

	public class ProjectProxy extends Proxy implements IProxy, IResponder
	{
		public static const NAME:String = "ProjectProxy";
		
		private var projectData:Project;
		
		public function ProjectProxy(data:Object=null)
		{
			super(NAME, data);
		}
		
		public function lookup(id:int):void {
			var mockProject:MockProjectDelegate = new MockProjectDelegate(this);
			mockProject.find(id);
		}
		
		public function result(rpcEvent:Object):void {
			projectData = new Project(rpcEvent.result.project);
			sendNotification(ObrrFacade.PROJECT_LOOKUP_SUCCESS, projectData);
		}

		public function fault(rpcEvent:Object):void {
			Alert.show(""+rpcEvent, "Error in Project Lookup", Alert.OK);
		}

	}
}