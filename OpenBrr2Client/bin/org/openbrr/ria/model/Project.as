package org.openbrr.ria.model
{
	public class Project
	{
		public var id:int;
		public var name:String;
		public var url:String;
		public var desc:String;
		
		public function Project(input:Object) {
			id = input.id;
			name = input.name;
			url = input.url;
			desc = input.desc;
		}
		
		/*
		public function Project(_id:int, _name:String, _url:String, _desc:String)
		{
			id = _id;
			name = _name;
			url = _url;
			desc = _desc;
		}
		*/
	}
}