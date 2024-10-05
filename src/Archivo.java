
import java.io.*;
import javax.swing.*;

public class Archivo {

    public static BufferedReader abrirArchivo(String nombreArchivo) {
        try {
            return new BufferedReader(new FileReader(nombreArchivo));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Archivo no encontrado: " + nombreArchivo);
            return null;
        }
    }

    public static boolean guardarArchivo(String nombreArchivo, String[] lineas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
            return false;
        }
    }

    public static String elegirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            return archivoSeleccionado.getAbsolutePath();
        }
        return null;
    }
}
