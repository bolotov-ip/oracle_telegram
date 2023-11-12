package com.bolotov.oraclebot.service;


import com.bolotov.oraclebot.exception.OraclePropertyServiceException;
import com.bolotov.oraclebot.model.Product;

/**Сервис управляющий ресурсами для предсказаний*/
public interface OraclePropertyService {

    /**Формирование таблиц ресурсов на основе данных в файловой системе сервера из
     * application_properties bot.catalog.source*/
    public void synchronizeWithFiles() throws OraclePropertyServiceException;

    /**Добавляет Product созданный из json строки*/
    public Product addProductByJson(String jsonProduct) throws OraclePropertyServiceException;

    /**Удаляет продукт по id*/
    public boolean removeProduct(Long productId);

    /**Удаляет все Source, SourceSet, SourceGroup, все ссылки на ресурсы в Product очищаются*/
    public void removeAllSources();

    /**Удаляет все Product*/
    public void removeAllProducts();
}
