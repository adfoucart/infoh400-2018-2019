/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.view;

import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import ulb.lisa.his.model.Practitioner;

/**
 *
 * @author Adrien Foucart
 */
public class PractitionerComboBoxModel extends AbstractListModel implements MutableComboBoxModel {
    
    private final List<Practitioner> practitioners;
    private Practitioner selected;

    public PractitionerComboBoxModel(List<Practitioner> practitioners) {
        this.practitioners = practitioners;
        selected = null;
    }

    @Override
    public int getSize() {
        return practitioners.size();
    }

    @Override
    public Object getElementAt(int index) {
        return practitioners.get(index);
    }

    @Override
    public void addElement(Object item) {
        practitioners.add((Practitioner) item);
    }

    @Override
    public void removeElement(Object obj) {
        practitioners.remove(obj);
    }

    @Override
    public void insertElementAt(Object item, int index) {
        practitioners.add(index, (Practitioner) item);
    }

    @Override
    public void removeElementAt(int index) {
        practitioners.remove(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selected = (Practitioner) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }
    
    
    
}
