package com.start.eventdrivenspringbootkotlin.configuration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

public class Response<P> {

    private P response;
    private boolean error;
    private String errorMsg;
    private HttpStatus httpStatus;
    private ArrayList<HttpHeaders> headers;
    public Response() {
    }

    private Response(String errorMsg) {
        this.error = true;
        this.errorMsg = errorMsg;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    private Response(String errorMsg, HttpStatus httpStatus) {
        this.error = true;
        this.errorMsg = errorMsg;
        this.httpStatus = httpStatus;
    }

    private Response(P response) {
        this.response = response;
        this.error=false;
        this.httpStatus = HttpStatus.OK;
    }
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <K> Response<K> withError(String errorMsg){
        return new Response<>(errorMsg);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <K> Response<K> withError(String errorMsg, HttpStatus httpStatus){
        return new Response<>(errorMsg,httpStatus);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <K> Response<K> ofResponse(K response){
        return new Response<>(response);
    }

    public P getResponse() {
        return response;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
