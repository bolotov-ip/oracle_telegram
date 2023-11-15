package com.bolotov.oraclebot.service.impl;

import com.bolotov.oraclebot.exception.OracleServiceException;
import com.bolotov.oraclebot.model.Oracle;
import com.bolotov.oraclebot.model.OracleCategory;
import com.bolotov.oraclebot.model.Purchase;
import com.bolotov.oraclebot.model.User;
import com.bolotov.oraclebot.repository.OracleCategoryRepository;
import com.bolotov.oraclebot.service.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OracleServiceImpl implements OracleService {

    @Autowired
    OracleCategoryRepository categoryRepository;

    @Override
    public Purchase purchaseFree(User user, Oracle product) throws OracleServiceException {
        return null;
    }

    @Override
    public Purchase purchaseTarget(User user, Oracle product, User oracleUser) throws OracleServiceException {
        return null;
    }

    @Override
    public void oracle(User oracleUser, Purchase purchase) throws OracleServiceException {

    }

    @Override
    public List<Purchase> getFreePurchase() {
        return null;
    }

    @Override
    public List<Purchase> getTargetPurchase() {
        return null;
    }

    @Override
    public List<OracleCategory> getCategoriesByParent(Long parentId) {
        OracleCategory category = getCategoryById(parentId);
        return categoryRepository.getCategoriesByParent(category);
    }

    @Override
    public OracleCategory getCategoryById(Long id) {
        if(id==null)
            return null;
        return categoryRepository.findById(id).get();
    }
}
