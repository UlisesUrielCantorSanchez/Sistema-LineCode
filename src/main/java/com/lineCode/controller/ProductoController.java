package com.lineCode.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lineCode.model.Producto;
import com.lineCode.model.Usuario;
import com.lineCode.service.IProductoService;
import com.lineCode.service.util.UploadFileService;
import com.lineCode.util.pagination.PageRender;
import com.lineCode.util.reports.ProductoExporterExel;
import com.lineCode.util.reports.ProductoExporterPDF;
import com.lowagie.text.DocumentException;

@Controller
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@Autowired
	private UploadFileService upload;

	@GetMapping("producto/obtener/{id}")
	public String verDetallesDelProducto(@PathVariable(value = "id") Integer id, Map<String, Object> modelo,
			RedirectAttributes flash) {
		Producto producto = productoService.findOne(id);
		if (producto == null) {
			flash.addFlashAttribute("error", "El producto no existe en la base de datos");
			return "redirect:productos/mostar";
		}

		modelo.put("producto", producto);
		modelo.put("titulo", "Detalles del empleado " + producto.getNombre());
		return "productos/mostar";
	}

	@GetMapping("/producto/listar")
	public String listarProductos(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Producto> productos = productoService.findAll(pageRequest);
		PageRender<Producto> pageRender = new PageRender<>("listar", productos);

		modelo.addAttribute("titulo", "Listado de Productos");
		modelo.addAttribute("productos", productos);
		modelo.addAttribute("page", pageRender);

		return "productos/listar";
	}

	@GetMapping("/producto/create")
	public String create() {
		return "productos/form";
	}

	@PostMapping("/producto/save")
	public String guardarProductos(@Valid Producto producto, @RequestParam("img") MultipartFile file, HttpSession session, BindingResult result, Model modelo,
			RedirectAttributes flash, SessionStatus status) throws IOException {
		if (result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de Producto");
			return "productos/from";
		}

		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);	
		
		//imagen
		if (producto.getId()==null) { // cuando se crea un producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		
		productoService.save(producto);
		return "redirect:/productos";
	}

	@GetMapping("/producto/edit/{id}")
	public String editarProducto(@PathVariable(value = "id") Integer id, Model model) {
		Producto producto= new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);
		producto= optionalProducto.get();
		
		model.addAttribute("producto", producto);
		
		return "productos/form";
	}
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file ) throws IOException {
		Producto p= new Producto();
		p=productoService.get(producto.getId()).get();
		
		if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagem
			
			producto.setImagen(p.getImagen());
		}else {// cuando se edita tbn la imagen			
			//eliminar cuando no sea la imagen por defecto
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);		
		return "redirect:/productos/listar";
	}
	
	
	@GetMapping("/producto/eliminar/{id}")
	public String eliminarProducto(@PathVariable(value = "id") Integer id,RedirectAttributes flash) {
		Producto producto = new Producto();
		//producto = productoService.findOne(id);
		producto = productoService.get(id).get();
		
		//eliminar cuando no sea la imagen por defecto
		if (!producto.getImagen().equals("default.jpg")) {
			upload.deleteImage(producto.getImagen());
		}
		if (id > 0) {
			productoService.delete(id);
			flash.addFlashAttribute("success", "Empleado eliminado con exito");
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/producto/exportarPDF")
	public void exportarListadoDeProductosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Productos__" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Producto> productos = productoService.findAll();
		
		ProductoExporterPDF exporter = new ProductoExporterPDF(productos);
		exporter.exportar(response);
	}
	
	@GetMapping("/producto/exportarExcel")
	public void exportarListadoDeProductosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Productos_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Producto> productos = productoService.findAll();
		
		ProductoExporterExel exporter = new ProductoExporterExel(productos);
		exporter.exportar(response);
		
	}

	}

