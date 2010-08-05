package org.openbrr.ria.util
{
	public class URL
	{
		//URL Components
		private static const Server:String = "/home/ashutosh/projects/obrr_client/mock_data";
		
		//Search URLS
		public static const SEARCH:String = Server + "/search/result.xml";
		
		//Project URL
		public static const PROJECT_LIST:String = Server + "/project/list.xml";
		public static const PROJECT_DETAIL:String = Server + "/project/1.xml";
		public function URL()
		{
		}

	}
}