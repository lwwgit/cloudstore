package com.example.cloudstore.controller;

import com.example.cloudstore.domain.Greeting;
import com.example.cloudstore.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	// @MessageMapping注解和我们之前使用的@RequestMapping类似
	// @SendTo注解表示当服务器有消息需要推送的时候，会对订阅了@SendTo中路径的浏览器发送消息。
	@MessageMapping("/Notice")
	@SendTo("/topic/greetings")
	public Greeting greeting(Message message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting(HtmlUtils.htmlEscape(message.getNotice()));
	}
	
	@RequestMapping("/send")
	@ResponseBody
	public void send(){
		templateTest();
	}

	//客户端只要订阅了/topic/subscribeTest主题，调用这个方法即可
    public void templateTest() {
        messagingTemplate.convertAndSend("/topic/greetings", new Greeting("服务器主动推的数据"));
    }
}
