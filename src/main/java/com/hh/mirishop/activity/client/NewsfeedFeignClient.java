package com.hh.mirishop.activity.client;

import com.hh.mirishop.activity.client.dto.NewsFeedCreate;
import com.hh.mirishop.activity.client.dto.NewsFeedDelete;
import com.hh.mirishop.activity.client.dto.NewsFeedUpdate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "newsfeed-service", url = "${external.newsfeed-service.url}")
public interface NewsfeedFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/newsfeeds")
    void createNewsFeed(NewsFeedCreate newsfeedCreate);

    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/internal/newsfeeds")
    void updateNewsFeed(NewsFeedUpdate newsFeedUpdate);

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/internal/newsfeeds")
    void deleteNewsFeed(NewsFeedDelete newsFeedDelete);
}
