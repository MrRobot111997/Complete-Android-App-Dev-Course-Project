package com.example.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.jar.Attributes;

import javax.management.StringValueExp;
import javax.xml.soap.Text;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

	Texture[] man ;
	int manState = 0  ,pause = 0;
	float gravity = 0.8f , velocity = 0 ;
	int manY = 0 ;
	Rectangle manRectangle ;

	ArrayList<Integer> coinsXs = new ArrayList<>() ;
	ArrayList<Integer> coinsYs = new ArrayList<>() ;
	ArrayList<Rectangle> coinRectangles = new ArrayList<>() ;
	int coinCount = 0 ;
	Texture coin ;

	ArrayList<Integer> bombXs = new ArrayList<>() ;
	ArrayList<Integer> bombYs = new ArrayList<>() ;
	ArrayList<Rectangle> bombRectangles = new ArrayList<>() ;
	Texture bomb ;
	int bombCount = 0 ;

	int score = 0 ;
	BitmapFont scoreFont ;

	int gameState ;

	Random random ;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png") ;
		man = new Texture[5] ;
		man[0] = new Texture( "frame-1.png" );
		man[1] = new Texture( "frame-2.png" );
		man[2] = new Texture( "frame-3.png" );
		man[3] = new Texture( "frame-4.png" );
		man[4] = new Texture( "dizzy-1.png" );

		manY = Gdx.graphics.getHeight()/2 ;


		coin = new Texture( "coin.png" ) ;
		bomb = new Texture("bomb.png") ;
		random = new Random() ;

		scoreFont = new BitmapFont() ;
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(10) ;
	}

	public void makeCoin(){
		float height = random.nextFloat() * Gdx.graphics.getHeight() ;
		coinsYs.add((int)height) ;
		coinsXs.add(Gdx.graphics.getWidth()) ;
	}

	public void createBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight() ;
		bombYs.add( (int) height );
		bombXs.add( Gdx.graphics.getWidth() ) ;
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background , 0 , 0 , Gdx.graphics.getWidth() , Gdx.graphics.getHeight());

		if( gameState == 0 ){
			if( Gdx.input.justTouched() )
				gameState = 1 ;
		}
		else if( gameState == 1){
			if( coinCount < 100 ){
				coinCount++ ;
			}else{
				coinCount = 0 ;
				makeCoin();
			}
			coinRectangles.clear() ;
			for(int i=0 ; i < coinsXs.size() ; i++){
				batch.draw( coin , coinsXs.get(i) , coinsYs.get(i) );
				coinsXs.set( i , coinsXs.get(i) - 4 ) ;
				coinRectangles.add( new Rectangle( coinsXs.get(i) , coinsYs.get(i) , coin.getWidth() , coin.getHeight() ) ) ;
			}

			if( bombCount < 400 ){
				bombCount++ ;
			}
			else{
				bombCount = 0 ;
				createBomb() ;
			}

			bombRectangles.clear() ;
			for( int i=0; i < bombYs.size() ; i++ ){
				batch.draw( bomb , bombXs.get(i) , bombYs.get(i) );
				bombXs.set( i ,  bombXs.get(i) - 8 ) ;
				bombRectangles.add( new Rectangle( bombXs.get(i) , bombYs.get(i) , bomb.getWidth() , bomb.getHeight() ) ) ;
			}

			if( Gdx.input.justTouched() ){
				velocity = -40 ;
			}

			if( pause < 5){
				pause++;
			}
			else {
				pause = 0 ;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}

			velocity += gravity ;
			manY -= velocity ;

			if( manY <= 0){
				manY = 0 ;
			}
		}
		else if( gameState == 2 ){
			if( Gdx.input.justTouched() ) {
				gameState = 1;
				score = 0;
				manY = Gdx.graphics.getHeight() / 2;
				velocity = 0;
				coinsXs.clear();
				coinsYs.clear();
				coinCount = 0;
				bombRectangles.clear();
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
			}
		}


		if( gameState == 2 ){
			batch.draw(man[4], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		else {
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		manRectangle = new Rectangle( Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 , manY , man[manState].getWidth() , man[manState].getHeight() ) ;

		for( int i = 0 ; i < coinRectangles.size() ; i++ ){
			if(Intersector.overlaps( manRectangle , coinRectangles.get(i) )) {
				score++ ;

				coinRectangles.remove(i) ;
				coinsXs.remove(i) ;
				coinsYs.remove(i) ;
				break ;
			}
		}

		for( int i= 0 ; i < bombRectangles.size() ; i++ ){
			if( Intersector.overlaps( manRectangle , bombRectangles.get(i) ) )
			{
				gameState = 2 ;
			}
		}

		scoreFont.draw( batch , String.valueOf(score) , 100 , 200 );

		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
