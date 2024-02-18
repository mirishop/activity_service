package com.hh.mirishop.activity.client;

import com.hh.mirishop.activity.client.dto.NewsFeedCreate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "newsfeed-service", url = "${external.newsfeed-service.url}")
public interface NewsfeedFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/internal/newsfeeds")
    void createNewsfeed (NewsFeedCreate newsfeedCreate);

}
