package Main;

import javafx.scene.control.Alert;

public class AlertCreator {
    public  AlertCreator(String msg, Alert.AlertType type){
        Alert alert = new Alert(type);
        if(type == Alert.AlertType.ERROR){
            alert.setTitle("Error occured!");
            alert.setHeaderText("An Error has occured. Check console!");
            alert.setContentText(msg);
            alert.showAndWait();
        }else if(type == Alert.AlertType.CONFIRMATION){
            alert.setTitle("Confirmation!");
            alert.setHeaderText("No errors occured!");
            alert.setContentText(msg);
            alert.showAndWait();
        }
     }
}
