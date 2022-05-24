package client;

import common.Request;
import common.Response;
import common.Utils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

import static client.Config.*;
import static common.RequestType.*;
import static common.ParamType.*;

public class Main {

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             InputStream input = socket.getInputStream();
             OutputStream output  = socket.getOutputStream()) {
            System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
            String command = scanner.nextLine();
            if ("exit".equals(command)) {
                Utils.sendReq(output, new Request(EXIT, null));
                return;
            }
            int action = Integer.parseInt(command);
            switch (action) {
                case 1: // get a file
                    getFile(input, output);
                    break;
                case 2: // create a file
                    createFile(input, output);
                    break;
                case 3:
                    deleteFile(input, output);
                    break;
                default:
                    System.out.println("Unknown action!");
            }
        } catch (UnknownHostException unknownHostException) {
            System.err.println("[Client ERR] Unknown host: " + address);
        } catch (IOException ioException) {
            System.err.println("[Client ERR] I/O error on main(): " + ioException);
            System.err.println("[Client] Restarting...");
            main(args);
        }
    }

    private static void getFile(InputStream input, OutputStream output) {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        Request request = new Request(GET, scanner.nextInt() == 1 ? BY_NAME : BY_ID);
        scanner.nextLine();
        switch (request.by) {
            case BY_NAME:
                System.out.print("Enter name: ");
                request.fileName = scanner.next();
                break;
            case BY_ID:
                System.out.print("Enter id: ");
                request.fileID = scanner.nextInt();
                break;
        }
        scanner.nextLine();
        try {
            Utils.sendReq(output, request);
            Response response = Utils.receiveResp(input);
            switch (response.code) {
                case 200:
                    System.out.print("The file was downloaded! Specify a name for it: ");
                    saveFile(response, scanner.nextLine());
                    break;
                case 404:
                    System.out.println("The response says that this file is not found!");
                    break;
                default:
                    System.err.println("Unknown response: " + response);
            }
        } catch (IOException ioException) {
            System.err.println("[Client ERR] I/O error on getFile(): " + ioException);
        } catch (ClassNotFoundException e) {
            System.err.println("[Client ERR] ClassNotFoundException on getFile(): " + e);
        }
    }

    private static void saveFile(Response response, String fileName) {
        File file = new File(dataPath + fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(file.toPath(), response.rawData);
            System.out.println("File saved on the hard drive!");
        } catch (IOException ioException) {
            System.err.println("[Client ERR] I/O error on saveFile(): " + ioException);
        }
    }

    private static void createFile(InputStream input, OutputStream output) {
        System.out.print("Enter the name of the file: ");
        String fileName = scanner.nextLine();
        File file = new File(dataPath + fileName);
        if (file.exists()) {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                System.out.print("Enter name of the file to be saved on server: ");
                String fileNameOnServer = scanner.nextLine().split("\\s+")[0];
                Request req = new Request(PUT, "".equals(fileNameOnServer) ? BY_ID : BY_NAME, fileNameOnServer, fileContent, -1);
                Utils.sendReq(output, req);
                Response response = Utils.receiveResp(input);
                switch (response.code) {
                    case 200:
                        System.out.println("Response says that file is saved! ID = " + response.fileID);
                        break;
                    case 403:
                        System.out.println("The response says that creating the file was forbidden!");
                        break;
                    default:
                        System.err.println("Unknown response: " + response);
                }
            } catch (IOException ioException) {
                System.err.println("[Client ERR] I/O error on createFile(): " + ioException);
            } catch (ClassNotFoundException e) {
                System.err.println("[Client ERR] ClassNotFoundException on createFile(): " + e);
            }
        } else {
            System.err.println("[Client ERR] The file " + fileName + " does not exist!");
        }
    }

    private static void deleteFile(InputStream input, OutputStream output) {
        System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
        Request request = new Request(DEL, scanner.nextInt() == 1 ? BY_NAME : BY_ID);
        scanner.nextLine();
        switch (request.by) {
            case BY_NAME:
                System.out.print("Enter name: ");
                request.fileName = scanner.next();
                break;
            case BY_ID:
                System.out.print("Enter id: ");
                request.fileID = scanner.nextInt();
                scanner.nextLine();
                break;
            default:
        }
        try {
            Utils.sendReq(output, request);
            Response response = Utils.receiveResp(input);
            switch (response.code) {
                case 200:
                    System.out.println("The response says that this file was deleted successfully!");
                    break;
                case 404:
                    System.out.println("The response says that this file is not found!");
                    break;
                default:
                    System.err.println("Unknown response: " + response);
            }
        } catch (IOException ioException) {
            System.err.println("[Client ERR] I/O error on deleteFile(): " + ioException);
        } catch (ClassNotFoundException e) {
            System.err.println("[Client ERR] ClassNotFoundException on deleteFile(): " + e);
        }
    }
}
