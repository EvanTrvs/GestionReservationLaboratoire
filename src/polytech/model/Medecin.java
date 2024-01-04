package polytech.model;

import javax.persistence.Column;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Entity
@Table(name = "Medecin")
public class Medecin {
    @Id
    @Column(name = "numero_secu_medecin")
    private int numeroSecuMedecin;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "honoraire", nullable = false)
    private double honoraire;

    @Column(name = "salaire")
    private Double salaire;

    public static Medecin getMed(SessionFactory sessionFactory, int numMed) {
        Session session = null;

        try {
            session = sessionFactory.openSession();

            // Utiliser une requête HQL pour récupérer le médecin par son numéro
            String hql = "FROM Medecin WHERE numeroSecuMedecin = :numMed";
            Query<Medecin> query = session.createQuery(hql, Medecin.class);
            query.setParameter("numMed", numMed);

            // Récupérer le résultat de la requête (la liste des médecins)
            List<Medecin> medecinList = query.list();

            // Vérifier si la liste contient au moins un médecin
            if (!medecinList.isEmpty()) {
                // Retourner le premier médecin trouvé (on suppose que les numéros de médecin sont uniques)
                return medecinList.get(0);
            } else {
                return null; // Aucun médecin trouvé
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retourner null en cas d'erreur
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    // Getters and setters
    public int getNumeroSecuMedecin() {
        return numeroSecuMedecin;
    }

    public void setNumeroSecuMedecin(int numeroSecuMedecin) {
        this.numeroSecuMedecin = numeroSecuMedecin;
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

    public double getHonoraire() {
        return honoraire;
    }

    public void setHonoraire(double honoraire) {
        this.honoraire = honoraire;
    }

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }
}
