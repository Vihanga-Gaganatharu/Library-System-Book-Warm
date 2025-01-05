package lk.ijse.bookworm.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.ijse.bookworm.bo.BOFactory;
import lk.ijse.bookworm.bo.custom.AdminBO;
import lk.ijse.bookworm.bo.custom.BranchBO;
import lk.ijse.bookworm.dto.AdminDto;
import lk.ijse.bookworm.dto.BranchDto;
import lk.ijse.bookworm.dto.UserDto;

import java.util.List;

public class BranchDataFormController {


    @FXML
    private MFXButton btnAction;

    @FXML
    private Label lblAction;

    @FXML
    private MFXTextField txtBranchAddress;

    @FXML
    private MFXTextField txtBranchName;

    @FXML
    private MFXTextField txtBranchID;



    @FXML
    private MFXComboBox<String> cmbAdmin;

    private BranchFormController branchFormController;


    BranchBO branchBO =(BranchBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.BRANCH);
    AdminBO adminBO = (AdminBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ADMIN);

    public void initialize(){
        loadAdmins();
    }

    private void loadAdmins() {
        List<AdminDto> adminDtoList = adminBO.getAllAdmin();
        for (AdminDto adminDto : adminDtoList) {
            cmbAdmin.getItems().add(adminDto.getUsername());
        }
    }


    @FXML
    void btnActionOnAction(ActionEvent event) {

        boolean isValidated = validateFields();

        if (!isValidated){
            return;
        }


        if (btnAction.getText().equals("Add")) {
            BranchDto branchDto = new BranchDto(txtBranchID.getText(), txtBranchName.getText(), txtBranchAddress.getText(), cmbAdmin.getValue());
            boolean isSaved = branchBO.saveBranch(branchDto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Branch Added Successfully").show();
                branchFormController.loadAllBranches();

                clearFields();
                closeTheWindow();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Add Branch").show();
            }
        } else {
            BranchDto branchDto = new BranchDto(txtBranchID.getText(), txtBranchName.getText(), txtBranchAddress.getText(), cmbAdmin.getText());
            boolean isUpdated = branchBO.updateBranch(branchDto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Branch Updated Successfully").show();

                branchFormController.loadAllBranches();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Update Branch").show();
            }
        }


    }

    private boolean validateFields() {

        boolean isBranchIDValid = txtBranchID.getText().matches("^BR[0-9]{3}$");

        if(!isBranchIDValid){
            txtBranchID.requestFocus();
            txtBranchID.getStyleClass().add("mfx-text-field-error");
            return false;
        }

        txtBranchID.getStyleClass().remove("mfx-text-field-error");

        boolean isBranchNameValid = txtBranchName.getText().matches("^[a-zA-Z0-9 .-_&]+$");

        if(!isBranchNameValid){
            txtBranchName.requestFocus();
            txtBranchName.getStyleClass().add("mfx-text-field-error");
            return false;
        }

        txtBranchName.getStyleClass().remove("mfx-text-field-error");

        boolean isBranchAddressValid = txtBranchAddress.getText().matches("^[a-zA-Z0-9,._#()/:;]+$");

        if(!isBranchAddressValid){
            txtBranchAddress.requestFocus();
            txtBranchAddress.getStyleClass().add("mfx-text-field-error");
            return false;
        }

        txtBranchAddress.getStyleClass().remove("mfx-text-field-error");

        boolean isCmbAdminValid = cmbAdmin.getText().isEmpty();

        if(isCmbAdminValid){
            cmbAdmin.requestFocus();
            cmbAdmin.getStyleClass().add("mfx-combo-box-error");
            return false;
        }

        cmbAdmin.getStyleClass().remove("mfx-combo-box-error");

        return true;
    }

    @FXML
    void btnCancel(ActionEvent event) {
        closeTheWindow();
    }


    public void clearFields(){
        txtBranchID.clear();
        txtBranchName.clear();
        txtBranchAddress.clear();
        cmbAdmin.clear();
    }


    private void closeTheWindow() {
        Stage userDataStage= (Stage) txtBranchName.getScene().getWindow();
        userDataStage.close();
    }

    public void setBranchFormController(BranchFormController branchFormController) {
        this.branchFormController = branchFormController;
    }

    public void loadBranchData(String branchID){
        BranchDto branchDto = branchBO.searchBranch(branchID);
        setFields(branchDto);
    }

    public void setBtnAndLblName(String action){
        btnAction.setText(action);
        lblAction.setText(action + " Branch");
    }

    private void setFields(BranchDto branchDto){
        txtBranchID.setText(branchDto.getBranchID());
        txtBranchName.setText(branchDto.getBranchName());
        txtBranchAddress.setText(branchDto.getAddress());
        cmbAdmin.setText(branchDto.getAdminID());

        //Disable Email Field
        txtBranchID.setEditable(false);
    }



}

