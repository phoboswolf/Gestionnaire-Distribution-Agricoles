package validator;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette classe permet de valider la cohérence des entrées fournies.
 * Il s'agit de s'assurer que les données seront exploitables et homogènes.
 */
public class ValidateurDonnee {

    private static Pattern pattern;
    private static Matcher matcher;

    /*
     * Permet de valider le SIRET, une chaîne de 14 chiffres.
     * 
     * @param siret Le SIRET sous forme de String.
     */
    public static boolean valideSiret(String siret) {
        // Une suite de 14 chiffres uniquement.
        pattern = Pattern.compile("^[0-9]{14}$");
        matcher = pattern.matcher(siret);

        // Si la regex match, true.
        return matcher.matches();
    } // valideSiret

    /*
     * Assure que le poids sous forme de String puisse être converti en double.
     * 
     * @param poids Le poids sous forme de String.
     */
    public static boolean validePoids(String poids) {

        // On vérifie que le poids fourni puisse être parsé.
        try {
            Float.parseFloat(poids);
        } catch (NumberFormatException e) {
            return false;
        } // try/catch

        // Le poids est bien parsable.
        return true;
    } // validePoids

    /*
     * Permet de valider le poids avec une contrainte de poids max.
     * On utilise validePoids pour vérifier qu'on puisse bien parser.
     *
     * @param poids Le poids sous forme de string.
     * 
     * @param max Le poids max en double.
     * 
     * @see validePoids
     */
    public static boolean validePoids(String poids, float max) {
        // Si non parsable, ou poids > max: false, sinon true.
        return validePoids(poids) && !(Float.parseFloat(poids) > max);
    } // validePoids

    /**
     * Vérifie une date.
     * Si elle est sous format LocalDate, on pourra la parser.
     * <p>
     * 
     * @param date LocalDate La date saisie.
     * @return bool
     */
    public static boolean valideDate(LocalDate date) {
        return date != null;
    } // validateDate

    /**
     * Assure qu'une heure est sous le bon format
     * <p>
     * 
     * @param heure L'heure saisie.
     * @return bool
     */
    public static boolean valideHeure(String heure) {
        pattern = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
        matcher = pattern.matcher(heure);

        return matcher.matches();
    } // valideHeure

    /*
     * Permet de valider un numéro de téléphone francais, +33 compris.
     * 
     * @param telephone Le numéro de téléphone sous forme de String.
     */
    public static boolean valideTelephone(String telephone) {
        pattern = Pattern.compile("^(0[0-9]|\\+33[0-9])([0-9][0-9]){4}$");
        matcher = pattern.matcher(telephone);

        // Si la regex match
        return matcher.matches();
    } // valideTelephone

    /*
     * Permet de valider un nom.
     *
     * @param nom Le nom sous forme de String.
     * @param maxChars La taille maximale du nom.
     */
    public static boolean valideNom(String nom, int maxChars) {
        // Jean val Jean, Jam-bom-beurre, Jean, Jean'ne'mar valide.
        // Accents valides, il doit y avoir une suite apres un tiret,
        // un espace ou une apostrophe.
        pattern = Pattern.compile("^([a-zA-ZÀ-ÿ]+((-| |')[a-zA-ZÀ-ÿ]+)*)$");
        matcher = pattern.matcher(nom);

        // Si la regex match et que le nom est de la bonne taille, true.
        return matcher.matches() && nom.length() <= maxChars ? true : false;
    } // valideNom

    /*
     * Permet de valider un pseudonyme.
     *
     * @param pseudonyme Le pseudonyme sous forme de String.
     * @param maxChars La taille maximale du pseudonynme.
     */
    public static boolean validePseudonyme(String pseudonyme, int maxChars) {
        // Kaleb, K3l3b, K7, R2-D2, C3_P0 valide.
        // Kal@b invalide.
        pattern = Pattern.compile("^[0-9a-zA-Z_-]{2,}+$");
        matcher = pattern.matcher(pseudonyme);

        // Si la regex match et que le pseudo est de la bonne taille, true.
        return matcher.matches() && pseudonyme.length() <= maxChars ? true : false;
    } // validePseudonyme

    /*
     * Regex trouvée en ligne.
     * https://regex101.com/r/VGjLx1/1
     * 
     * Elle en compte les formats anciens / récents.
     * Néanmoins, ici, on demandera obligatoirement un tiret entre les groupes,
     * par soucis d'homogénéité.
     * 
     * @param immatriculation L'immatriculation sous forme de String.
     */
    public static boolean valideImmatriculation(String immatriculation) {
        String regex = "[A-HJ-NP-TV-Z]{2}[-]"
                + "[0-9]{3}[-][A-HJ-NP-TV-Z]{2}"
                + "|[0-9]{2,4}[-][A-Z]{1,3}[-][0-9]{2}";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(immatriculation);

        return matcher.matches();
    } // valideImmatriculation

    /**
     * Validateur de code postal
     * Prend en compte les numéros de département (jusqu'à 98)
     * Ne peut contenir que des chiffres
     * 
     * @param codePostal Le code postal à tester
     */
    public static boolean valideCodePostal(String codePostal) {
        String regex = "^(?:0[1-9]|[1-8]\\d|9[0-8])\\d{3}$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(codePostal);

        return matcher.matches();
    } // valideCodePostal

    /**
     * Vérifie que le mot de passe est en conformité avec les recommandations
     * minimales de l'ANSSI, à savoir une longueur d'au moins 9 caractères, au moins
     * 1 caractère spécial, 1 majuscule, 1 minuscule et 1 chiffre
     * 
     * https://www.ssi.gouv.fr/guide/recommandations-relatives-a-lauthentification-multifacteur-et-aux-mots-de-passe/
     * 
     * @param password Le mot de passe de l'utilisateur
     * @return La validité du mot de passe avec les recommandations minimales de
     *         l'ANSSI
     */
    public static boolean validePassword(String password) {
        return password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{9,}$");
    } // validePassword

} // ValidateurDonnee