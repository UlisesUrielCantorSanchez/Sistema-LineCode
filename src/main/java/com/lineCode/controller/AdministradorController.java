package com.lineCode.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import com.lineCode.model.Orden;
import com.lineCode.model.Producto;
import com.lineCode.service.IOrdenService;
import com.lineCode.service.IProductoService;
import com.lineCode.service.IUsuarioService;
import com.lineCode.util.pagination.PageRender;
import com.lineCode.util.reports.OrdenExporterExel;
import com.lowagie.text.DocumentException;

@Controller
public class AdministradorController {
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordensService;

	@GetMapping({"/administrador","/administrador/listar"})
	public String home(@RequestParam(name = "page", defaultValue = "0") int page,Model model) {
		Pageable pageRequest = PageRequest.of(page, 8);
		Page<Producto> productos = productoService.findAll(pageRequest);
		PageRender<Producto> pageRender = new PageRender<>("listar", productos);

		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		

		return "administrador/home";
	}
	
	@GetMapping("/administrador/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}
	
	@GetMapping("/administrador/ordenes")
	public String ordenes(@RequestParam(name = "page", defaultValue = "0") int page,Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Orden> ordenes = ordensService.findAll(pageRequest);
		PageRender<Orden> pageRender = new PageRender<>("ordenes", ordenes);
		
		model.addAttribute("titulo", "Listado de Ordenes");
		model.addAttribute("ordenes", ordenes);
		model.addAttribute("page", pageRender);
		
		return "administrador/ordenes";
	}
	
	@GetMapping("/administrador/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		Orden orden= ordensService.findById(id).get();
		model.addAttribute("detalles", orden.getDetalle());
		
		return "administrador/detalleorden";
	}
	
	@GetMapping("/administrador/usuario/delete/{id}")
	public String eliminarUsuario(@PathVariable(value = "id") Integer id) {
		usuarioService.delete(id);
		return "redirect:/administrador/usuarios";
	}
	
	@GetMapping("/administrador/delete/{id}")
	public String eliminarProducto(@PathVariable(value = "id") Integer id) {
		ordensService.delete(id);
		return "redirect:/administrador/ordenes";
	}
	
	@GetMapping("/administrador/ordenes/exportarExcel")
	public void exportarListadoDeOrdenesEnExcel(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Ordenes_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Orden> ordenes = ordensService.findAll();
		
		OrdenExporterExel exporter = new OrdenExporterExel(ordenes);
		exporter.exportar(response);
		
	}


}
