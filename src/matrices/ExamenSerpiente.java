package matrices;

import java.util.Scanner;

public class ExamenSerpiente {
    // Constantes que definen el tamaño del tablero y los caracteres de los elementos del juego
    private static final int ANCHO = 20;          // Número de columnas del tablero
    private static final int ALTO = 10;           // Número de filas del tablero
    private static final char VACIO = ' ';        // Representa un espacio vacío en el tablero
    private static final char SERPIENTE = 'O';    // Representa el cuerpo de la serpiente
    private static final char COMIDA = 'X';       // Representa la comida
    private static final char PARED = '#';        // Representa los bordes del tablero (las paredes)

    // Variables para manejar el estado del juego
    private static final char[][] tablero = new char[ALTO][ANCHO];   // Matriz que representa el tablero del juego
    private static final int[] serpienteX = new int[ANCHO * ALTO];   // Array que almacena las posiciones X del cuerpo de la serpiente
    private static final int[] serpienteY = new int[ANCHO * ALTO];   // Array que almacena las posiciones Y del cuerpo de la serpiente
    private static int longitudSerpiente = 1;                  // Longitud actual de la serpiente (inicialmente es de 1)
    private static int comidaX, comidaY;                       // Coordenadas de la comida en el tablero
    private static int puntuacion = 0;                         // Contador de la puntuación del jugador

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);   // Scanner para leer la entrada del jugador
        inicializarJuego();                    // Inicializa el juego colocando la serpiente y la comida en el tablero

        // Bucle principal del juego
        while (true) {
            imprimirTablero();                 // Imprime el tablero en la consola
            System.out.println("Puntuación: " + puntuacion);   // Muestra la puntuación actual
            System.out.println("Introduce dirección (WASD): ");

            try {
                String input = sc.next();      // Lee la dirección ingresada por el jugador
                if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                    throw new IllegalArgumentException("No puede introducir números, solo las teclas acordadas.");
                }

                // Convierte la dirección a mayúscula para manejarla de manera uniforme
                char direccion = Character.toUpperCase(input.charAt(0));

                // Mueve la serpiente en la dirección indicada y verifica colisiones
                if (!moverSerpiente(direccion)) {
                    // Si la serpiente choca con una pared o consigo misma, termina el juego
                    System.out.println("¡Juego Terminado! Has chocado con la pared o te has comido a ti mismo.");
                    break;
                }

                // Si el jugador alcanza la puntuación máxima, gana el juego
                if (puntuacion == 10) {
                    System.out.println("¡Felicidades! Has ganado el juego.");
                    break;
                }
            } catch (IllegalArgumentException e) {
                // Captura cualquier excepción relacionada con la entrada inválida del usuario
                System.out.println(e.getMessage());
            }
        }
    }

    // Método que inicializa el tablero, coloca la serpiente y la comida
    private static void inicializarJuego() {
        // Rellena el tablero con espacios vacíos
        for (int i = 0; i < ALTO; i++) {
            for (int j = 0; j < ANCHO; j++) {
                tablero[i][j] = VACIO;
            }
        }
        // Coloca la serpiente en el centro del tablero
        serpienteX[0] = ALTO / 2;
        serpienteY[0] = ANCHO / 2;
        tablero[serpienteX[0]][serpienteY[0]] = SERPIENTE;

        // Coloca la comida en una posición aleatoria del tablero
        colocarComida();
    }

    // Método que imprime el tablero en la consola
    private static void imprimirTablero() {
        // Imprime la fila superior de la pared
        for (int i = 0; i < ANCHO + 2; i++) System.out.print(PARED);
        System.out.println();

        // Imprime cada fila del tablero
        for (int i = 0; i < ALTO; i++) {
            System.out.print(PARED);  // Imprime la pared izquierda
            for (int j = 0; j < ANCHO; j++) {
                // Imprime el contenido de cada celda (vacío, serpiente o comida)
                System.out.print(tablero[i][j]);
            }
            System.out.print(PARED);  // Imprime la pared derecha
            System.out.println();     // Salto de línea después de cada fila
        }

        // Imprime la fila inferior de la pared
        for (int i = 0; i < ANCHO + 2; i++) System.out.print(PARED);
        System.out.println();
    }

    // Método que coloca la comida en una posición aleatoria en el tablero
    private static void colocarComida() {
        do {
            // Genera coordenadas aleatorias para la comida
            comidaX = (int) (Math.random() * ALTO);
            comidaY = (int) (Math.random() * ANCHO);
        } while (tablero[comidaX][comidaY] != VACIO);  // Asegura que la comida no caiga sobre la serpiente
        tablero[comidaX][comidaY] = COMIDA;            // Coloca la comida en el tablero
    }

    // Método que mueve la serpiente en la dirección indicada por el jugador
    private static boolean moverSerpiente(char direccion) {
        // Variables que almacenan las nuevas coordenadas de la cabeza de la serpiente
        int nuevaX = serpienteX[0];
        int nuevaY = serpienteY[0];

        // Actualiza las coordenadas de la cabeza según la dirección
        switch (direccion) {
            case 'W': nuevaX--; break;  // Arriba
            case 'S': nuevaX++; break;  // Abajo
            case 'A': nuevaY--; break;  // Izquierda
            case 'D': nuevaY++; break;  // Derecha
            default: return true;       // Si se ingresa una tecla no válida, no se hace nada
        }

        // Verifica si la serpiente ha chocado con una pared o consigo misma
        if (nuevaX < 0 || nuevaX >= ALTO || nuevaY < 0 || nuevaY >= ANCHO || tablero[nuevaX][nuevaY] == SERPIENTE) {
            return false;  // Si hay colisión, devuelve false (termina el juego)
        }

        // Si la serpiente come la comida
        if (tablero[nuevaX][nuevaY] == COMIDA) {
            longitudSerpiente++;   // Aumenta la longitud de la serpiente
            puntuacion++;          // Incrementa la puntuación
            colocarComida();       // Coloca una nueva comida en el tablero
        } else {
            // Borra la última parte del cuerpo de la serpiente
            tablero[serpienteX[longitudSerpiente - 1]][serpienteY[longitudSerpiente - 1]] = VACIO;
        }

        // Mueve cada parte de la serpiente hacia adelante (actualiza las posiciones)
        for (int i = longitudSerpiente - 1; i > 0; i--) {
            serpienteX[i] = serpienteX[i - 1];
            serpienteY[i] = serpienteY[i - 1];
        }

        // Actualiza la posición de la cabeza de la serpiente
        serpienteX[0] = nuevaX;
        serpienteY[0] = nuevaY;

        // Coloca la cabeza de la serpiente en su nueva posición
        tablero[serpienteX[0]][serpienteY[0]] = SERPIENTE;
        return true;  // Devuelve true si el movimiento fue exitoso
    }
}