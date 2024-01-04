package polytech.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Entity
@Table(name = "Patient")
public class Patient {
    @Id
    @Column(name = "numero_secu")
    private int numeroSecu;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    
    public static Patient getByNumeroSecu(int numeroSecu) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // Utiliser une requête HQL pour récupérer le Patient
            String hql = "FROM Patient WHERE numeroSecu = :numSecu";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            query.setParameter("numSecu", numeroSecu);
            
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourner null en cas d'erreur
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    public static void createNewPatient(int numeroSecu, String nom, String prenom, String motDePasse) {
        SessionFactory sessionFactory = null;
        Session session = null;
        org.hibernate.Transaction transaction = null;

        try {
            sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Créer un nouvel objet Patient
            Patient newPatient = new Patient();
            newPatient.setNumeroSecu(numeroSecu);
            newPatient.setNom(nom);
            newPatient.setPrenom(prenom);
            newPatient.setMotDePasse(motDePasse);

            // Enregistrer le nouvel objet Patient dans la base de données
            session.save(newPatient);

            transaction.commit();
            System.out.println("Successfully created a new Patient\n");

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

    // Getters and setters
    public int getNumeroSecu() {
        return numeroSecu;
    }

    public void setNumeroSecu(int numeroSecu) {
        this.numeroSecu = numeroSecu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
