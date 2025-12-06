//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Spreadsheet.API;
import SpreadsheetCell.Cell;

import java.util.Scanner;
import java.io.IOException;

/*
~~IMPORTANT INFORMATION~~

The export and Import functions are independent of the Spreadsheet.Spreadsheet Data Structure.

The only functions dependent on the DS are : getCell, editCell, emptyCell, getRowRange
                                             getColRange

The DS chosen is two Maps of SkipLists. The structure that has a SkipList of rows
    (so for example the Map has the key "1" for row 1 and a SkipList of column letters),
    has been provided with a custom comparator in order to achieve a Spreadsheet.Spreadsheet order
    instead of an alphabetical one (so instead of A,AA,AB,AAA,...,B,... the order is
    A,B,...,AA,AB,...,AAA etc).

This ensures a search/delete/insert of O(log n), where n = number of items in the SkipLists.

Range retrieval is O(log(n + k)), were n = number of items in the SkipLists and k = number of
    existing cells in the range.

This comes at the cost of 2 insertions and deletions (but only 1 search), since we
    have to update 2 structures, but its very efficient for obtaining ranges,
    supposing we will have a sparse Spreadsheet.Spreadsheet most of the time.

Spacewise, the cells are stored once, so the space complexity is O(logN), N = total number
    of cells, with the overhead of two maps and two SkipLists.

At the end of this file you can find a command list to enter to the program execution in order
    to create the example provided:
    [A1](=C1+C2) [B1](4)     [C1](1)
                             [C2](2)
                 [B3](TOTAL) [C3](=A1+B1)

TODO: Guard implementation for Cell Content and Coordinates.
TODO: Recreate ghost nodes if the creation of another node in their place fails, probably through update calculation
      in formulas.
 */

public class Main{
    static void instructions(){
        System.out.println("/_____________________________\\");
        System.out.println("|> 1: Import file.            | ");
        System.out.println("|> 2: Export file.            | ");
        System.out.println("|> 3: Get the value of a cell.| ");
        System.out.println("|> 4: Set the value of a cell.| ");
        System.out.println("|> 5: Empty a cell.           | ");
        System.out.println("|> 6: Print the Spreadsheet.  | "); //Not working.
        System.out.println("|> 7: Exit Program.           | ");
        System.out.println("\\~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/");
        System.out.print  ("> ");
    }

    public static void main(String[] args) throws IOException{
        int     choice = 0;
        boolean exit   = false;
        Scanner sc     = new Scanner(System.in);

        API spreadsheetController = new API();

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
                        String content = retrievedCell.getStringContent();

                        System.out.println("Cell Content: " + content);

                        if(content.charAt(0) == '='){
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
                        System.out.println("Cell [" + newCell.getCoordinates() + "] created.");
                    }
                }
                case 5 -> {
                    System.out.print("Cell coordinates: ");
                    String coords = sc.nextLine();

                    spreadsheetController.emptyCell(coords);
                }
                case 6 -> {
                    //spreadsheetController.printSpreadsheet();
                }
                case 7 -> {
                    exit = true;
                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }
    }
}

/*
After you execute the program, copy-paste the following text and press enter:

4
B1
4

4
A1
=C1+C2

4
C1
1

4
C2
2

4
B3
TOTAL

4
C3
=A1+B1

2
ex.s2v

7

-------

4
A1
=A2

4
A2
=A3

4
A3
=A1

3
A3

 */