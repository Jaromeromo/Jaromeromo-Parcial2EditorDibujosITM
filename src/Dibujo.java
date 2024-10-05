import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Dibujo {

    private Trazo cabeza;

    public Dibujo() {
        cabeza = null;
    }

    public void agregar(Trazo n) {
        if (n != null) {
            if (cabeza == null) {
                cabeza = n;
            } else {
                Trazo apuntador = cabeza;
                while (apuntador.siguiente != null) {
                    apuntador = apuntador.siguiente;
                }
                apuntador.siguiente = n;
            }
        }
    }

    public void desdeArchivo(String nombreArchivo) {
        try (BufferedReader br = Archivo.abrirArchivo(nombreArchivo)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    Trazo n = new Trazo(datos[0],
                            Integer.parseInt(datos[1]),
                            Integer.parseInt(datos[2]),
                            Integer.parseInt(datos[3]),
                            Integer.parseInt(datos[4]));
                    agregar(n);
                }
            }
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    public void dibujar(JPanel pnl) {
        Graphics g = pnl.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, pnl.getWidth(), pnl.getHeight());
        g.setColor(Color.yellow);

        Trazo apuntador = cabeza;
        while (apuntador != null) {
            switch (apuntador.tipo) {
                case LINEA -> g.drawLine(apuntador.x1, apuntador.y1, apuntador.x2, apuntador.y2);
                case RECTANGULO -> g.drawRect(Math.min(apuntador.x1, apuntador.x2),
                        Math.min(apuntador.y1, apuntador.y2),
                        Math.abs(apuntador.x2 - apuntador.x1),
                        Math.abs(apuntador.y2 - apuntador.y1));
                case CIRCULO -> g.drawOval(Math.min(apuntador.x1, apuntador.x2),
                        Math.min(apuntador.y1, apuntador.y2),
                        Math.abs(apuntador.x2 - apuntador.x1),
                        Math.abs(apuntador.y2 - apuntador.y1));
            }
            apuntador = apuntador.siguiente;
        }
        g.dispose();
    }

    public String[] aTexto() {
        List<String> lineas = new ArrayList<>();
        Trazo apuntador = cabeza;
        while (apuntador != null) {
            lineas.add(apuntador.toTexto());
            apuntador = apuntador.siguiente;
        }
        return lineas.toArray(String[]::new);
    }

    public void guardarComoArchivo(String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : aTexto()) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    private Trazo trazoSeleccionado = null;

    public Trazo getTrazoSeleccionado() {
        return trazoSeleccionado;
    }

    public Trazo seleccionarTrazo(int x, int y) {
        Trazo apuntador = cabeza;
        while (apuntador != null) {
            if (apuntador.contieneCoordenadas(x, y)) {  
                trazoSeleccionado = apuntador;
                return apuntador;  
            }
            apuntador = apuntador.siguiente;
        }
        trazoSeleccionado = null;
        return null;  
    }

    public void eliminar(Trazo t) {
        if (cabeza == t) {
            cabeza = cabeza.siguiente;
        } else {
            Trazo apuntador = cabeza;
            while (apuntador.siguiente != null && apuntador.siguiente != t) {
                apuntador = apuntador.siguiente;
            }
            if (apuntador.siguiente == t) {
                apuntador.siguiente = t.siguiente;
            }
        }
    }
}



