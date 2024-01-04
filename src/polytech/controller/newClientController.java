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
import polytech.model.VigenereCipher;

/**
 * This class is a Controller, match with CRUDemployee.FXML <br/>
 * Use in Central Application as GUI for CRUD staff <br/>
 * GUI representation of every employee's informations in a scene for Reading, Updating or Deletion
 */
public class newClientController {
	
	private GridPanController parent;

	@FXML
	private TextField id;
	
	@FXML
	private TextField prenom;
	
	@FXML
	private TextField nom;
	
	@FXML
	private TextField password;
	
	@FXML
	private TextField password1;
	
	/****************************  CONSTRUCTOR  *******************************/
	
	public newClientController(GridPanController parent) {
		this.parent = parent;
	}

	public void initialize() {
	
    }
	
	/******************************  METHODS  *********************************/
	
	public void connexionButton(ActionEvent event) throws IOException {
		String num_secu = id.getText();
		String nom_patient = nom.getText();
		String prenom_patient = prenom.getText();
		String pass1 = password.getText();
		String pass2 = password1.getText();
		
		// Vérifier que les champs sont remplis
		if (num_secu.isEmpty() || nom_patient.isEmpty() || prenom_patient.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
		    System.out.println("Tous les champs doivent être remplis.");
		    return;
		}
		
		// Vérifier que les champs sont remplis
		if (!pass1.equals(pass2)) {
			System.out.println("Les mots de passe doivent être les même.");
			return;
		}
        
		Integer secu = Integer.valueOf(num_secu);
		
        if (Patient.getByNumeroSecu((int)secu) != null) {
        	System.out.println("Numero de securite social deja existant.");
		    return;
        }
        
        if(secu == 0) {
        	System.out.println("Numero de securite social ne doit pas être nul.");
		    return;
        }
        
    	Patient.createNewPatient(secu, nom_patient, prenom_patient, VigenereCipher.encrypt(pass1, secu));
    	
    	System.out.println("Patient "+ secu +"crée.");

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/Home.fxml"));
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
