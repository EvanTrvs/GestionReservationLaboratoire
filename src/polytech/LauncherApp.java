/**
 * 
 */
package polytech;

import polytech.controller.*;
import polytech.model.HibernateUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LauncherApp extends Application {
	
	public static void main(String[] args) {
		HibernateUtil.createTablesIfNotExist();
		launch(args);
	}
	
	/**
	 * Process when Central Application interface is shutdown, save and disconnect
	 */
	@Override
	public void stop() {
		System.out.println("Application ferme");
	}

	/**
	 * Launched method, initialize and set GUI with FXML views and controllers for
	 * first scene
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Platform.setImplicitExit(true);

			primaryStage.setTitle("Application Client Laboratoire");
			GridPanController parentContainer = new GridPanController();

			FXMLLoader leftMenuLoader = new FXMLLoader(
					getClass().getResource("/polytech/view/LeftMenu.fxml"));
			LeftMenuController leftMenuController = new LeftMenuController();
			leftMenuLoader.setController(leftMenuController);
			leftMenuController.setParent(parentContainer);

			FXMLLoader home = new FXMLLoader(getClass().getResource("/polytech/view/Home.fxml"));

			VBox leftMenuRoot = leftMenuLoader.load();
			VBox workSessionRoot = home.load();

			GridPane.setColumnIndex(leftMenuRoot, 0);
			GridPane.setColumnIndex(workSessionRoot, 1);

			parentContainer.setHgap(10);
			parentContainer.getChildren().add(leftMenuRoot);
			parentContainer.getChildren().add(workSessionRoot);
			Scene scene = new Scene(parentContainer, 920, 600);

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
