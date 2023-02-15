package com.lineCode.util.reports;


import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.lineCode.model.Producto;

public class ProductoExporterPDF {
	
	private List<Producto> listaProductos;

	public ProductoExporterPDF(List<Producto> listaProductos) {
		super();
		this.listaProductos = listaProductos;
	}
	
	//PdfPTable
	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.BLUE);
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Nombre", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Cantidad", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Precio", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Descripcion", fuente));
		tabla.addCell(celda);
	}
	
	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Producto producto : listaProductos) {
			tabla.addCell(String.valueOf(producto.getId()));
			tabla.addCell(producto.getNombre());	
			tabla.addCell(String.valueOf(producto.getCantidad()));
			tabla.addCell(String.valueOf(producto.getPrecio()));
			tabla.addCell(producto.getDescripcion());
			
		}
	}
	
	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLUE);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("Lista de Productos", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 3.3f, 2.3f, 4f, 9.9f});
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}
