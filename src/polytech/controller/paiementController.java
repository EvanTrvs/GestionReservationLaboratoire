package polytech.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import polytech.model.HibernateUtil;
import polytech.model.Reservation;

/**
 * This class is a Controller, match with CRUDemployee.FXML <br/>
 * Use in Central Application as GUI for CRUD staff <br/>
 * GUI representation of every employee's informations in a scene for Reading, Updating or Deletion
 */
public class paiementController {
	
	private GridPanController parent;

	@FXML
	private TextField id;
	
	@FXML
	private TextField cout;
	
	@FXML
	private TextField consul;
	
	@FXML
	private TextField nom;
	
	@FXML
	private TextField code;
	
	@FXML
	private TextField exp;
	
	@FXML
	private TextField crypt;

	Reservation reservation;
	
	/****************************  CONSTRUCTOR  *******************************/
	
	public paiementController(Reservation reservation, GridPanController parent) {
		this.parent = parent;
		this.reservation = reservation;
	}

	public void initialize() {
        
		cout.setText(String.valueOf(reservation.getPrix()));
		cout.setEditable(false);
		
		consul.setText(String.valueOf(reservation.getIdReservation()));
		consul.setEditable(false);
    }
	
	/******************************  METHODS  *********************************/
	
	public void validButton(ActionEvent event) throws IOException {
		
		String num_secu = id.getText();
		String nomCB = nom.getText();
		String codeCB = code.getText();
		String expCB = exp.getText();
		String cryptCB = crypt.getText();
		
		// Vérifier que les champs sont remplis
		if (num_secu.isEmpty() || nomCB.isEmpty() || codeCB.isEmpty() || expCB.isEmpty() || cryptCB.isEmpty()) {
		    System.out.println("Tous les champs doivent être remplis.");
		    return;
		}
        
        if (Integer.valueOf(num_secu) != reservation.getPatient().getNumeroSecu()) {
        	System.out.println("Numero de patient invalide");
		    return;
        }
        
    	System.out.println("Informations de Paiement Valide, transmission au service de paiement tierce");
    	
    	Reservation.setReglementToTrue(HibernateUtil.getSessionFactory(), reservation.getIdReservation());

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/myReservation.fxml"));
		myReservationController controller = new myReservationController(reservation.getPatient().getNumeroSecu() ,parent);

	    loader.setController(controller);
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
