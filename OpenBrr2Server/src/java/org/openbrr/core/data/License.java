package org.openbrr.core.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.openbrr.collector.flossmole.data.ProjectAttribute;
import org.openbrr.core.db.PersistenceUtil;

@Entity
@Table(name="licenses")
public class License implements ProjectAttribute {

	private static Map<Integer, License> allLicenses;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="lic_id")
	Integer licId;
	
	@Column(name="name")
	String name;

	@Column(name="family_id")
	Integer familyId;
	
	@Column(name="home_url")
	String homeUrl;
	
	@Column(name="code")
	String code;

	@SuppressWarnings("unused")
	private License() {
		//for hibernate
	}
	
	public License(String _name) {
		name = _name;
	}

	public Integer getId() {
		return licId;
	}

	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	//@SuppressWarnings(value = { "Type safety" })
	@SuppressWarnings("unchecked")
	public static License find(int _id) {
		License license = null;
		if(allLicenses == null) {
			allLicenses = new HashMap<Integer, License>();
			Session session = null;

			try {
				session = PersistenceUtil.getSession();
				List<License> data = session.createCriteria(License.class).list();
				for(License lic : data) {
					allLicenses.put(lic.licId, lic);
				}
			} finally {
				if (session != null)
					session.close();
			}
		}
		
		license = allLicenses.get(_id);
		
		return license;
	}
}
