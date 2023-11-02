
package vistas;

import static dao.CiudadData.listarCiudades;
import dao.*;
import excepciones.*;
import java.awt.event.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import models.*;


public class Menu2 extends javax.swing.JInternalFrame {
  
    
    
    // Usados en el menu para cargar ciudades, provincias e paises
    private String vistaCiudadPais = ""; 
    private String vistaCiudadProvincia = "";

    // Usados en el menu para cargar pasajes
    private String vistaPasajeOrigen = "";
    private String vistaPasajeDestino = "";
    private String vistaPasajeTransporte = "";
    private String vistaPasajeImporteStr = ""; 
    private double vistaPasajeImporte;

    // Usados en el menu para cargar paquetes
    private LocalDate temporadaAlta;
    private String vistaPaqueteOrigen = "";
    private String vistaPaqueteDestino = "";
    private String vistaPaqueteEstablecimiento = "";
    
    
    
    private String pasaje = "";
    
    public Menu2() {
        
        initComponents();        
 
        cargarElementosVistaCiudad();
        
        cargarElementosVistaPasaje();
        
        cargarElementosVistaPaquete();
 
        this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
        ui.setNorthPane(null);
    }
    

    //**************************************************************************    
    // MENU PARA CARGAR CIUDADES

    //Menu Cargar ciudades
    private void cargarProvincias() {
        
        jCBVistaCiudadProvincia.removeAllItems();  
        jCBVistaCiudadProvincia.addItem("");
    
        Set<Provincia> provincias = ProvinciaData.listarProvincias();
        
        for (Provincia provincia : provincias) {
            jCBVistaCiudadProvincia.addItem(provincia.getNombre());
        }

        jCBVistaCiudadProvincia.addItem("Nueva Provincia");   
    }
    
    // Menu Cargar ciudades
    private void cargarPaises() {
        
        jCBVistaCiudadPais.removeAllItems();  
        jCBVistaCiudadPais.addItem("");
        
        Set<Pais> paises = PaisData.listarPaises();
        
        for (Pais pais : paises) {
            jCBVistaCiudadPais.addItem(pais.getNombre());
        }
        jCBVistaCiudadPais.addItem("Nuevo Pais");   
    } 

    // Menu Cargar ciudades
    private void activarBotonCargarCiudad() {   
        jTVistaCiudadCiudad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarEstadoBoton();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarEstadoBoton();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarEstadoBoton();
            }

