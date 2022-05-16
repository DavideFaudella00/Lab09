
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	private ComboBox<Country> cmbNazione;

	@FXML
	void doCalcolaConfini(ActionEvent event) {
		txtResult.clear();
		int anno;
		try {
			anno = Integer.parseInt(txtAnno.getText());
			if ((anno < 1816) || (anno > 2016)) {
				txtResult.setText("Inserire un anno nell'intervallo 1816 - 2016");
				return;
			}
		} catch (NumberFormatException e) {
			txtResult.setText("Inserire un anno nell'intervallo 1816 - 2016");
			return;
		}
		model.creaGrafo(anno);
		List<Country> countries = model.getCountries();
		cmbNazione.getItems().addAll(countries);
		txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents()));
		txtResult.appendText(model.getV());
	}

	@FXML
	void doTrovaRaggiungibili(ActionEvent event) {
		txtResult.clear();
		Country start = cmbNazione.getSelectionModel().getSelectedItem();
		List<Country> l = model.doIteratore(start);
		txtResult.appendText(l.toString());
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
		assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Borders.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
