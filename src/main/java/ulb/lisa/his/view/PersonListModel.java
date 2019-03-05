/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.view;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Adrien Foucart
 */
public class PersonListModel<T> extends AbstractListModel {
    List<T> persons = null;
    
    public PersonListModel(List p){
        persons = p;
    }
    
    public void setPersons(List p){
        persons = p;
    }
    
    @Override
    public int getSize() {
        return persons.size();
    }

    @Override
    public Object getElementAt(int index) {
        return persons.get(index);
    }
    
    public List<T> getAll(){
        return persons;
    }
}
