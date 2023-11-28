package com.bolotov.oraclebot.service.impl;

import com.bolotov.oraclebot.exception.OracleServiceException;
import com.bolotov.oraclebot.model.*;
import com.bolotov.oraclebot.repository.BalanceRepository;
import com.bolotov.oraclebot.repository.OracleCategoryRepository;
import com.bolotov.oraclebot.repository.OracleRepository;
import com.bolotov.oraclebot.repository.PurchaseRepository;
import com.bolotov.oraclebot.service.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class OracleServiceImpl implements OracleService {

    private final long COUNT_MS_IN_DAY = 86400000;

    @Autowired
    OracleCategoryRepository categoryRepository;
    @Autowired
    OracleRepository oracleRepository;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    PurchaseRepository purchaseRepository;

    @Override
    public Purchase purchase(User user, Oracle product) throws OracleServiceException {
        Balance balance = balanceRepository.findByUser(user);
        if(balance == null || balance.getAmount()<product.getPrice())
            throw new OracleServiceException("Недостаточно средств на балансе");
        Timestamp startDate = new Timestamp(System.currentTimeMillis() - COUNT_MS_IN_DAY*product.getCountDay());
        int countPurchased = purchaseRepository.getCountPurchasePerTimes(user, product.getId(), startDate);
        if(countPurchased>=product.getLimit())
            throw new OracleServiceException("Вы превысили лимит покупок данного продукта");
        Purchase newPurchase = new Purchase();
        newPurchase.setDatePurchase(new Timestamp(System.currentTimeMillis()));
        newPurchase.setOracleId(product.getId());
        newPurchase.setCustomer(user);
        newPurchase.setPrice(product.getPrice());
        newPurchase.setOracleUser(product.getOwner());
        newPurchase.setState(Purchase.STATE.WAIT_ANSWER);
        purchaseRepository.save(newPurchase);
        balance.setAmount(balance.getAmount() - product.getPrice());
        balanceRepository.save(balance);
        return newPurchase;
    }

    @Override
    public List<Purchase> getAllFreePurchase() {
        return purchaseRepository.getPurchasesByState(Purchase.STATE.WAIT_ANSWER);
    }

    @Override
    public Purchase getPurchaseById(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        if(purchase.isPresent())
            return purchase.get();
        return null;
    }

    @Override
    public Purchase selectPurchaseForAnswer(User user, Long idPurchase) {
        Purchase purchase =null;
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(idPurchase);
        if(optionalPurchase.isPresent()) {
            List<Purchase> selectedPurchases = purchaseRepository.findStatePurchaseByUser(user, Purchase.STATE.SELECTED);
            for(Purchase p : selectedPurchases) {
                p.setState(Purchase.STATE.WAIT_ANSWER);
                purchaseRepository.save(p);
            }
            purchase = optionalPurchase.get();
            purchase.setOracleUser(user);
            purchase.setState(Purchase.STATE.SELECTED);
            purchaseRepository.save(purchase);
        }

        return purchase;
    }

    @Override
    public List<Purchase> getSelectedPurchase(User user) {
        List<Purchase> purchases = purchaseRepository.findStatePurchaseByUser(user, Purchase.STATE.SELECTED);
        return purchases;
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
        if(parentId == null) {
            return categoryRepository.getCategoryRoot();
        }
        OracleCategory category = getCategoryById(parentId);
        List<OracleCategory> categories = categoryRepository.getCategoriesByParent(category);
        return categories;
    }

    @Override
    public OracleCategory getCategoryById(Long id) {
        if(id==null)
            return null;
        return categoryRepository.findById(id).get();
    }

    @Override
    public Oracle getOracleById(Long id) {
        Optional<Oracle> optionalOracle = oracleRepository.findById(id);
        if(optionalOracle.isPresent())
            return optionalOracle.get();
        return null;
    }

    @Override
    public List<Oracle> getOraclesByCategory(OracleCategory oracleCategory) {
        List<Oracle> oracles = oracleRepository.getOraclesByCategory(oracleCategory);
        return oracles;
    }

    @Override
    public void donePurchase(Purchase purchase) {
        purchase.setState(Purchase.STATE.DONE);
        purchaseRepository.save(purchase);
    }

    @Override
    public void unselectPurchase(Long purchaseId) throws OracleServiceException {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchaseId);
        if(optionalPurchase.isPresent()) {
            Purchase purchase = optionalPurchase.get();
            purchase.setState(Purchase.STATE.WAIT_ANSWER);
            purchaseRepository.save(purchase);
        }
        else
            throw new OracleServiceException("Заказа с таким id не сущетсвует");
    }
}
