
package vistas;

import static dao.CiudadData.buscarCiudadPorNombre;
import static dao.CiudadData.listarCiudades;
import static dao.EstadiaData.buscarEstadiaPorEstablecimiento;
import static dao.EstadiaData.listarEstadias;
import dao.*;
import excepciones.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import models.*;


public class Menu3 extends JInternalFrame {
    private String ciudadOrigen;
    private String ciudadDestino = "";
    private String provincia;
    private String tipoTranporte;
    private String establecimiento;
    private String temporada;
    private String servicio;
    private int cantidadPersonas = 1;
    LocalDate checkin;
 
    Set<PaqueteTuristico> paquetesTuristicosEncontrados;
  
    private final DefaultTableModel modelo ;
    
    public Menu3() {        
        Dimension dimensionDeseada = new Dimension(150, 500); 
    
        initComponents();   
        modelo = new DefaultTableModel(){
             @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 1;
                }       
        };
        armarCabecera();   
        agregarTableModelListener();
      
        cargarOrigen();
        cargarProvincia();
        cargarTransporte();
        cargarTemporada();
        cargarServicio();
        cargarEstadia();
        
        editar (jCBOrigen,jLOrigen);
        editar (jCBDestino,jLDestino);
        editar (jCBProvincia,jLProvincia);
        editar (jCBTransporte,jLTransporte);
        editar (jCBEstadia,jLEstadia);
        editar (jCBServicio,jLServicio);
        editar (jCBTemporada,jLTemporada);
        
        jTValorMaximo.setPreferredSize(dimensionDeseada);        
   
