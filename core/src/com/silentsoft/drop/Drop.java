package com.silentsoft.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * La clase Game es responsable de manejar multiples pantallas y proporciona algunos metodos auxiliares para este
 * proposito, junto con una implementacion de ApplicationListener para su uso.
 */
public class Drop extends Game {

	/* El SpriteBatch es una clase especial que se utiliza para dibujar imagenes en 2D, al igual que las texturas
	 * cargadas. */
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create() {

		batch = new SpriteBatch(); // Representa objetos en la pantalla, como texturas
		font = new BitmapFont(); // Representa texto en la pantalla (fuente Arial predeterminada de LibGDX)
		this.setScreen(new MainMenuScreen(this)); // Le pasa un objeto Drop al menu principal

	}

	// Ejecuta cada fotograma, lo que probablemente sea unas 60 veces por segundo en este momento
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
