package controllers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import modele.Producteur;
import utility.ControllersUtils;
import validForm.FormAddProdValidator;
import validForm.FormProdValidator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur permettant l'ajout d'un Producteur.
 */
public class AddProdCtrl extends AbstractConnCtrl implements Initializable {

    @FXML
    private TextField prodSiretField;
    @FXML
    private TextField propNameField;
    @FXML
    private TextField addressNumField;
    @FXML
    private ChoiceBox<String> pathTypeChoiceBox;
    @FXML
    private TextField pathNameField;
    @FXML
    private TextField townNameField;
    @FXML
    private TextField postcodeField;
    @FXML
    private TextField prodPhoneField;
    @FXML
    private TextField prodPasswordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Text formErrorText;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        formErrorText.setVisible(false);
        ObservableList<String> listePath = FXCollections.observableArrayList();
        listePath.add("Rue");
        listePath.add("Boulevard");
        listePath.add("Avenue");
        listePath.add("Allée");
        listePath.add("Chemin");
        listePath.add("Route");
        listePath.add("Impasse");
        listePath.add("Lieu Dit");
        listePath.add("Place");
        pathTypeChoiceBox.setItems(listePath);
    } // initialize

    /**
     * Méthode qui valide l'ajout d'un Producteur.
     * 
     * @param event ActionEvent
     */
    public void validateAddProd(ActionEvent event) {
        FormProdValidator fapc = new FormAddProdValidator(prodSiretField.getText(), propNameField.getText(),
                addressNumField.getText(), pathTypeChoiceBox.getValue(), pathNameField.getText(),
                townNameField.getText(), postcodeField.getText(), prodPhoneField.getText(), prodPasswordField.getText(),
                confirmPasswordField.getText());

        if (fapc.isValid()) {
            formErrorText.setVisible(false);
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
            String hashedPs = argon2.hash(2, 15 * 1024, 1, prodPasswordField.getText().toCharArray());

            pDAO.add(new Producteur(
                    prodSiretField.getText(),
                    propNameField.getText(),
                    fapc.getAdresseCSV(),
                    prodPhoneField.getText(),
                    fapc.getCoordsXY(),
                    hashedPs
                    ) // Producteur
            ); // add
            ControllersUtils.closePopupAndUpdateParent(event);
        } //if
        else {
            formErrorText.setText(fapc.getErrors());
            formErrorText.setVisible(true);
        } // else
    } //validateAddProd

    /**
     * Méthode qui permet de fermer la vue d'ajout d'un Producteur.
     * 
     * @param event ActionEvent
     */
    public void cancelAddProd(ActionEvent event) {
        ControllersUtils.closePopupAndUpdateParent(event);
    } // cancelAddProd

} // AddProdCtrl