package polytech.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import polytech.model.HibernateUtil;

/**
 * This class is a Controller, match with LeftMenu.FXML <br/>
 * Use in Central Application as GUI for navigation throw scenes and functionalities <br/>
 * GUI representation of a menu button bar for Application navigation
 */
public class LeftMenuController {

	private GridPanController parent;
	
	@FXML
	private AnchorPane anchorp;

	/****************************  CONSTRUCTOR  *******************************/

	/**
	 * Initialize GUI, associate controller elements with view elements
	 */
	@FXML
	public void initialize() {
		anchorp.setStyle("-fx-background-color: lightblue;");
	}
	
	/******************************  METHODS  *********************************/
	
	/**
	 * Button function for Home scene access
	 * @param event Button click
	 * @throws IOException deletion failure
	 */
	public void homeButton(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/Home.fxml"));
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}
	
	/**
	 * Button function students access
	 * @param event Button click
	 * @throws IOException deletion failure
	 */
	public void newResButton(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/newReservation.fxml"));
	    newReservationController controlleur = new newReservationController(parent);

	    loader.setController(controlleur);
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}
	
	/**
	 * Button function students access
	 * @param event Button click
	 * @throws IOException deletion failure
	 */
	public void myResButton(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/authentification.fxml"));
	    authentificationController controller = new authentificationController(parent);

	    loader.setController(controller);
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}
	
	/**
	 * Button function students access
	 * @param event Button click
	 * @throws IOException deletion failure
	 */
	public void newCliButton(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/newClient.fxml"));
	    newClientController controlleur = new newClientController(parent);

	    loader.setController(controlleur);
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}
	
	/**
	 * Button function for reset data to a preset fictive data
	 * @param event Button click
	 * @throws IOException deletion failure
	 */
	public void resetButton(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/Home.fxml"));
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	    
	    HibernateUtil.dropAllTables();
	  	HibernateUtil.createTablesIfNotExist();
	  	HibernateUtil.insertPatientData();
	  	HibernateUtil.insertMedecinData();
	  	HibernateUtil.insertAnalyseData();
	  	HibernateUtil.insertPratiqueData();
	  	HibernateUtil.insertReservationData();
	}
	
	/*************************  GETTER AND SETTER  ****************************/

	public GridPanController getParent() {
		return parent;
	}

	public void setParent(GridPanController parent) {
		this.parent = parent;
	}
}