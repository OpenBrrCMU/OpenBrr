package org.openbrr.ria.view
{
	import mx.collections.ArrayCollection;
	//import mx.controls.Alert;
	
	import org.openbrr.ria.ObrrFacade;
	import org.openbrr.ria.model.Project;
	import org.openbrr.ria.proxy.ProjectProxy;
	import org.openbrr.ria.proxy.SearchProxy;
	import org.openbrr.ria.view.component.ProjectDetail;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.mediator.Mediator;
	
	public class ProjectMediator extends Mediator
	{
		public static const NAME:String = 'ProjectMediator';
		
		private var project:Project;
		
		private var searchProxy:SearchProxy;
		private var projectProxy:ProjectProxy;
		private var viewComp:ProjectDetail;
		
		public function ProjectMediator(viewComponent:ProjectDetail)
		{
			super(NAME, viewComponent);
			viewComp = viewComponent;
			
			searchProxy = facade.retrieveProxy(SearchProxy.NAME) as SearchProxy;
			projectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
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
            return [ ObrrFacade.PROJECT_LOOKUP,
            		 ObrrFacade.PROJECT_LOOKUP_SUCCESS,
            		 ObrrFacade.PROJECT_LOOKUP_ERROR
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
				case ObrrFacade.PROJECT_LOOKUP:
					projectProxy.lookup(parseInt(note.getBody() as String));
					//Alert.show('Passed Last search results to ProjectDetail: '+viewComp 
					//	+", data: "+lastSearchResult, 'ProjectMediator', Alert.OK);
					break;
					
				case ObrrFacade.PROJECT_LOOKUP_SUCCESS:
					var lastSearchResult:ArrayCollection = searchProxy.getLastSearchResult();
					viewComp.setSearchResult(lastSearchResult);
					//*
					//Alert.show(''+note.getBody(),'ProjectMediator.LOOKUP_SUCCESS',Alert.OK);
					project = note.getBody() as Project;
					//Alert.show('name['+project.name+'], desc['+project.desc+']',
					//	'ProjectMediator.LOOKUP_SUCCESS - 2',Alert.OK);
					viewComp.setProject(project);
					/*
					*/
					break;
            }
        }
		
		
	}
}