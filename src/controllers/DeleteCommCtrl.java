package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import modele.Commande;
import utility.ControllersUtils;
import validForm.FormDeleteCommand;
import validForm.FormValidator;

/**
 * Contrôleur permettant la suppression d'une Commande.
 */
public class DeleteCommCtrl extends AbstractConnCtrl implements Initializable {

    @FXML
    private Text commLabelText;
    @FXML
    private Text deleteErrorText;

    private static Commande commande;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        commLabelText.setText(commande.getLibelle());
    } // initialize

    /**
     * Méthode qui valide la suppression de la Commande.
     * 
     * @param event ActionEvent
     */
    public void validateDeleteComm(ActionEvent event) {
        FormValidator formulaire = new FormDeleteCommand(commande);

        if (formulaire.isValid()) {
            commDAO.delete(commande);
            ControllersUtils.closePopupAndUpdateParent(event);
        } // if
        else {
            deleteErrorText.setVisible(true);
            deleteErrorText.setText(formulaire.getErrors());
        } // else
    } // validateDeleteComm

    /**
     * Méthode qui annule la suppression de la Commande.
     * 
     * @param event ActionEvent
     */
    public void cancelDeleteComm(ActionEvent event) {
        ControllersUtils.closePopupAndUpdateParent(event);
    } // cancelDeleteComm

    /**
     * Méthode qui récupère la Commande sélectionnée dans la listView
     * de la vue précédente (prodSelectMenu)
     * 
     * @param comm Commande
     */
    public static void setCommande(Commande comm) {
        commande = comm;
    } // setCommande

} // DeleteCommCtrl