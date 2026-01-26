import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;

import SpreadsheetCell.Cell;
import Spreadsheet.SpreadsheetAPI;

public class Main{
    public static void main(String[] args) throws IOException, InterruptedException {
        int     choice = 0;
        boolean exit   = false;
        Scanner sc     = new Scanner(System.in);

        System.setErr(System.out);

        SpreadsheetAPI spreadsheetController = new SpreadsheetAPI();

        try{
            while(!exit){
                instructions();

                if(sc.hasNextInt()){
                    choice = sc.nextInt();
                    sc.nextLine();

                }
                else{
                    System.out.println("Invalid input. Please enter an integer.");
                    sc.nextLine();

                    continue;
                }

                switch(choice){
                    case 1 -> {
                        System.out.print("File path: ");
                        String path = sc.nextLine();

                        spreadsheetController.import_S2V(path);
                    }
                    case 2 -> {
                        System.out.print("File path: ");
                        String path = sc.nextLine();

                        spreadsheetController.export_S2V(path);
                    }
                    case 3 -> {
                        System.out.print("Cell coordinates: ");
                        String coords = sc.nextLine();

                        Cell retrievedCell = spreadsheetController.getCell(coords);

                        if(retrievedCell != null){
                            double value   = retrievedCell.getCellValue();
                            String content = retrievedCell.getCellContent();

                            System.out.println("Cell Content: " + content);

                            if(content.charAt(0) == '=' && content.length() > 1){
                                System.out.println("Cell Value: " + value);
                            }
                        }
                        else {
                            System.out.println("Cell Content: 0");
                        }
                    }
                    case 4 -> {
                        System.out.print("Cell coordinates: ");
                        String coords = sc.nextLine();
                        System.out.print("Cell content: ");
                        String content = sc.nextLine();

                        Cell newCell = spreadsheetController.setCell(coords, content);

                        if(newCell != null){
                            System.out.println("Cell [" + newCell.getCellCoordinates() + "] created.");
                        }
                    }
                    case 5 -> {
                        System.out.print("Cell coordinates: ");
                        String coords = sc.nextLine();

                        spreadsheetController.emptyCell(coords);
                    }
                    case 6 -> {
                        printSpreadsheet(spreadsheetController.getCellRows());
                    }
                    case 7 -> {
                        exit = true;
                    }
                    default -> {
                        System.out.println("Invalid choice");
                    }
                }
            }            
        } finally{
            sc.close();
        }
    }

    static void instructions(){
        String menu =
                """
                     _____________________________ \s
                    |> 1: Import file.            |\s
                    |> 2: Export file.            |\s
                    |> 3: Get the value of a cell.|\s
                    |> 4: Set the value of a cell.|\s
                    |> 5: Empty a cell.           |\s
                    |> 6: Print the Spreadsheet.  |\s
                    |> 7: Exit Program.           |\s
                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \s
                    >\s""";

        System.out.print(menu);
    }

    public static void printSpreadsheet(ArrayList<ArrayList<Cell>> cellRows){
        for(ArrayList<Cell> row : cellRows){
            boolean         printed = false;

            for(Cell cell : row){
                if (cell.getCellContent().length() > 1){
                    if ((cell.getCellContent().charAt(0) == '=')
                            &&  (cell.getCellContent().charAt(1) == '=')){

                        continue;
                    }
                    else if(cell.getCellContent().charAt(0) == '='){
                        System.out.print("[" + cell.getCellCoordinates() + "](" + cell.getCellContent() +
                                (",{=") + cell.getCellValue() + "}) ");

                        printed = true;
                    }
                    else{
                        System.out.print("[" + cell.getCellCoordinates() + "](" + cell.getCellContent() + (") "));

                        printed = true;
                    }
                }
                else{
                    System.out.print("[" + cell.getCellCoordinates() + "](" + cell.getCellContent() + (") "));

                    printed = true;
                }
            }

            if(printed){
                System.out.println();
            }
        }
    }
}