package com.bolotov.oraclebot.service.impl;

import com.bolotov.oraclebot.exception.AddOracleException;
import com.bolotov.oraclebot.exception.AddSourceException;
import com.bolotov.oraclebot.model.*;
import com.bolotov.oraclebot.repository.*;
import com.bolotov.oraclebot.service.OracleDataService;
import com.bolotov.oraclebot.telegram.message.TelegramMessageFactory;
import com.bolotov.oraclebot.telegram.message.TelegramMessageMedia;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.util.*;

@Service
@PropertySource("application.properties")
public class OracleDataServiceImpl implements OracleDataService {

    @Autowired
    private SourceSetRepository sourceSetRepository;
    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OracleCategoryRepository categoryRepository;
    @Autowired
    OracleRepository oracleRepository;
    @Autowired
    TelegramMessageFactory messageFactory;
    @Value("${bot.temp}")
    private String pathTemp;

    List<String> imageFormats = new ArrayList<>();
    List<String> videoFormats = new ArrayList<>();
    {
        imageFormats.add(".png"); imageFormats.add(".jpg"); imageFormats.add(".jpeg"); imageFormats.add(".gif"); imageFormats.add(".jp2"); imageFormats.add(".raw");
        videoFormats.add(".mp4"); videoFormats.add(".wmv"); videoFormats.add(".avi"); videoFormats.add(".m4v"); videoFormats.add(".mov"); videoFormats.add(".mpg");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SourceSet addSourceSet(String json) throws AddSourceException {

        SourceSet sourceSet = new SourceSet();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode oracleNameNode = rootNode.get("name");
            sourceSet.setName(oracleNameNode.textValue());

            JsonNode oracleGroupNameNode = rootNode.get("groupName");
            sourceSet.setGroupName(oracleGroupNameNode.textValue());

            JsonNode sourceNode = rootNode.get("sources");
            if(sourceNode.isContainerNode()) {
                Iterator<String> names = sourceNode.fieldNames();
                while(names.hasNext()) {
                    String nameSource = names.next();
                    String url = sourceNode.get(nameSource).textValue();
                    Source.Type type = getType(url);
                    String filename = pathTemp + File.separator + UUID.randomUUID() +url.substring(url.lastIndexOf("."));
                    File sourceFile = downloadFile(url, filename);
                    String telegramId = getTelegramId(sourceFile);
                    sourceSet.addSource(nameSource, telegramId, type);
                }
            }
            sourceSetRepository.save(sourceSet);
        }
        catch (Exception e) {
            throw new AddSourceException(e);
        }
        return sourceSet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Oracle addOracle(String json) throws AddOracleException {
        Oracle addOracle = new Oracle();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode oracleNameNode = rootNode.get("name");
            addOracle.setName(oracleNameNode.textValue());

            JsonNode oracleDescriptionNode = rootNode.get("description");
            addOracle.setDescription(oracleDescriptionNode.textValue());

            JsonNode oraclePriceNode = rootNode.get("price");
            addOracle.setPrice(oraclePriceNode.doubleValue());

            JsonNode oracleLimitNode = rootNode.get("limit");
            addOracle.setLimit(oracleLimitNode.intValue());

            JsonNode oracleGroupSource = rootNode.get("groupSource");
            addOracle.setSourceGroup(oracleGroupSource.textValue());

            JsonNode oracleCountDayNode = rootNode.get("countDay");
            addOracle.setCountDay(oracleCountDayNode.intValue());

            JsonNode oracleUserNameNode = rootNode.get("userName");
            User ownerOracleUser = userRepository.findByUsername(oracleUserNameNode.textValue());
            addOracle.setOwner(ownerOracleUser);

            JsonNode oracleCategoryNode = rootNode.get("oracleCategory");
            List<String> treeCategory = new ArrayList<>();
            if(oracleCategoryNode.isArray()) {
                Iterator<JsonNode> nodes = oracleCategoryNode.elements();
                while(nodes.hasNext())
                    treeCategory.add(nodes.next().textValue());
            }
            List<OracleCategory> categories = getOracleCategories(treeCategory);

            JsonNode oracleResultNode = rootNode.get("oracleResult");
            if(oracleResultNode.isContainerNode()) {
                Iterator<String> names = oracleResultNode.fieldNames();
                while(names.hasNext()) {
                    String nameResult = names.next();
                    String valueResult = oracleResultNode.get(nameResult).textValue();
                    addOracle.addOracleResult(nameResult, valueResult);
                }
            }
            addOracle.setCategory(categories.size()>0 ? categories.get(categories.size()-1) : null);
            saveOracle(addOracle, categories);

        } catch (Exception e) {
            throw new AddOracleException(e);
        }
        return addOracle;
    }

