package com.bolotov.oraclebot.service;


import com.bolotov.oraclebot.exception.AddOracleException;
import com.bolotov.oraclebot.exception.AddSourceException;
import com.bolotov.oraclebot.model.Source;
import com.bolotov.oraclebot.model.SourceSet;
import com.bolotov.oraclebot.model.Oracle;

import java.util.List;

/**Сервис управляющий ресурсами для предсказаний*/
public interface OracleDataService {

    public SourceSet addSourceSet(String json) throws AddSourceException;

    public Oracle addOracle(String json) throws AddOracleException;

    public void deleteOracle(Long id);

    public void deleteSourceSet(Long id);

    public List<String> getAllGroupsSource();

    public List<SourceSet> getSourceSetByGroup(String groupName);

    public SourceSet getSourceSet(Long id);

}
