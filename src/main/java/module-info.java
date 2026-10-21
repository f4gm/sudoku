module com.example.sudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudoku to javafx.fxml;
    opens com.example.sudoku.controller to javafx.fxml;
    opens com.example.sudoku.model to javafx.fxml;

    exports com.example.sudoku;
    exports com.example.sudoku.model;
    exports com.example.sudoku.service.navigation;
    opens com.example.sudoku.service.navigation to javafx.fxml;
    exports com.example.sudoku.service.timer;
    opens com.example.sudoku.service.timer to javafx.fxml;
}