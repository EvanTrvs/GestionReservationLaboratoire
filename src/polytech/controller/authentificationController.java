package polytech.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import polytech.model.Patient;
import polytech.model.VigenereCipher;

/**
 * This class is a Controller, match with CRUDemployee.FXML <br/>
 * Use in Central Application as GUI for CRUD staff <br/>
 * GUI representation of every employee's informations in a scene for Reading, Updating or Deletion
 */
public class authentificationController {
	
	private GridPanController parent;

	@FXML
	private TextField id;
	
	@FXML
	private TextField password;
	
	
	/****************************  CONSTRUCTOR  *******************************/
	
	public authentificationController(GridPanController parent) {
		this.parent = parent;
	}

	public void initialize() {
		
	}
	
	/******************************  METHODS  *********************************/
	
	public void connexionButton(ActionEvent event) throws IOException {
		String num_secu = id.getText();
		String pass = password.getText();

		// Vérifier que les deux champs sont remplis
        if (num_secu.isEmpty() || pass.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }
		
        Integer secu = Integer.valueOf(num_secu);
        
        Patient patient = Patient.getByNumeroSecu(secu);
		
        if (patient == null) {
        	System.out.println("Aucun patient trouvé avec ce numéro de sécurité sociale.");
            return;
        }

        // Vérifier si le mot de passe correspond
        if (!patient.getMotDePasse().equals(VigenereCipher.encrypt(pass, secu) )) {
        	System.out.println("Mot de passe incorrect.");
            return;
        }
        
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/myReservation.fxml"));
		myReservationController controller = new myReservationController(secu ,parent);

	    loader.setController(controller);
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
