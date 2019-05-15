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
import java.io.IOException;
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

}
