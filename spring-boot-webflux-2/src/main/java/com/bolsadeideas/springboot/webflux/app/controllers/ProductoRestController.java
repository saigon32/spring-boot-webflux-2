package com.bolsadeideas.springboot.webflux.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
	
	@Autowired
	private ProductoDao dao;
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

	
	//full de todos los productos
	//http://localhost:8080/api/productos
	@GetMapping
	public Flux<Producto> index() {
		Flux<Producto> productos = dao.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				}).doOnNext(prod -> log.info(prod.getNombre()));
		return productos;
	}
	
	//lista con el parametro pathvariable
	//http://localhost:8080/api/productos/6806bc280cbbcb68a8e1f482
	@GetMapping("/{id}")
	public Mono<Producto> show(@PathVariable String id){
		Flux<Producto> productos = dao.findAll();
		//devuelve un mono con .next()
		Mono<Producto> producto = productos.filter(p -> p.getId().equals(id))
				.next()
				.doOnNext(prod -> log.info(prod.getNombre()));
		return producto;
	}
	
}
