package com.silentsoft.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

// Menu principal
public class MainMenuScreen implements Screen {

	final Drop game;

	// Camara tridimensional
	OrthographicCamera camara;

	// Como la interfaz Screen no implementa un metodo create(), entonces se usa un constructor en su lugar
	public MainMenuScreen(final Drop game) {
		this.game = game;

		camara = new OrthographicCamera();
		camara.setToOrtho(false, 800, 480);

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camara.update();
		game.batch.setProjectionMatrix(camara.combined);

		game.batch.begin();
		// Representa el texto en pantalla
		game.font.draw(game.batch, "Pantalla principal", 100, 150);
		game.font.draw(game.batch, "Toca en cualquier lado para empezar!", 100, 100);
		game.batch.end();

		// Si se toca la pantalla, configura la pantalla de juego en una instancia de GameScreen e desecha la instancia actual 
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
