package org.openbrr.collector.flossmole.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.openbrr.core.db.PersistenceUtil;

public class AttrDataCache {

	@SuppressWarnings("unchecked")
	private static Map<Class, Map<Integer, ProjectAttribute>> allProjectAttrs = null;
	
	static {
		initialize();
	}
	
	@SuppressWarnings("unchecked")
	private static void initialize() {
		allProjectAttrs = new HashMap<Class, Map<Integer,ProjectAttribute>>();
	}
	
	@SuppressWarnings("unchecked")
	private static void initializeData(Class<? extends ProjectAttribute> _class) {
		Session session = null;

		try {
			session = PersistenceUtil.getSession();
			
			List<ProjectAttribute> attrs = session.createCriteria(_class).list();
			Map<Integer, ProjectAttribute> attrData = new HashMap<Integer, ProjectAttribute>();
			
			for(ProjectAttribute attr : attrs) {
				attrData.put(attr.getId(), attr);
			}
			
			allProjectAttrs.put(_class, attrData);
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	public static ProjectAttribute find(Class<? extends ProjectAttribute> _class, int _id) {
		ProjectAttribute attr = null;
		
		Map<Integer, ProjectAttribute> attrData = allProjectAttrs.get(_class);
		if(attrData == null) {
			initializeData(_class);
			attrData = allProjectAttrs.get(_class);
		}
		
		attr = attrData.get(_id);
		
		return attr;
	}
	
}
