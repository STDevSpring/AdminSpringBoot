package com.fd.admin.data_service.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fd.admin.data_service.repository.PersonRepository;
import com.fd.admin.model.criteria.PersonListDetailsSearchCriteria;
import com.fd.admin.model.entity.Person;
import com.fd.admin.model.result.PersonListDetailsResult;

/**
 * 
 * @author Muguruza
 *
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositoryImpl.class);
	
	private static ServiceRegistry serviceRegistry;
	private static Session session;
	
	@Override
	public PersonListDetailsResult retrievePersonListDetails(PersonListDetailsSearchCriteria searchCriteria) {

		PersonListDetailsResult personListDetailsResult = new PersonListDetailsResult();

		initSession();
		
		// Query personDetails
		List<Person> personListDetailsEntity = session.createQuery("from PersonListDetailsEntity ",Person.class).getResultList();
		displayPersonListDetailsEntity(personListDetailsEntity);

		endSession(true);

		return personListDetailsResult;
	}

	private void displayPersonListDetailsEntity(List<Person> personListDetailsEntity) {
		for (Person personListDetailsEntity2 : personListDetailsEntity) {
			System.out.println(personListDetailsEntity2);
		}
	}

	@Override
	public boolean savePersonListDetails(Person personListDetailsEntity) {
		initSession();
		try {
			session.save(personListDetailsEntity);
			endSession(true);
		}catch(Exception e){
			LOGGER.error("savePersonListDetails: " , e);
			endSession(false);
		}
		return true;
	}
	
	private static void initSession() {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		serviceRegistry	= new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		
		SessionFactory sessionFactory = configuration.addAnnotatedClass(Person.class).buildSessionFactory(serviceRegistry);
		
		session = sessionFactory.openSession();
		session.beginTransaction();
	}
	
	private static void endSession(boolean commit) {
		if(commit){
			session.getTransaction().commit();
		}else{
			session.getTransaction().rollback();
		}
		session.close();
		StandardServiceRegistryBuilder.destroy(serviceRegistry);		
	}

}