            private void actualizarEstadoBoton() {
                if (jTVistaCiudadCiudad.getText().trim().isEmpty()) {
                    jBVistaCiudadAgregar.setEnabled(false);
                } else {
                    jBVistaCiudadAgregar.setEnabled(true);
                }
            }
        });
    }
    
    // Menu Cargar ciudades
    private void cargarElementosVistaCiudad() {
        cargarProvincias(); 
        cargarPaises();
        
        jBVistaCiudadAgregar.setEnabled(false);
        activarBotonCargarCiudad();
        
        jLNombreProvincia.setVisible(false);
        jLNombrePais.setVisible(false);
        
        jTVistaCiudadProvincia.setVisible(false);
        jTVistaCiudadPais.setVisible(false);   
    }

    //**************************************************************************
    // MENU PARA CARGAR PASAJES

    //Menu cargar pasajes
    private void cargarPasajeOrigen(){       
        jCBVistaPasajeOrigen.removeAllItems();
        jCBVistaPasajeOrigen.addItem("");
        
        Set<Ciudad> ciudades = listarCiudades();
 
        for (Ciudad ciu : ciudades) {
            if (vistaPasajeDestino.equals(ciu.getNombre())) {
                jCBVistaPasajeOrigen.addItem(ciu.getNombre());
            }
        }
    }
    
    //Menu cargar pasajes
    private void cargarPasajeDestino(String origen){      
        jCBVistaPasajeDestino.removeAllItems();
        jCBVistaPasajeDestino.addItem("");
        
        Set<Ciudad> ciudades = listarCiudades();
        
        for (Ciudad ciu : ciudades) {
            if (!origen.equals(ciu.getNombre())) {
                jCBVistaPasajeDestino.addItem(ciu.getNombre());
            }
        }  
    } 

    // Menu cargar pasaje
    private void cargarTransporte(){
        jCBVistaPasajeTransporte.removeAllItems();
        jCBVistaPasajeTransporte.addItem("");
        for (TipoTransporte tipo : TipoTransporte.values()){
             jCBVistaPasajeTransporte.addItem(tipo.getValorStr());
        } 
    }

    // Menu cargar pasajes
    private void IngresarImportePasaje(){
        try {
           vistaPasajeImporte = Double.valueOf(vistaPasajeImporteStr);
   
           if (vistaPasajeImporte <= 0 ){
               throw new excepciones.ValoresException("Valor debe ser mayor a 0");
           }           
           
           activarBotonCargarPasaje();
           
        } catch (NumberFormatException e ) {
            JOptionPane.showMessageDialog (null, "Ingrese un número valido"); 
            jTVistaPasajeImporte.setText("");
        } catch (ValoresException e) {
            JOptionPane.showMessageDialog (null, e.getMessage());       
            jTVistaPasajeImporte.setText("");
        } 
    }  
       
    // Menu cargar pasaje 
    private void activarBotonCargarPasaje() {
        if (!vistaPasajeOrigen.isBlank() && !vistaPasajeDestino.isBlank()
                && !vistaPasajeTransporte.isBlank() && vistaPasajeImporte > 0) {
            jBVistaPasajeCargar.setEnabled(true);
        }   
    }
    
    // Menu cargar pasaje 
    private void cargarElementosVistaPasaje() {
        cargarPasajeOrigen();
        cargarPasajeDestino(vistaPaqueteOrigen);
        cargarTransporte();
        jBVistaPasajeCargar.setEnabled(false);
    }
    
    //**************************************************************************
    // MENU PARA CREAR PAQUETES
    
    // Menu crear paquetes
    private void cargarCiudadOrigen(){       
        jCBVistaPaqueteOrigen.removeAllItems();  
        jCBVistaPaqueteOrigen.addItem("");

        Set<Ciudad> ciudades = listarCiudades();
   
        for (Ciudad ciu : ciudades) {
            if (!ciu.getNombre().equals(vistaPaqueteDestino)) {
                jCBVistaPaqueteOrigen.addItem(ciu.getNombre());              
            }
        }
    }
    
    // Menu crear paquetes
    private void cargarCiudadDestino(String origen){        
        jCBVistaPaqueteDestino.removeAllItems();
        jCBVistaPaqueteDestino.addItem("");
        
        Set<Ciudad> ciudades = listarCiudades();
 
        for (Ciudad ciu : ciudades) {
            if (!origen.equals(ciu.getNombre())) {
                jCBVistaPaqueteDestino.addItem(ciu.getNombre());
            }
        }
    } 
 
    // Menu crear paquetes
    private void cargarEstablecimiento() {      
        jCBVistaPaqueteEstablecimiento.removeAllItems();  
        jCBVistaPaqueteEstablecimiento.addItem("");
        
        Set<Estadia> estadias = EstadiaData.listarEstadias();
        for (Estadia est : estadias) {
            if (est.getCiudadDestino().getNombre().equals(vistaPaqueteDestino)) {
                jCBVistaPaqueteEstablecimiento.addItem(est.getEstablecimiento().getNombre());
            }
        }
    }  

    // Menu crear paquetes
    private void cargarPasaje() {       
        jCBVistaPaquetePasajeViajando.removeAllItems();
        jCBVistaPaquetePasajeViajando.addItem("");
        
        Set<Pasaje> pasajes = PasajeData.listarPasajes();
        
        for (Pasaje pasa : pasajes) {
            
            if (pasa.getCiudadDestino().getNombre().equals(vistaPaqueteDestino) && 
                    pasa.getCiudadOrigen().getNombre().equals(vistaPaqueteOrigen)) {
                jCBVistaPaquetePasajeViajando.addItem(pasa.getTipoTransporte().getValorStr());
            }
        }
    }

    // Menu crear paquetes
    private void activarBotonCrearPaquete() {   
        if (!vistaPaqueteDestino.isBlank() && !vistaPaqueteOrigen.isBlank() 
                && !vistaPaqueteEstablecimiento.isBlank() && !pasaje.isBlank()) {
            jBVistaPaqueteCrear.setEnabled(true);
        }
    }
    
    
    // Menu crear paquetes
    private void cargarElementosVistaPaquete() {        
        cargarCiudadOrigen();
        cargarCiudadDestino(vistaPaqueteOrigen);
        cargarEstablecimiento();
        jTFVistaPaquetePais.setEnabled(false);
        jTFVistaPaqueteTemporada.setEditable(false);
        jTFVistaPaqueteValorTotal.setEnabled(false);
        jBVistaPaqueteCrear.setEnabled(false);
    
    }
  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTPContenedor = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLTemporadaAlta = new javax.swing.JLabel();
        jLNombreCiudad = new javax.swing.JLabel();
        jTVistaCiudadCiudad = new javax.swing.JTextField();
        jDCVistaCiudadTemporadaAlta = new com.toedter.calendar.JDateChooser();
        jLProvincia = new javax.swing.JLabel();
        jCBVistaCiudadProvincia = new javax.swing.JComboBox<>();
        jLNombreProvincia = new javax.swing.JLabel();
        jTVistaCiudadProvincia = new javax.swing.JTextField();
        jLPais = new javax.swing.JLabel();
        jCBVistaCiudadPais = new javax.swing.JComboBox<>();
        jLNombrePais = new javax.swing.JLabel();
        jTVistaCiudadPais = new javax.swing.JTextField();
        jBVistaCiudadAgregar = new javax.swing.JButton();
        jLTitulo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTAgregarEstablec = new javax.swing.JTextField();
        jBVistaEstablecimientoAgregar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTDireccionEstablec = new javax.swing.JTextField();
        jTDTelefono = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLTitulo1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLVistaPasajeTransporte = new javax.swing.JLabel();
        jLVistaPasajeOrigen = new javax.swing.JLabel();
        jCBVistaPasajeOrigen = new javax.swing.JComboBox<>();
        jLVistaPasajeDestno = new javax.swing.JLabel();
        jCBVistaPasajeDestino = new javax.swing.JComboBox<>();
        jLVistaPasajeImporte = new javax.swing.JLabel();
        jTVistaPasajeImporte = new javax.swing.JTextField();
        jBVistaPasajeCargar = new javax.swing.JButton();
        jCBVistaPasajeTransporte = new javax.swing.JComboBox<>();
        jLTitulo2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLTitulo3 = new javax.swing.JLabel();
        jCBVistaEstadiaServicio = new javax.swing.JComboBox<>();
        jDVistaEstadiaCheckIn = new com.toedter.calendar.JDateChooser();
        jCBVistaEstadiaCiudadDestino = new javax.swing.JComboBox<>();
        jTVistaEstadiaImporteDiario = new javax.swing.JTextField();
        jCBVistaEstadiaTemporada = new javax.swing.JComboBox<>();
        jCBVistaEstadiaEstablecimiento = new javax.swing.JComboBox<>();
        jLProvincia1 = new javax.swing.JLabel();
        jLProvincia2 = new javax.swing.JLabel();
        jLProvincia3 = new javax.swing.JLabel();
        jLProvincia4 = new javax.swing.JLabel();
        jDVistaEstadiaCheckOut = new com.toedter.calendar.JDateChooser();
        jLProvincia5 = new javax.swing.JLabel();
        jLProvincia6 = new javax.swing.JLabel();
        jLProvincia7 = new javax.swing.JLabel();
        jBVistaEstadiaAgregar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLVistaPaqueteDestino = new javax.swing.JLabel();
        jLVistaPaqueteEstablecimiento = new javax.swing.JLabel();
        jLVistaPaqueteOrigen = new javax.swing.JLabel();
        jLVistaPaquetePasajeViajandoEn = new javax.swing.JLabel();
        jCBVistaPaqueteOrigen = new javax.swing.JComboBox<>();
        jCBVistaPaqueteEstablecimiento = new javax.swing.JComboBox<>();
        jCBVistaPaquetePasajeViajando = new javax.swing.JComboBox<>();
        jCBVistaPaqueteDestino = new javax.swing.JComboBox<>();
        jTFVistaPaquetePais = new javax.swing.JTextField();
        jLVistaPaqueteTemporada1 = new javax.swing.JLabel();
        jLVistaPaquetePais = new javax.swing.JLabel();
        jTFVistaPaqueteTemporada = new javax.swing.JTextField();
        jLVistaPaquetePrecio = new javax.swing.JLabel();
        jTFVistaPaqueteValorTotal = new javax.swing.JTextField();
        jBVistaPaqueteCrear = new javax.swing.JButton();
        jLTitulo4 = new javax.swing.JLabel();

        jTPContenedor.setBackground(new java.awt.Color(72, 92, 113));

        jPanel1.setBackground(new java.awt.Color(72, 92, 113));

        jLTemporadaAlta.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLTemporadaAlta.setForeground(new java.awt.Color(255, 255, 255));
        jLTemporadaAlta.setText("Temporada Alta");

        jLNombreCiudad.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLNombreCiudad.setForeground(new java.awt.Color(255, 255, 255));
        jLNombreCiudad.setText("Nombre Ciudad");

        jTVistaCiudadCiudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTVistaCiudadCiudadActionPerformed(evt);
            }
        });

        jLProvincia.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia.setText("Provincia");

        jCBVistaCiudadProvincia.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaCiudadProvincia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaCiudadProvinciaItemStateChanged(evt);
            }
        });

        jLNombreProvincia.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLNombreProvincia.setForeground(new java.awt.Color(255, 255, 255));
        jLNombreProvincia.setText("Nombre Provincia");

        jTVistaCiudadProvincia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTVistaCiudadProvinciaActionPerformed(evt);
            }
        });

        jLPais.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLPais.setForeground(new java.awt.Color(255, 255, 255));
        jLPais.setText("Pais");

        jCBVistaCiudadPais.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaCiudadPais.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaCiudadPaisItemStateChanged(evt);
            }
        });

        jLNombrePais.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLNombrePais.setForeground(new java.awt.Color(255, 255, 255));
        jLNombrePais.setText("Nombre Pais");

        jTVistaCiudadPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTVistaCiudadPaisActionPerformed(evt);
            }
        });

        jBVistaCiudadAgregar.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jBVistaCiudadAgregar.setForeground(new java.awt.Color(255, 255, 255));
        jBVistaCiudadAgregar.setText("Agregar");
        jBVistaCiudadAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVistaCiudadAgregarActionPerformed(evt);
            }
        });

        jLTitulo.setBackground(new java.awt.Color(193, 126, 48));
        jLTitulo.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLTitulo.setForeground(new java.awt.Color(193, 126, 48));
        jLTitulo.setText("Geolocalización");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLProvincia)
                            .addComponent(jLNombreCiudad)
                            .addComponent(jLPais))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCBVistaCiudadProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCBVistaCiudadPais, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTVistaCiudadCiudad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(54, 54, 54)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLNombreProvincia)
                                    .addComponent(jLTemporadaAlta)
                                    .addComponent(jLNombrePais))
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTVistaCiudadProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDCVistaCiudadTemporadaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTVistaCiudadPais, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jBVistaCiudadAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(jLTitulo)))
                .addContainerGap(619, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLTitulo)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTVistaCiudadCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLNombreCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLTemporadaAlta))
                    .addComponent(jDCVistaCiudadTemporadaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBVistaCiudadProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLProvincia)
                    .addComponent(jTVistaCiudadProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLNombreProvincia))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTVistaCiudadPais, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVistaCiudadPais, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLPais)
                    .addComponent(jLNombrePais))
                .addGap(60, 60, 60)
                .addComponent(jBVistaCiudadAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(277, Short.MAX_VALUE))
        );

        jTPContenedor.addTab("Agregar geolocalización", jPanel1);

        jPanel2.setBackground(new java.awt.Color(72, 92, 113));

        jTAgregarEstablec.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jTAgregarEstablec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTAgregarEstablecActionPerformed(evt);
            }
        });

        jBVistaEstablecimientoAgregar.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jBVistaEstablecimientoAgregar.setForeground(java.awt.Color.white);
        jBVistaEstablecimientoAgregar.setText("Agregar");
        jBVistaEstablecimientoAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVistaEstablecimientoAgregarActionPerformed(evt);
            }
        });

        jLabel1.setBackground(java.awt.Color.white);
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Nombre");

        jTDireccionEstablec.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jTDireccionEstablec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTDireccionEstablecActionPerformed(evt);
            }
        });

        jTDTelefono.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jTDTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTDTelefonoActionPerformed(evt);
            }
        });

        jLabel2.setBackground(java.awt.Color.white);
        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Dirección");

        jLabel3.setBackground(java.awt.Color.white);
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("Telefono");

        jLTitulo1.setBackground(new java.awt.Color(193, 126, 48));
        jLTitulo1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLTitulo1.setForeground(new java.awt.Color(193, 126, 48));
        jLTitulo1.setText("Agregar Establecimiento");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(384, 384, 384)
                        .addComponent(jLTitulo1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTDireccionEstablec, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addComponent(jTAgregarEstablec, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jBVistaEstablecimientoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTDTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addContainerGap(506, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitulo1)
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTAgregarEstablec, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTDireccionEstablec, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTDTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addComponent(jBVistaEstablecimientoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
        );

        jTPContenedor.addTab("Agregar Establecimiento", jPanel2);

        jPanel3.setBackground(new java.awt.Color(72, 92, 113));
        jPanel3.setForeground(java.awt.Color.white);

        jLVistaPasajeTransporte.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPasajeTransporte.setForeground(java.awt.Color.white);
        jLVistaPasajeTransporte.setText("Transporte");

        jLVistaPasajeOrigen.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPasajeOrigen.setForeground(java.awt.Color.white);
        jLVistaPasajeOrigen.setText("Ciudad Origen");

        jCBVistaPasajeOrigen.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPasajeOrigen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPasajeOrigenItemStateChanged(evt);
            }
        });
        jCBVistaPasajeOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBVistaPasajeOrigenActionPerformed(evt);
            }
        });

        jLVistaPasajeDestno.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPasajeDestno.setForeground(java.awt.Color.white);
        jLVistaPasajeDestno.setText("Ciudad Destino");

        jCBVistaPasajeDestino.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPasajeDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPasajeDestinoItemStateChanged(evt);
            }
        });
        jCBVistaPasajeDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBVistaPasajeDestinoActionPerformed(evt);
            }
        });

        jLVistaPasajeImporte.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPasajeImporte.setForeground(java.awt.Color.white);
        jLVistaPasajeImporte.setText("Importe");

        jTVistaPasajeImporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTVistaPasajeImporteActionPerformed(evt);
            }
        });
        jTVistaPasajeImporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTVistaPasajeImporteKeyPressed(evt);
            }
        });

        jBVistaPasajeCargar.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jBVistaPasajeCargar.setForeground(java.awt.Color.white);
        jBVistaPasajeCargar.setText("Cargar");
        jBVistaPasajeCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVistaPasajeCargarActionPerformed(evt);
            }
        });

        jCBVistaPasajeTransporte.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPasajeTransporte.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPasajeTransporteItemStateChanged(evt);
            }
        });
        jCBVistaPasajeTransporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBVistaPasajeTransporteActionPerformed(evt);
            }
        });

        jLTitulo2.setBackground(new java.awt.Color(193, 126, 48));
        jLTitulo2.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLTitulo2.setForeground(new java.awt.Color(193, 126, 48));
        jLTitulo2.setText("Agregar Pasaje");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLTitulo2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCBVistaPasajeDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLVistaPasajeImporte)
                                    .addComponent(jLVistaPasajeOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLVistaPasajeDestno)
                                    .addComponent(jLVistaPasajeTransporte))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(jCBVistaPasajeOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jBVistaPasajeCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jCBVistaPasajeTransporte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTVistaPasajeImporte, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(213, 213, 213)))
                .addContainerGap(758, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitulo2)
                .addGap(43, 43, 43)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPasajeOrigen)
                    .addComponent(jCBVistaPasajeOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPasajeDestno)
                    .addComponent(jCBVistaPasajeDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPasajeTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVistaPasajeTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPasajeImporte)
                    .addComponent(jTVistaPasajeImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addComponent(jBVistaPasajeCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        jTPContenedor.addTab("Agregar Pasaje", jPanel3);

        jPanel4.setBackground(new java.awt.Color(72, 92, 113));

        jLTitulo3.setBackground(new java.awt.Color(193, 126, 48));
        jLTitulo3.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLTitulo3.setForeground(new java.awt.Color(193, 126, 48));
        jLTitulo3.setText("Agregar Estadia");

        jCBVistaEstadiaServicio.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaEstadiaServicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaEstadiaServicioItemStateChanged(evt);
            }
        });

        jCBVistaEstadiaCiudadDestino.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaEstadiaCiudadDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaEstadiaCiudadDestinoItemStateChanged(evt);
            }
        });

        jTVistaEstadiaImporteDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTVistaEstadiaImporteDiarioActionPerformed(evt);
            }
        });

        jCBVistaEstadiaTemporada.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaEstadiaTemporada.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaEstadiaTemporadaItemStateChanged(evt);
            }
        });

        jCBVistaEstadiaEstablecimiento.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaEstadiaEstablecimiento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaEstadiaEstablecimientoItemStateChanged(evt);
            }
        });

        jLProvincia1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia1.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia1.setText("Check In");

        jLProvincia2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia2.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia2.setText("Importe Diario");

        jLProvincia3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia3.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia3.setText("Temporada");

        jLProvincia4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia4.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia4.setText("Establecimiento");

        jLProvincia5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia5.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia5.setText("Servicio");

        jLProvincia6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia6.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia6.setText("Check Out");

        jLProvincia7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia7.setForeground(new java.awt.Color(255, 255, 255));
        jLProvincia7.setText("Ciudad Destino");

        jBVistaEstadiaAgregar.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jBVistaEstadiaAgregar.setForeground(java.awt.Color.white);
        jBVistaEstadiaAgregar.setText("Agregar");
        jBVistaEstadiaAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVistaEstadiaAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(293, 293, 293)
                        .addComponent(jLTitulo3))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLProvincia4)
                            .addComponent(jLProvincia1)
                            .addComponent(jLProvincia6)
                            .addComponent(jLProvincia5)
                            .addComponent(jLProvincia3)
                            .addComponent(jLProvincia7)
                            .addComponent(jLProvincia2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDVistaEstadiaCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBVistaEstadiaEstablecimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDVistaEstadiaCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBVistaEstadiaServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBVistaEstadiaTemporada, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBVistaEstadiaCiudadDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTVistaEstadiaImporteDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBVistaEstadiaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(744, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitulo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCBVistaEstadiaEstablecimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLProvincia4))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jDVistaEstadiaCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(jLProvincia1)))
                                .addGap(23, 23, 23)
                                .addComponent(jDVistaEstadiaCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLProvincia6))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBVistaEstadiaServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLProvincia5))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBVistaEstadiaTemporada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLProvincia3))
                        .addGap(36, 36, 36)
                        .addComponent(jCBVistaEstadiaCiudadDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLProvincia7))
                .addGap(29, 29, 29)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTVistaEstadiaImporteDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLProvincia2))
                .addGap(32, 32, 32)
                .addComponent(jBVistaEstadiaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        jTPContenedor.addTab("Agregar Estadia", jPanel4);

        jPanel5.setBackground(new java.awt.Color(72, 92, 113));

        jLVistaPaqueteDestino.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaqueteDestino.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaqueteDestino.setText("Destino");

        jLVistaPaqueteEstablecimiento.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaqueteEstablecimiento.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaqueteEstablecimiento.setText("Alojamiento");

        jLVistaPaqueteOrigen.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaqueteOrigen.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaqueteOrigen.setText("Origen");

        jLVistaPaquetePasajeViajandoEn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaquetePasajeViajandoEn.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaquetePasajeViajandoEn.setText("Viajando en ");

        jCBVistaPaqueteOrigen.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPaqueteOrigen.setForeground(new java.awt.Color(0, 0, 0));
        jCBVistaPaqueteOrigen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPaqueteOrigenItemStateChanged(evt);
            }
        });

        jCBVistaPaqueteEstablecimiento.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPaqueteEstablecimiento.setForeground(new java.awt.Color(0, 0, 0));
        jCBVistaPaqueteEstablecimiento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPaqueteEstablecimientoItemStateChanged(evt);
            }
        });

        jCBVistaPaquetePasajeViajando.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPaquetePasajeViajando.setForeground(new java.awt.Color(0, 0, 0));
        jCBVistaPaquetePasajeViajando.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPaquetePasajeViajandoItemStateChanged(evt);
            }
        });

        jCBVistaPaqueteDestino.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jCBVistaPaqueteDestino.setForeground(new java.awt.Color(0, 0, 0));
        jCBVistaPaqueteDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBVistaPaqueteDestinoItemStateChanged(evt);
            }
        });

        jTFVistaPaquetePais.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jTFVistaPaquetePais.setForeground(new java.awt.Color(0, 0, 0));

        jLVistaPaqueteTemporada1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaqueteTemporada1.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaqueteTemporada1.setText("Temporada");

        jLVistaPaquetePais.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaquetePais.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaquetePais.setText("Pais");

        jTFVistaPaqueteTemporada.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jTFVistaPaqueteTemporada.setForeground(new java.awt.Color(0, 0, 0));

        jLVistaPaquetePrecio.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLVistaPaquetePrecio.setForeground(new java.awt.Color(255, 255, 255));
        jLVistaPaquetePrecio.setText("Valor del Paquete");

        jTFVistaPaqueteValorTotal.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jTFVistaPaqueteValorTotal.setForeground(new java.awt.Color(0, 0, 0));
        jTFVistaPaqueteValorTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFVistaPaqueteValorTotalActionPerformed(evt);
            }
        });

        jBVistaPaqueteCrear.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        jBVistaPaqueteCrear.setForeground(new java.awt.Color(255, 255, 255));
        jBVistaPaqueteCrear.setText("Crear");
        jBVistaPaqueteCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVistaPaqueteCrearActionPerformed(evt);
            }
        });

        jLTitulo4.setBackground(new java.awt.Color(193, 126, 48));
        jLTitulo4.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLTitulo4.setForeground(new java.awt.Color(193, 126, 48));
        jLTitulo4.setText("Armado de paquetes");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLVistaPaqueteDestino)
                    .addComponent(jLVistaPaqueteEstablecimiento)
                    .addComponent(jLVistaPaqueteOrigen)
                    .addComponent(jLVistaPaquetePasajeViajandoEn))
                .addGap(44, 44, 44)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBVistaPaqueteOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCBVistaPaqueteEstablecimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCBVistaPaquetePasajeViajando, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCBVistaPaqueteDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLVistaPaqueteTemporada1)
                                    .addComponent(jLVistaPaquetePrecio)
                                    .addComponent(jLVistaPaquetePais))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTFVistaPaquetePais, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTFVistaPaqueteTemporada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTFVistaPaqueteValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(621, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jBVistaPaqueteCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(jLTitulo4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitulo4)
                .addGap(43, 43, 43)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPaqueteOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVistaPaqueteOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPaqueteDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVistaPaqueteDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLVistaPaquetePais, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFVistaPaquetePais, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPaqueteEstablecimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVistaPaqueteEstablecimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLVistaPaqueteTemporada1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFVistaPaqueteTemporada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLVistaPaquetePasajeViajandoEn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVistaPaquetePasajeViajando, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLVistaPaquetePrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFVistaPaqueteValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addComponent(jBVistaPaqueteCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(231, Short.MAX_VALUE))
        );

        jTPContenedor.addTab("Armar Paquete", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTPContenedor)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTPContenedor)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTAgregarEstablecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTAgregarEstablecActionPerformed
        
    }//GEN-LAST:event_jTAgregarEstablecActionPerformed

    private void jBVistaEstablecimientoAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVistaEstablecimientoAgregarActionPerformed
       try {
            EstablecimientoData establecimientoData = new EstablecimientoData();
            String nombre = jTAgregarEstablec.getText();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo Nombre está vacío. Por favor, ingresa un Nombre de Establecimiento.");
                return;
            }

            Establecimiento establecimiento = new Establecimiento();
            establecimiento.setNombre(nombre);

            String direccion = jTDireccionEstablec.getText(); 
            String telefono = jTDTelefono.getText();   

            establecimiento.setDireccion(direccion);
            establecimiento.setTelefono(telefono);

            EstablecimientoData.cargarEstablecimiento(establecimiento);

            jTAgregarEstablec.setText("");

            jTDTelefono.setText("");
            jTDireccionEstablec.setText("");
            jTAgregarEstablec.requestFocus();
        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_jBVistaEstablecimientoAgregarActionPerformed

    private void jTDireccionEstablecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTDireccionEstablecActionPerformed
        
    }//GEN-LAST:event_jTDireccionEstablecActionPerformed

    private void jTDTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTDTelefonoActionPerformed
        
    }//GEN-LAST:event_jTDTelefonoActionPerformed

    private void jTVistaCiudadCiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTVistaCiudadCiudadActionPerformed
       
    }//GEN-LAST:event_jTVistaCiudadCiudadActionPerformed

    private void jCBVistaCiudadProvinciaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaCiudadProvinciaItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            vistaCiudadProvincia = (String) jCBVistaCiudadProvincia.getSelectedItem();
            if (vistaCiudadProvincia.equals("Nueva Provincia")) {
                jLNombreProvincia.setVisible(true);
                jTVistaCiudadProvincia.setVisible(true);
            }
        }
    }//GEN-LAST:event_jCBVistaCiudadProvinciaItemStateChanged

    private void jTVistaCiudadProvinciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTVistaCiudadProvinciaActionPerformed

    }//GEN-LAST:event_jTVistaCiudadProvinciaActionPerformed

    private void jCBVistaCiudadPaisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaCiudadPaisItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            vistaCiudadPais = (String) jCBVistaCiudadPais.getSelectedItem();
            if (vistaCiudadPais.equals("Nuevo Pais")) {
                jLNombrePais.setVisible(true);
                jTVistaCiudadPais.setVisible(true);            
            }
        }    
    }//GEN-LAST:event_jCBVistaCiudadPaisItemStateChanged
    
    private void jBVistaCiudadAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVistaCiudadAgregarActionPerformed
        Ciudad ciudadNueva = new Ciudad();
        Provincia provinciaAux = new Provincia();
        Pais paisAux = new Pais();

        Date selectedDateCheckin = jDCVistaCiudadTemporadaAlta.getDate();

        if (selectedDateCheckin != null) {
            temporadaAlta = selectedDateCheckin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        } else {
            temporadaAlta = LocalDate.of(2023,12,31);
        }

        if (!vistaCiudadProvincia.equals("Nueva Provincia")) {
            if (!vistaCiudadPais.equals("Nuevo Pais")) {
                paisAux = PaisData.buscarPaisPorNombre(vistaCiudadPais);
                provinciaAux = ProvinciaData.buscarProvinciaPorNombre(vistaCiudadProvincia);
                ciudadNueva.setPais(paisAux);
                ciudadNueva.setProvincia(provinciaAux);
                ciudadNueva.setNombre(jTVistaCiudadCiudad.getText());
                ciudadNueva.setFechaDeTemporadaAlta(temporadaAlta);
                ciudadNueva.setEstado(true);
                CiudadData.cargarCiudad(ciudadNueva);
            } else if (vistaCiudadPais.equals("Nuevo Pais")) {
                paisAux.setNombre(jTVistaCiudadPais.getText());
                PaisData.cargarPais(paisAux);
                provinciaAux = ProvinciaData.buscarProvinciaPorNombre(vistaCiudadProvincia);
                ciudadNueva.setPais(paisAux);
                ciudadNueva.setProvincia(provinciaAux);
                ciudadNueva.setNombre(jTVistaCiudadCiudad.getText());
                ciudadNueva.setFechaDeTemporadaAlta(temporadaAlta);
                ciudadNueva.setEstado(true);
                CiudadData.cargarCiudad(ciudadNueva);
            }
        } else if (!vistaCiudadPais.equals("Nuevo Pais")) {
            paisAux = PaisData.buscarPaisPorNombre(vistaCiudadPais);
            provinciaAux.setNombre(jTVistaCiudadProvincia.getText());
            provinciaAux.setPais(paisAux);
            ProvinciaData.cargarProvincia(provinciaAux);
            ciudadNueva.setNombre(jTVistaCiudadCiudad.getText());
            ciudadNueva.setProvincia(provinciaAux);
            ciudadNueva.setPais(paisAux);
            ciudadNueva.setFechaDeTemporadaAlta(temporadaAlta);
            ciudadNueva.setEstado(true);
            CiudadData.cargarCiudad(ciudadNueva);
        } else {
            paisAux.setNombre(jTVistaCiudadPais.getText());
            PaisData.cargarPais(paisAux);
            provinciaAux.setNombre(jTVistaCiudadProvincia.getText());
            provinciaAux.setPais(paisAux);
            ProvinciaData.cargarProvincia(provinciaAux);
            ciudadNueva.setNombre(jTVistaCiudadCiudad.getText());
            ciudadNueva.setProvincia(provinciaAux);
            ciudadNueva.setPais(paisAux);
            ciudadNueva.setFechaDeTemporadaAlta(temporadaAlta);
            ciudadNueva.setEstado(true);
            CiudadData.cargarCiudad(ciudadNueva);
        }

        jTVistaCiudadCiudad.setText("");

        jDCVistaCiudadTemporadaAlta.setDate(null);

        jTVistaCiudadProvincia.setText("");
        jTVistaCiudadProvincia.setVisible(false);
        jTVistaCiudadPais.setVisible(false);

        jTVistaCiudadPais.setText("");
        jLNombreProvincia.setVisible(false);
        jLNombrePais.setVisible(false);

        cargarProvincias();
        cargarPaises();
    }//GEN-LAST:event_jBVistaCiudadAgregarActionPerformed

    private void jTVistaCiudadPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTVistaCiudadPaisActionPerformed
        
    }//GEN-LAST:event_jTVistaCiudadPaisActionPerformed

    private void jCBVistaPaqueteOrigenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPaqueteOrigenItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            vistaPaqueteOrigen = (String) jCBVistaPaqueteOrigen.getSelectedItem();
            cargarCiudadDestino(vistaPaqueteOrigen);
            activarBotonCrearPaquete();
        } 
    }//GEN-LAST:event_jCBVistaPaqueteOrigenItemStateChanged

    private void jCBVistaPaqueteDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPaqueteDestinoItemStateChanged
       if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            vistaPaqueteDestino = (String) jCBVistaPaqueteDestino.getSelectedItem();
            cargarEstablecimiento();
            cargarPasaje();
            activarBotonCrearPaquete();
            Ciudad ciudadAux = CiudadData.buscarCiudadPorNombre(vistaPaqueteDestino);
            if (ciudadAux != null) {
                jTFVistaPaquetePais.setText(ciudadAux.getPais().getNombre());
            }
       } 
    }//GEN-LAST:event_jCBVistaPaqueteDestinoItemStateChanged

    private void jCBVistaPaqueteEstablecimientoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPaqueteEstablecimientoItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            vistaPaqueteEstablecimiento = (String) jCBVistaPaqueteEstablecimiento.getSelectedItem();
            Establecimiento estableAux = EstablecimientoData.buscarEstablecimientoPorNombre(vistaPaqueteEstablecimiento);
            activarBotonCrearPaquete();
            if (estableAux != null) {
                Estadia alojamientoAux = EstadiaData.buscarEstadiaPorEstablecimiento(estableAux);
                if (alojamientoAux != null) {
                    jTFVistaPaqueteTemporada.setText(alojamientoAux.getTemporada().getValorStr());        
                }            
            }
        }
    }//GEN-LAST:event_jCBVistaPaqueteEstablecimientoItemStateChanged

    private void jCBVistaPaquetePasajeViajandoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPaquetePasajeViajandoItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            pasaje = (String) jCBVistaPaquetePasajeViajando.getSelectedItem();
            activarBotonCrearPaquete();
        }
    }//GEN-LAST:event_jCBVistaPaquetePasajeViajandoItemStateChanged

    private void jBVistaPaqueteCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVistaPaqueteCrearActionPerformed
        PaqueteTuristico paqueteNuevo = new PaqueteTuristico();
        paqueteNuevo.setCiudadOrigen(CiudadData.buscarCiudadPorNombre(vistaPaqueteOrigen));
        paqueteNuevo.setCiudadDestino(CiudadData.buscarCiudadPorNombre(vistaPaqueteDestino));
        paqueteNuevo.setEstadia(EstadiaData.buscarEstadiaPorEstablecimiento(EstablecimientoData.buscarEstablecimientoPorNombre(vistaPaqueteEstablecimiento)));

        Set<Pasaje> pasajes = PasajeData.listarPasajes();

        for(Pasaje pasa : pasajes) {
            if (pasa.getCiudadDestino().getNombre().equals(vistaPaqueteDestino) && pasa.getCiudadOrigen().getNombre().equals(vistaPaqueteOrigen) &&
                    pasa.getTipoTransporte().getValorStr().equals(pasaje))
                paqueteNuevo.setPasaje(pasa);
        }

        double importePasaje = paqueteNuevo.getPasaje().getImporte();
        double importeDiario = paqueteNuevo.getEstadia().getImporteDiario();
        double incrementoPorServicio = paqueteNuevo.getEstadia().getServicio().getPorcentaje();
        double incrementoPorTemporada = paqueteNuevo.getEstadia().getTemporada().getPorcentaje();
        long cantidadDias = ChronoUnit.DAYS.between(paqueteNuevo.getEstadia().getCheckIn(), paqueteNuevo.getEstadia().getCheckOut());
        
        double valorTotal = importePasaje + (importeDiario * incrementoPorServicio * cantidadDias * incrementoPorTemporada);
        System.out.println("Calculo en la vista: ");
        System.out.println(importePasaje + " + ( " + importeDiario + " * " + incrementoPorServicio + " * " + cantidadDias + " * " + incrementoPorTemporada + " ) = " + valorTotal);
        jTFVistaPaqueteValorTotal.setText(valorTotal + "");
        paqueteNuevo.setValorTotal(valorTotal);
        paqueteNuevo.setEstado(true);
       
        PaqueteData.crearPaquete(paqueteNuevo); 
        
        jCBVistaPaqueteOrigen.removeAllItems(); 
        jCBVistaPaqueteDestino.removeAllItems();  
        jCBVistaPaqueteEstablecimiento.removeAllItems();  
        jCBVistaPaquetePasajeViajando.removeAllItems();  
        jTFVistaPaquetePais.setText("");
        jTFVistaPaqueteTemporada.setText("");
        jTFVistaPaqueteValorTotal.setText("");
        vistaPaqueteOrigen = "";
        vistaPaqueteDestino = "";
        vistaPaqueteEstablecimiento = "";
        pasaje = "";
        
        cargarElementosVistaPaquete();
        
    }//GEN-LAST:event_jBVistaPaqueteCrearActionPerformed

    private void jTFVistaPaqueteValorTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFVistaPaqueteValorTotalActionPerformed
        
    }//GEN-LAST:event_jTFVistaPaqueteValorTotalActionPerformed

    private void jCBVistaPasajeOrigenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPasajeOrigenItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            vistaPasajeOrigen = (String) jCBVistaPasajeOrigen.getSelectedItem();
            cargarPasajeDestino(vistaPasajeOrigen);
            activarBotonCargarPasaje();
        }
    }//GEN-LAST:event_jCBVistaPasajeOrigenItemStateChanged

    private void jCBVistaPasajeOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBVistaPasajeOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBVistaPasajeOrigenActionPerformed

    private void jCBVistaPasajeDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPasajeDestinoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            vistaPasajeDestino = (String) jCBVistaPasajeDestino.getSelectedItem();  
           
            activarBotonCargarPasaje();            
        } 
    }//GEN-LAST:event_jCBVistaPasajeDestinoItemStateChanged

    private void jCBVistaPasajeDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBVistaPasajeDestinoActionPerformed

    }//GEN-LAST:event_jCBVistaPasajeDestinoActionPerformed

    private void jTVistaPasajeImporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTVistaPasajeImporteActionPerformed
        vistaPasajeImporteStr = jTVistaPasajeImporte.getText();
        IngresarImportePasaje();
        

    }//GEN-LAST:event_jTVistaPasajeImporteActionPerformed

    private void jBVistaPasajeCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVistaPasajeCargarActionPerformed
        Pasaje pasaje = new Pasaje();
        
        Ciudad ciudadaux = CiudadData.buscarCiudadPorNombre(vistaPasajeOrigen);
        pasaje.setCiudadOrigen(ciudadaux);

        ciudadaux = CiudadData.buscarCiudadPorNombre(vistaPasajeDestino);
        pasaje.setCiudadDestino(ciudadaux);
        
        for (TipoTransporte tipo : TipoTransporte.values()){
            if (tipo.getValorStr().equals(vistaPasajeTransporte)){
                pasaje.setTipoTransporte(tipo); 
                break;
            }

        }
        
        pasaje.setImporte(vistaPasajeImporte);
        pasaje.setEstado(true);
        PasajeData.crearPasaje(pasaje);

        cargarTransporte();
        cargarPasajeOrigen();
        cargarPasajeDestino(vistaPasajeOrigen);
        jTVistaPasajeImporte.setText("");
        jBVistaPasajeCargar.setEnabled(false);
    
    }//GEN-LAST:event_jBVistaPasajeCargarActionPerformed

    private void jCBVistaPasajeTransporteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaPasajeTransporteItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            vistaPasajeTransporte = (String) jCBVistaPasajeTransporte.getSelectedItem(); 
            activarBotonCargarPasaje();
        }
        
    }//GEN-LAST:event_jCBVistaPasajeTransporteItemStateChanged

    private void jCBVistaPasajeTransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBVistaPasajeTransporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBVistaPasajeTransporteActionPerformed

    private void jTVistaPasajeImporteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTVistaPasajeImporteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTVistaPasajeImporteKeyPressed

    private void jCBVistaEstadiaServicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaEstadiaServicioItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBVistaEstadiaServicioItemStateChanged

    private void jCBVistaEstadiaCiudadDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaEstadiaCiudadDestinoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBVistaEstadiaCiudadDestinoItemStateChanged

    private void jCBVistaEstadiaTemporadaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaEstadiaTemporadaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBVistaEstadiaTemporadaItemStateChanged

    private void jCBVistaEstadiaEstablecimientoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBVistaEstadiaEstablecimientoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBVistaEstadiaEstablecimientoItemStateChanged

    private void jTVistaEstadiaImporteDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTVistaEstadiaImporteDiarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTVistaEstadiaImporteDiarioActionPerformed

    private void jBVistaEstadiaAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVistaEstadiaAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBVistaEstadiaAgregarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBVistaCiudadAgregar;
    private javax.swing.JButton jBVistaEstablecimientoAgregar;
    private javax.swing.JButton jBVistaEstadiaAgregar;
    private javax.swing.JButton jBVistaPaqueteCrear;
    private javax.swing.JButton jBVistaPasajeCargar;
    private javax.swing.JComboBox<String> jCBVistaCiudadPais;
    private javax.swing.JComboBox<String> jCBVistaCiudadProvincia;
    private javax.swing.JComboBox<String> jCBVistaEstadiaCiudadDestino;
    private javax.swing.JComboBox<String> jCBVistaEstadiaEstablecimiento;
    private javax.swing.JComboBox<String> jCBVistaEstadiaServicio;
    private javax.swing.JComboBox<String> jCBVistaEstadiaTemporada;
    private javax.swing.JComboBox<String> jCBVistaPaqueteDestino;
    private javax.swing.JComboBox<String> jCBVistaPaqueteEstablecimiento;
    private javax.swing.JComboBox<String> jCBVistaPaqueteOrigen;
    private javax.swing.JComboBox<String> jCBVistaPaquetePasajeViajando;
    private javax.swing.JComboBox<String> jCBVistaPasajeDestino;
    private javax.swing.JComboBox<String> jCBVistaPasajeOrigen;
    private javax.swing.JComboBox<String> jCBVistaPasajeTransporte;
    private com.toedter.calendar.JDateChooser jDCVistaCiudadTemporadaAlta;
    private com.toedter.calendar.JDateChooser jDVistaEstadiaCheckIn;
    private com.toedter.calendar.JDateChooser jDVistaEstadiaCheckOut;
    private javax.swing.JLabel jLNombreCiudad;
    private javax.swing.JLabel jLNombrePais;
    private javax.swing.JLabel jLNombreProvincia;
    private javax.swing.JLabel jLPais;
    private javax.swing.JLabel jLProvincia;
    private javax.swing.JLabel jLProvincia1;
    private javax.swing.JLabel jLProvincia2;
    private javax.swing.JLabel jLProvincia3;
    private javax.swing.JLabel jLProvincia4;
    private javax.swing.JLabel jLProvincia5;
    private javax.swing.JLabel jLProvincia6;
    private javax.swing.JLabel jLProvincia7;
    private javax.swing.JLabel jLTemporadaAlta;
    private javax.swing.JLabel jLTitulo;
    private javax.swing.JLabel jLTitulo1;
    private javax.swing.JLabel jLTitulo2;
    private javax.swing.JLabel jLTitulo3;
    private javax.swing.JLabel jLTitulo4;
    private javax.swing.JLabel jLVistaPaqueteDestino;
    private javax.swing.JLabel jLVistaPaqueteEstablecimiento;
    private javax.swing.JLabel jLVistaPaqueteOrigen;
    private javax.swing.JLabel jLVistaPaquetePais;
    private javax.swing.JLabel jLVistaPaquetePasajeViajandoEn;
    private javax.swing.JLabel jLVistaPaquetePrecio;
    private javax.swing.JLabel jLVistaPaqueteTemporada1;
    private javax.swing.JLabel jLVistaPasajeDestno;
    private javax.swing.JLabel jLVistaPasajeImporte;
    private javax.swing.JLabel jLVistaPasajeOrigen;
    private javax.swing.JLabel jLVistaPasajeTransporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField jTAgregarEstablec;
    private javax.swing.JTextField jTDTelefono;
    private javax.swing.JTextField jTDireccionEstablec;
    private javax.swing.JTextField jTFVistaPaquetePais;
    private javax.swing.JTextField jTFVistaPaqueteTemporada;
    private javax.swing.JTextField jTFVistaPaqueteValorTotal;
    private javax.swing.JTabbedPane jTPContenedor;
    private javax.swing.JTextField jTVistaCiudadCiudad;
    private javax.swing.JTextField jTVistaCiudadPais;
    private javax.swing.JTextField jTVistaCiudadProvincia;
    private javax.swing.JTextField jTVistaEstadiaImporteDiario;
    private javax.swing.JTextField jTVistaPasajeImporte;
    // End of variables declaration//GEN-END:variables
}
