package com.hhwy.common.socket.controller;

import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.socket.service.ISocketIOServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*@RestController
@ResponseBody
@RequestMapping("/api/socket.io")*/
public class SocketIOController {

    @Autowired
    private ISocketIOServerService socketIOServerService;

    @PostMapping(value = "/test/pushMessageToClient")
    public AjaxResult pushMessageToClient(@RequestParam String clientId, @RequestParam String msgContent) {
        socketIOServerService.pushMessageToClient("push_data_event_client", clientId, msgContent);
        return AjaxResult.success();
    }

}
