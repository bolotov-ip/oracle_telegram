package com.bolotov.oraclebot.service;

import com.bolotov.oraclebot.exception.OracleServiceException;
import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.OracleCategory;
import com.bolotov.oraclebot.model.Purchase;
import com.bolotov.oraclebot.model.User;

import java.util.List;

public interface OracleService {

    public Purchase purchase(User user, Oracle product) throws OracleServiceException;

    public List<Purchase> getAllFreePurchase();

    public Purchase getPurchaseById(Long id);

    public Purchase selectPurchaseForAnswer(User user, Long idPurchase);

    public List<Purchase> getSelectedPurchase(User user);

    public void oracle(User oracleUser, Purchase purchase) throws OracleServiceException;

    public List<Purchase> getFreePurchase();

    public List<Purchase> getTargetPurchase();

    public List<OracleCategory> getCategoriesByParent(Long parentId);

    public OracleCategory getCategoryById(Long id);

    public Oracle getOracleById(Long id);

    public List<Oracle> getOraclesByCategory(OracleCategory oracleCategory);

    public void donePurchase(Purchase purchase);

    public void unselectPurchase(Long purchaseId) throws OracleServiceException;

}
