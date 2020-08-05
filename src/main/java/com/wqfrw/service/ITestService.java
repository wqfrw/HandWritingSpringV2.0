package com.wqfrw.service;

public interface ITestService {

    String query(String name,Integer id);

    String getException() throws Exception;

    String queryPage(String name);
}
