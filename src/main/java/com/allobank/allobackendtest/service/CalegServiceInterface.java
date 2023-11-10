package com.allobank.allobackendtest.service;

import java.util.List;

import com.allobank.allobackendtest.model.Caleg;

public interface CalegServiceInterface {

  public List<Caleg> getCalegListByDapilAndPartai(String namaDapil, String namaPartai);

}
