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

public class Drop extends ApplicationAdapter {

	private OrthographicCamera camara;
	/* El SpriteBatch es una clase especial que se utiliza para dibujar imagenes en 2D, al igual que las texturas
	 * cargadas. */
	private SpriteBatch batch;

	// Un cubo/gota de lluvia tiene una posicion x/y en nuestro mundo de 800x480 unidades.
	// Un cubo/gota de lluvia tiene un ancho y una altura, expresados ​​en las unidades de nuestro mundo.
	/* Un cubo/gota de lluvia tiene una representacion grafica, que ya las tenemos en forma de las instancias Texture que
	 * cargamos. */
	private Texture gotaImg;
	private Texture baldeImg;
	private Sound gotaSonido;
	private Music lluviaMusica;

	// Clase de libGDX para almacenar la posicion y tamaño del cubo y la gota
	private Rectangle balde;

	@Override
	public void create() {

		// Carga las imagenes para la gota y el balde (64x64 pixeles cada una)
		gotaImg = new Texture(Gdx.files.internal("gota.png"));
		baldeImg = new Texture(Gdx.files.internal("balde.png")); // El metodo internal() hace referencia a nuestros recursos

		// Carga el efecto de sonido de la gota y la "musica" de fondo de lluvia
		gotaSonido = Gdx.audio.newSound(Gdx.files.internal("gota.wav"));
		lluviaMusica = Gdx.audio.newMusic(Gdx.files.internal("lluvia.mp3"));

		/* libGDX diferencia entre los efectos de sonido, que se almacenan en la memoria, y la musica, que se transmite desde
		 * donde se almacena. La musica suele ser demasiado grande para guardarla en la memoria por completo, de ahi la
		 * diferencia. Como regla general, debe usar una instancia Sound si su muestra dura menos de 10 segundos y una instancia
		 * Music para piezas de audio mas largas.
		 * 
		 * La carga de una instancia Sound o Music se realiza mediante Gdx.audio.newSound() y Gdx.audio.newMusic(). Ambos
		 * metodos toman un FileHandle, al igual que el Textureconstructor. */
		// Inicia la reproduccion de la musica de fondo inmediatamente
		lluviaMusica.setLooping(true);
		lluviaMusica.play();

		/* Esto asegurara que la camara siempre nos muestre un area de nuestro mundo de juego de 800x480 unidades de ancho. */
		camara = new OrthographicCamera();
		camara.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		// El origen del dibujo se encuentra en la esquina inferior izquierda de la pantalla.
		/* Queremos que el cubo este 20 pixeles por encima del borde inferior de la pantalla y centrado horizontalmente. */
		balde = new Rectangle();
		balde.x = 800 / 2 - 64 / 2;
		balde.y = 20;
		/* El ancho y la altura del rectangulo se establecen en 64x64, nuestra porcion mas pequeña de nuestra altura de
		 * resoluciones objetivo. */
		balde.width = 64;
		balde.height = 64;

	}

	@Override
	public void render() {
		/* La primera llamada establecera el color claro en azul. Los argumentos son el componente rojo, verde, azul y alfa
		 * (transparencia) de ese color, cada uno dentro del rango [0, 1]. */
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		// Borra la pantalla
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/* A continuacion, debemos decirle a nuestra camara que se asegure de que este actualizada. Las camaras utilizan una
		 * entidad matematica llamada matriz que es responsable de configurar el sistema de coordenadas para la representacion.
		 * Estas matrices deben volver a calcularse cada vez que cambiamos una propiedad de la camara, como su posicion. No
		 * hacemos esto en nuestro ejemplo simple, pero generalmente es una buena practica actualizar la camara una vez por
		 * cuadro. */
		camara.update();

		/* La primera linea le dice SpriteBatch que use el sistema de coordenadas especificado por la camara. Como se dijo
		 * anteriormente, esto se hace con algo llamado matriz, para ser mas especificos, una matriz de proyeccion. El campo
		 * camera.combined es tal matriz. A partir de ahi el SpriteBatch renderizara todo en el sistema de coordenadas descrito
		 * anteriormente.
		 * 
		 * A continuacion, le decimos al SpriteBatchque que inicie un nuevo lote. ¿Por que necesitamos esto y que es un lote?
		 * OpenGL odia nada mas que contarlo sobre imagenes individuales. Quiere que le digan acerca de tantas imagenes para
		 * renderizar como sea posible a la vez.
		 * 
		 * La clase SpriteBatch ayuda a hacer feliz a OpenGL. Registrara todos los comandos de dibujo entre SpriteBatch.begin()
		 * y SpriteBatch.end(). Una vez que lo llamemos SpriteBatch.end(), enviara todas las solicitudes de dibujo que hicimos a
		 * la vez, acelerando un poco el renderizado. Todo esto puede parecer engorroso al principio, pero es lo que marca la
		 * diferencia entre renderizar 500 sprites a 60 cuadros por segundo y renderizar 100 sprites a 20 cuadros por
		 * segundo. */
		batch.setProjectionMatrix(camara.combined);
		batch.begin();
		batch.draw(baldeImg, balde.x, balde.y);
		batch.end();

	}

	@Override
	public void dispose() {

	}
}
