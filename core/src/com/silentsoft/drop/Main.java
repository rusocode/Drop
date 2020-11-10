package com.silentsoft.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Main extends ApplicationAdapter {
	/* SpriteBatch es una clase especial que se usa para dibujar imagenes 2D, como las texturas que cargamos. */
	private SpriteBatch batch;
	private OrthographicCamera camara;

	private Rectangle balde;

	private Texture gotaImg, baldeImg;
	private Sound gotaSound;
	private Music lluviaMusic;

	@Override
	public void create() {

		// Carga las imagenes para la gota y el balde, 64x64 pixeles cada una
		/* La carga de una instancia de sonido o musica se realiza a traves de Gdx.audio.newSound() y Gdx.audio.newMusic().
		 * Ambos metodos toman un FileHandle, al igual que el constructor Texture. */
		gotaImg = new Texture(Gdx.files.internal("gota.png"));
		baldeImg = new Texture(Gdx.files.internal("balde.png"));

		// Carga el efecto de sonido de gota y la musica de fondo de lluvia
		/* libGDX diferencia entre los efectos de sonido, que se almacenan en la memoria, y la musica, que se transmite desde
		 * donde se almacena. */
		gotaSound = Gdx.audio.newSound(Gdx.files.internal("gota.wav"));
		lluviaMusic = Gdx.audio.newMusic(Gdx.files.internal("lluvia.mp3"));

		// Inicia la reproduccion de la musica de fondo inmediatamente
		lluviaMusic.setLooping(true);
		lluviaMusic.play();

		// ----------

		camara = new OrthographicCamera();
		camara.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		// ----------
		/* De forma predeterminada, todo el renderizado en libGDX (y OpenGL) se realiza con el eje y apuntando hacia arriba. */
		balde = new Rectangle();
		balde.x = 800 / 2 - 64 / 2;
		balde.y = 20;
		balde.width = 64;
		balde.height = 64;

	}

	@Override
	public void render() {
		// Limpia la pantalla con un color azul oscuro
		Gdx.gl.glClearColor(0, 0, 0.2f, 1); // Establece el color claro en azul
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Indica a OpenGL que borre la pantalla
		
		camara.update();
	}

	@Override
	public void dispose() {
		// batch.dispose();
		// img.dispose();
	}
}
