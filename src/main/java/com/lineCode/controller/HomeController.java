package com.lineCode.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lineCode.model.DetalleOrden;
import com.lineCode.model.Orden;
import com.lineCode.model.Producto;
import com.lineCode.model.Usuario;
import com.lineCode.service.IDetalleOrdenService;
import com.lineCode.service.IOrdenService;
import com.lineCode.service.IProductoService;
import com.lineCode.service.IUsuarioService;

@Controller
public class HomeController {

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();

	@GetMapping({ "/", "" })
	public String home(Model model, HttpSession session) {
		model.addAttribute("productos", productoService.findAll());
		
		//session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "index/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();

		model.addAttribute("producto", producto);

		return "index/productohome";
	}

	@PostMapping("productohome/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validar que le producto no se añada 2 veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

		if (!ingresado) {
			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "index/carrito";
	}

	@GetMapping("productohome/getCart")
	public String getCart(Model model) {

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "index/carrito";
	}

	@GetMapping("productohome/order")
	public String order(Model model, HttpSession session) {

		//Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		//model.addAttribute("usuario", usuario);

		return "index/resumenorden";
	}
	
	// quitar un producto del carrito
		@GetMapping("/delete/cart/{id}")
		public String deleteProductoCart(@PathVariable Integer id, Model model) {

			// lista nueva de prodcutos
			List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

			for (DetalleOrden detalleOrden : detalles) {
				if (detalleOrden.getProducto().getId() != id) {
					ordenesNueva.add(detalleOrden);
				}
			}

			// poner la nueva lista con los productos restantes
			detalles = ordenesNueva;

			double sumaTotal = 0;
			sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

			orden.setTotal(sumaTotal);
			model.addAttribute("cart", detalles);
			model.addAttribute("orden", orden);

			return "index/carrito";
		}

	// guardar la orden
	@GetMapping("productohome/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());

		// usuario
		//Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		//orden.setUsuario(usuario);
		ordenService.save(orden);

		// guardar detalles
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}

		/// limpiar lista y orden
		orden = new Orden();
		detalles.clear();

		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		List<Producto> productos= productoService.findAll().stream().filter( p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);		
		return "index/home";
	}

}
