/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.GUI;

import Deepspace.ShieldToUI;
import Deepspace.SpaceStationToUI;
import Deepspace.WeaponToUI;
import java.awt.Component;
import java.util.ArrayList;

/**
 *
 * @author diagmatrix
 */
public class StationToGUI extends javax.swing.JPanel {

    private ArrayList<WeaponToUI> _weapons = new ArrayList<>();
    private ArrayList<ShieldToUI> _shields = new ArrayList<>();
    private WeaponToGUI w = new WeaponToGUI();
    private ShieldBoosterToGUI s = new ShieldBoosterToGUI();
    private DamageToGUI d = new DamageToGUI();
    private HangarToGUI h = new HangarToGUI();
    
    /**
     * Creates new form StationToGUI
     */
    public StationToGUI() {
        initComponents();
        damage.add(d);
        weapons.add(w);
        shields.add(s);
        hangar.add(h);
    }
    
    public void setStationToGUI(SpaceStationToUI sp) {
        name.setText(sp.getName());
        power.setText(Float.toString(sp.getAmmoPower()));
        shield.setText(Float.toString(sp.getShieldPower()));
        medals.setText(Integer.toString(sp.getnMedals()));
        fuel.setText(Float.toString(sp.getFuelUnits()));
        _weapons = sp.getWeapons();
        _shields = sp.getShieldBoosters();
        
        weapons.removeAll();
        for (WeaponToUI wui: _weapons) {
            w.setWeaponToGUI(wui);
            weapons.add(w);
        }
        
        shields.removeAll();
        for (ShieldToUI sui: _shields) {
            s.setShieldBoosterToGUI(sui);
            shields.add(s);
        }
        
        hangar.removeAll();
        if (sp.getHangar()!=null) {
            h.setHangarToGUI(sp.getHangar());
            hangar.add(h);
            hangar.setVisible(true);
        } else
            hangar.setVisible(false);
        
        damage.removeAll();
        if (sp.getPendingDamage()!=null) {
            d.setDamageToGUI(sp.getPendingDamage());
            damage.add(d);
            damage.setVisible(true);
        } else
            damage.setVisible(false);
        
        repaint();
        revalidate();
    }
    
        public ArrayList<Integer> getSelectedWeapons() {
        ArrayList<Integer> aux = new ArrayList<>();
        int i = 0;
        for (Component c: weapons.getComponents()) {
            if (((CombatItems) c).isSelected())
                aux.add(i);
            i++;
        }
        return aux;
    }
    
    public ArrayList<Integer> getSelectedShields() {
        ArrayList<Integer> aux = new ArrayList<>();
        int i = 0;
        for (Component c: shields.getComponents()) {
            if (((CombatItems) c).isSelected())
                aux.add(i);
            i++;
        }
        return aux;
    }
    
    public HangarToGUI getHangar() { return h; }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        medals = new javax.swing.JLabel();
        power = new javax.swing.JLabel();
        shield = new javax.swing.JLabel();
        fuel = new javax.swing.JLabel();
        damage = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        shields = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        weapons = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        hangar = new javax.swing.JPanel();

        jLabel1.setText("Nombre:");

        jLabel2.setText("Medallas:");

        jLabel3.setText("Poder de disparo:");

        jLabel4.setText("Poder de escudo:");

        jLabel5.setText("Combustible:");

        name.setText("name");

        medals.setText("medals");

        power.setText("p");

        shield.setText("s");

        fuel.setText("fuel");

        damage.setBorder(javax.swing.BorderFactory.createTitledBorder("Daño"));

        shields.setBorder(javax.swing.BorderFactory.createTitledBorder("Escudos"));
        jScrollPane1.setViewportView(shields);

        weapons.setBorder(javax.swing.BorderFactory.createTitledBorder("Armas"));
        jScrollPane2.setViewportView(weapons);

        jScrollPane3.setViewportView(hangar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(medals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fuel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(power, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(shield, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(damage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(damage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(medals))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(power))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(shield))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(fuel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel damage;
    private javax.swing.JLabel fuel;
    private javax.swing.JPanel hangar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel medals;
    private javax.swing.JLabel name;
    private javax.swing.JLabel power;
    private javax.swing.JLabel shield;
    private javax.swing.JPanel shields;
    private javax.swing.JPanel weapons;
    // End of variables declaration//GEN-END:variables
}