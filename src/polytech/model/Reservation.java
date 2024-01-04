package polytech.model;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "Reservation")
public class Reservation {
    @Id
    @Column(name = "id_reservation")
    private int idReservation;

    @Column(name = "date_reservation", nullable = false)
    private java.sql.Date dateReservation;

    @Column(name = "prix", nullable = false)
    private double prix;

    @Column(name = "reglement", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean reglement;

    @ManyToOne
    @JoinColumn(name = "numero_secu_medecin", foreignKey = @ForeignKey(name = "fk_res_medecin"))
    private Medecin medecin;

    @ManyToOne
    @JoinColumn(name = "id_analyse", foreignKey = @ForeignKey(name = "fk_res_analyse"))
    private Analyse analyse;

    @ManyToOne
    @JoinColumn(name = "numero_secu", foreignKey = @ForeignKey(name = "fk_res_patient"))
    private Patient patient;
    
    public static void newRes(SessionFactory sessionFactory, int idReservation, String dateReservation,
            boolean reglement, int numMedecin, int idAnalyse, int numPatient) {
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			// Créer une nouvelle instance de Reservation
			Reservation reservation = new Reservation();
			reservation.setIdReservation(idReservation);
			reservation.setDateReservation(java.sql.Date.valueOf(dateReservation));
			reservation.setPrix(session.get(Medecin.class, numMedecin).getHonoraire()+session.get(Analyse.class, idAnalyse).getCout());
			reservation.setReglement(reglement);
			reservation.setMedecin(session.get(Medecin.class, numMedecin));
			reservation.setAnalyse(session.get(Analyse.class, idAnalyse));
			reservation.setPatient(session.get(Patient.class, numPatient));
			
			// Sauvegarder la nouvelle réservation dans la base de données
			session.save(reservation);
			
			// Commit la transaction
			transaction.commit();
			System.out.println("Nouvelle reservation creee avec succes.\n");
			
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
    
    public static ArrayList<Reservation> getAllReservations(SessionFactory sessionFactory) {
        Session session = null;

        try {
            session = sessionFactory.openSession();

            // Utiliser une requête HQL pour récupérer toutes les réservations
            String hql = "FROM Reservation";
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            List<Reservation> reservationList = query.list();

            return new ArrayList<>(reservationList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    public static ArrayList<Reservation> getReservationsByPatient(SessionFactory sessionFactory, int numeroSecuPatient) {
        Session session = null;

        try {
            session = sessionFactory.openSession();

            // Utiliser une requête HQL pour récupérer les réservations pour un patient spécifique
            String hql = "FROM Reservation WHERE patient.numeroSecu = :numeroSecu";
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            query.setParameter("numeroSecu", numeroSecuPatient);

            List<Reservation> reservationList = query.list();

            return new ArrayList<>(reservationList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    public static ArrayList<Reservation> getReservationsByMedecin(SessionFactory sessionFactory, int numeroSecuMedecin) {
        Session session = null;

        try {
            session = sessionFactory.openSession();

            // Utiliser une requête HQL pour récupérer les réservations pour un médecin spécifique
            String hql = "FROM Reservation WHERE medecin.numeroSecuMedecin = :numeroSecuMedecin";
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            query.setParameter("numeroSecuMedecin", numeroSecuMedecin);

            List<Reservation> reservationList = query.list();

            return new ArrayList<>(reservationList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    public static void setReglementToTrue(SessionFactory sessionFactory, int idReservation) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Récupérer la réservation à mettre à jour
            Reservation reservation = session.get(Reservation.class, idReservation);

            // Vérifier si la réservation existe
            if (reservation != null) {
                // Mettre à jour le boolean reglement à true
                reservation.setReglement(true);

                // Enregistrer la mise à jour dans la base de données
                session.update(reservation);

                // Commit la transaction
                transaction.commit();
                System.out.println("La reservation avec l'ID " + idReservation + " a ete mise à jour avec reglement=true.\n");
            } else {
                System.out.println("Aucune reservation trouvee avec l'ID " + idReservation + ". Mise à jour annulee.\n");
            }

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
    
    public static ArrayList<Reservation> getReservationByMedecinDate(SessionFactory sessionFactory, int numeroSecuMedecin, LocalDate date) {
        Session session = null;

        try {
            session = sessionFactory.openSession();

            // Utiliser une requête HQL pour récupérer les réservations pour un médecin spécifique et à partir d'une certaine date
            String hql = "FROM Reservation WHERE medecin.numeroSecuMedecin = :numeroSecuMedecin AND date_reservation >= :date";
            Query<Reservation> query = session.createQuery(hql, Reservation.class);
            query.setParameter("numeroSecuMedecin", numeroSecuMedecin);
            query.setParameter("date", java.sql.Date.valueOf(date));

            List<Reservation> reservationList = query.list();

            return new ArrayList<>(reservationList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static HashMap<Integer, ArrayList<String>> getAvailableDateByMedecin(SessionFactory sessionFactory, int idAnalyse, LocalDate currentDate) {
    	
    	HashMap<Integer, ArrayList<String>> MapMedDate = new HashMap<>() ;
		
    	ArrayList<Integer> listMed = Pratique.getAllMedecinPratique(sessionFactory, idAnalyse);
    	
		for (Integer numMed : listMed) {
			ArrayList<Reservation> resList = Reservation.getReservationByMedecinDate(sessionFactory, numMed, currentDate);
			ArrayList<String> nouvelleListe = new ArrayList<>();
			
			if (resList.isEmpty() == false) {
				ArrayList<String> listeDate = new ArrayList<>();
				for (Reservation res : resList) {
					listeDate.add(res.getDateReservationAsString());
				}
				if (listeDate.contains(currentDate.toString()) == false) {
					nouvelleListe.add(currentDate.toString());
				}
				if (listeDate.contains(currentDate.plusDays(1).toString()) == false) {
					nouvelleListe.add(currentDate.plusDays(1).toString());
				}
				if (listeDate.contains(currentDate.plusDays(2).toString()) == false) {
					nouvelleListe.add(currentDate.plusDays(2).toString());
				}				
			}
			else {
				nouvelleListe.add(currentDate.toString());
				nouvelleListe.add(currentDate.plusDays(1).toString());
				nouvelleListe.add(currentDate.plusDays(2).toString());
			}
			
			MapMedDate.put(numMed, nouvelleListe);
		}
		
		return MapMedDate;
    }

    public String getDateReservationAsString() {
        if (dateReservation != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(dateReservation);
        } else {
            return null; // or an empty string based on your requirements
        }
    }
    
	// Getters and setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public java.sql.Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(java.sql.Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public boolean isReglement() {
        return reglement;
    }

    public void setReglement(boolean reglement) {
        this.reglement = reglement;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Analyse getAnalyse() {
        return analyse;
    }

    public void setAnalyse(Analyse analyse) {
        this.analyse = analyse;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
