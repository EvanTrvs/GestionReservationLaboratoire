package polytech.controller;


import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import polytech.model.HibernateUtil;
import polytech.model.Reservation;

/**
 * This class is a Controller, match with employees.FXML <br/>
 * Use in Central Application as GUI for Staff management <br/>
 * GUI representation of every employee in a table with severals informations
 */
public class myReservationController {
	
	private int Idpatient;
	
	private GridPanController parent;
	
	public ObservableList<Reservation> list;

	@FXML
	private TableView<Reservation> tableView;

	@FXML
	private TableColumn<Reservation, Integer> idColumn;

	@FXML
	private TableColumn<Reservation, String> dateColumn;

	@FXML
	private TableColumn<Reservation, String> analyseColumn;
	
	@FXML
	private TableColumn<Reservation, String> medColumn;

	@FXML
	private TableColumn<Reservation, Double> prix;
	
	@FXML
	private TableColumn<Reservation, String> statut;
	
	@FXML
	private TableColumn<Reservation, String> paiement;
	
	/****************************  CONSTRUCTOR  *******************************/

	/**
	 * Constructor with objects from model (non FXML objects)
	 * @param appli Main singleton object
	 * @param parent interface origin scene
	 */
	public myReservationController(Integer Idpatient, GridPanController parent) {
		this.parent = parent;
		this.Idpatient = Idpatient;
	}

	/**
	 * Initialize GUI, associate controller elements with view elements
	 */
	@FXML
	public void initialize() {
		
		list = FXCollections.observableList(Reservation.getReservationsByPatient(HibernateUtil.getSessionFactory(), Idpatient));

		// Associer chaque colonne à un attribut de la classe Reservation
		idColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("idReservation"));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty( cellData.getValue().getDateReservationAsString() ));
        analyseColumn.setCellValueFactory(cellData -> new SimpleStringProperty("(" + cellData.getValue().getAnalyse().getIdAnalyse() + ")" + cellData.getValue().getAnalyse().getLibelle() ));
        medColumn.setCellValueFactory(cellData -> new SimpleStringProperty("(" + cellData.getValue().getMedecin().getNumeroSecuMedecin() + ")" + cellData.getValue().getMedecin().getNom() ));
        prix.setCellValueFactory(new PropertyValueFactory<Reservation, Double>("prix"));
        statut.setCellValueFactory(cellData -> {
        	if (cellData.getValue().isReglement() == true) {
    		    return new SimpleStringProperty("Payé");
    		    }
    		else {
    		   	return new SimpleStringProperty("Impayé");
    		}
        } );
		
        paiement.setCellFactory(col -> {
            TableCell<Reservation, String> cell = new TableCell<Reservation, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                    } else {
                        setText("   Payer");
                        setStyle("-fx-border-style: solid inside;"
                                + "-fx-border-width: 2;"
                                + "-fx-border-radius: 5;"
                                + "-fx-border-color: gray;");

                        // Vérifier si isReglement est true, et désactiver le clic si nécessaire
                        Reservation reservation = getTableView().getItems().get(getIndex());
                        if (reservation.isReglement()) {
                            setDisable(true);
                            setStyle("-fx-opacity: 0.5;"); // Facultatif : Pour indiquer visuellement que le bouton est désactivé
                        } else {
                            setDisable(false);
                        }
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                // Ne rien faire si isReglement est true
                Reservation reservation = cell.getTableView().getItems().get(cell.getIndex());
                if (!reservation.isReglement()) {
                    try {
                        this.openPaiementRes(reservation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return cell;
        });
		
		tableView.setItems(list);
	}
	
	/******************************  METHODS  *********************************/
	
	/**
	 * Button function for adding employee to central application
	 * @param event Button click
	 * @throws IOException fxml or employee creation failure
	 */
	public void addRes(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/newReservation.fxml"));
	    newReservationController controlleur = new newReservationController(parent);

	    loader.setController(controlleur);
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}

	/**
	 * Employee creation method <br/>
	 * Open new scene for new employee purpose 
	 * @param scene Current interface scene
	 * @param empl New employee object
	 * @param isnew True if it is a employee creation (current case)
	 * @throws IOException fxml failure
	 */
	public void openPaiementRes(Reservation reservation) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/polytech/view/paiement.fxml"));
		paiementController controlleur = new paiementController(reservation, parent);

	    loader.setController(controlleur);
	    VBox employeesRoot = loader.load();
	    parent.setRight(employeesRoot);
	}
	
	/*************************  GETTER AND SETTER  ****************************/
	
	public GridPane getParent() {
		return parent;
	}

	public void setParent(GridPanController parent) {
		this.parent = parent;
	}
}