package polytech.controller;

import java.util.Iterator;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Interface controller class, only for GUI purpose
 * Permit to launch central Application with split view, left for menu button (LeftMenu), right for displayed scene
 */
public class GridPanController extends GridPane {
	
	/******************************  METHODS  *********************************/

	/**
	 * Empty interface right side
	 */
	public void setEmpty() {
		int columnIndex = 1;

		Iterator<Node> iterator = this.getChildren().iterator();
		
		while (iterator.hasNext()) {
			Node child = iterator.next();
			Integer childColumnIndex = GridPane.getColumnIndex(child);
			
			if (childColumnIndex != null && childColumnIndex == columnIndex) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Set interface right side
	 */
	public void setRight(VBox elem)
	{
		setEmpty();
	    GridPane.setColumnIndex(elem, 1);
	    this.getChildren().add(elem);
	}
}
