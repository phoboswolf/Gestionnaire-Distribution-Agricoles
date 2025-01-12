package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import exceptions.InvalidRouteException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import modele.Vehicule;
import modele.Commande;
import modele.Tournee;
import utility.ControllersUtils;
import utility.DateManager;
import utility.UserAuth;
import validForm.FormAddTourValidator;
import validator.ValidateurTournee;

/**
 * Contrôleur permettant l'ajout d'une Tournée.
 */
public class AddTourCtrl extends AbstractConnCtrl implements Initializable {

    @FXML
    private TextField tourLabelField;

    @FXML
    private ChoiceBox<Vehicule> vehicleChoiceBox;

    @FXML
    private ListView<Commande> commListView;

    @FXML
    private ChoiceBox<Commande> commChoiceBox;

    @FXML
    private Button addCommBtn;

    @FXML
    private Button remCommBtn;

    @FXML
    private Label maxWeightLabel;

    @FXML
    private Label startLabel;

    @FXML
    private Label endLabel;

    @FXML
    private Label datetimeLabel;

    @FXML
    private Text formErrorText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vehicleChoiceBox.setConverter(new StringConverter<Vehicule>() {

            @Override
            public Vehicule fromString(String arg0) {
                return null;
            } // fromString

            @Override
            public String toString(Vehicule arg0) {
                if (arg0 == null) {
                    return "";
                } // if
                return arg0.getLibelle();
            } // toString
        } // StringConverter
        ); // setConverter

        commChoiceBox.setConverter(new StringConverter<Commande>() {

            @Override
            public Commande fromString(String arg0) {
                return null;
            } // fromString

            @Override
            public String toString(Commande arg0) {
                if (arg0 == null) {
                    return "";
                } // if
                return arg0.getLibelle();
            } // toString
        } // StringConverter<Commande>
        ); // setConverter

        commListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Commande row, boolean empty) {
                super.updateItem(row, empty);
                setText(empty ? null : row.getLibelle() + " | " + row.getHoraireDebut() + " | " + row.getHoraireFin());
            }
        } // updateItem
        ); // setCellFactory

        commListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Commande>() {

            @Override
            public void changed(ObservableValue<? extends Commande> arg0, Commande arg1, Commande arg2) {
                if (commListView.getItems().size() > 0) {
                    remCommBtn.setDisable(false);
                } // if
                else {
                    remCommBtn.setDisable(true);
                } // else
            } // changed
        } // ChangeListener<Commande>
        ); // addListener

        vehicleChoiceBox.getItems().addAll(UserAuth.getProd().getVehicules());
        commChoiceBox.getItems().addAll(
                UserAuth.getProd().getCommandes().stream().filter(commande -> commande.getTournee() == null).toList());

        commChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Commande>() {

            @Override
            public void changed(ObservableValue<? extends Commande> arg0, Commande arg1, Commande arg2) {
                addCommBtn.setDisable(false);
            } // changed

        } // ChangeListener<Commande>
        ); // addListener


        maxWeightLabel.setText("0");
    } // initialize

    /**
     * Ajoute une Commande, sélectionnée dans commChoiceBox, dans commListView.
     * 
     * @param event
     */
    public void addComm(ActionEvent event) {
        Commande comm = commChoiceBox.getSelectionModel().getSelectedItem();
        ArrayList<Commande> commsList = new ArrayList<>(commListView.getItems());
        commsList.add(comm);

        Timestamp[] horaires;

        try {
            horaires = ValidateurTournee.calculTournee(commsList, UserAuth.getProd().getGpsProd());
        } catch (IOException | InterruptedException | InvalidRouteException e) {
            commsList.remove(comm);
            return;
        } // try/catch

        commListView.getItems().add(comm);

        commChoiceBox.getSelectionModel().clearSelection();
        commChoiceBox.getItems().remove(comm);
        addCommBtn.setDisable(true);

        // On conserve uniquement les Commandes dont les horaires de fin sont après
        // l'horaire d'arrivée effectif de la dernière Commande
        List<Commande> newComms = commChoiceBox.getItems().stream()
                .filter(c -> c.getHoraireFin().compareTo(horaires[1]) > 0).toList();
        commChoiceBox.getItems().clear();
        commChoiceBox.getItems().addAll(newComms);

        changeLabel(Float.parseFloat(maxWeightLabel.getText()) + comm.getPoids(), horaires[0], horaires[1],
                horaires[0]);
    } // addComm

    /**
     * Retire une Commande, sélectionnée dans commListView, de commChoiceBox.
     * 
     * @param event ActionEvent
     */
    public void remComm(ActionEvent event) {
        Commande commDel = commListView.getSelectionModel().getSelectedItem(); // commande sélectionnée

        float poids = commDel.getPoids();
        commListView.getItems().remove(commDel);
        commChoiceBox.getItems().clear();

        List<Commande> commsDispo = new ArrayList<>(
                UserAuth.getProd().getCommandes().stream().filter(commande -> commande.getTournee() == null).toList()
        ); // ArrayList<Commande>
        // Toutes les Commandes disponibles de l'utilisateur

        if (commListView.getItems().size() == 0) {
            startLabel.setText("");
            endLabel.setText("");
            datetimeLabel.setText("");
            maxWeightLabel.setText("0");
        } // if
        else {
            Timestamp[] horaires;

            try {
                horaires = ValidateurTournee.calculTournee(new ArrayList<>(commListView.getItems()),
                        UserAuth.getProd().getGpsProd());
            } catch (IOException | InterruptedException | InvalidRouteException e) {
                return;
            } // try/catch

            changeLabel(Float.parseFloat(maxWeightLabel.getText()) - poids, horaires[0], horaires[1],
                    horaires[0]);

            // On conserve uniquement les Commandes dont les horaires de fin sont après
            // l'horaire d'arrivée effectif de la dernière Commande
            commsDispo = commsDispo.stream()
                    .filter(c -> c.getHoraireFin().compareTo(horaires[1]) > 0 && !commListView.getItems().contains(c))
                    .toList();
        } // else

        commChoiceBox.getItems().addAll(commsDispo);
    } // remComm

    /**
     * Méthode qui valide l'ajout d'une Tournée.
     * 
     * @param event ActionEvent
     */
    public void validateAddTour(ActionEvent event) {
        FormAddTourValidator fatv = new FormAddTourValidator(tourLabelField.getText(), UserAuth.getProd(),
                vehicleChoiceBox.getSelectionModel().getSelectedItem(),
                new ArrayList<Commande>(commListView.getItems()), maxWeightLabel.getText());
        if (fatv.isValid()) {
            Tournee maTournee = new Tournee(
                    fatv.getHeureDebut(),
                    fatv.getHeureFin(),
                    Float.parseFloat(maxWeightLabel.getText()),
                    tourLabelField.getText(),
                    vehicleChoiceBox.getSelectionModel().getSelectedItem()); // maTournee
            commListView.getItems().forEach(cmd -> maTournee.addCommande(cmd));
            tDAO.add(maTournee);

            ControllersUtils.closePopupAndUpdateParent(event);
        } // if
        else {
            formErrorText.setVisible(true);
            formErrorText.setText(fatv.getErrors());
        } // else
    } // validateAddTour

    /**
     * Méthode qui permet de fermer la vue d'ajout d'une Tournée.
     * 
     * @param event ActionEvent
     */
    public void cancelAddTour(ActionEvent event) {
        ControllersUtils.closePopupAndUpdateParent(event);
    } // cancelAddTour

    /**
     * Permet de modifier les labels maxWeightLabel, startLabel,
     * endLabel et datetimeLabel en fonction des commandes ajoutées
     * dans la ListView commListView.
     *
     * @see addComm
     * @see remComm
     * @param weight    Float
     * @param startTime Timestamp
     * @param endTime   Timestamp
     * @param dateTime  Timestamp
     */
    public void changeLabel(Float weight, Timestamp startTime, Timestamp endTime, Timestamp dateTime) {
        maxWeightLabel.setText(Float.toString(weight));
        startLabel.setText(DateManager.TimestampToHourString(startTime));
        endLabel.setText(DateManager.TimestampToHourString(endTime));
        datetimeLabel.setText(DateManager.TimestampToDateString(dateTime));
    } // changeLabel

} // AddTourCtrl