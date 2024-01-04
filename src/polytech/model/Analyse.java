package polytech.model;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Entity
@Table(name = "Analyse")
public class Analyse {
    @Id
    @Column(name = "id_analyse")
    private int idAnalyse;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "cout", nullable = false)
    private double cout;

    // Méthode pour récupérer toutes les analyses depuis la base de données
    public static ArrayList<Analyse> getAllAnalyse(SessionFactory sessionFactory) {
    	ArrayList<Analyse> analyses = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            // Utiliser HQL (Hibernate Query Language) pour effectuer la requête
            String hql = "FROM Analyse";
            Query<Analyse> query = session.createQuery(hql, Analyse.class);
            analyses = (ArrayList<Analyse>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return analyses;
    }
    
    // Méthode pour récupérer le coût d'une analyse donnée
    public static double getCout(SessionFactory sessionFactory, int idAnalyse) {
        double cout = 0.0;

        try (Session session = sessionFactory.openSession()) {
            // Utiliser HQL (Hibernate Query Language) pour effectuer la requête
            String hql = "SELECT a.cout FROM Analyse a WHERE a.idAnalyse = :idAnalyse";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("idAnalyse", idAnalyse);

            ArrayList<Double> resultList = (ArrayList<Double>) query.list();
            if (!resultList.isEmpty()) {
                cout = resultList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cout;
    }
    
    // Getters and setters
    public int getIdAnalyse() {
        return idAnalyse;
    }

    public void setIdAnalyse(int idAnalyse) {
        this.idAnalyse = idAnalyse;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
}
