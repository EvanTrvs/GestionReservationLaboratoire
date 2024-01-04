package polytech.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.SessionFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import polytech.model.Analyse;
import polytech.model.HibernateUtil;
import polytech.model.Medecin;
import polytech.model.Patient;
import polytech.model.Reservation;

/**
 * This class is a Controller, match with CRUDemployee.FXML <br/>
 * Use in Central Application as GUI for CRUD staff <br/>
 * GUI representation of every employee's informations in a scene for Reading, Updating or Deletion
 */
public class selectDateController {
	
	private GridPanController parent;

	@FXML
	private TextField id;
	
	@FXML
	private TextField prenom;
	
	@FXML
	private TextField nom;
	
	@FXML
	private TextField motif;
	
	@FXML
	private TextField cout;
	
	@FXML
	private ChoiceBox<String> dateBox;
	
	String num_secu;
	
	String nom_patient;

	String prenom_patient;
	
	String choix_motif;
	
	int idAnalyse;
	
	/****************************  CONSTRUCTOR  *******************************/
	
	public selectDateController(String num_secu, String nom_patient, String prenom_patient, String choix_motif, int idAnalyse, GridPanController parent) {
		this.parent = parent;
		this.num_secu = num_secu;
		this.nom_patient = nom_patient;
		this.prenom_patient = prenom_patient;
		this.choix_motif = choix_motif;
		this.idAnalyse = idAnalyse;
	}

	public void initialize() {
		
		// Obtenez la session factory (assurez-vous que vous avez une instance de la session factory)
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        
		motif.setText(choix_motif);
		motif.setEditable(false);
		
		id.setText(num_secu);
		id.setEditable(false);
		
		prenom.setText(prenom_patient);
		prenom.setEditable(false);
		
		nom.setText(nom_patient);
		nom.setEditable(false);
		
		cout.setText(String.valueOf(Analyse.getCout(sessionFactory, idAnalyse))  );
		cout.setEditable(false);
		
		LocalDate currentDate = LocalDate.now().plusDays(1);
		
		HashMap<Integer, ArrayList<String>> map = null;
		
		while (map == null) {
			map = Reservation.getAvailableDateByMedecin(sessionFactory, idAnalyse, currentDate);
			
			int counter = 0;
			for (HashMap.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
	            counter += 1;
	        }
			if (counter == 0) {
				map = null;
			}
		}
		
		for (HashMap.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
            int entier = entry.getKey();
            ArrayList<String> listeDeChaines = entry.getValue();
            
            Medecin med = Medecin.getMed(sessionFactory, entier);
            
            for ( String dateR : listeDeChaines) {
            	dateBox.getItems().add(dateR + ", Docteur (" + entier + ")" +  med.getNom());
            }
        }
        
        // Valeur par defaut
        dateBox.setValue("Choisir une date de consultation");
    }
	
	/******************************  METHODS  *********************************/
	
	public void validButton(ActionEvent event) throws IOException {
		
		String date = dateBox.getValue();

		// Vérifier que les champs sont remplis
        if (date == null || date.isEmpty() || date=="Choisir une date de consultation") {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }
        
        // Séparer la chaîne en deux parties
        String[] parts = date.split(",");
        String dateConsul = parts[0];
        
        String[] parts2 = parts[1].split("\\D+");
        int num_medecin = Integer.parseInt(parts2[1]);
        
        Integer secu = Integer.valueOf(num_secu);
        Patient patient = Patient.getByNumeroSecu(secu);
		
        if (patient == null) {
        	System.out.println("Aucun patient trouve avec ce numero de securite sociale. Veuillez creer un compte");
            return;
        }
        
        if (!prenom_patient.equals(patient.getNom()) || !nom_patient.equals(patient.getPrenom())) {
        	System.out.println("Les informations renseigne ne corresponde pas avec ce numero de securite sociale.");
        	return;
        }
        
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        
		Reservation.newRes(sessionFactory, LocalTime.now().getSecond()+secu, dateConsul, false, num_medecin, idAnalyse, secu);
        
    	System.out.println("Reservation prise avec succes : " + nom_patient + " " + prenom_patient + " le " + dateConsul + " pour " + choix_motif);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/Home.fxml"));
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}

	/**
	 * Button function for cancel and exit employee consulting/editing
	 * @param event Button click
	 * @throws IOException fxml failure
	 */
	public void cancelButton(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/newReservation.fxml"));
	    newReservationController controlleur = new newReservationController(parent);

	    loader.setController(controlleur);
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}

	/*************************  GETTER AND SETTER  ****************************/

	public GridPanController getParent() {
		return parent;
	}

	public void setParent(GridPanController parent) {
		this.parent = parent;
	}
}
