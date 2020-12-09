package com.silentsoft.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.silentsoft.drop.views.screens.MainMenuScreen;

/**
 * La clase abstracta Game es responsable de manejar multiples pantallas y proporciona algunos metodos auxiliares para
 * este proposito, junto con una implementacion de ApplicationListener para su uso. Delega la mayor parte del trabajo a
 * la Screen actual.
 */

public class Drop extends Game {

	/* El SpriteBatch es una clase especial que se utiliza para dibujar imagenes en 2D, al igual que las texturas
	 * cargadas. */
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create() {

		// Diferencia entre Sprite y SpriteBatch
		/* De una manera simple se puede decir que la clase Sprite es un paquete completo (donde dibujar y con diferentes
		 * factores como tamaño, factor de escala, rotacion…), en donde se evita tener que pasar por parametro los diferentes
		 * factores.
		 * 
		 * Pero si usas "batch.draw(textura, 20,...);" debes indicar la posicion, el tamaño y otros factores que se requieren
		 * para dibujar mediante el metodo draw(). */
		batch = new SpriteBatch(); // Representa objetos en la pantalla, como texturas
		font = new BitmapFont(); // Representa texto en la pantalla (fuente Arial predeterminada de LibGDX)
		this.setScreen(new MainMenuScreen(this)); // Cambia de pantalla pasandole un objeto Game al menu principal

	}

	/* Esta funcion se ejecuta en paralelo al programa. Su objetivo es dibujar (actualizar) 60 veces por segundo (en este
	 * caso) un sprite/fotograma en pantalla.
	 * 
	 * En otras palabras funciona como un bucle. */
	public void render() {
		super.render();
	}

	/* Los desechables son generalmente recursos nativos que no son manejados por el recolector de basura de Java. Esta es
	 * la razon por la que necesitamos deshacernos de ellos manualmente. */
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		// Libera los recursos de la ventana de juego manualmente
		screen.dispose();
	}

}
