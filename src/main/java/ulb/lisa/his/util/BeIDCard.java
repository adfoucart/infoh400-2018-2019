/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.util;

import ulb.lisa.his.model.Person;

/**
 *
 * @author Adrien Foucart
 */
public class BeIDCard {
    
    private static BeIDCard instance;
    
    public static BeIDCard getInstance(){
        if( instance == null ){
            instance = new BeIDCard();
        }
        
        return instance;
    }
    
    private BeIDCard() {
        
    }

    public boolean hasCard() {
        // PLACEHOLDER
        return false;
    }
        
    public Person read(){
        // PLACEHOLDER       
        return new Person();
    }
    
}
