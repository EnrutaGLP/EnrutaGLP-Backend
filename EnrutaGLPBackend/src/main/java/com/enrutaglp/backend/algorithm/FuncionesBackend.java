package com.enrutaglp.backend.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.enrutaglp.backend.models.Pedido;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FuncionesBackend {
	
	public static int[] capacidadGLP = {25,15,10,5};
	
	
	
	public static List<Pedido> dividirPedido(Pedido pADividir){
		
		double restoGlp = pADividir.getCantidadGlp();
		List<Pedido> pDivididos = new ArrayList<Pedido>();
		int codSubPedido = 1;
		for (int cap: capacidadGLP) {
			int cociente = (int)(restoGlp/cap);
			for (int i=0; i<cociente; i++) {
				Pedido p = new Pedido(pADividir);
				p.setCodigo(p.getCodigo() + " - " + String.valueOf(codSubPedido++));
				pDivididos.add(p);
			}
			restoGlp = restoGlp%cap;
		}
		if (restoGlp > 0) {
			Pedido p = new Pedido(pADividir);
			p.setCodigo(p.getCodigo() + " - " + String.valueOf(codSubPedido++));
			pDivididos.add(p);
		}
		return pDivididos;
	}
	
	
	public static void testFuncionesBackend () {
		
		
		
	}
}
