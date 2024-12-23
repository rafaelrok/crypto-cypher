package br.com.rafaelvieira.cryptocypher.payload;

import br.com.rafaelvieira.cryptocypher.util.Response;

public class BaseResponse implements Response {

    private String response;
    private boolean success;

    public BaseResponse(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    @Override
    public String getResponse() {
        return response;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
