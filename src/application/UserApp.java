
package application;

import dao.*;
import java.sql.*;
import java.time.*;
import models.*;


public class UserApp {

	public static Connection conexion;

	public static void main(String[] args) {
		
                LocalDate fechaPivot1 = LocalDate.of(2023, Month.DECEMBER, 30);
                LocalDate fechaPivot2 = LocalDate.of(2023, Month.JULY, 30);
                
                Pais argentina = new Pais("Argentina");
                Pais brasil = new Pais("Brasil");
                Pais españa = new Pais("España");
                
                PaisData.cargarPais(argentina);
                PaisData.cargarPais(brasil);
                PaisData.cargarPais(españa);
                
                
                Provincia buenosAires = new Provincia("Buenos Aires", argentina);
                Provincia cordoba = new Provincia("Cordoba", argentina);
                Provincia rioNegro = new Provincia("Rio Negro", argentina);
                Provincia santaCatarina = new Provincia("Santa Catarina", brasil);
                Provincia desconocida = new Provincia("Desconocida", argentina);

                
                ProvinciaData.cargarProvincia(buenosAires);
                ProvinciaData.cargarProvincia(cordoba);
                ProvinciaData.cargarProvincia(rioNegro);
                ProvinciaData.cargarProvincia(santaCatarina);
                ProvinciaData.cargarProvincia(desconocida);
                
                
                Ciudad marDelPlata = new Ciudad("Mar del Plata", buenosAires, argentina, fechaPivot1);
		Ciudad caba = new Ciudad("CABA", buenosAires, argentina, fechaPivot1);
		Ciudad carlosPaz = new Ciudad("Carlos Paz", cordoba, argentina, fechaPivot1);
		Ciudad bariloche = new Ciudad("Bariloche", rioNegro, argentina, fechaPivot2 );
                Ciudad florianapolis = new Ciudad("Florianapolis", santaCatarina, brasil, fechaPivot1);
      
                CiudadData.cargarCiudad(marDelPlata);
		CiudadData.cargarCiudad(caba);
		CiudadData.cargarCiudad(carlosPaz);
		CiudadData.cargarCiudad(bariloche);
                CiudadData.cargarCiudad(florianapolis);

		
                Establecimiento hotelSheraton = new Establecimiento("Hotel Sheraton", "Calle Esperanza", "xxxxxxxxx");
                Establecimiento hotelPresidente = new Establecimiento("Hotel Presidente", "Calle Alegria", "xxxxxxxxx");
                Establecimiento hotelCerroCatedral = new Establecimiento("Hotel Cerro Catedral", "Calle Montaña", "xxxxxxxxx");
                Establecimiento pousadaOsIngleses = new Establecimiento("Pousada Os Ingleses", "Avenida Atlantica", "xxxxxxx");

                EstablecimientoData.cargarEstablecimiento(hotelSheraton);
                EstablecimientoData.cargarEstablecimiento(hotelPresidente);
                EstablecimientoData.cargarEstablecimiento(hotelCerroCatedral);
                EstablecimientoData.cargarEstablecimiento(pousadaOsIngleses);
 
                LocalDate checkIn1 = LocalDate.of(2023, 11, 01);
		LocalDate checkOut1 = LocalDate.of(2023, 11, 15);
		Estadia estadiaMarDelPlata = new Estadia(hotelSheraton, checkIn1, checkOut1,
                                          Servicio.PENSION_COMPLETA,  250.00, marDelPlata);

                
                LocalDate checkIn2 = LocalDate.of(2024, 06, 15);
		LocalDate checkOut2 = LocalDate.of(2024, 06, 30);
                Estadia estadiaCarlosPaz= new Estadia(hotelPresidente, checkIn2, checkOut2,
				                    Servicio.MEDIA_PENSION, 150.00, carlosPaz);                                                    

                LocalDate checkIn3 = LocalDate.of(2024, 06, 30);
		LocalDate checkOut3 = LocalDate.of(2024, 07, 15);
                Estadia estadiaBariloche = new Estadia(hotelCerroCatedral, checkIn3, checkOut3,
				                    Servicio.SIN_SERVICIO, 300.00, bariloche);                                      

                LocalDate checkIn4 = LocalDate.of(2023, 12, 23);
		LocalDate checkOut4 = LocalDate.of(2024, 01, 07);
		Estadia estadiaFloripa = new Estadia(pousadaOsIngleses, checkIn4, checkOut4,
                                          Servicio.PENSION_COMPLETA,  400.00, florianapolis);
 		
                EstadiaData.cargarEstadia(estadiaMarDelPlata);
                EstadiaData.cargarEstadia(estadiaCarlosPaz);
                EstadiaData.cargarEstadia(estadiaBariloche);
                EstadiaData.cargarEstadia(estadiaFloripa);
                
                Pasaje pasajeAMarDelPlata = new Pasaje(TipoTransporte.MICRO_LARGA_DISTANCIA, 100.00, caba, marDelPlata);
                Pasaje pasajeACarlosPaz = new Pasaje(TipoTransporte.AVION, 250.00, caba, carlosPaz);
                Pasaje pasajeABariloche = new Pasaje(TipoTransporte.MICRO_LARGA_DISTANCIA, 1000.00, marDelPlata, bariloche);
                Pasaje pasajeAFloripa = new Pasaje(TipoTransporte.MICRO_LARGA_DISTANCIA, 400.00, marDelPlata, florianapolis);      
                
                PasajeData.crearPasaje(pasajeAMarDelPlata);
                PasajeData.crearPasaje(pasajeACarlosPaz);
                PasajeData.crearPasaje(pasajeABariloche);
                PasajeData.crearPasaje(pasajeAFloripa);
//                
//                PaqueteTuristico enMarDelPlata = new PaqueteTuristico(estadiaMarDelPlata, pasajeAMarDelPlata);
//                PaqueteTuristico enCarlosPaz = new PaqueteTuristico(estadiaCarlosPaz, pasajeACarlosPaz);
//                PaqueteTuristico enBariloche = new PaqueteTuristico(estadiaBariloche, pasajeABariloche);
//                PaqueteTuristico enFloripa = new PaqueteTuristico(estadiaFloripa, pasajeAFloripa);
//               
//                PaqueteData.crearPaquete(enMarDelPlata);
//                PaqueteData.crearPaquete(enCarlosPaz);
//                PaqueteData.crearPaquete(enBariloche);
//                PaqueteData.crearPaquete(enFloripa);
	}
}