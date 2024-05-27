package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Student;
import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.util.List;

public class HibernateTest
{
	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("databaseConfig");

	public static void main(String[] args){
		setUpData();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		// Uppgift 1
		Subject science = em.find(Subject.class, 2);
		TypedQuery<String> query1 = em.createQuery(
				"SELECT s.name FROM Tutor t JOIN t.teachingGroup s WHERE :subject MEMBER OF t.subjectsToTeach",
					String.class);
		query1.setParameter("subject", science);
		List<String> studentNames = query1.getResultList();
		System.out.println("Part 1\nNames of students with a Tutor who can teach science:");
		for (String name : studentNames) {
			System.out.println(name);
		}

		// Uppgift 2
		Query query2 = em.createQuery("SELECT s.name, t.name FROM Tutor t JOIN t.teachingGroup s");
		List<Object[]> resultList = query2.getResultList();
		System.out.println("\nPart 2");
		for (Object[] obj : resultList) {
			System.out.println("Student: " + obj[0] + ", Tutor: " + obj[1]);
		}

		// Uppgift 3
		Query query3 = em.createQuery("SELECT avg(numberOfSemesters) FROM Subject");
		Double averageNumberOfSemesters = (Double) query3.getSingleResult();
		System.out.println("\nPart 3\nAverage number of semesters: " + averageNumberOfSemesters);


		tx.commit();
		em.close();
	}

	public static void setUpData(){
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();


		Subject mathematics = new Subject("Mathematics", 2);
		Subject science = new Subject("Science", 2);
		Subject programming = new Subject("Programming", 3);
		em.persist(mathematics);
		em.persist(science);
		em.persist(programming);

		Tutor t1 = new Tutor("ABC123", "Johan Smith", 40000);
		t1.addSubjectsToTeach(mathematics);
		t1.addSubjectsToTeach(science);


		Tutor t2 = new Tutor("DEF456", "Sara Svensson", 20000);
		t2.addSubjectsToTeach(mathematics);
		t2.addSubjectsToTeach(science);

		// This tutor is the only tutor who can teach History
		Tutor t3 = new Tutor("GHI678", "Karin Lindberg", 0);
		t3.addSubjectsToTeach(programming);

		em.persist(t1);
		em.persist(t2);
		em.persist(t3);


		t1.createStudentAndAddtoTeachingGroup("Jimi Hendriks", "1-HEN-2019", "Street 1", "city 2", "1212");
		t1.createStudentAndAddtoTeachingGroup("Bruce Lee", "2-LEE-2019", "Street 2", "city 2", "2323");
		t3.createStudentAndAddtoTeachingGroup("Roger Waters", "3-WAT-2018", "Street 3", "city 3", "34343");

		tx.commit();
		em.close();
	}


}