        this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
        ui.setNorthPane(null);
    }
 
    private void agregarTableModelListener() {      
        modelo.addTableModelListener((TableModelEvent e) -> {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setGroupingSeparator('.');
            simbolos.setDecimalSeparator(',');
            
            DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00", simbolos);
            
            if (e.getType() == TableModelEvent.UPDATE) {           
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                if (column == 1) {
                    List<PaqueteTuristico> listaTemporal = new ArrayList<>(paquetesTuristicosEncontrados);
                    PaqueteTuristico p = listaTemporal.get(row);
                    Integer cantidadDeDiasEditada;
                    try {
                        cantidadDeDiasEditada = Integer.valueOf((String)modelo.getValueAt(row, 1));
                        if (cantidadDeDiasEditada > 0) {
                            double importeDiario = p.getEstadia().getImporteDiario();
                            double servicioPorcentaje = p.getEstadia().getServicio().getPorcentaje();
                            double temporadaPorcentaje = p.getEstadia().getTemporada().getPorcentaje();
                            double valorPasaje = p.getPasaje().getImporte();
                            double valorTotalEditado = cantidadPersonas * importeDiario * servicioPorcentaje * cantidadDeDiasEditada * temporadaPorcentaje + valorPasaje * cantidadPersonas;
                            
                            modelo.setValueAt(formatoMoneda.format(valorTotalEditado), row, 8);
                            return;
                        } else {
                            JOptionPane.showMessageDialog (null, "La cantidad de dias debe ser mayor a cero.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog (null, "La cantidad de dias debe ser un numero mayor a cero.");
                    }
                    cargarTablaConBuscados();               
                }
            }
        });
    }
  
    private void cargarTablaConBuscados() {         
        modelo.setRowCount(0);
    
        PaqueteTuristico paqueteBuscado = new PaqueteTuristico();
        
        Ciudad ciudadOrigenAux = null;
        Ciudad ciudadDestinoAux = null;
        Estadia aloAux = null;
  
        List<PaqueteTuristico> paquetesARemover = new ArrayList<>();
        
        ciudadOrigenAux = buscarCiudadPorNombre(ciudadOrigen);
        paqueteBuscado.setCiudadOrigen(ciudadOrigenAux);

        ciudadDestinoAux = buscarCiudadPorNombre(ciudadDestino);
        paqueteBuscado.setCiudadDestino(ciudadDestinoAux);

        if (!establecimiento.isEmpty()) {
            Establecimiento estabAux = EstablecimientoData.buscarEstablecimientoPorNombre(establecimiento);
            aloAux = buscarEstadiaPorEstablecimiento(estabAux);         
            paqueteBuscado.setEstadia(aloAux);
        }
  
        paquetesTuristicosEncontrados = PaqueteData.buscarPaquetesDinamico(paqueteBuscado);
 
        if (!provincia.isEmpty()) {
             for (PaqueteTuristico paquete : paquetesTuristicosEncontrados) {
                if (!provincia.equals(paquete.getCiudadDestino().getProvincia().getNombre())){
                    paquetesARemover.add(paquete);           
                }
            }
        }
 
        if (!temporada.isEmpty()) {           
            for (PaqueteTuristico paquete : paquetesTuristicosEncontrados) {               
                if (!temporada.equals(paquete.getEstadia().getTemporada().getValorStr())){
                    paquetesARemover.add(paquete);           
                }
            }
        }
 
        if (!tipoTranporte.isEmpty()) {
            for (PaqueteTuristico paquete : paquetesTuristicosEncontrados) {                
                if (!tipoTranporte.equals(paquete.getPasaje().getTipoTransporte().getValorStr())){
                    paquetesARemover.add(paquete);           
                }
            }
        }
 
        if (!servicio.isEmpty()) {
            for(PaqueteTuristico paquete : paquetesTuristicosEncontrados) {
                if (!servicio.equals(paquete.getEstadia().getServicio().getValorStr())) {
                   paquetesARemover.add(paquete);
                }
            }
        }
        
        try {    
            if (!jTValorMinimo.getText().isEmpty() || !jTValorMaximo.getText().isEmpty()) {
                double valorMinimo = Double.valueOf(jTValorMinimo.getText());
                double valorMaximo = Double.valueOf(jTValorMaximo.getText());               
                if (valorMinimo > valorMaximo) {
                     throw new ValoresException("El valor minimo deve ser menor al valor maximo.");                                  
                }           
                for(PaqueteTuristico paquete : paquetesTuristicosEncontrados) {
                    if (paquete.getValorTotal() < valorMinimo || paquete.getValorTotal() > valorMaximo) {
                        paquetesARemover.add(paquete);
                    } 
                }           
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog (null, "Los campos precisan contener valores numericos");
            jTValorMinimo.setText("");
            jTValorMaximo.setText("");
            return;
        } catch (ValoresException e) {
            JOptionPane.showMessageDialog (null, e.getMessage());
            jTValorMinimo.setText("");
            jTValorMaximo.setText("");
            return;
        }
 
        Date selectedDateCheckin = jDCCheckin.getDate();
        if (selectedDateCheckin != null) {
            checkin = selectedDateCheckin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();            
            LocalDate quinceDiasAntes = checkin.minusDays(15);
            LocalDate quinceDiasDespues = checkin.plusDays(15);    
            for (PaqueteTuristico paquete : paquetesTuristicosEncontrados) {
                LocalDate fechaCheckinPaquete = paquete.getEstadia().getCheckIn();
                if (fechaCheckinPaquete.isBefore(quinceDiasAntes) || fechaCheckinPaquete.isAfter(quinceDiasDespues)) {
                    paquetesARemover.add(paquete);
               }
            }
        }

        paquetesTuristicosEncontrados.removeAll(paquetesARemover);
        paquetesARemover.clear();
   
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.'); 
        simbolos.setDecimalSeparator(','); 
    
        DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00", simbolos);
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   
        for (PaqueteTuristico paquete: paquetesTuristicosEncontrados){     
            long cantidadDias = ChronoUnit.DAYS.between(paquete.getEstadia().getCheckIn(), paquete.getEstadia().getCheckOut());
            String fechaFormateada = paquete.getEstadia().getCheckIn().format(formateador);
            double valorPorCantidad =  paquete.getValorTotal() *  cantidadPersonas;
            modelo.addRow(new Object[]{
                                    fechaFormateada,
                                    cantidadDias,
                                    paquete.getCiudadOrigen().getNombre(), 
                                    paquete.getCiudadDestino().getNombre(),
                                    paquete.getPasaje().getTipoTransporte().getValorStr(),
                                    paquete.getEstadia().getEstablecimiento().getNombre(),
                                    paquete.getEstadia().getServicio().getValorStr(),
                                    paquete.getEstadia().getTemporada().getValorStr(),
                                    formatoMoneda.format(valorPorCantidad),
                                    paquete.getValorTotal(),                               
                                    });          
        }
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTResultados.getModel());
        sorter.setComparator(0, (String dateStr1, String dateStr2) -> {
            LocalDate date1 = LocalDate.parse(dateStr1, formateador);
            LocalDate date2 = LocalDate.parse(dateStr2, formateador);
            return date1.compareTo(date2);
        });
    
        sorter.setComparator(1, (Long num1, Long num2) -> num1.compareTo(num2));
    
        sorter.setComparator(8, (String str1, String str2) -> {
            try {
                Number num1 = formatoMoneda.parse(str1);
                Number num2 = formatoMoneda.parse(str2);
                return Double.compare(num1.doubleValue(), num2.doubleValue());
            } catch (java.text.ParseException e) {
                throw new RuntimeException(e);
            }
        });
     
        checkin = null;
        jTResultados.setRowSorter(sorter);
        sorter.toggleSortOrder(0);
    }                      
   
    private void cargarOrigen(){        
        jCBOrigen.removeAllItems();  
        jCBOrigen.addItem("");
        
        Set<Ciudad> ciudades = listarCiudades();
        for (Ciudad ciu : ciudades) {
            jCBOrigen.addItem(ciu.getNombre());
        }
    }
    
    private void cargarDestino(){       
        jCBDestino.removeAllItems();  
        jCBDestino.addItem("");
      
        Set<Ciudad> ciudades = listarCiudades();
  
        if (provincia != null) {
             for (Ciudad ciu : ciudades) {
                 Provincia provinviaAux = ProvinciaData.buscarProvinciaPorId(ciu.getProvincia().getIdProvincia());
                 if (provinviaAux.getNombre().equals(provincia)) {
                    jCBDestino.addItem(ciu.getNombre());                   
                    return;
                 }
             }
        }
        
        for (Ciudad ciu : ciudades) {
            if (!ciu.getNombre().equals(ciudadOrigen)) {
                jCBDestino.addItem(ciu.getNombre());
            }
        }
    }
   
    private void cargarProvincia() {       
        jCBProvincia.removeAllItems();
        jCBProvincia.addItem("");
        
        Set<Ciudad> ciudades = listarCiudades();
        Set<String> provincias = new HashSet<>();        
        Provincia provinviaAux; 
        
        if (!ciudadDestino.isEmpty()) {       
            Ciudad ciudad = buscarCiudadPorNombre(ciudadDestino);
            provinviaAux = ProvinciaData.buscarProvinciaPorId(ciudad.getProvincia().getIdProvincia());
            jCBProvincia.addItem(provinviaAux.getNombre());
            return;
        }
        
        for(Ciudad ciu : ciudades) {
            provinviaAux = ProvinciaData.buscarProvinciaPorId(ciu.getProvincia().getIdProvincia());
            provincias.add(provinviaAux.getNombre());
        }
        
        for (String prov : provincias) {
            if (!prov.equals("Desconocida") ) {
                jCBProvincia.addItem(prov);
            }
        }
    }
    
    private void cargarTransporte() {        
        jCBTransporte.removeAllItems();
        jCBTransporte.addItem("");
        
        for (TipoTransporte tipo : TipoTransporte.values()) {            
            jCBTransporte.addItem(tipo.getValorStr());
        }
    }
    
    private void cargarEstadia() {
        jCBEstadia.removeAllItems();  
        jCBEstadia.addItem("");
        
        Set<Estadia> estadias = listarEstadias();
          
        if (ciudadOrigen.isEmpty() &&  ciudadDestino.isEmpty()) {
            for (Estadia est : estadias) {
                    jCBEstadia.addItem(est.getEstablecimiento().getNombre());
                }
            return;
        } 
        
        if (!ciudadOrigen.isEmpty() &&  ciudadDestino.isEmpty()) {
            for (Estadia est : estadias) {
                if (!est.getCiudadDestino().getNombre().equals(ciudadOrigen))    
                    jCBEstadia.addItem(est.getEstablecimiento().getNombre());
            }
            return;
        }
        
        if (ciudadOrigen.isEmpty() &&  !ciudadDestino.isEmpty()) {
            for (Estadia est : estadias) {
                if (est.getCiudadDestino().getNombre().equals(ciudadDestino))    
                    jCBEstadia.addItem(est.getEstablecimiento().getNombre());
            }
            return;
        }
        
        if (!ciudadOrigen.isEmpty() &&  !ciudadDestino.isEmpty()) {
            for (Estadia est : estadias) {
                if (!est.getCiudadDestino().getNombre().equals(ciudadOrigen) && 
                    est.getCiudadDestino().getNombre().equals(ciudadDestino))    
                    jCBEstadia.addItem(est.getEstablecimiento().getNombre());
            }
        }     
    }
    
    private void cargarTemporada() {
        jCBTemporada.removeAllItems();
        jCBTemporada.addItem("");

        LocalDate checkInSeleccionado = null;

        if (checkin != null) {
            checkInSeleccionado = checkin;
        } else {
            Date selectedDateCheckin = jDCCheckin.getDate();
            if (selectedDateCheckin != null) {
                checkInSeleccionado = selectedDateCheckin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }

        if (!ciudadDestino.isBlank() && checkInSeleccionado != null) {
            Ciudad ciudadAux = CiudadData.buscarCiudadPorNombre(ciudadDestino);

            if (ciudadAux != null) {
                LocalDate fechaDeTemporadaAltaActual = LocalDate.of(LocalDate.now().getYear(), ciudadAux.getFechaDeTemporadaAlta().getMonthValue(), ciudadAux.getFechaDeTemporadaAlta().getDayOfMonth());
                checkInSeleccionado = checkInSeleccionado.withYear(ciudadAux.getFechaDeTemporadaAlta().getYear());

                long diasDiferencia = ChronoUnit.DAYS.between(fechaDeTemporadaAltaActual, checkInSeleccionado);
    
                if (Math.abs(diasDiferencia) <= 45) {               
                    jCBTemporada.addItem(Temporada.ALTA.getValorStr());
                } else if (Math.abs(diasDiferencia) > 45 && (Math.abs(diasDiferencia) <= 90)) {                   
                    jCBTemporada.addItem(Temporada.MEDIA.getValorStr());
                } else {                   
                    jCBTemporada.addItem(Temporada.BAJA.getValorStr());
                }

                checkInSeleccionado = null;
                checkin = null;
                return;
            }
        }

        for (Temporada tipo : Temporada.values()) {
            jCBTemporada.addItem(tipo.getValorStr());
        }
    }

    private void cargarServicio() {
        jCBServicio.removeAllItems();
        jCBServicio.addItem("");  
        for (Servicio serv : Servicio.values()) {            
            jCBServicio.addItem(serv.getValorStr());
        }
    }
 
    private void armarCabecera(){ 
        modelo.addColumn("CheckIn");
        modelo.addColumn("Duracion");
        modelo.addColumn("Origen");
        modelo.addColumn("Destino");    
        modelo.addColumn("Transporte");  
        modelo.addColumn("Alojamiento");  
        modelo.addColumn("Servicio"); 
        modelo.addColumn("Temporada");  
        modelo.addColumn("Valor para: " + jSCantidad.getValue() + " persona");  
        modelo.addColumn("Valor para 1");  
        jTResultados.setModel(modelo);
        ocultarColumna(9);      
    }
    
    private void ocultarColumna(int columnIndex) {
        jTResultados.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        jTResultados.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        jTResultados.getColumnModel().getColumn(columnIndex).setWidth(0);
        jTResultados.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }
  
    private void editar(JComboBox jcb, JLabel jl){
//        Font font = new Font("SansSerif", Font.PLAIN, 14);
//        Dimension dimensionDeseada = new Dimension(150, 30); 
//        jl.setFont(font);        
//        jcb.setPreferredSize(dimensionDeseada);
//        jcb.setFont(font);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLOrigen = new javax.swing.JLabel();
        jLDestino = new javax.swing.JLabel();
        jLProvincia = new javax.swing.JLabel();
        jCBOrigen = new javax.swing.JComboBox<>();
        jCBProvincia = new javax.swing.JComboBox<>();
        jCBDestino = new javax.swing.JComboBox<>();
        jLTransporte = new javax.swing.JLabel();
        jCBTransporte = new javax.swing.JComboBox<>();
        jLEstadia = new javax.swing.JLabel();
        jCBEstadia = new javax.swing.JComboBox<>();
        jLServicio = new javax.swing.JLabel();
        jCBServicio = new javax.swing.JComboBox<>();
        jLValorMinimo = new javax.swing.JLabel();
        jTValorMinimo = new javax.swing.JTextField();
        jLValorMaximo = new javax.swing.JLabel();
        jTValorMaximo = new javax.swing.JTextField();
        jLTemporada = new javax.swing.JLabel();
        jCBTemporada = new javax.swing.JComboBox<>();
        jLCheckIn = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTResultados = new javax.swing.JTable();
        jLTitulo = new javax.swing.JLabel();
        jBLimpiar = new javax.swing.JButton();
        jBBuscar = new javax.swing.JButton();
        jSCantidad = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        jLValorMinimo1 = new javax.swing.JLabel();
        jDCCheckin = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(0, 102, 102));

        jPanel1.setBackground(new java.awt.Color(72, 92, 113));

        jLOrigen.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLOrigen.setForeground(java.awt.Color.white);
        jLOrigen.setText("Partiendo desde");

        jLDestino.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLDestino.setForeground(java.awt.Color.white);
        jLDestino.setText("Llegando a");

        jLProvincia.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLProvincia.setForeground(java.awt.Color.white);
        jLProvincia.setText("En la provincia");

        jCBOrigen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBOrigenItemStateChanged(evt);
            }
        });

        jCBProvincia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBProvinciaItemStateChanged(evt);
            }
        });

        jCBDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBDestinoItemStateChanged(evt);
            }
        });
        jCBDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBDestinoActionPerformed(evt);
            }
        });

        jLTransporte.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLTransporte.setForeground(java.awt.Color.white);
        jLTransporte.setText("Viajando en ");

        jCBTransporte.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBTransporteItemStateChanged(evt);
            }
        });

        jLEstadia.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLEstadia.setForeground(java.awt.Color.white);
        jLEstadia.setText("Voy a dormir en");

        jCBEstadia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBEstadiaItemStateChanged(evt);
            }
        });
        jCBEstadia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEstadiaActionPerformed(evt);
            }
        });

        jLServicio.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLServicio.setForeground(java.awt.Color.white);
        jLServicio.setText("Con servicio de");

        jCBServicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBServicioItemStateChanged(evt);
            }
        });

        jLValorMinimo.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLValorMinimo.setForeground(java.awt.Color.white);
        jLValorMinimo.setText("Gastando entre ");

        jLValorMaximo.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLValorMaximo.setForeground(java.awt.Color.white);
        jLValorMaximo.setText("y");

        jLTemporada.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLTemporada.setForeground(java.awt.Color.white);
        jLTemporada.setText("En temporada");

        jCBTemporada.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCBTemporadaItemStateChanged(evt);
            }
        });
        jCBTemporada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTemporadaActionPerformed(evt);
            }
        });

        jLCheckIn.setBackground(java.awt.Color.white);
        jLCheckIn.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLCheckIn.setForeground(java.awt.Color.white);
        jLCheckIn.setText("Comenzando el viaje ");

        jTResultados.setBackground(new java.awt.Color(72, 92, 113));
        jTResultados.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jTResultados.setForeground(java.awt.Color.white);
        jTResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTResultados.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTResultados);

        jLTitulo.setBackground(new java.awt.Color(193, 126, 48));
        jLTitulo.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLTitulo.setForeground(new java.awt.Color(193, 126, 48));
        jLTitulo.setText("Consultas");

        jBLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jBLimpiar.setText("Limpiar");
        jBLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimpiarActionPerformed(evt);
            }
        });

        jBBuscar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jBBuscar.setText("Buscar");
        jBBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBBuscarActionPerformed(evt);
            }
        });

        jSCantidad.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSCantidadStateChanged(evt);
            }
        });

        jLValorMinimo1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLValorMinimo1.setForeground(java.awt.Color.white);
        jLValorMinimo1.setText("Pasajeros");

        jDCCheckin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDCCheckinMouseClicked(evt);
            }
        });
        jDCCheckin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDCCheckinPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jBBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(jBLimpiar)))
                .addContainerGap(42, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jCBOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(jLDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLTitulo)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCBDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLCheckIn)
                                            .addComponent(jDCCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(35, 35, 35)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLValorMinimo1)
                                            .addComponent(jSCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCBServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCBProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 200, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jCBTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLValorMinimo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTValorMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLEstadia, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLValorMaximo, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTValorMaximo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLTemporada, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBEstadia, 0, 149, Short.MAX_VALUE)
                            .addComponent(jCBTemporada, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(117, 519, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLTitulo)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLOrigen)
                    .addComponent(jLDestino)
                    .addComponent(jLProvincia)
                    .addComponent(jCBProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLTransporte)
                    .addComponent(jLEstadia)
                    .addComponent(jLServicio)
                    .addComponent(jCBServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBEstadia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLTemporada)
                    .addComponent(jLCheckIn)
                    .addComponent(jLValorMinimo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDCCheckin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBTemporada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLValorMaximo)
                                .addComponent(jTValorMaximo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTValorMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLValorMinimo)))
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCBTemporadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTemporadaActionPerformed
        
    }//GEN-LAST:event_jCBTemporadaActionPerformed

    private void jCBEstadiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEstadiaActionPerformed
       
    }//GEN-LAST:event_jCBEstadiaActionPerformed

    private void jCBDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBDestinoActionPerformed
        
    }//GEN-LAST:event_jCBDestinoActionPerformed

    private void jCBOrigenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBOrigenItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            ciudadOrigen = (String) jCBOrigen.getSelectedItem();
            cargarDestino(); 
        }       
    }//GEN-LAST:event_jCBOrigenItemStateChanged

    private void jCBDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBDestinoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            ciudadDestino = (String) jCBDestino.getSelectedItem();
            cargarProvincia();
            cargarEstadia();
            jCBTemporada.removeAllItems();
            cargarTemporada();
        } 
    }//GEN-LAST:event_jCBDestinoItemStateChanged

    private void jBBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBuscarActionPerformed
        cargarTablaConBuscados();
    }//GEN-LAST:event_jBBuscarActionPerformed

    private void jBLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarActionPerformed
        jCBOrigen.removeAllItems();  
        jCBDestino.removeAllItems();
        jCBTransporte.removeAllItems();
        jCBEstadia.removeAllItems();
        jCBTemporada.removeAllItems();  
        jDCCheckin.setDate(null);
        jCBServicio.removeAllItems();
        jTValorMinimo.setText("");
        jTValorMaximo.setText("");
        jSCantidad.setValue(1);
        modelo.setRowCount(0);
        checkin = null;
        
        cargarOrigen();
        cargarTransporte();
        cargarTemporada();
        cargarServicio();
        cargarProvincia();
        cargarEstadia();        
    }//GEN-LAST:event_jBLimpiarActionPerformed

    private void jCBTransporteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBTransporteItemStateChanged
        tipoTranporte = (String) jCBTransporte.getSelectedItem();
    }//GEN-LAST:event_jCBTransporteItemStateChanged

    private void jCBEstadiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBEstadiaItemStateChanged
        establecimiento = (String) jCBEstadia.getSelectedItem();
    }//GEN-LAST:event_jCBEstadiaItemStateChanged

    private void jCBProvinciaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBProvinciaItemStateChanged
        provincia = (String) jCBProvincia.getSelectedItem();
    }//GEN-LAST:event_jCBProvinciaItemStateChanged

    private void jCBServicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBServicioItemStateChanged
        servicio = (String) jCBServicio.getSelectedItem();
    }//GEN-LAST:event_jCBServicioItemStateChanged

    private void jSCantidadStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSCantidadStateChanged
        cantidadPersonas = (Integer) jSCantidad.getValue();
        
        jTResultados.getColumnModel().getColumn(8).setHeaderValue("Valor para " + cantidadPersonas + " personas");
        jTResultados.getTableHeader().repaint(); 
    
        DefaultTableModel modelo2 = (DefaultTableModel) jTResultados.getModel();
         
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            double valorOriginal = Double.parseDouble(modelo2.getValueAt(i, 9).toString().replace("$", "").replace(",", "."));
            double valorActualizado = valorOriginal * cantidadPersonas;
            String valorFormateado = String.format("$%,.2f", valorActualizado).replace('.', ',');
            modelo2.setValueAt(valorFormateado, i, 8);
        }        
        modelo2.fireTableDataChanged();
        jTResultados.repaint();  
    }//GEN-LAST:event_jSCantidadStateChanged

    private void jCBTemporadaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCBTemporadaItemStateChanged
        temporada = (String) jCBTemporada.getSelectedItem();        
    }//GEN-LAST:event_jCBTemporadaItemStateChanged

    private void jDCCheckinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDCCheckinMouseClicked
        cargarTemporada();
    }//GEN-LAST:event_jDCCheckinMouseClicked

    private void jDCCheckinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDCCheckinPropertyChange
        if ("date".equals(evt.getPropertyName())) {
            Date selectedDateCheckin = (Date) evt.getNewValue();             
            if (selectedDateCheckin != null) {
                Instant instant = selectedDateCheckin.toInstant();
                checkin = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                cargarTemporada();
            }
        }
    }//GEN-LAST:event_jDCCheckinPropertyChange
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBBuscar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JComboBox<String> jCBDestino;
    private javax.swing.JComboBox<String> jCBEstadia;
    private javax.swing.JComboBox<String> jCBOrigen;
    private javax.swing.JComboBox<String> jCBProvincia;
    private javax.swing.JComboBox<String> jCBServicio;
    private javax.swing.JComboBox<String> jCBTemporada;
    private javax.swing.JComboBox<String> jCBTransporte;
    private com.toedter.calendar.JDateChooser jDCCheckin;
    private javax.swing.JLabel jLCheckIn;
    private javax.swing.JLabel jLDestino;
    private javax.swing.JLabel jLEstadia;
    private javax.swing.JLabel jLOrigen;
    private javax.swing.JLabel jLProvincia;
    private javax.swing.JLabel jLServicio;
    private javax.swing.JLabel jLTemporada;
    private javax.swing.JLabel jLTitulo;
    private javax.swing.JLabel jLTransporte;
    private javax.swing.JLabel jLValorMaximo;
    private javax.swing.JLabel jLValorMinimo;
    private javax.swing.JLabel jLValorMinimo1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSCantidad;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTResultados;
    private javax.swing.JTextField jTValorMaximo;
    private javax.swing.JTextField jTValorMinimo;
    // End of variables declaration//GEN-END:variables
}
