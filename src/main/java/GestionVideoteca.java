import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class GestionVideoteca {
    public static void main(String[] args) {
        // Esta es la dirección de tu "almacén" (Local)
        String uri = "mongodb+srv://admin:Usuario.25.@mongodb.o7p1hl1.mongodb.net/?appName=MongoDB";

        // Intentamos abrir el túnel
        try (MongoClient cliente = MongoClients.create(uri)) {

            // Elegimos la habitación (Base de datos)
            MongoDatabase baseDatos = cliente.getDatabase("MongoDB");

            // Si llegamos aquí sin errores, es que funciona
            System.out.println("¡Conectado correctamente!");

            // 1. Entramos al "archivador" llamado peliculas
            // Si no existe, MongoDB lo crea automáticamente al insertar algo
            MongoCollection<Document> colPeliculas = baseDatos.getCollection("peliculas");
            MongoCollection<Document> colPrestamos = baseDatos.getCollection("prestamos");

            // 1. Preparamos el teclado y la variable
            Scanner sc = new Scanner(System.in);
            int opcion;

            // ========== MENÚ PRINCIPAL ==========
            do {
                System.out.println("\n---- MENÚ VIDEOTECA ----");
                System.out.println("1. Ver catálogo de Películas");
                System.out.println("2. Añadir nueva Película");
                System.out.println("3. Registrar un Préstamo");
                System.out.println("4. Ver lista de Préstamos");
                System.out.println("5. Devolver Película");
                System.out.println("6. Borrar Película");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");

                // Leemos la opción
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiamos el buffer
                System.out.println();

                // Procesamos la opción
                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    case 1:
                        System.out.println("--- LISTA DE PELÍCULAS ---");

                        // Recorremos la colección
                        for (Document doc : colPeliculas.find()) {
                            // Imprimimos el campo "titulo"
                            System.out.println("- " + doc.getString("titulo") + " (Género: " + doc.getString("genero") + ")" +
                                    (doc.getBoolean("disponible", true) ? " [Disponible]" : " [No Disponible]"));
                        }
                        System.out.println();
                        System.out.println("Presiona ENTER para continuar...");
                        sc.nextLine();
                        break;
                    case 2:
                        System.out.println("--- AÑADIR NUEVA PELÍCULA ---");
                        // Añadir nueva película con título y género
                        System.out.print("Introduce el título de la nueva película: ");
                        String titulo = sc.nextLine();
                        System.out.print("Introduce el género de la película: ");
                        String genero = sc.nextLine();

                        // Creamos el documento y lo insertamos
                        Document nuevaPelicula = new Document("titulo", titulo)
                                .append("genero", genero)
                                .append("disponible", true);
                        colPeliculas.insertOne(nuevaPelicula);

                        System.out.println("Película añadida correctamente.");

                        System.out.println();
                        System.out.println("Presiona ENTER para continuar...");
                        sc.nextLine();
                        break;
                    case 3:
                        // Registrar un préstamo
                        System.out.println("--- REGISTRAR PRÉSTAMO ---");

                        System.out.print("Introduce el nombre del cliente: ");
                        String clienteNombre = sc.nextLine();
                        System.out.print("Introduce el título de la película: ");
                        String peliculaTitulo = sc.nextLine();

                        // Verificamos si la película está disponible
                        Document pelicula = colPeliculas.find(new Document("titulo", peliculaTitulo)).first();
                        if (pelicula != null && pelicula.getBoolean("disponible", true)) {
                            // Crear el documento de préstamo
                            Document nuevoPrestamo = new Document("cliente", clienteNombre)
                                    .append("pelicula", peliculaTitulo);
                            colPrestamos.insertOne(nuevoPrestamo);
                            // Actualizar la disponibilidad de la película
                            colPeliculas.updateOne(new Document("titulo", peliculaTitulo),
                                    new Document("$set", new Document("disponible", false)));

                            System.out.println("Préstamo registrado correctamente.");
                        } else {
                            System.out.println("La película no está disponible para préstamo.");
                        }

                        System.out.println();
                        System.out.println("Presiona ENTER para continuar...");
                        sc.nextLine();
                        break;
                    case 4:
                        System.out.println("--- LISTA DE PRÉSTAMOS ---");

                        // Recorremos la colección de préstamos
                        for (Document doc : colPrestamos.find()) {
                            // Imprimimos el cliente y la película prestada
                            System.out.println("- Cliente: " + doc.getString("cliente") + ", Película: " + doc.getString("pelicula"));
                        }

                        System.out.println();
                        System.out.println("Presiona ENTER para continuar...");
                        sc.nextLine();
                        break;
                    case 5:
                        // Devolver película
                        System.out.println("--- DEVOLVER PELÍCULA ---");

                        System.out.print("Introduce el título de la película a devolver: ");
                        String devolverTitulo = sc.nextLine();

                        // Buscar el préstamo correspondiente
                        Document prestamo = colPrestamos.find(new Document("pelicula", devolverTitulo)).first();

                        // Si existe, lo borramos y actualizamos la película
                        if (prestamo != null) {
                            // Borrar el préstamo
                            colPrestamos.deleteOne(prestamo);
                            // Actualizar la disponibilidad de la película
                            colPeliculas.updateOne(new Document("titulo", devolverTitulo),
                                    new Document("$set", new Document("disponible", true)));

                            System.out.println("Película devuelta correctamente.");

                        } else {
                            System.out.println("No se encontró un préstamo para esa película.");
                        }

                        System.out.println();
                        System.out.println("Presiona ENTER para continuar...");
                        sc.nextLine();
                        break;
                    case 6:
                        // Borrar película
                        System.out.println("--- BORRAR PELÍCULA ---");

                        System.out.print("Introduce el título de la película a borrar: ");
                        String borrarTitulo = sc.nextLine();
                        // Borrar la película
                        colPeliculas.deleteOne(new Document("titulo", borrarTitulo));
                        System.out.println("Película borrada correctamente.");

                        System.out.println();
                        System.out.println("Presiona ENTER para continuar...");
                        sc.nextLine();
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        System.out.println();
                }
            }while (opcion != 0) ;

            System.out.println("Programa finalizado.");
        }catch(Exception e){
            // Si la dirección está mal o MongoDB está apagado, saltará esto
            System.out.println("Algo ha salido mal: " + e.getMessage());
        }
    }
}