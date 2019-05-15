/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.util;

import be.fedict.commons.eid.client.BeIDCard;
import ulb.lisa.his.model.Person;
import be.fedict.commons.eid.client.BeIDCards;
import be.fedict.commons.eid.client.CancelledException;
import be.fedict.commons.eid.client.FileType;
import be.fedict.commons.eid.consumer.Gender;
import be.fedict.commons.eid.consumer.Identity;
import be.fedict.commons.eid.consumer.tlv.TlvParser;
import be.fedict.commons.eid.jca.BeIDProvider;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;

/**
 *
 * @author Adrien Foucart
 */
public class BeID {

    private final BeIDCards cards;
    private static BeID instance;

    public static BeID getInstance() {
        if (instance == null) {
            instance = new BeID();
        }

        return instance;
    }

    private BeID() {
        cards = new BeIDCards();
    }

    public boolean hasTerminal() {
        return cards.hasCardTerminals();
    }

    public boolean hasCard() {
        return cards.hasBeIDCards();
    }

    public Person read() {
        try {
            BeIDCard card = cards.getOneBeIDCard();
            byte[] idData = card.readFile(FileType.Identity);
            byte[] photo = card.readFile(FileType.Photo);
            Identity id = TlvParser.parse(idData, Identity.class);
            
            Person p = new Person();
            p.setNameFamily(id.getName());
            p.setNameGiven(id.getFirstName());
            p.setBirthdate(id.getDateOfBirth().getTime());
            p.setNationalNumber(id.getNationalNumber());
            if( id.getGender().equals(Gender.MALE))
                p.setGender("M");
            else if( id.getGender().equals(Gender.FEMALE))
                p.setGender("F");
            p.setActive(true);
            p.setPhoto(photo);

            return p;
        } catch (CardException | IOException | InterruptedException | CancelledException ex) {
            Logger.getLogger(BeID.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public boolean authenticate(Person p){
        try {
            BeIDCard card = cards.getOneBeIDCard();
            
            // Check that the person corresponds to the eID:
            byte[] idData = card.readFile(FileType.Identity);
            Identity id = TlvParser.parse(idData, Identity.class);
            if( !id.getNationalNumber().equals(p.getNationalNumber()) ) 
                return false;
            
            // Check the authentication certificate:
            Security.addProvider(new BeIDProvider());
            try {
                // Get KeyStore
                KeyStore ks = KeyStore.getInstance("BeID");
                ks.load(null);
                
                // Prepare access to authentication Private Key (Note that the key is not read here yet)
                PrivateKey pk = (PrivateKey) ks.getKey("Authentication", null);

                // Generate 64bits random challenge
                byte[] challenge = SecureRandom.getInstance("SHA1PRNG").generateSeed(64); 

                // Prepare challenge signature
                Signature sig = Signature.getInstance("SHA1withRSA");
                sig.initSign(pk);
                sig.update(challenge);
                
                // Sign challenge: 
                // IT'S ONLY HERE THAT THE PIN CODE DIALOG IS SHOWN, AND THE PRIVATE KEY IS USED.
                byte[] sigValue = sig.sign();
                
                // Get Certificate to check validity of signature
                Certificate cert = ks.getCertificate("Authentication");
                
                // Prepare signature verification (using same algorithm as before)
                Signature verifSig = Signature.getInstance("SHA1withRSA");
                verifSig.initVerify(cert);
                verifSig.update(challenge);
                
                // Verifiy signature
                return verifSig.verify(sigValue);
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException | InvalidKeyException | SignatureException ex) {
                Logger.getLogger(BeID.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            
        } catch (CardException | IOException | InterruptedException | CancelledException ex) {
            Logger.getLogger(BeID.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
