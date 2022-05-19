package com.webService.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class filesclass {
	private List<MultipartFile> lista;
	public List<MultipartFile> getLista() {
		return lista;
	}
	public void setLista(List<MultipartFile> lista) {
		this.lista = lista;
	}
	
}
