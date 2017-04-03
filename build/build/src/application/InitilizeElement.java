package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;

public class InitilizeElement implements Initializable {

	@FXML
	Button exitApp;
	@FXML
	Label testError;
	@FXML
	Label appError;
	@FXML
	TextField testName;
	@FXML
	private Accordion accord;
	@FXML
	private TitledPane pane1,pane2;
	@FXML
	public ComboBox<String> combobox;
	ObservableList<String> list=FXCollections.observableArrayList("ILD","SPIN","IL-Desktop");
	@FXML
	ToggleButton nextPane;
	@FXML
	Button RunTest;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		combobox.setItems(list);
		accord.setExpandedPane(pane1);
		nextPane.setOnAction(e->{
			accord.setExpandedPane(pane2);
//			testError=new Label();
//			appError=new Label();
		});
	
		RunTest.setOnAction(e->{
			if(testName.getText().isEmpty()) {
				testError.setText("TEST NAME IS EMPTY!!!!!!!!!");
			}else {
				testError.setText("");
			}
				
		if(combobox.getValue()==null) {
			appError.setText("APPLICATION SELECT!!!!!!!!!");
		}else {
			appError.setText("");
		}
				RunTest.setDisable(true);
		});
		
		exitApp.setOnAction(e->{
			System.exit(0);
		});
	}
	
}
