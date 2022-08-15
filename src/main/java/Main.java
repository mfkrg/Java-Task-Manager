package main.java;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "qwertyqwerty";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        Connection connect = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        while (true) {
            System.out.println("================================================");
            System.out.println("1. Показать список всех невыполненных задач");
            System.out.println("2. Пометить задачу как выполненную");
            System.out.println("3. Пометить задачу как невыполненную");
            System.out.println("4. Создать новую задачу");
            System.out.println("5. Показать список всех-всех задач");
            System.out.println("6. Выйти");
            System.out.println("================================================");
            System.out.println("");

            int command = sc.nextInt();

            if (command == 1) {
                // request db
                Statement statement = connect.createStatement();
                String SQL_SELECT_TASKS = "select * from tasks where state = 'IN_PROCESS' order by id";
                //contains result
                ResultSet res = statement.executeQuery(SQL_SELECT_TASKS);
                while (res.next()) {
                    System.out.println(res.getInt("id") + " " + res.getString("name") + " " + ANSI_YELLOW+res.getString("state")+ANSI_RESET);
                }
            }

            if (command == 2) {
                Statement statement = connect.createStatement();
                String SQL_SELECT_TASKS = "select * from tasks where state = 'IN_PROCESS' order by id";
                ResultSet res = statement.executeQuery(SQL_SELECT_TASKS);
                while (res.next()) {
                    System.out.println(res.getInt("id") + " " + res.getString("name") + " " + ANSI_YELLOW+res.getString("state")+ANSI_RESET);
                }
                String SQL_MARK_AS_DONE = "update tasks set state = 'DONE' where id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(SQL_MARK_AS_DONE);
                System.out.println("--------------------");
                System.out.println("Введите ID задачи: ");
                int taskID = sc.nextInt();
                preparedStatement.setInt(1, taskID);
                preparedStatement.executeUpdate();
            }

            if (command == 3) {
                Statement statement = connect.createStatement();
                String SQL_SELECT_PROCESSING_TASKS = "select * from tasks where state = 'DONE' order by id";
                ResultSet res = statement.executeQuery(SQL_SELECT_PROCESSING_TASKS);
                while (res.next()) {
                    System.out.println(res.getInt("id") + " " + res.getString("name") + " " + res.getString("state"));
                }
                String SQL_MARK_AS_PROCESSING = "update tasks set state = 'IN_PROCESS' where id = ?";
                PreparedStatement preparedStatement = connect.prepareStatement(SQL_MARK_AS_PROCESSING);
                System.out.println("--------------------");
                System.out.println("Введите ID задачи: ");
                int taskID = sc.nextInt();
                preparedStatement.setInt(1, taskID);
                preparedStatement.executeUpdate();
            }

            if (command == 4) {
                String SQL_CREATE_NEW_TASK = "insert into tasks (name, state) values (?, 'IN_PROCESS')";
                PreparedStatement preparedStatement = connect.prepareStatement(SQL_CREATE_NEW_TASK);
                System.out.println("------------------------");
                System.out.println("Введите название задачи:");
                sc.nextLine();
                String taskName = sc.nextLine();
                preparedStatement.setString(1, taskName);
                preparedStatement.executeUpdate();
            }

            if (command == 5) {
                Statement statement = connect.createStatement();
                String SQL_SELECT_ALL_TASKS = "select * from tasks order by id";
                ResultSet res = statement.executeQuery(SQL_SELECT_ALL_TASKS);
                while (res.next()) {
                    System.out.println(res.getInt("id") + " " + res.getString("name") + " " + res.getString("state"));
                }
            }

            if (command == 6) {
                System.exit(0);
            }

            if (command > 6) {
                System.err.println("Ошибочка! Проверь все еще раз.");
            }

        }
    }
}
