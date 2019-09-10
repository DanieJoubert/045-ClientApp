package com.example.clientapp.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.clientapp.viewmodels.Sendmsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class msgController {
    @Autowired
    private Environment env;

    @GetMapping("/sendmsg")
    public String sendmsgForm(Model model) throws URISyntaxException 
    {
        // model.addAttribute("name", "");
        model.addAttribute("sendmsg", new Sendmsg());
        return "sendmsg";
    }


    @PostMapping("/sendmsg")
    public String sendmsg(@ModelAttribute Sendmsg sendmsg) throws URISyntaxException 
    {
        try
        {
            String remoteUrl = env.getProperty("remote.server.ip").concat(":").concat(env.getProperty("remote.server.port"));
            String servicePath = remoteUrl.concat("/save");
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(servicePath);
            ResponseEntity<String> result = restTemplate.postForEntity(uri, sendmsg.getMsg(), String.class);

            //analyse result for systematic failures/ status analysis
        }
        catch (Exception e) {
            e.printStackTrace();
            sendmsg.setMsg("FAILED");
            return "sendmsg_error";
        }

        return "sendmsg_result";
    }
}