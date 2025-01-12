-- Création de la base de données
DROP DATABASE IF EXISTS GDATest;
CREATE DATABASE IF NOT EXISTS GDATest;
USE GDATest;
SET GLOBAL SQL_MODE='ALLOW_INVALID_DATES';

#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Producteur
#------------------------------------------------------------

CREATE TABLE Producteur(
                           idProducteur Int UNSIGNED  Auto_increment  NOT NULL ,
                           proprietaire Varchar (50) NOT NULL ,
                           adresseProd  Varchar (50) NOT NULL ,
                           numTelProd   Varchar (12) NOT NULL ,
                           gpsProd      Char (35) NOT NULL ,
                           mdpProd      Char (161) NOT NULL ,
                           siret        Varchar (14) NOT NULL
    ,CONSTRAINT Producteur_AK UNIQUE (siret)
    ,CONSTRAINT Producteur_PK PRIMARY KEY (idProducteur)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Vehicule
#------------------------------------------------------------

CREATE TABLE Vehicule(
                         idVehicule   Int UNSIGNED  Auto_increment  NOT NULL ,
                         poidsMax     Float UNSIGNED NOT NULL ,
                         libelle      Varchar (50) NOT NULL ,
                         numImmat     Char (10) NOT NULL ,
                         idProducteur Int UNSIGNED NOT NULL
    ,CONSTRAINT Vehicule_AK UNIQUE (numImmat)
    ,CONSTRAINT Vehicule_PK PRIMARY KEY (idVehicule)

    ,CONSTRAINT Vehicule_Producteur_FK FOREIGN KEY (idProducteur) REFERENCES Producteur(idProducteur) ON DELETE CASCADE
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Tournee
#------------------------------------------------------------

CREATE TABLE Tournee(
                        idTournee    Int UNSIGNED  Auto_increment  NOT NULL ,
                        horaireDebut TimeStamp NOT NULL ,
                        horaireFin   TimeStamp NOT NULL DEFAULT '1970-01-01 01:01:00',
                        poids        Float UNSIGNED NOT NULL ,
                        libelle      Varchar (50) NOT NULL ,
                        idVehicule   Int UNSIGNED
    ,CONSTRAINT Tournee_PK PRIMARY KEY (idTournee)

    ,CONSTRAINT Tournee_Vehicule_FK FOREIGN KEY (idVehicule) REFERENCES Vehicule(idVehicule) ON DELETE SET NULL
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Administrateur
#------------------------------------------------------------

CREATE TABLE Administrateur(
                               idAdministrateur Int UNSIGNED  Auto_increment  NOT NULL ,
                               mdpAdmin         Char (161) NOT NULL ,
                               pseudo           Varchar (50) NOT NULL
    ,CONSTRAINT Administrateur_AK UNIQUE (pseudo)
    ,CONSTRAINT Administrateur_PK PRIMARY KEY (idAdministrateur)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Client
#------------------------------------------------------------

CREATE TABLE Client(
                       idClient      Int UNSIGNED  Auto_increment  NOT NULL ,
                       nomClient     Varchar (50) NOT NULL ,
                       adresseClient Varchar (50) NOT NULL ,
                       gpsClient     Char (35) NOT NULL ,
                       numTelClient  Varchar (12) NOT NULL
    ,CONSTRAINT Client_PK PRIMARY KEY (idClient)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Commande
#------------------------------------------------------------

CREATE TABLE Commande(
                         idCommande   Int UNSIGNED  Auto_increment  NOT NULL ,
                         libelle      Varchar (50) NOT NULL ,
                         poids        Float UNSIGNED NOT NULL ,
                         horaireDebut TimeStamp NOT NULL ,
                         horaireFin   TimeStamp NOT NULL DEFAULT '1970-01-01 01:01:00',
                         idTournee    Int UNSIGNED ,
                         idProducteur Int UNSIGNED NOT NULL ,
                         idClient     Int UNSIGNED NOT NULL
    ,CONSTRAINT Commande_PK PRIMARY KEY (idCommande)

    ,CONSTRAINT Commande_Tournee_FK FOREIGN KEY (idTournee) REFERENCES Tournee(idTournee) ON DELETE SET NULL
    ,CONSTRAINT Commande_Producteur0_FK FOREIGN KEY (idProducteur) REFERENCES Producteur(idProducteur) ON DELETE CASCADE
    ,CONSTRAINT Commande_Client1_FK FOREIGN KEY (idClient) REFERENCES Client(idClient) ON DELETE CASCADE
)ENGINE=InnoDB;

-- Insertion de l'administrateur initial
INSERT INTO Administrateur (mdpAdmin, pseudo) VALUES ('$argon2id$v=19$m=15360,t=2,p=1$KNa1ygG8o5PFZCSyggBdzIX7cSXR04F/PXdU8MgnZLs$umKBNIGL0JjZILuugYsh9IasEdCiSxzM+2712pHf0qkKYjEqZt1Nz2cdYF9oET+UIITizo48va17oL01tZTaDw', 'Admin');

-- Insertion du producteur initial
INSERT INTO Producteur (proprietaire, adresseProd, numTelProd, gpsProd, mdpProd, siret) VALUES ('Prod', '8,Rue,de Bordeaux,Tours,37000', '0678364728', '0.6582871,47.3727869', '$argon2id$v=19$m=15360,t=2,p=1$KNa1ygG8o5PFZCSyggBdzIX7cSXR04F/PXdU8MgnZLs$umKBNIGL0JjZILuugYsh9IasEdCiSxzM+2712pHf0qkKYjEqZt1Nz2cdYF9oET+UIITizo48va17oL01tZTaDw', '12345678900001');

#------------------------------------------------------------
# Table: users -- used for test/database
#------------------------------------------------------------

CREATE TABLE `users` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(256) NOT NULL,
    `email` varchar(355) NOT NULL,
    `password` varchar(256) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Création de l'utilisateur et paramétrage des droits
DROP USER IF EXISTS 'GDATest'@'localhost';
CREATE USER 'GDATest'@'localhost' IDENTIFIED BY '1234';
GRANT ALL ON *.* TO 'GDATest'@'localhost';
FLUSH PRIVILEGES;