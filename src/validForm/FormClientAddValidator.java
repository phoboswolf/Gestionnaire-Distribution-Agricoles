package validForm;

import exceptions.AdresseInvalideException;
import validator.ValidateurAdresse;
import validator.ValidateurDonnee;

/**
 * Validateur de formulaire pour AddClientCtrl
 * @see controllers.AddClientCtrl
 */
public class FormClientAddValidator extends FormValidator {

    public String adresseCsv;
    public String coordsXY;

    /**
     * Constructeur de FormClientAddValidator,
     * permet de changer les champs d'adresse en une adresse au format CSV
     * et génère les coordonnées GPS
     * @param clientName Nom du Client récupéré dans la vue
     * @param addressNumField Numéro de voie du Client récupéré dans la vue
     * @param addressPathType Type de voie du Client récupéré dans la vue
     * @param addressPathName Nom de voie du Client récupéré dans la vue
     * @param addressTownName Nom de ville du Client récupéré dans la vue
     * @param addressPostCode Code postal du Client récupéré dans la vue
     * @param clientPhone Numéro de téléphone du Client récupéré dans la vue
     */
    public FormClientAddValidator(String clientName,
                                  String addressNumField,
                                  String addressPathType,
                                  String addressPathName,
                                  String addressTownName,
                                  String addressPostCode,
                                  String clientPhone) {
        if (clientName.equals("")) {
            setInvalid("Veuillez entrer le nom du Client");
        }
        if (!ValidateurDonnee.valideTelephone(clientPhone)) {
            setInvalid("Le numéro de téléphone n'est pas valide");
        }
        if (addressPathType == null) {
            setInvalid("Veuillez sélectionner un type de voie");
            return;
        }
        if (addressNumField.equals("") && (!addressPathType.equals("Lieu Dit"))) {
            setInvalid("Veuillez entrer un numéro de voie");
            return;
        }
        if (addressPathName.equals("")) {
            setInvalid("Veuillez entrer une voie");
            return;
        }
        if (addressTownName.equals("")) {
            setInvalid("Veuillez entrer le nom de la ville");
            return;
        }
        if (addressPostCode.equals("")) {
            setInvalid("Veuillez entrer un code postal");
            return;
        }
        ValidateurAdresse validadresse;
        try {
            validadresse = ValidateurAdresse.create(addressNumField, addressPathType,
                addressPathName, addressTownName, addressPostCode);
            adresseCsv = validadresse.csv();
            coordsXY = validadresse.getCoordXY();
        } catch (NumberFormatException | AdresseInvalideException e) {
            setInvalid("L'adresse n'est pas valide");
        }
    }

    public String getAdresseCsv() {
        return adresseCsv;
    }

    public String getCoordsXY() {
        return coordsXY;
    }
}