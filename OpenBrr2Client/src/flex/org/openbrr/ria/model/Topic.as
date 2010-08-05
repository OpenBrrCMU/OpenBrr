package org.openbrr.ria.model
{
	import mx.collections.ArrayCollection;
	
	public class Topic
	{
		public function Topic()
		{
			private var topicId:int;
			private var parentTopicId:int;
			private var name:String;
			private var childTopics:ArrayCollection;
		}

	}
}