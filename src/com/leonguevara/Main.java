package com.leonguevara;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int option;

	    // El programa mostrará el menú y ejecutará las acciones correspondientes mientras se elijan las opciones
        // 1 (Captura de alumnos) o 2 (Listado de alumnos).  Si se elige la opción 3 o se presiona cualquier otro
        // carácter, se terminará la ejecución del programa.
        do {
            showMenu();
            option = input.nextInt();

            if (option == 1) {
                // Si el usuario eligió la opción 1, procedemos a registrar a los alumnos.
                registerStudents();
            } else if (option == 2) {
                // Si el usuario eligió la opción 2, procedemos a mostrar los alumnos en pantalla.
                listStudents();
            }

        } while (option == 1 || option == 2);

        System.out.println("Gracias por utilizar nuestro sistema de registro.\nVuelva pronto.");
    }

    // Este método imprime el menú.
    protected static void showMenu() {
        System.out.println("Bienvenido al registro de alumnos.\nPor favor introduzca una opción del menú:\n\n");
        System.out.println("1.  Registrar alumnos.\n2.  Mostrar registro.\n3.  Salir.\n");
        System.out.print("¿Opción? ");
    }

    // Aquí se le pregunta al usuario cuántos alumnos se can a capturar en la corrida, se guardan dichos
    // alumnos en un arreglo evitando duplicidades de nombre y/o matrícula y, una vez que se termina la
    // captura en pantalla, se le envía el arreglo a la función crearArchivo para que almacene los datos
    // en un archivo de texto.
    protected static void registerStudents() {
        int numAlum;
        String[] studentsRecords;
        Scanner input = new Scanner(System.in);

        System.out.print("¿Cuántos alumnos vas a capturar? ");
        numAlum = input.nextInt();

        // Si el número de alumnos a capturar es un entero positivo, continuamos con el registro de alumnos; de
        // lo contrario, lanzamos un mensaje de error y salimos del método.
        if (numAlum > 0) {
            studentsRecords = new String[numAlum];
            int registeredStudents = 0;
            String studentName;
            String studentID;
            String fileName;

            while (registeredStudents < numAlum) {
                System.out.print("Nombre del alumno " + (numAlum + 1) + ": ");
                studentName = input.nextLine();
                System.out.print("Matrícula del alumno " + (numAlum + 1) + ": ");
                studentID = input.nextLine();

                if (!alreadyRegistered(studentName, studentID, studentsRecords)){
                    studentsRecords[registeredStudents] = studentID + "|" + studentName;
                    registeredStudents++;
                } else {
                    System.out.println("Los datos ingresados ya estaban cargados.  Favor de ingresar otro alumno.");
                }
            }

            System.out.print("\nNombre del archivo en el que se guardarán los alumnos: ");
            fileName = input.nextLine();

            crearArchivo(fileName, studentsRecords);
        } else {
            System.out.println("El número de alumnos a capturar debe ser un entero positivo.  Regresamos al menú.\n");
        }
    }

    // Aquí se le pide al usuario el nombre del archivo que contiene la lista a mostrar.
    // Si el archivo especificado no existe, se envía el mensaje de error al usuario.  Si el archivo existe, es
    // procesado línea por línea para mostrar los registros capturados.
    protected static void listStudents() {
        File file;
        String fileName;
        Scanner input = new Scanner(System.in);

        System.out.print("Nombre del archivo a leer: ");
        fileName = input.nextLine();
        file = new File(fileName);

        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String record;
            int i = 0;

            do {
                record = reader.readLine();
                if (record != null) {
                    String[] data = record.split("|");
                    System.out.println( ++i + ". Matrícula: " + data[0] + ". Nombre: " + data[1]);
                }
            } while (record != null);
        } else {
            System.out.println("El archivo especificado no existe.  Regresamos al menú.");
        }
    }

    // Este método valida que una matrícula y un nombre no estén ya capturados en el arreglo enviado. Recorreremos
    // el arreglo de principio a fin, o hasta encontrar una coincidencia de nombre o matrícula.  Por conveniencia,
    // el alumno está registrado en el formato "Matrícula|Nombre" y usaremos la función split para separarlos en dos
    // cadenas individuales.  Este método regresa un boolean indicando si el alumno ya está capturado en el arreglo.
    protected static boolean alreadyRegistered(String name, String studentID, String[] students) {
        boolean found = false;
        int student = 0;

        while (!found && student < students.length) {
            String[] parts = students[0].split("|");

            if (parts[0].toLowerCase() == studentID.toLowerCase() || parts[1].toLowerCase() == name.toLowerCase()) {
                found = true;
            } else {
                student++;
            }
        }
    }

    // Este método crea el archivo en el que se resguardarán los datos de los alumnos.  Recibe el nombre del archivo
    // e intenta crearlo.  Si por algún motivo no puede hacerlo, lanza un mensaje de error a la pantalla.  En caso de
    // lograr guardar con exito los registros, también lanza un mensaje notificándolo.  Si el archivo ya existe,
    // pide que se le de otro nombre al archivo.
    protected static void crearArchivo(String fileName, String[] records) {
        File file = new File(fileName);
        Scanner input = new Scanner(System.in);
        boolean success = true;

        while (file.exists()) {
            System.out.print("El nombre de archivo ya existe.  Favor de proporcionar otro: ");
            fileName = input.nextLine();
            file = new File(fileName);
        }

        try {
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);

            for (int i = 0; i < records.length; i++) {
                printWriter.println(records[i]);
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            System.out.println("Cause: " + ex.getCause());
            success = false;
        }

        if (success) {
            System.out.println("Registros guardados con éxito en el archivo " + fileName);
        } else {
            System.out.println("Los registros no pudieron guardarse en el archivo " + fileName);
        }
    }
}
