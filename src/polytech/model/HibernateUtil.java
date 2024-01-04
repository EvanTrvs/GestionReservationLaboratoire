/**
 * 
 */
package polytech.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Evan T
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	static {
		try {
			Configuration conf = new Configuration().configure();
			
			conf.addAnnotatedClass(polytech.model.Patient.class);
			conf.addAnnotatedClass(polytech.model.Medecin.class);
			conf.addAnnotatedClass(polytech.model.Analyse.class);
			conf.addAnnotatedClass(polytech.model.Reservation.class);
			conf.addAnnotatedClass(polytech.model.Pratique.class);
			
			conf.configure();
			ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
			sessionFactory = conf.buildSessionFactory(sr);
		} catch (Throwable th) {
			System.err.println("Enitial SessionFactory creation failed" + th);
			throw new ExceptionInInitializerError(th);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void dropAllTables() {
        SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Supprimer les tables
            String[] dropTableQueries = {
                    "SET FOREIGN_KEY_CHECKS = 0;",
                    "DROP TABLE IF EXISTS Pratique;",
                    "DROP TABLE IF EXISTS Reservation;",
                    "DROP TABLE IF EXISTS Analyse;",
                    "DROP TABLE IF EXISTS Medecin;",
                    "DROP TABLE IF EXISTS Patient;",
                    "SET FOREIGN_KEY_CHECKS = 1;"
            };

            for (String dropTableQuery : dropTableQueries) {
                session.createNativeQuery(dropTableQuery).executeUpdate();
                System.out.println("Table dropped or does not exist.");
            }

            transaction.commit();
            System.out.println("Successfully dropped all tables\n");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
	
	public static void createTablesIfNotExist() {
        SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Créer les tables si elles n'existent pas déjà
            String[] createTableQueries = {
                    "CREATE TABLE IF NOT EXISTS Patient(" +
                            "   numero_secu INT," +
                            "   nom VARCHAR(50)," +
                            "   prenom VARCHAR(50)," +
                            "   mot_de_passe VARCHAR(255) NOT NULL," +
                            "   PRIMARY KEY(numero_secu)" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS Medecin(" +
                            "   numero_secu_medecin INT," +
                            "   nom VARCHAR(50)," +
                            "   prenom VARCHAR(50)," +
                            "   honoraire REAL NOT NULL," +
                            "   salaire REAL," +
                            "   PRIMARY KEY(numero_secu_medecin)" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS Analyse(" +
                            "   id_analyse INT," +
                            "   libelle VARCHAR(250) NOT NULL," +
                            "   cout REAL NOT NULL," +
                            "   PRIMARY KEY(id_analyse)" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS Reservation(" +
                            "   id_reservation INT," +
                            "   date_reservation DATE NOT NULL," +
                            "   prix REAL NOT NULL," +
                            "   reglement BOOLEAN DEFAULT false," +
                            "   numero_secu_medecin INT NOT NULL," +
                            "   id_analyse INT NOT NULL," +
                            "   numero_secu INT NOT NULL," +
                            "   PRIMARY KEY(id_reservation)," +
                            "   FOREIGN KEY(numero_secu_medecin) REFERENCES Medecin(numero_secu_medecin) ON DELETE CASCADE," +
                            "   FOREIGN KEY(id_analyse) REFERENCES Analyse(id_analyse) ON DELETE CASCADE," +
                            "   FOREIGN KEY(numero_secu) REFERENCES Patient(numero_secu) ON DELETE CASCADE" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS Pratique(" +
                            "   numero_secu_medecin INT," +
                            "   id_analyse INT," +
                            "   PRIMARY KEY(numero_secu_medecin, id_analyse)," +
                            "   FOREIGN KEY(numero_secu_medecin) REFERENCES Medecin(numero_secu_medecin) ON DELETE CASCADE," +
                            "   FOREIGN KEY(id_analyse) REFERENCES Analyse(id_analyse) ON DELETE CASCADE" +
                            ");"
            };

            for (String createTableQuery : createTableQueries) {
                session.createNativeQuery(createTableQuery).executeUpdate();
                System.out.println("Table created or already exists.");
            }

            transaction.commit();
            System.out.println("Successfully created tables\n");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
	
	public static void insertPatientData() {
		SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            Patient patient1 = new Patient();
            patient1.setNumeroSecu(123456789);
            patient1.setNom("Dupont");
            patient1.setPrenom("Alice");
            patient1.setMotDePasse("qmlerwax");
            session.save(patient1);

            Patient patient2 = new Patient();
            patient2.setNumeroSecu(987654321);
            patient2.setNom("Martin");
            patient2.setPrenom("Bob");
            patient2.setMotDePasse("yurongsp");
            session.save(patient2);

            Patient patient3 = new Patient();
            patient3.setNumeroSecu(111223344);
            patient3.setNom("Dubois");
            patient3.setPrenom("Claire");
            patient3.setMotDePasse("qlzmqjwf");
            session.save(patient3);

            Patient patient4 = new Patient();
            patient4.setNumeroSecu(222222222);
            patient4.setNom("Johnson");
            patient4.setPrenom("Jane");
            patient4.setMotDePasse("rwgeootz");
            session.save(patient4);

            Patient patient5 = new Patient();
            patient5.setNumeroSecu(555555555);
            patient5.setNom("Lefevre");
            patient5.setPrenom("David");
            patient5.setMotDePasse("udbjpbwg");
            session.save(patient5);

            transaction.commit();
            System.out.println("Successfully inserted Patient data\n");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
	
	public static void insertMedecinData() {
		SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
	        // Insert data into Medecin table
	        Medecin medecin1 = new Medecin();
	        medecin1.setNumeroSecuMedecin(111111111);
	        medecin1.setNom("Smith");
	        medecin1.setPrenom("John");
	        medecin1.setHonoraire(80.00);
	        medecin1.setSalaire(120000.00);
	        session.save(medecin1);

	        Medecin medecin2 = new Medecin();
	        medecin2.setNumeroSecuMedecin(222222222);
	        medecin2.setNom("Johnson");
	        medecin2.setPrenom("Jane");
	        medecin2.setHonoraire(75.00);
	        medecin2.setSalaire(100000.00);
	        session.save(medecin2);

	        Medecin medecin3 = new Medecin();
	        medecin3.setNumeroSecuMedecin(333333333);
	        medecin3.setNom("Brown");
	        medecin3.setPrenom("Chris");
	        medecin3.setHonoraire(70.00);
	        medecin3.setSalaire(90000.00);
	        session.save(medecin3);

	        transaction.commit();
	        System.out.println("Successfully inserted Medecin data\n");

	    } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}
	
	public static void insertAnalyseData() {
		SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
	        // Insert data into Analyse table
	        Analyse analyse1 = new Analyse();
	        analyse1.setIdAnalyse(1);
	        analyse1.setLibelle("Analyse de sang");
	        analyse1.setCout(50.00);
	        session.save(analyse1);

	        Analyse analyse2 = new Analyse();
	        analyse2.setIdAnalyse(2);
	        analyse2.setLibelle("Radiographie");
	        analyse2.setCout(100.00);
	        session.save(analyse2);

	        Analyse analyse3 = new Analyse();
	        analyse3.setIdAnalyse(3);
	        analyse3.setLibelle("Vitesse de sédimentation");
	        analyse3.setCout(80.00);
	        session.save(analyse3);

	        Analyse analyse4 = new Analyse();
	        analyse4.setIdAnalyse(4);
	        analyse4.setLibelle("Hemogramme");
	        analyse4.setCout(120.00);
	        session.save(analyse4);

	        transaction.commit();
	        System.out.println("Successfully inserted Analyse data\n");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}
	
	public static void insertReservationData() {
		SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
	    	// Insert data into Reservation table
	        Reservation reservation1 = new Reservation();
	        reservation1.setIdReservation(101);
	        reservation1.setMedecin(session.get(Medecin.class, 111111111));
	        reservation1.setAnalyse(session.get(Analyse.class, 1));
	        reservation1.setDateReservation(java.sql.Date.valueOf("2023-12-01"));
	        reservation1.setPrix(140.00);
	        reservation1.setReglement(true);
	        reservation1.setPatient(session.get(Patient.class, 123456789));
	        session.save(reservation1);

	        Reservation reservation2 = new Reservation();
	        reservation2.setIdReservation(102);
	        reservation2.setMedecin(session.get(Medecin.class, 222222222));
	        reservation2.setAnalyse(session.get(Analyse.class, 1));
	        reservation2.setDateReservation(java.sql.Date.valueOf("2023-12-01"));
	        reservation2.setPrix(135.00);
	        reservation2.setReglement(true);
	        reservation2.setPatient(session.get(Patient.class, 987654321));
	        session.save(reservation2);

	        Reservation reservation3 = new Reservation();
	        reservation3.setIdReservation(103);
	        reservation3.setMedecin(session.get(Medecin.class, 333333333));
	        reservation3.setAnalyse(session.get(Analyse.class, 1));
	        reservation3.setDateReservation(java.sql.Date.valueOf("2023-12-01"));
	        reservation3.setPrix(130.00);
	        reservation3.setReglement(false);
	        reservation3.setPatient(session.get(Patient.class, 111223344));
	        session.save(reservation3);

	        Reservation reservation4 = new Reservation();
	        reservation4.setIdReservation(104);
	        reservation4.setMedecin(session.get(Medecin.class, 222222222));
	        reservation4.setAnalyse(session.get(Analyse.class, 2));
	        reservation4.setDateReservation(java.sql.Date.valueOf("2023-12-02"));
	        reservation4.setPrix(185.00);
	        reservation4.setReglement(true);
	        reservation4.setPatient(session.get(Patient.class, 987654321));
	        session.save(reservation4);

	        Reservation reservation5 = new Reservation();
	        reservation5.setIdReservation(105);
	        reservation5.setMedecin(session.get(Medecin.class, 333333333));
	        reservation5.setAnalyse(session.get(Analyse.class, 3));
	        reservation5.setDateReservation(java.sql.Date.valueOf("2023-12-03"));
	        reservation5.setPrix(160.00);
	        reservation5.setReglement(true);
	        reservation5.setPatient(session.get(Patient.class, 111223344));
	        session.save(reservation5);

	        Reservation reservation6 = new Reservation();
	        reservation6.setIdReservation(106);
	        reservation6.setMedecin(session.get(Medecin.class, 333333333));
	        reservation6.setAnalyse(session.get(Analyse.class, 4));
	        reservation6.setDateReservation(java.sql.Date.valueOf("2023-12-04"));
	        reservation6.setPrix(200.00);
	        reservation6.setReglement(false);
	        reservation6.setPatient(session.get(Patient.class, 555555555));
	        session.save(reservation6);

	        transaction.commit();
	        System.out.println("Successfully inserted Reservation data\n");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	public static void insertPratiqueData() {
		SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
	    	// Insert data into Pratique table
	        Pratique pratique1 = new Pratique();
	        pratique1.setMedecin(session.get(Medecin.class, 111111111));
	        pratique1.setAnalyse(session.get(Analyse.class, 1));
	        session.save(pratique1);

	        Pratique pratique2 = new Pratique();
	        pratique2.setMedecin(session.get(Medecin.class, 111111111));
	        pratique2.setAnalyse(session.get(Analyse.class, 2));
	        session.save(pratique2);

	        Pratique pratique3 = new Pratique();
	        pratique3.setMedecin(session.get(Medecin.class, 111111111));
	        pratique3.setAnalyse(session.get(Analyse.class, 3));
	        session.save(pratique3);

	        Pratique pratique4 = new Pratique();
	        pratique4.setMedecin(session.get(Medecin.class, 111111111));
	        pratique4.setAnalyse(session.get(Analyse.class, 4));
	        session.save(pratique4);

	        Pratique pratique5 = new Pratique();
	        pratique5.setMedecin(session.get(Medecin.class, 222222222));
	        pratique5.setAnalyse(session.get(Analyse.class, 1));
	        session.save(pratique5);

	        Pratique pratique6 = new Pratique();
	        pratique6.setMedecin(session.get(Medecin.class, 222222222));
	        pratique6.setAnalyse(session.get(Analyse.class, 2));
	        session.save(pratique6);

	        Pratique pratique7 = new Pratique();
	        pratique7.setMedecin(session.get(Medecin.class, 333333333));
	        pratique7.setAnalyse(session.get(Analyse.class, 1));
	        session.save(pratique7);

	        transaction.commit();
	        System.out.println("Successfully inserted Pratique data\n");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}
}