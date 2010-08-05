package org.openbrr.ria.view.event
{
	import flash.events.Event;
	
	public class SearchEvent extends Event
	{
		
		public static const SEARCH:String = "search";
		 
		private var searchString:String;
		
		public function SearchEvent(type:String, seachString:String, 
			bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.searchString = searchString;
		}

	}
}