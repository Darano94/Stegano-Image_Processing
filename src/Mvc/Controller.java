package Mvc;

import AesEncryption.AdvancedEncryptionStandard;
import Main.AlertCreator;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //vars
    private final Model model;
    private FileChooser fc = new FileChooser();
    private Stage primaryStage;
    private Scene scene;

    @FXML
    private ImageView imgView;
    @FXML
    private Button btnExtract;
    @FXML
    private ImageView txtModifiedImage;
    @FXML
    private GridPane gridPaneToolbox;
    @FXML
    private TextArea txtPictureInfo;
    @FXML
    private Button btnBoxBlur;
    @FXML
    private Slider sliderKernelsize;
    @FXML
    private Label lblKernelsize;
    @FXML
    private Button btnHideMSG;
    @FXML
    private TextArea txtAreaInput;
    @FXML
    private RadioButton radioKey;
    @FXML
    private TextField txtKey;
    private AdvancedEncryptionStandard aes;

    //methods
    @FXML
    public void hideMessage() {
        String str = txtAreaInput.getText().trim();

        if (str == null || str.length() == 0 || txtKey.getText().length() == 0) {
            return;
        }


        //before here str must be encrypted
        byte[] secretKey = txtKey.getText().getBytes();
        aes = new AdvancedEncryptionStandard(secretKey);
        try {
            byte[] encryptedMessage = aes.encrypt(str);
            model.hideMessage(new String(encryptedMessage));
            new AlertCreator("Message has been hidden!", Alert.AlertType.CONFIRMATION);
        } catch (Exception e) {
            new AlertCreator("Message could not be hidden!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        txtAreaInput.setText("");
        //when decrypting image straight after hiding message
        Image image = SwingFXUtils.toFXImage(model.getModified_image(), null);
        txtModifiedImage.setImage(image);
    }

    @FXML
    public void extractMessage() {
        String result = null;

        if (model.getImage() == null || txtKey.getText().length() == 0) {
            return;
        }
        String s = model.extractMessage(model.getImage());

        //after here string has to be decrypted
        byte[] secretKey = txtKey.getText().getBytes();
        aes = new AdvancedEncryptionStandard(secretKey);
        try {
           String decryptedMessage = aes.decrypt(s.getBytes());
           result = decryptedMessage;

        } catch (Exception e) {
            new AlertCreator("No message could be extracted!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        txtPictureInfo.appendText("\nExtracted Message:\n_______________________________\n" + result);
    }

    @FXML
    public void rndKey_clicked() {
        if (!radioKey.isSelected())
            txtKey.setText("");
        else
            txtKey.setText(new String(AdvancedEncryptionStandard.createRandomKey(16)));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtAreaInput.setWrapText(true);
        txtPictureInfo.setWrapText(true);
        sliderKernelsize.setMax(30);
        lblKernelsize.textProperty().bind(Bindings.format("Kernelsize: %.2f", sliderKernelsize.valueProperty()));
        StringBuilder msg = new StringBuilder();
        radioKey.setSelected(true);
        txtKey.setText(new String(AdvancedEncryptionStandard.createRandomKey(16)));
    }

    @FXML
    public void openFile() {
        //reset TextArea for picture info
        txtPictureInfo.setText("");
        txtModifiedImage.setImage(null);
        this.fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image Files", "png", "jpg", "jpeg"));
        this.fc.setTitle("Open an Image");
        File selectedImage = this.fc.showOpenDialog(this.primaryStage);

        //Set the selected file
        try {
            BufferedImage chosenPic = ImageIO.read(selectedImage);
            model.setImage(chosenPic);
            model.setModified_image(chosenPic); //for reading data when loading image
            Image image = SwingFXUtils.toFXImage(model.getImage(), null);
            imgView.setImage(image);
            loadInfoTextArea();
        } catch (IOException e) {
            new AlertCreator("The file could not be opened!!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    @FXML
    public void closeSMIG() {
        System.exit(0);
    }

    @FXML
    public void saveImage() {
        if (model.getModified_image() != null) {
            this.fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Save Image", "png", "jpg", "jpeg"));
            this.fc.setTitle("Please specify the file format as well!");
            File file = this.fc.showSaveDialog(new Stage());

            if (file != null) {
                try {
                    BufferedImage bImage = model.getModified_image();

                    if (file.getName().matches(".*\\.(jpg|JPG|png)$")) {
                        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        System.out.println(file.getName() + extension);
                        if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))
                        // TODO: 14.02.2019 saving jpg without corrupting it
                        {
                            //ImageIO.write(bufferedImageRGB, extension, file);
                            throw new Exception(file.getName() + " has no valid file-extension. PNG required so far.");
                        } else
                            ImageIO.write(bImage, extension, file);
                    } else {
                        throw new Exception(file.getName() + " has no valid file-extension.");
                    }
                    new AlertCreator("Image saved!", Alert.AlertType.CONFIRMATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    new AlertCreator("Image could not be saved!", Alert.AlertType.ERROR);
                }
            }
        } else {
            new AlertCreator("Please open a file before!", Alert.AlertType.ERROR);
        }
    }


    // TODO: 14.02.2019 not working properyl (prob. should use Properties and proper bindings)
    @FXML
    public void resetImageview() {
        this.imgView = new ImageView();
    }

    @FXML
    public void boxBlur() {
//        model.boxBlurImage((int) sliderKernelsize.getValue());
//        txtModifiedImage.setImage(model.getModifiedImage());
    }

    private void loadInfoTextArea() {
        txtPictureInfo.appendText("Width: " + model.getWidth() + "\n");
        txtPictureInfo.appendText("Height: " + model.getHeight() + "\n");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Controller(Model model) {
        this.model = model;
    }
}
