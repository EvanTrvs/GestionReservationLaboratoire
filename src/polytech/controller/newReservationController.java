package polytech.controller;

import java.io.IOException;
import java.util.ArrayList;
import org.hibernate.SessionFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import polytech.model.Analyse;
import polytech.model.HibernateUtil;

/**
 * This class is a Controller, match with CRUDemployee.FXML <br/>
 * Use in Central Application as GUI for CRUD staff <br/>
 * GUI representation of every employee's informations in a scene for Reading, Updating or Deletion
 */
public class newReservationController {
	
	private GridPanController parent;

	@FXML
	private TextField id;
	
	@FXML
	private TextField prenom;
	
	@FXML
	private TextField nom;
	
	@FXML
	private ChoiceBox<String> motifBox;
	
	/****************************  CONSTRUCTOR  *******************************/
	
	public newReservationController(GridPanController parent) {
		this.parent = parent;
	}

	public void initialize() {
		// Obtenez la session factory (assurez-vous que vous avez une instance de la session factory)
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Obtenez toutes les analyses depuis la base de données
        ArrayList<Analyse> analyses = Analyse.getAllAnalyse(sessionFactory);

        // Ajoutez les libellés au ChoiceBox
        for (Analyse analyse : analyses) {
            motifBox.getItems().add(analyse.getIdAnalyse() + " - " + analyse.getLibelle());
        }
        
        // Valeur par defaut
        motifBox.setValue("Choisir un motif de consultation");
    }
	
	/******************************  METHODS  *********************************/
	
	public void dateButton(ActionEvent event) throws IOException {
		String num_secu = id.getText();
		String nom_patient = nom.getText();
		String prenom_patient = prenom.getText();
		String choix_motif = motifBox.getValue();

		// Vérifier que les champs sont remplis
        if (num_secu.isEmpty() || nom_patient.isEmpty() || prenom_patient.isEmpty() || choix_motif == null || choix_motif.isEmpty() || choix_motif == "Choisir un motif de consultation") {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }
        
        // Séparer la chaîne en deux parties : id_analyse et libelle
        String[] parts = choix_motif.split(" - ");
        int idAnalyse = Integer.parseInt(parts[0]);
        
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/selectDate.fxml"));
		selectDateController controller = new selectDateController(num_secu, nom_patient, prenom_patient, choix_motif, idAnalyse ,parent);

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
