package com.silentsoft.drop.views.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.silentsoft.drop.Drop;
import com.silentsoft.drop.utils.Recursos;

// Pantalla del juego
public class GameScreen extends View {

	final Drop game;

	private OrthographicCamera camara;

	// Recursos
	private Texture gotaImg;
	private Texture baldeImg;
	private Sound dropSound;
	private Music rainMusic;

	// Crea un rectangulo para cada textura
	private Rectangle cubo;
	private Rectangle gota;
	/* La clase Array es una clase de utilidad libGDX que se utiliza en lugar de las colecciones estandar de Java como
	 * ArrayList. El problema con estos ultimos es que producen basura de diversas formas. La clase Array intenta minimizar
	 * la basura tanto como sea posible. */
	private Array<Rectangle> gotas;

	/* Tambien necesitamos realizar un seguimiento de la ultima vez que generamos una gota de lluvia, por lo que agregamos
	 * otro campo. */
	private long lastDropTime;

	private int dropsCounts;

	public GameScreen(final Drop game) {
		this.game = game;

		// Carga las imagenes para la gota y el cubo (64x64 pixeles cada una)
		gotaImg = new Texture(Gdx.files.internal(Recursos.RUTA_COGOLLO));
		baldeImg = new Texture(Gdx.files.internal(Recursos.RUTA_FRASCO)); // El metodo internal() hace referencia a los recursos del proyecto

		/* libGDX diferencia entre los efectos de sonido, que se almacenan en memoria, y la musica, que se transmite desde donde
		 * se almacena. La musica suele ser demasiado grande para guardarla en la memoria por completo, de ahi la diferencia.
		 * Como regla general, debe usar una instancia Sound si su muestra dura menos de 10 segundos y una instancia Music para
		 * piezas de audio mas largas.
		 * 
		 * La carga de una instancia Sound o Music se realiza mediante Gdx.audio.newSound() y Gdx.audio.newMusic(). Ambos
		 * metodos toman un FileHandle, al igual que el constructor Texture. */
		dropSound = Gdx.audio.newSound(Gdx.files.internal(Recursos.RUTA_SONIDO_GOTA));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal(Recursos.RUTA_SONIDO_LLUVIA));
		rainMusic.play(); // Reproduce la musica
		rainMusic.setLooping(true); // Vuelve a reproducir la musica

		// Esto asegurara que la camara siempre nos muestre un area de nuestro mundo de 800x480
		camara = new OrthographicCamera();
		camara.setToOrtho(false, 800, 480);
		game.batch = new SpriteBatch();

