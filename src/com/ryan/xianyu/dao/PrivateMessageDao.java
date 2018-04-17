package com.ryan.xianyu.dao;

import com.ryan.xianyu.domain.PrivateMessage;

import java.util.List;

public interface PrivateMessageDao {

    Integer insertMessage(Integer fromId, Integer toId, String message);

    List<PrivateMessage> getPrivateMessageByToId(Integer userId);

    Integer updateMessage2Read(Integer userId);

    PrivateMessage getPrivateMessageById(Integer messageId);

    Integer deleteById(Integer messageId);
}
