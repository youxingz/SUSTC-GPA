package com.gpaer.service;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * Created by neo on 13/01/2017.
 */
@Component
public class TurnBackUtil {

    public String general(ResponseForm res) {
        if (res == null) {
            return "";
        }
        return JSON.toJSONString(res);
    }

    public String formIt(int status, String message, Object data) {
        ResponseForm res = new ResponseForm();
        res.setStatus(status);
        res.setData(data);
        res.setMessage(message);
        return general(res);
    }

    class ResponseForm {
        private Integer status;
        private Object data;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getStatus() {
            return status;
        }

        public Object getData() {
            return data;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}

