package com.bolotov.oraclebot.service;


import com.bolotov.oraclebot.exception.AddOracleException;
import com.bolotov.oraclebot.exception.AddSourceException;
import com.bolotov.oraclebot.model.SourceSet;
import com.bolotov.oraclebot.model.Oracle;

/**Сервис управляющий ресурсами для предсказаний*/
public interface OracleDataService {

    public SourceSet addSourceSet(String json) throws AddSourceException;

    public Oracle addOracle(String json) throws AddOracleException;

    public void deleteOracle(Long id);

    public void deleteSourceSet(Long id);

}
