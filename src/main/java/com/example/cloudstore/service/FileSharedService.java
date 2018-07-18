package com.example.cloudstore.service;

import com.example.cloudstore.domain.entity.ShareDetails;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface FileSharedService {
    Map<String, Object> CreateSharedLink(String[] paths, String ifPasswd) throws URISyntaxException, IOException;

    String ShareVerify(String id, String username);

    List<ShareDetails> AllShare();

    List<Map<String, Object>> ToShare(String id, String passwd);

    String RemoveShare(String[] id);

    String Report(String id);
}
