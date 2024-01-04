package polytech.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

@Entity
@Table(name = "Pratique")
public class Pratique implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "numero_secu_medecin", foreignKey = @ForeignKey(name = "fk_prat_medecin"))
    private Medecin medecin;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_analyse", foreignKey = @ForeignKey(name = "fk_prat_analyse"))
    private Analyse analyse;
    
    public static ArrayList<Integer> getAllMedecinPratique(SessionFactory sessionFactory, int idAnalyse) {
    	ArrayList<Integer> medecinsPratiquant = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            // Utiliser HQL (Hibernate Query Language) pour obtenir les numéros de médecin
            String hql = "SELECT p.medecin.numeroSecuMedecin FROM Pratique p WHERE p.analyse.idAnalyse = :idAnalyse";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("idAnalyse", idAnalyse);
            medecinsPratiquant = (ArrayList<Integer>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return medecinsPratiquant;
    }

    // Getters and setters
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pratique pratique = (Pratique) o;

        return Objects.equals(medecin, pratique.medecin) &&
                Objects.equals(analyse, pratique.analyse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medecin, analyse);
    }
}
