package com.bolotov.oraclebot.service;

import com.bolotov.oraclebot.exception.OracleServiceException;
import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.OracleCategory;
import com.bolotov.oraclebot.model.Purchase;
import com.bolotov.oraclebot.model.User;

import java.util.List;

public interface OracleService {

    public Purchase purchaseFree(User user, Oracle product) throws OracleServiceException;

    public Purchase purchaseTarget(User user, Oracle product, User oracleUser) throws OracleServiceException;

    public void oracle(User oracleUser, Purchase purchase) throws OracleServiceException;

    public List<Purchase> getFreePurchase();

    public List<Purchase> getTargetPurchase();

    public List<OracleCategory> getCategoriesByParent(Long parentId);

    public OracleCategory getCategoryById(Long id);
}
