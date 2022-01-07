package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Camion {

	private int id; 
	private String codigo; 
	private String placa; 
	private int ubicacionActualX;
	private int ubicacionActualY;	
	private Integer idPuntoActual;
	private double cargaActualGLP;
	private double cargaActualPetroleo;
	private byte estado;
	private TipoCamion tipo;
	private byte tipoByte;
	private LocalDateTime siguienteMovimiento;
	private List<EntregaPedido>entregas;
	private List<Punto>ruta;
	private String color;
	
	public Camion(Camion camion) {
		this.id = camion.getId();
		this.codigo = camion.getCodigo(); 
		this.placa = camion.getPlaca(); 
		this.ubicacionActualX = camion.getUbicacionActualX();
		this.ubicacionActualY = camion.getUbicacionActualY(); 
		this.cargaActualGLP = camion.getCargaActualGLP();
		this.cargaActualPetroleo = camion.getCargaActualPetroleo(); 
		this.estado = camion.getEstado(); 
		this.tipo = camion.getTipo(); 
		this.ruta = new ArrayList<Punto>();
	}

	
	public Camion(String codigo, int ubicacionActualX,int ubicacionActualY, double cargaActualGLP, double cargaActualPetroleo) {
		this.codigo = codigo;
		this.ubicacionActualX = ubicacionActualX;
		this.ubicacionActualY = ubicacionActualY;
		this.cargaActualGLP = cargaActualGLP;
		this.cargaActualPetroleo = cargaActualPetroleo;
		this.ruta = new ArrayList<Punto>();
	}

	public Camion(String codigo, int ubicacionActualX,int ubicacionActualY, double cargaActualGLP, double cargaActualPetroleo,
			TipoCamion tipo) {
		this.codigo = codigo;
		this.ubicacionActualX = ubicacionActualX;
		this.ubicacionActualY = ubicacionActualY;
		this.cargaActualGLP = cargaActualGLP;
		this.cargaActualPetroleo = cargaActualPetroleo;
		this.ruta = new ArrayList<Punto>();
		this.tipo = tipo;
	}
	

	public Camion(int id, String codigo, String placa, int ubicacionActualX, int ubicacionActualY,
			double cargaActualGLP, double cargaActualPetroleo, byte estado, LocalDateTime siguienteMovimiento) {
		this.id = id;
		this.codigo = codigo;
		this.placa = placa;
		this.ubicacionActualX = ubicacionActualX;
		this.ubicacionActualY = ubicacionActualY;
		this.cargaActualGLP = cargaActualGLP;
		this.cargaActualPetroleo = cargaActualPetroleo;
		this.estado = estado;
	}


	public Camion(int id, String codigo, String placa, int ubicacionActualX, int ubicacionActualY,
			Integer idPuntoActual, double cargaActualGLP, double cargaActualPetroleo, byte estado, byte tipoByte,
			LocalDateTime siguienteMovimiento, String color) {
		this.id = id;
		this.codigo = codigo;
		this.placa = placa;
		this.ubicacionActualX = ubicacionActualX;
		this.ubicacionActualY = ubicacionActualY;
		this.idPuntoActual = idPuntoActual;
		this.cargaActualGLP = cargaActualGLP;
		this.cargaActualPetroleo = cargaActualPetroleo;
		this.estado = estado;
		this.tipoByte = tipoByte;
		this.siguienteMovimiento = siguienteMovimiento;
		this.color = color; 
	}
	
	public Camion(int id, String codigo, String placa, int ubicacionActualX, int ubicacionActualY,
			double cargaActualGLP, double cargaActualPetroleo, byte estado,TipoCamion tipoCamion) {
		this.id = id;
		this.codigo = codigo;
		this.placa = placa;
		this.ubicacionActualX = ubicacionActualX;
		this.ubicacionActualY = ubicacionActualY;
		this.cargaActualGLP = cargaActualGLP;
		this.cargaActualPetroleo = cargaActualPetroleo;
		this.estado = estado;
		this.tipo = tipoCamion;
		this.ruta = new ArrayList<Punto>();
	}
	
	public EntregaPedido addPedido(LocalDateTime horaSalida, Pedido pedido) {
		//Verificar 
		int distancia, tiempo;
		LocalDateTime horaLlegada;
		double cantidadEntregada = 0.0;
		EntregaPedido entregaPedido = null; 
		if(entregas.isEmpty()) {
			ubicacionActualX = 0; 
			ubicacionActualY = 0;
			cargaActualGLP	= tipo.getCapacidadGLP();
			distancia = Math.abs(pedido.getUbicacionX()-ubicacionActualX) + Math.abs(pedido.getUbicacionY()-ubicacionActualY);
			tiempo = distancia/(int)tipo.getVelocidadPromedio();
			horaLlegada = horaSalida.plusHours(tiempo);
			cantidadEntregada = (pedido.getCantidadGlp() > cargaActualGLP)? pedido.getCantidadGlp() : cargaActualGLP;
			entregaPedido = new EntregaPedido(cantidadEntregada, horaLlegada, horaSalida, 
					calcularConsumoPetroleo(distancia));
			this.entregas.add(entregaPedido);
		}
		return entregaPedido;
	}
	
	public double calcularConsumoPetroleo(double distancia) {
		double peso = this.tipo.getPesoBruto() + this.cargaActualGLP*0.5;
		return distancia*peso/150;
	}


	public String to_string () {
		String str = ""; 
		str.concat("ID: " + this.id + "\n");
		str.concat("COD: " + this.codigo + "\n"); 
		str.concat("PLACA: " + this.placa + "\n"); 
		str.concat("UBICACION: " + this.ubicacionActualX + ", " + this.ubicacionActualY + "\n");
		str.concat("Carga actual glp: " + this.cargaActualGLP + "\n");
		str.concat("Carga actual petroleo: " + this.cargaActualPetroleo + "\n");
		str.concat("Estado: " + this.estado + "\n");
		str.concat("Tipo: " + this.tipo.toString() + "\n");
		String format = "yyyy-MM-dd HH:mm:ss a";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		String str_datetime = this.siguienteMovimiento.format(formatter);
		str.concat("Siguiente movimiento: " + str_datetime + "\n\n");
		return str;
	}
}
