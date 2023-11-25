package com.bolotov.oraclebot.repository;

import com.bolotov.oraclebot.model.Balance;
import com.bolotov.oraclebot.model.Message;
import com.bolotov.oraclebot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    public Balance findByUser(User user);

}
