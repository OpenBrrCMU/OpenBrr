package org.openbrr.ria.command
{
	import org.openbrr.ria.view.ObrrMediator;
	import org.puremvc.as3.interfaces.INotification;
	import org.puremvc.as3.patterns.command.SimpleCommand;

	public class ViewPrepCommand extends SimpleCommand
	{
		public function ViewPrepCommand()
		{
			super();
		}
		
		override public function execute(notification:INotification):void {
			facade.registerMediator(new ObrrMediator(notification.getBody() as ObrrApp));
			//facade.registerMediator(new SearchMediator(notification.getBody()));
		}
	}
}