		// Crea los rectangulos para el cubo y las gotas con sus respectivos tamaños y posiciones
		spawnCubo();
		gotas = new Array<Rectangle>();
		spawnGota();

	}

	@Override
	public void render(float delta) {

		/* La primera llamada establecera el fondo de color azul claro. Los argumentos son el componente rojo, verde, azul y
		 * alfa (transparencia) de ese color, con un rango de [0, 1] para la transparencia. */
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		// Borra la pantalla
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/* A continuacion, debemos decirle a nuestra camara que se asegure de que este actualizada. Las camaras utilizan una
		 * entidad matematica llamada matriz que es responsable de configurar el sistema de coordenadas para la representacion.
		 * Estas matrices deben volver a calcularse cada vez que cambiamos una propiedad de la camara, como su posicion. No
		 * hacemos esto en nuestro ejemplo simple, pero generalmente es una buena practica actualizar la camara una vez por
		 * cuadro. */
		camara.update();

		/* La primera linea le dice al SpriteBatch que use el sistema de coordenadas especificado por la camara. Como se dijo
		 * anteriormente, esto se hace con algo llamado matriz, para ser mas especificos, una matriz de proyeccion. El campo
		 * camera.combined es tal matriz. A partir de ahi el SpriteBatch renderizara todo en el sistema de coordenadas descrito
		 * anteriormente.
		 * 
		 * A continuacion, le decimos al SpriteBatch que inicie un nuevo lote. ¿Por que necesitamos esto y que es un lote?
		 * OpenGL odia nada mas que contarlo sobre imagenes individuales. Quiere que le digan acerca de tantas imagenes para
		 * renderizar como sea posible a la vez.
		 * 
		 * La clase SpriteBatch ayuda a hacer feliz a OpenGL. Registrara todos los comandos de dibujo entre SpriteBatch.begin()
		 * y SpriteBatch.end(). Una vez que llamemos a SpriteBatch.end(), este enviara todas las solicitudes de dibujo que
		 * hicimos a la vez, acelerando un poco el renderizado. Todo esto puede parecer engorroso al principio, pero es lo que
		 * marca la diferencia entre renderizar 500 sprites a 60 cuadros por segundo y renderizar 100 sprites a 20 cuadros por
		 * segundo. */
		game.batch.setProjectionMatrix(camara.combined);
		game.batch.begin();
		game.font.draw(game.batch, "Cogollos atrapados: " + dropsCounts, 0, 480);
		game.batch.draw(baldeImg, cubo.x, cubo.y); // Dibuja sobre el rectangulo de la posicion x/y la textura
		for (Rectangle raindrop : gotas)
			game.batch.draw(gotaImg, raindrop.x, raindrop.y);
		game.batch.end();

		moverCubo();

		// Verifica cuanto tiempo ha pasado desde que generamos una nueva gota y crea una nueva si es necesario
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnGota();

		moverGotas();
	}

	@Override
	public void show() {
		// Inicia la reproduccion de la musica de fondo cuando se muestra la pantalla
		rainMusic.play();
	}

	/* Los desechables son generalmente recursos nativos que no son manejados por el recolector de basura de Java. Esta es
	 * la razon por la que necesitamos deshacernos de ellos manualmente. */
	@Override
	public void dispose() {
		gotaImg.dispose();
		baldeImg.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		// game.batch.dispose();
	}

	private void spawnCubo() {
		cubo = new Rectangle();
		// Por defecto, el origen del rectangulo comienza es en la esquina inferior izquierda de la pantalla
		cubo.x = 800 / 2 - 64 / 2; // Centrado horizontalmente
		cubo.y = 20; // 20 pixeles por encima del borde inferior de la pantalla
		/* El ancho y la altura del rectangulo se establecen en 64x64 (tamaño en pixeles de la textura), nuestra porcion mas
		 * pequeña de nuestra altura de resoluciones objetivo. */
		cubo.width = 64;
		cubo.height = 64;
	}

	private void spawnGota() {
		/* Un cubo/gota de lluvia tiene una posicion x/y en nuestro mundo de 800x480 unidades. Un cubo/gota de lluvia tiene un
		 * ancho y una altura, expresados en las unidades de nuestro mundo. Un cubo/gota de lluvia tiene una representacion
		 * grafica, que ya las tenemos en forma de las instancias Texture que cargamos. */
		gota = new Rectangle();
		gota.x = MathUtils.random(0, 800 - 64);
		gota.y = 480;
		gota.width = 64;
		gota.height = 64;
		gotas.add(gota); // Agrega el rectangulo a la lista de rectangulos
		lastDropTime = TimeUtils.nanoTime(); // Registra el tiempo actual en nano segundos de la ultima gota
	}

	private void moverCubo() {
		/* Hacer que el cubo se mueva (toque/mouse) */
		/* Primero le preguntamos al modulo de entrada si la pantalla esta actualmente tocada (o si se presiona un boton del
		 * mouse) llamando a Gdx.input.isTouched(). A continuacion, queremos transformar las coordenadas de toque/mouse al
		 * sistema de coordenadas de nuestra camara. Esto es necesario porque el sistema de coordenadas en el que se informan
		 * las coordenadas toque/mouse pueden ser diferente al sistema de coordenadas que usamos para representar objetos en
		 * nuestro mundo.
		 * 
		 * Gdx.input.getX() y Gdx.input.getY() devuelven la posicion actual de toque/mouse. Para transformar estas coordenadas
		 * en el sistema de coordenadas de nuestra camara, debemos llamar al metodo camera.unproject(), que solicita a Vector3,
		 * un vector tridimensional. Creamos dicho vector, establecemos las coordenadas actuales de toque/mouse y llamamos al
		 * metodo. El vector ahora contendra las coordenadas de toque/mouse en el sistema de coordenadas en el que vive nuestro
		 * cubo. Finalmente cambiamos la posicion del cubo para que se centre alrededor de las coordenadas de toque/mouse. */
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camara.unproject(touchPos); // Ahora la camara seguira al cubo cuando la ventana se redimensione
			cubo.x = touchPos.x - 64 / 2;
		}

		/* Hacer que el cubo se mueva (teclado) */
		/* Queremos que el cubo se mueva sin aceleracion, a doscientos pixeles/unidades por segundo, ya sea hacia la izquierda o
		 * hacia la derecha. Para implementar este movimiento basado en el tiempo, necesitamos saber el tiempo que paso entre el
		 * ultimo y el actual fotograma de renderizado.
		 * 
		 * El metodo Gdx.input.isKeyPressed() nos dice si se presiono una tecla especifica. La enumeracion Keys contiene todos
		 * los codigos clave que admite libGDX. El metodo Gdx.graphics.getDeltaTime() devuelve el tiempo transcurrido entre el
		 * ultimo y el actual fotograma en segundos. Todo lo que tenemos que hacer es modificar la coordenada x del cubo
		 * sumando/restando 200 unidades por el tiempo delta en segundos. */
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) cubo.x -= 200 * Gdx.graphics.getDeltaTime();
		// System.out.println(Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cubo.x += 200 * Gdx.graphics.getDeltaTime();

		// Mantiene al cubo dentro de los limites de la pantalla
		if (cubo.x < 0) cubo.x = 0;
		if (cubo.x > 800 - 64) cubo.x = 800 - 64;
	}

	private void moverGotas() {
		// Itera cada gota de la lista para darle una sensacion de movimiento
		for (Iterator<Rectangle> gota = gotas.iterator(); gota.hasNext();) {
			Rectangle raindrop = gota.next();
			// Mueve la gota a una velocidad constante de 200 pixeles/unidades por segundo
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			// Si la gota esta por debajo del borde inferior de la pantalla, entonces...
			if (raindrop.y + 64 < 0) eliminarGota(gota);
			// Si la gota toca el cubo, entonces...
			if (raindrop.overlaps(cubo)) { // Comprueba si este rectangulo se superpone con otro rectangulo
				dropsCounts++;
				dropSound.play();
				eliminarGota(gota);
			}
		}
	}

	private void eliminarGota(Iterator<Rectangle> gota) {
		gota.remove();
	}

}