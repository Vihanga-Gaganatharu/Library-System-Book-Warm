package lk.ijse.bookworm.controller;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.bookworm.bo.BOFactory;
import lk.ijse.bookworm.bo.custom.BookBO;
import lk.ijse.bookworm.bo.custom.BookTransactionBO;
import lk.ijse.bookworm.bo.custom.impl.UserBOImpl;
import lk.ijse.bookworm.dto.BorrowBookDto;
import lk.ijse.bookworm.tm.BookTm;
import lk.ijse.bookworm.tm.UserBorrowBookTm;

import java.time.LocalDate;
import java.util.List;

public class UserBorrowBookFormController {



    @FXML
    private MFXFilterComboBox<String> cmbBookID;

    @FXML
    private TableColumn<UserBorrowBookTm, String> colBookID;

    @FXML
    private TableColumn<UserBorrowBookTm, String> colBorrowDate;

    @FXML
    private TableColumn<UserBorrowBookTm, String> colID;

    @FXML
    private TableColumn<UserBorrowBookTm, String> colReturnDate;

    @FXML
    private TableColumn<UserBorrowBookTm, String> colReturned;

    @FXML
    private MFXDatePicker dpReturnDate;

    @FXML
    private TableView<UserBorrowBookTm> tblBorrowBook;

    @FXML
    private MFXTextField txtSearch;

    BookBO bookBO = (BookBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.BOOK);

    BookTransactionBO bookTransactionBO = (BookTransactionBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.BOOK_TRANSACTION);


    public void initialize() {
        loadBookIds();
        setCellValueFactory();
        loadBorrowedBooks();
    }

    private void loadBookIds() {
        cmbBookID.getItems().clear();
        bookBO.getAllBooks().forEach(dto -> {
            boolean availability = dto.isAvailability();
            if (availability) {
                cmbBookID.getItems().add(dto.getBookId());
            }
        });
    }

    private void loadBorrowedBooks() {
        List<BorrowBookDto> allBorrowedBooks = bookTransactionBO.getAllBorrowedBooks();
        ObservableList<UserBorrowBookTm> observableList = FXCollections.observableArrayList();
        for (BorrowBookDto dto : allBorrowedBooks) {
            if (dto.getUserId().equals(UserBOImpl.loggedUser.getEmail())){
                observableList.add(new UserBorrowBookTm(
                        dto.getId(),
                        dto.getBookId(),
                        dto.getBorrowDate(),
                        dto.getReturnDate(),
                        dto.isReturned()?"Yes":"No"
                ));
            }
        }
        tblBorrowBook.setItems(observableList);
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colReturned.setCellValueFactory(new PropertyValueFactory<>("isReturned"));
    }


    @FXML
    void btnBorrowOnAction(ActionEvent event) {

        boolean isValidated = validateFields();

        if (!isValidated){
            return;
        }


        String bookID = cmbBookID.getText();
        LocalDate returnDate = dpReturnDate.getValue();


        BorrowBookDto borrowBookDto = new BorrowBookDto(UserBOImpl.loggedUser.getEmail(),bookID,returnDate.toString());

        boolean isSaved = bookTransactionBO.saveBorrowedBook(borrowBookDto);

        if (isSaved){
            new Alert(Alert.AlertType.CONFIRMATION, "Book Borrowed").show();
            loadBorrowedBooks();
            loadBookIds();
            clearFields();
        }else {
            new Alert(Alert.AlertType.ERROR, "Book Not Borrowed").show();
        }


    }

    private void clearFields() {
        cmbBookID.clear();
        dpReturnDate.clear();
    }

    private boolean validateFields() {

        boolean isBookIDEmpty = cmbBookID.getText().isEmpty();

        if (isBookIDEmpty){
            cmbBookID.requestFocus();
            cmbBookID.getStyleClass().add("mfx-combo-box-error");
            return false;
        }

        cmbBookID.getStyleClass().remove("mfx-combo-box-error");


        boolean isReturnDateEmpty = dpReturnDate.getText().isEmpty();

        if (isReturnDateEmpty){
            dpReturnDate.requestFocus();
            dpReturnDate.getStyleClass().add("mfx-dp-picker-error");
            return false;
        }

        dpReturnDate.getStyleClass().remove("mfx-dp-picker-error");

        return true;
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadBorrowedBooks();
        } else {
            FilteredList<UserBorrowBookTm> filteredData = new FilteredList<>(tblBorrowBook.getItems(), userBorrowBookTm ->
                    userBorrowBookTm.getId().toLowerCase().contains(keyword) ||
                            userBorrowBookTm.getBookId().toLowerCase().contains(keyword) ||
                            userBorrowBookTm.getBorrowDate().toLowerCase().contains(keyword) ||
                            userBorrowBookTm.getReturnDate().toLowerCase().contains(keyword) ||
                            userBorrowBookTm.getIsReturned().toLowerCase().contains(keyword)
            );
            tblBorrowBook.setItems(filteredData);
        }
    }


}

