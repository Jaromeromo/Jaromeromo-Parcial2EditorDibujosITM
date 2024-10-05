public class Trazo {

    TipoTrazo tipo;
    int x1, y1, x2, y2;
    Trazo siguiente;

    public Trazo(String tipo, int x1, int y1, int x2, int y2) {
        this.tipo = determinarTipoTrazo(tipo);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    // Método para determinar el tipo de trazo
    private TipoTrazo determinarTipoTrazo(String tipo) {
        try {
            return TipoTrazo.valueOf(tipo.toUpperCase()); 
        } catch (IllegalArgumentException e) {
            return TipoTrazo.LINEA; 
        }
    }

    // Método que verifica si las coordenadas (x, y) están dentro del trazo
    public boolean contieneCoordenadas(int x, int y) {
        return switch (this.tipo) {
            case LINEA -> puntoEnLinea(x, y, this.x1, this.y1, this.x2, this.y2);
            case RECTANGULO -> x >= Math.min(this.x1, this.x2) && x <= Math.max(this.x1, this.x2) &&
                              y >= Math.min(this.y1, this.y2) && y <= Math.max(this.y1, this.y2);
            case CIRCULO -> {
                int radio = (int) Math.hypot(this.x2 - this.x1, this.y2 - this.y1) / 2;
                int centroX = (this.x1 + this.x2) / 2;
                int centroY = (this.y1 + this.y2) / 2;
                yield Math.pow(x - centroX, 2) + Math.pow(y - centroY, 2) <= Math.pow(radio, 2);
            }
            default -> false; 
        };
    }
    

    
    private boolean puntoEnLinea(int px, int py, int x1, int y1, int x2, int y2) {
        double distancia = Math.abs((y2 - y1) * px - (x2 - x1) * py + x2 * y1 - y2 * x1) /
                            Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        return distancia < 5.0;  // Margen de 5 píxeles para la selección de líneas
    }

    // Método que convierte el trazo a texto para guardarlo
    public String toTexto() {
        return switch (tipo) {
            case LINEA -> "LINEA;" + x1 + ";" + y1 + ";" + x2 + ";" + y2;
            case RECTANGULO -> "RECTANGULO;" + x1 + ";" + y1 + ";" + x2 + ";" + y2;
            case CIRCULO -> "CIRCULO;" + x1 + ";" + y1 + ";" + x2 + ";" + y2;
            default -> "TRAZO DESCONOCIDO";
        };
    }

    @Override
    public String toString() {
        return toTexto();
    }
}




   
