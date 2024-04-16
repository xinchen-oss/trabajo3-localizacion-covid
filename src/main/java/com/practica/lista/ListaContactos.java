package com.practica.lista;

import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

import java.nio.file.NotDirectoryException;

public class ListaContactos {
	private NodoTemporal lista;
	private int size;
	
	/**
	 * Insertamos en la lista de nodos temporales, y a la vez inserto en la lista de nodos de coordenadas.
	 * En la lista de coordenadas metemos el documento de la persona que está en esa coordenada 
	 * en un instante 
	 */
	public void insertarNodoTemporal (PosicionPersona p) {
		NodoTemporal aux = lista, ant=null;
		boolean[] salirEncontrado = new boolean[]{false,false};
		/**
		 * Busco la posición adecuada donde meter el nodo de la lista, excepto
		 * que esté en la lista. Entonces solo añadimos una coordenada.
		 */
		ant = buscarPosicion(salirEncontrado,aux,p);
		/**
		 * No hemos encontrado ninguna posición temporal, así que
		 * metemos un nodo nuevo en la lista
		 */
		if(!salirEncontrado[1]) {
			NodoTemporal nuevo = new NodoTemporal();
			nuevo.setFecha(p.getFechaPosicion());
			insertarListaCoordenada(nuevo,p);
			setSiguiente(ant,nuevo,aux);
			this.size++;
		}
	}

	private void setSiguiente(NodoTemporal ant, NodoTemporal nuevo, NodoTemporal aux){
		if(ant!=null) {
			nuevo.setSiguiente(aux);
			ant.setSiguiente(nuevo);
		}else {
			nuevo.setSiguiente(lista);
			lista = nuevo;
		}
	}
	private int comparar(FechaHora fechaHora1, FechaHora fechaHora2){
		return fechaHora1.compareTo(fechaHora2);
	}
	private NodoTemporal buscarPosicion(boolean[] salirEncontrada, NodoTemporal aux, PosicionPersona p){
		NodoTemporal ant = null;
		while (aux!=null && !salirEncontrada[0]) {
			if(comparar(aux.getFecha(),p.getFechaPosicion())==0) {
				salirEncontrada[1] = true;
				salirEncontrada[0] = true;
				/**
				 * Insertamos en la lista de coordenadas
				 */
				insertarListaCoordenada(aux,p);
			}else if(comparar(aux.getFecha(),p.getFechaPosicion())<0) {
				ant = aux;
				aux=aux.getSiguiente();
			}else if(comparar(aux.getFecha(),p.getFechaPosicion())>0) {
				salirEncontrada[0]=true;
			}
		}
		return ant;
	}
	private void insertarListaCoordenada(NodoTemporal aux,PosicionPersona p){
		NodoPosicion npActual = aux.getListaCoordenadas();
		NodoPosicion npAnt=null;
		boolean npEncontrado = false;
		while (npActual!=null && !npEncontrado) {
			if(igualCoordenada(npActual.getCoordenada(),p.getCoordenada())) {
				npEncontrado=true;
				npActual.setNumPersonas(npActual.getNumPersonas()+1);
			}else {
				npAnt = npActual;
				npActual = npActual.getSiguiente();
			}
		}NodoPosicion npNuevo = new NodoPosicion(p.getCoordenada(),1, null);
		if(setListaCoordenada(npEncontrado,aux.getListaCoordenadas())) {
			aux.setListaCoordenadas(npNuevo);
		}else npAnt.setSiguiente(npNuevo);
	}

	private boolean igualCoordenada(Coordenada coordenada1, Coordenada coordenada2){
		return  coordenada1.equals(coordenada2);
	}

	private boolean setListaCoordenada (boolean encontrado, NodoPosicion getCoordenada){
		return  !encontrado && getCoordenada==null;
	}


	
	private boolean buscarPersona (String documento, NodoPersonas nodo) {
		NodoPersonas aux = nodo;
		while(aux!=null) {
			if(aux.getDocumento().equals(documento)) {
				return true;				
			}else {
				aux = aux.getSiguiente();
			}
		}
		return false;
	}
	
	private void insertarPersona (String documento, NodoPersonas nodo) {
		NodoPersonas aux = nodo, nuevo = new NodoPersonas(documento, null);
		while(aux.getSiguiente()!=null) {				
			aux = aux.getSiguiente();				
		}
		aux.setSiguiente(nuevo);		
	}
	
	public int personasEnCoordenadas () {
		NodoPosicion aux = this.lista.getListaCoordenadas();
		if(aux==null)
			return 0;
		else {
			int cont;
			for(cont=0;aux!=null;) {
				cont += aux.getNumPersonas();
				aux=aux.getSiguiente();
			}
			return cont;
		}
	}
	
	public int tamanioLista () {
		return this.size;
	}

	public String getPrimerNodo() {
		NodoTemporal aux = lista;
		String cadena = aux.getFecha().getFecha().toString();
		cadena+= ";" +  aux.getFecha().getHora().toString();
		return cadena;
	}

	/**
	 * Métodos para comprobar que insertamos de manera correcta en las listas de 
	 * coordenadas, no tienen una utilidad en sí misma, más allá de comprobar que
	 * nuestra lista funciona de manera correcta.
	 */
	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		if(this.size==0)
			return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		int a;
		cont = 0;
		while(aux!=null) {
			if(aux.getFecha().compareTo(inicio)>=0 && aux.getFecha().compareTo(fin)<=0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while(nodo!=null) {
					cont = cont + nodo.getNumPersonas();
					nodo = nodo.getSiguiente();
				}				
				aux = aux.getSiguiente();
			}else {
				aux=aux.getSiguiente();
			}
		}
		return cont;
	}
	
	
	
	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		if(this.size==0)
			return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		int a;
		cont = 0;
		while(aux!=null) {
			if(aux.getFecha().compareTo(inicio)>=0 && aux.getFecha().compareTo(fin)<=0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while(nodo!=null) {
					cont = cont + 1;
					nodo = nodo.getSiguiente();
				}				
				aux = aux.getSiguiente();
			}else {
				aux=aux.getSiguiente();
			}
		}
		return cont;
	}
	
	
	
	@Override
	public String toString() {
		String cadena="";
		int a,cont;
		cont=0;
		NodoTemporal aux = lista;
		for(cont=1; cont<size; cont++) {
			cadena += aux.getFecha().getFecha().toString();
			cadena += ";" +  aux.getFecha().getHora().toString() + " ";
			aux=aux.getSiguiente();
		}
		cadena += aux.getFecha().getFecha().toString();
		cadena += ";" +  aux.getFecha().getHora().toString();
		return cadena;
	}
	
	
	
}