    @Override
    public void deleteOracle(Long id) {

    }

    @Override
    public void deleteSourceSet(Long id) {

    }

    @Override
    public List<String> getAllGroupsSource() {

        return sourceSetRepository.getAllGroups();
    }

    @Override
    public List<SourceSet> getSourceSetByGroup(String groupName) {
        List<SourceSet> sourceSets = sourceSetRepository.findByGroupName(groupName);
        return sourceSets;
    }

    @Override
    public SourceSet getSourceSet(Long id) {
        Optional<SourceSet> sourceSetOptional = sourceSetRepository.findById(id);
        if(sourceSetOptional.isPresent())
            return sourceSetOptional.get();
        return null;
    }

    private List<OracleCategory> getOracleCategories(List<String> categoryTree) {
        List<OracleCategory> categories = new ArrayList<>();
        OracleCategory parent = null;
        for(String nameCategory : categoryTree) {

            OracleCategory category = null;
            if(parent == null)
                category = categoryRepository.getCategoryWithParentNull(nameCategory);
            else
                category = categoryRepository.getCategory(nameCategory, parent);
            if(category == null) {
                category = new OracleCategory();
                category.setName(nameCategory);
                category.setParentId(parent);
            }
            parent = category;
            categories.add(category);
            categoryRepository.save(category);
        }

        return categories;
    }

    private void saveOracle(Oracle oracle, List<OracleCategory> categories) {
        oracleRepository.save(oracle);
        categoryRepository.saveAll(categories);
    }

    private File downloadFile(String fileAddress, String filename) throws IOException {
        URL url = null;
        try {
            url = new URL(fileAddress);
        }
        catch (Exception e) {
            File urlFile = new File(fileAddress);
            url = urlFile.toURI().toURL();
        }
        FileUtils.copyURLToFile(url, new File(filename));
        return new File(filename);
    }

    private String getTelegramId(File file) throws AddSourceException, TelegramApiException {
        User owner = userRepository.findByUsername("bolotov_ip");
        TelegramMessageMedia messageMedia = messageFactory.newTelegramMessageMedia(owner.getChatId(), "");
        if(getType(file.getName()).equals(Source.Type.VIDEO)){
            messageMedia.addVideoFile(file);
            messageMedia.send();
            List<String> videosId = messageMedia.getVideosId();
            if(videosId.size()>0)
                return videosId.get(0);
        }

        if(getType(file.getName()).equals(Source.Type.PHOTO)){
            messageMedia.addPhotoFile(file);
            messageMedia.send();
            List<String> photosId = messageMedia.getPhotosId();
            if(photosId.size()>0)
                return photosId.get(0);
        }

        return null;
    }

    private Source.Type getType(String filename) throws AddSourceException {
        String format = filename.substring(filename.lastIndexOf("."));
        if(videoFormats.contains(format))
            return Source.Type.VIDEO;
        if(imageFormats.contains(format))
            return Source.Type.PHOTO;
        throw new AddSourceException("Не читается формат файла из адреса");
    }
}
