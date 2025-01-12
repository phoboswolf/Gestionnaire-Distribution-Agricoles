package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import modele.Administrateur;
import modele.Client;
import modele.Producteur;
import utility.ControllersUtils;

/**
 * Contrôleur permettant la consultation par l'Administrateur des Administrateurs, Producteurs et Clients.
 */
public class AdminSelectMenuCtrl extends AbstractConnCtrl implements Initializable {
    @FXML
    private Label adminLoginLabel;

    @FXML
    private Button adminProfileBtn;

    @FXML
    private Button addAdminBtn;

    @FXML
    private ListView<Administrateur> adminListView;

    @FXML
    private Button addProdBtn;

    @FXML
    private Button modifyProdBtn;

    @FXML
    private Button deleteProdBtn;

    @FXML
    private ListView<Producteur> prodListView;

    @FXML
    private Button addClientBtn;

    @FXML
    private Button modifyClientBtn;

    @FXML
    private Button deleteClientBtn;

    @FXML
    private ListView<Client> clientListView;

    private ControllersUtils util;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        this.util = new ControllersUtils();
        modifyClientBtn.setDisable(false);
        deleteClientBtn.setDisable(false);
        modifyProdBtn.setDisable(false);
        deleteProdBtn.setDisable(false);

        // Affichage du libellé uniquement sur le listView.
        adminListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Administrateur row, boolean empty) {
                super.updateItem(row, empty);
                setText(empty ? null : row.getPseudo());
            }
        } // updateItem
        ); // setCellFactory
        prodListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Producteur row, boolean empty) {
                super.updateItem(row, empty);
                setText(empty ? null : row.getProprietaire());
            }
        } // updateItem
        ); // setCellFactory
        clientListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Client row, boolean empty) {
                super.updateItem(row, empty);
                setText(empty ? null : row.getNomClient());
            }
        } // updateItem
        ); // setCellFactory

        prodListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Producteur>() {
            @Override
            public void changed(ObservableValue<? extends Producteur> arg0, Producteur arg1, Producteur arg2) {
                // TODO Auto-generated method stub$
                if (prodListView.getItems().size() > 0) {
                    modifyProdBtn.setDisable(false);
                    deleteProdBtn.setDisable(false);
                } // if
                else {
                    modifyProdBtn.setDisable(true);
                    deleteProdBtn.setDisable(true);
                } // else
            } // changed
        } // ChangeListener<Producteur>
        ); // addListener

        clientListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> arg0, Client arg1, Client arg2) {
                // TODO Auto-generated method stub$
                if (clientListView.getItems().size() > 0) {
                    modifyClientBtn.setDisable(false);
                    deleteClientBtn.setDisable(false);
                } // if
                else {
                    modifyClientBtn.setDisable(true);
                    deleteClientBtn.setDisable(true);
                } // else
            } // changed
        } // ChangeListener<Client>
        ); // addListener

        // Si une pop-up est close.
        ControllersUtils.getStage().setOnCloseRequest(
                event -> {
                    reloadListViews();
                } // event
        ); // setOnCloseRequest

        loadListViews();
    } // initialize

    /**
     * Reload les listViews.
     */
    private void reloadListViews() {
        clearListViews();
        loadListViews();
    } // reloadListViews

    /**
     * Clear les listViews.
     */
    private void clearListViews() {
        adminListView.getItems().clear();
        prodListView.getItems().clear();
        clientListView.getItems().clear();
    } // clearListViews

    /**
     * Charge les ListViews
     */
    private void loadListViews() {
        adminListView.getItems().addAll(aDAO.getAll());
        prodListView.getItems().addAll(pDAO.getAll());
        clientListView.getItems().addAll(cltDAO.getAll());
    } // loadListViews

    /**
     * Méthode qui ouvre une pop-up affichant le profil de l'utilisateur.
     * 
     * @param event MouseEvent
     */
    public void userProfile(ActionEvent event) {
        util.loadPopup(event, "/views/adminProfile.fxml");
    } // userProfile

    /**
     * Méthode qui déconnecte l'utilisateur et redirige vers la première vue.
     * 
     * @param event ActionEvent
     */
    public void deconnection(ActionEvent event) {
        util.loadView(event, "/views/homePage.fxml");
    } // deconnection

    /**
     * Méthode qui ouvre une pop-up pour l'ajout d'un Administrateur.
     * 
     * @param event ActionEvent
     */
    public void popupAddAdmin(ActionEvent event) {
        util.loadPopup(event, "/views/addAdmin.fxml");
    } // popupAddAdmin

    /**
     * Méthode qui ouvre une pop-up pour l'ajout d'un Producteur.
     * 
     * @param event ActionEvent
     */
    public void popupAddProd(ActionEvent event) {
        util.loadPopup(event, "/views/addProd.fxml");
    } // popupAddProd

    /**
     * Méthode qui ouvre une pop-up pour la modification d'un Producteur.
     * 
     * @param event ActionEvent
     */
    public void popupModifyProd(ActionEvent event) {
        if (!prodListView.getSelectionModel().isEmpty()) {
            ModifyProdCtrl.setProd(prodListView.getSelectionModel().getSelectedItem());
            util.loadPopup(event, "/views/modifyProd.fxml");
        } // if
    } // popupModifyProd

    /**
     * Méthode qui ouvre une pop-up pour demander la confirmation
     * de la suppression d'un Producteur.
     * 
     * @param event ActionEvent
     */
    public void popupDeleteProd(ActionEvent event) {
        if (!prodListView.getSelectionModel().isEmpty()) {
            DeleteProdCtrl.setProd(prodListView.getSelectionModel().getSelectedItem());
            util.loadPopup(event, "/views/deleteProd.fxml");
        } // if
    } // popupDeleteProd

    /**
     * Méthode qui ouvre une pop-up permettant de
     * consulter un Producteur double-cliqué dans la ListView prodListView.
     * 
     * @param event MouseEvent
     */
    public void popupConsultProd(MouseEvent event) {
        if (event.getClickCount() >= 2 && !prodListView.getSelectionModel().isEmpty()) {
            ConsultProdCtrl.setProd(prodListView.getSelectionModel().getSelectedItem());
            util.loadPopup(event, "/views/consultProd.fxml");
        } // if
    } // popupConsultProd

    /**
     * Méthode qui ouvre une pop-up pour l'ajout d'un Client.
     * 
     * @param event ActionEvent
     */
    public void popupAddClient(ActionEvent event) {
        util.loadPopup(event, "/views/addClient.fxml");
    } // popupAddClient

    /**
     * Méthode qui ouvre une pop-up pour la modification d'un Client.
     * 
     * @param event ActionEvent
     */
    public void popupModifyClient(ActionEvent event) {
        if (!clientListView.getSelectionModel().isEmpty()) {
            ModifyClientCtrl.setClient(clientListView.getSelectionModel().getSelectedItem());
            util.loadPopup(event, "/views/modifyClient.fxml");
        } // if
    } // popupModifyClient

    /**
     * Méthode qui ouvre une pop-up pour demander la confirmation
     * de la suppression d'un Client.
     * 
     * @param event ActionEvent
     */
    public void popupDeleteClient(ActionEvent event) {
        if (!clientListView.getSelectionModel().isEmpty()) {
            DeleteClientCtrl.setClient(clientListView.getSelectionModel().getSelectedItem());
            util.loadPopup(event, "/views/deleteClient.fxml");
        } // if
    } // popupDeleteClient

    /**
     * Méthode qui ouvre une pop-up permettant de
     * consulter un Client double-cliqué dans la ListView clientListView.
     * 
     * @param event MouseEvent
     */
    public void popupConsultClient(MouseEvent event) {
        if (event.getClickCount() >= 2 && !clientListView.getSelectionModel().isEmpty()) {
            ConsultClientCtrl.setClient(clientListView.getSelectionModel().getSelectedItem());
            util.loadPopup(event, "/views/consultClient.fxml");
        } // if
    } // popupConsultClient

} // AdminSelectMenuCtrl