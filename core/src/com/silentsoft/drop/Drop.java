package com.silentsoft.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * La clase Game es responsable de manejar multiples pantallas y proporciona algunos metodos auxiliares para este
 * proposito, junto con una implementacion de ApplicationListener para su uso.
 */

// Juego
public class Drop extends Game {

	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		batch = new SpriteBatch(); // Representa objetos en la pantalla, como texturas
		font = new BitmapFont(); // Representa texto en la pantalla (fuente Arial predeterminada de LibGDX)
		this.setScreen(new MainMenuScreen(this)); // Le pasa un objeto Drop al menu principal
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();

		// Libera los recursos de la ventana de juego manualmente
		screen.dispose();

	}

}