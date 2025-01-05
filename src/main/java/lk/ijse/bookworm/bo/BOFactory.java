package lk.ijse.bookworm.bo;

import lk.ijse.bookworm.bo.custom.impl.*;

public class BOFactory {

    private static BOFactory bOFactory;
    private BOFactory(){}

    public static BOFactory getInstance(){
        return (bOFactory==null)?bOFactory=new BOFactory():bOFactory;
    }

    public enum BOTypes{
       USER,ADMIN,BRANCH,BOOK,BOOK_TRANSACTION
    }

    public SuperBO getBO(BOTypes boTypes){
        switch(boTypes){
            case USER:
                return new UserBOImpl();
            case ADMIN:
                return new AdminBOImpl();
            case BRANCH:
                return new BranchBOImpl();
            case BOOK:
                return new BookBOImpl();
            case BOOK_TRANSACTION:
                return new BookTransactionBOImpl();
            default:
                return null;
        }
    }




}

