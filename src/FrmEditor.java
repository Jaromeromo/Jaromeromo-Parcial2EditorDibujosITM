import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public class FrmEditor extends JFrame {

    private final JButton btnCargar;
    private final JButton btnGuardar;
    private final JButton btnEliminar;
    private final JButton btnSeleccionar;
    private final JComboBox<String> cmbTipo;
    private final JToolBar tbEditor;
    private final JPanel pnlGrafica;

    private Estado estado;
    private int x;
    private int y;

    private final Dibujo dibujo = new Dibujo();

    public FrmEditor() {
        tbEditor = new JToolBar();
        btnCargar = new JButton();
        btnGuardar = new JButton();
        btnEliminar = new JButton();
        cmbTipo = new JComboBox<>();
        btnSeleccionar = new JButton();
        pnlGrafica = new JPanel();

        setSize(600, 300);
        setTitle("Editor de gráficas");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnCargar.setIcon(new ImageIcon(getClass().getResource("/iconos/AbrirArchivos.png")));
        btnCargar.setToolTipText("Cargar");
        btnCargar.addActionListener(e -> btnCargarClick());
        tbEditor.add(btnCargar);

        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/iconos/Guardar.png")));
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.addActionListener(e -> btnGuardarClick());
        tbEditor.add(btnGuardar);

        cmbTipo.setModel(new DefaultComboBoxModel<>(new String[]{"Línea", "Rectángulo", "Círculo"}));
        tbEditor.add(cmbTipo);

        btnSeleccionar.setIcon(new ImageIcon(getClass().getResource("/iconos/Seleccionar.png")));
        btnSeleccionar.setToolTipText("Seleccionar");
        btnSeleccionar.addActionListener(e -> btnSeleccionarClick());
        tbEditor.add(btnSeleccionar);

        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/iconos/Eliminar.jpeg")));
        btnEliminar.setToolTipText("Eliminar");
        btnEliminar.addActionListener(e -> btnEliminarClick());
        tbEditor.add(btnEliminar);

        pnlGrafica.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                pnlGraficaMouseClicked(evt);
            }
        });
        pnlGrafica.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evt) {
                pnlGraficaMouseMoved(evt);
            }
        });

        pnlGrafica.setPreferredSize(new Dimension(300, 200));

        getContentPane().add(tbEditor, BorderLayout.NORTH);
        getContentPane().add(pnlGrafica, BorderLayout.CENTER);

        estado = Estado.NADA;

        this.pack();
        limpiarPanel();
    }

    private void limpiarPanel() {
        Graphics g = pnlGrafica.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, pnlGrafica.getWidth(), pnlGrafica.getHeight());
        g.dispose();
    }
    

    private void btnCargarClick() {
        String nombreArchivo = Archivo.elegirArchivo();
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            dibujo.desdeArchivo(nombreArchivo);
            dibujo.dibujar(pnlGrafica);
            JOptionPane.showMessageDialog(this, "Archivo cargado: " + nombreArchivo);
        }
    }

    private void btnGuardarClick() {
        String nombreArchivo = Archivo.elegirArchivo();
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            String[] lineas = dibujo.aTexto();
            if (Archivo.guardarArchivo(nombreArchivo, lineas)) {
                JOptionPane.showMessageDialog(this, "Dibujo guardado exitosamente en: " + nombreArchivo);
        
            }
        }
    }

    private void btnSeleccionarClick() {
        estado = Estado.SELECCIONANDO;
        JOptionPane.showMessageDialog(this, "Modo selección activado");
        pnlGrafica.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Trazo seleccionado = dibujo.seleccionarTrazo(e.getX(), e.getY());
                if (seleccionado != null) {
                    JOptionPane.showMessageDialog(pnlGrafica, "Trazo seleccionado: " + seleccionado);
                } else {
                    JOptionPane.showMessageDialog(pnlGrafica, "No se seleccionó ningún trazo");
                }
            }
        });
    }

    private void btnEliminarClick() {
        Trazo seleccionado = dibujo.getTrazoSeleccionado();
        if (seleccionado != null) {
            dibujo.eliminar(seleccionado);
            dibujo.dibujar(pnlGrafica);
            JOptionPane.showMessageDialog(this, "Trazo eliminado");
        } else {
            JOptionPane.showMessageDialog(this, "No hay trazo seleccionado para eliminar");
        }
    }

    private void pnlGraficaMouseClicked(MouseEvent evt) {
        if (estado == Estado.TRAZANDO) {
            // Agregar el nuevo trazo a la lista después de completar el trazo
            Trazo nuevoTrazo = null;
            switch (cmbTipo.getSelectedIndex()) {
                case 0 -> nuevoTrazo = new Trazo("LINEA", x, y, evt.getX(), evt.getY());
                case 1 -> nuevoTrazo = new Trazo("RECTANGULO", x, y, evt.getX(), evt.getY());
                case 2 -> nuevoTrazo = new Trazo("CIRCULO", x, y, evt.getX(), evt.getY());
            }
            if (nuevoTrazo != null) {
                dibujo.agregar(nuevoTrazo);  // Guardar el trazo en la lista ligada
            }
    
            // Volver al estado de no trazado y redibujar todo
            estado = Estado.NADA;
            limpiarPanel();
            dibujo.dibujar(pnlGrafica);  // Redibuja todos los trazos
        } else {
            estado = Estado.TRAZANDO;
            x = evt.getX();
            y = evt.getY();
        }
    }
    

    private void pnlGraficaMouseMoved(MouseEvent evt) {
        if (estado == Estado.TRAZANDO) {
            limpiarPanel();
            dibujo.dibujar(pnlGrafica);  
    
        
            Graphics g = pnlGrafica.getGraphics();
            g.setColor(Color.yellow);
            int x1, y1;
            switch (cmbTipo.getSelectedIndex()) {
                case 0 -> g.drawLine(x, y, evt.getX(), evt.getY()); // Línea
                case 1 -> { // Rectángulo
                    x1 = Math.min(evt.getX(), x);
                    y1 = Math.min(evt.getY(), y);
                    g.drawRect(x1, y1, Math.abs(evt.getX() - x), Math.abs(evt.getY() - y));
                }
                case 2 -> { // Círculo
                    x1 = Math.min(evt.getX(), x);
                    y1 = Math.min(evt.getY(), y);
                    g.drawOval(x1, y1, Math.abs(evt.getX() - x), Math.abs(evt.getY() - y));
                }
                default -> {} 
            }
            g.dispose();
        }
    }
    

}





