package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Purchase;
import com.bolotov.oraclebot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT count(p) FROM purchase p where p.customer=:user and p.datePurchase>:time and p.oracleId=:oracleId")
    public int getCountPurchasePerTimes(@Param("user")  User user, @Param("oracleId") Long oracleId, @Param("time") Timestamp time);

    @Query("SELECT p FROM purchase p where p.state=:state")
    public List<Purchase> getPurchasesByState(@Param("state")  Purchase.STATE state);

    @Query("SELECT p FROM purchase p where p.state=:state and p.oracleUser=:user")
    public List<Purchase> findStatePurchaseByUser(@Param("user")  User user, @Param("state")  Purchase.STATE state);

    @Query("SELECT count(p) FROM purchase p where p.state=:state and p.customer=:user")
    public int countStatePurchaseByCustomer(@Param("user")  User user, @Param("state")  Purchase.STATE state);

    @Query("SELECT count(p) FROM purchase p where p.state=:state")
    public int countPurchaseByState(@Param("state")  Purchase.STATE state);
}
