package algoritmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Ladrao extends ProgramaLadrao {
	//Constantes 
	
	static private int numeroLadrao;
	
	public final int PARADO = 0;
	public final int CIMA = 1;
	public final int BAIXO = 2;
	public final int DIREITA = 3;
	public final int ESQUERDA = 4;
 
	public final int VISAO_INDISPONIVEL = -2;
	public final int VISAO_MUNDO_EXTERIOR = -1;
	public final int VISAO_MAPA_VAZIA = 0;
	public final int VISAO_PAREDE = 1;
	public final int VISAO_BANCO = 3;
	public final int VISAO_MOEDA = 4;
	public final int VISAO_PASTILHA = 5;
 
	public final int[] VISAO_POUPADOR = { 100, 110 };
	public final int[] VISAO_LADRAO = { 200, 210, 220, 230 };
	
	//Atributos
	Random random;
	int [][] mapa;
	
	int desejoDeIrParaCima;
	int desejoDeIrParaBaixo;
	int desejoDeIrParaDireita;
	int desejoDeIrParaEsquerda;
	int desejoDeFicarParado;
	
	//static identifica��o Ladr�o
	static int jogadaDosLadroes =0;
	
	//Construtor
	public Ladrao() {
		super();
		mapa = new int[30][30];
		inicializarMapa();
	}

	private String exibirNumeroComDoisDigito(int num){
		if(num > 9){
			return Integer.toString(num);
		}
		return "0"+num;
	}
	
	public void inicializarMapa() {
		for(int x = 0; x < mapa.length; x++) {
			for(int y= 0; y < mapa.length; y++) {
				mapa[x][y] = 0;
			}
		}
	}
	
	private void mapear(){
		int posicaoNoVetorDaVisao = 0;
		for(int y1 = -2; y1 <= 2; y1++){
			for(int x1 = -2; x1 <= 2; x1++){
				if(x1 == 0 && y1 == 0 ){
					
				}else{
					
					switch (sensor.getVisaoIdentificacao()[posicaoNoVetorDaVisao++]) {
					
					case VISAO_PAREDE:
					case VISAO_BANCO:
						mapa[sensor.getPosicao().x + x1][sensor.getPosicao().y+y1] = 99;
						break;
					
					case VISAO_MOEDA:
					case VISAO_PASTILHA:
						mapa[sensor.getPosicao().x + x1][sensor.getPosicao().y+y1] = 88;
						break;
					
					case VISAO_INDISPONIVEL :
					case VISAO_MUNDO_EXTERIOR :
					case VISAO_MAPA_VAZIA:
						
					default:
						break;
					}
				}
			}
		}
	}
	
	private int localMenosAndado(int x,int y, int menor){
		 if(x > 0 && y >0 && x < 30 && y < 30)
			 if(menor > mapa[x][y])
				 return mapa[x][y];		 
		 return menor;
	 }

	private int pesoParaEscolhadoCaminho(int x, int y, int menor){
		 if(x > 0 && y >0 && x < 30 && y < 30)
			 if(menor == mapa[x][y])
				 return 30;		 
		 return 0;
	 }

	private int decisaoDeMovimento(){
		int somaDesejo = desejoDeIrParaCima+desejoDeIrParaBaixo+desejoDeIrParaDireita+desejoDeIrParaEsquerda+desejoDeFicarParado; 
		random = new Random();
		int sorteio = random.nextInt(somaDesejo);
		
		if(jogadaDosLadroes%4==numeroLadrao){
			System.out.println("Peso Cima = "+desejoDeIrParaCima+"  Peso Baixo = "+desejoDeIrParaBaixo+" Peso Direita = "+desejoDeIrParaDireita+"Peso Esquerda"+desejoDeIrParaEsquerda+"desejoDeFicarParado"+desejoDeFicarParado);
			System.out.println("random = "+sorteio+" Soma dos Pesos = "+somaDesejo);

		}

		sorteio = sorteio - desejoDeIrParaCima;
		if(sorteio <= 0){
			return CIMA;
		}
		sorteio = sorteio -desejoDeIrParaBaixo;
		if(sorteio <= 0){
			return BAIXO;
		}sorteio = sorteio -desejoDeIrParaDireita;
		if(sorteio <= 0){
			return DIREITA;
		}
		sorteio = sorteio - desejoDeIrParaEsquerda;
		if(sorteio <= 0){
			return ESQUERDA;
		}
		return PARADO;
	}
	
	
	private void somarDirecaoDesejo(int index, int desejo, int valor){
		if (sensor.getVisaoIdentificacao()[index] == 100 || sensor.getVisaoIdentificacao()[index] == 110) { 
			desejo += valor; 
		} 
	}
	
	//Metodo Agente 
	//Simples
	private void desejoDeRoubarPoupador(){
		
		somarDirecaoDesejo(7,desejoDeIrParaCima, 200);
		somarDirecaoDesejo(16,desejoDeIrParaBaixo, 200);
		somarDirecaoDesejo(12,desejoDeIrParaDireita, 200);
		somarDirecaoDesejo(11,desejoDeIrParaCima, 200);
		
		
		//		if(sensor.getVisaoIdentificacao()[7] == 100 || sensor.getVisaoIdentificacao()[7] == 110 )
//			desejoDeIrParaCima +=200;
//		if(sensor.getVisaoIdentificacao()[16] == 100)
//			desejoDeIrParaBaixo +=200;
//		if(sensor.getVisaoIdentificacao()[12] == 100)
//			desejoDeIrParaDireita += 200;
//		if(sensor.getVisaoIdentificacao()[11] == 100)
//			desejoDeIrParaEsquerda +=200;
	}
	private void cheirar(){
		if(sensor.getAmbienteOlfatoPoupador()[1] > 0){
			desejoDeIrParaCima += (120/sensor.getAmbienteOlfatoPoupador()[1]);
		}
			
		if(sensor.getAmbienteOlfatoPoupador()[6] > 0){
			desejoDeIrParaBaixo +=120/sensor.getAmbienteOlfatoPoupador()[6];
		}
		if(sensor.getAmbienteOlfatoPoupador()[4] > 0){
			desejoDeIrParaDireita +=120/sensor.getAmbienteOlfatoPoupador()[4];
		}
		if(sensor.getAmbienteOlfatoPoupador()[3] > 0){
			desejoDeIrParaEsquerda += 120/sensor.getAmbienteOlfatoPoupador()[3];
		}
	}
	
	//Baseado em Modelo
	
	 private void salvarPosicao(){
		 mapa[sensor.getPosicao().x][sensor.getPosicao().y] ++;
		 
	 }
	 
	 
	
	//Objetivo
	
	 
	 
	//Metodo Utilidade
	 private void explorar(){
		mapear();
		int menor = 999;
		
		menor = localMenosAndado((int)sensor.getPosicao().x-1,(int) sensor.getPosicao().y,menor);
		menor = localMenosAndado((int)sensor.getPosicao().x+1,(int) sensor.getPosicao().y,menor);
		menor = localMenosAndado((int)sensor.getPosicao().x,(int) sensor.getPosicao().y-1,menor);
		menor = localMenosAndado((int)sensor.getPosicao().x,(int) sensor.getPosicao().y+1,menor);
		
		if(jogadaDosLadroes%4==numeroLadrao){
			System.out.println("Ladrao"+numeroLadrao%4+" encontou o caminho com menos peso  = " +menor);
		}
		desejoDeIrParaEsquerda += pesoParaEscolhadoCaminho((int)sensor.getPosicao().x-1,(int) sensor.getPosicao().y,menor);
		desejoDeIrParaDireita += pesoParaEscolhadoCaminho((int)sensor.getPosicao().x+1,(int) sensor.getPosicao().y,menor);
		desejoDeIrParaCima += pesoParaEscolhadoCaminho((int)sensor.getPosicao().x,(int) sensor.getPosicao().y-1,menor);
		desejoDeIrParaBaixo += pesoParaEscolhadoCaminho((int)sensor.getPosicao().x,(int) sensor.getPosicao().y+1,menor);
		
		System.out.println();
		
		
	}
	
	 //A��o
	public int acao() {
		
		if(jogadaDosLadroes%4==numeroLadrao){
			System.out.println();
			for(int y = 0 ; y < 30; y++){
				for(int x = 0 ; x < 30; x++){
					System.out.print(exibirNumeroComDoisDigito(mapa[x][y])+" ");
				}
				System.out.println();
			}
		}
		
		//Inicializa��o
		jogadaDosLadroes++;
		desejoDeIrParaCima = 1;
		desejoDeIrParaBaixo = 1;
		desejoDeIrParaEsquerda =1;
		desejoDeIrParaDireita =1;
		desejoDeFicarParado = 1;
		salvarPosicao();
		//AgenteSimples
			desejoDeRoubarPoupador();
			cheirar();
		//Agente Utilidade
			explorar();
			
		return decisaoDeMovimento();
	}

	
	
	
	
	
	
	
	
	
	//================================TESTE=====================================================
	//Atributos
//	Graph grafoMapa = new Graph("Mapa");
	
	private void mapearGrafo(){
		int cont = 0;
		for(int y1 = -2; y1 <= 2; y1++){
			for(int x1 = -2; x1 <= 2; x1++){
				if(x1 == 0 && y1 == 0 ){
					
				}else{
					
					switch (sensor.getVisaoIdentificacao()[cont++]) {
					case -2 :
					case -1 :
						break;
					case 1:
					case 3:
						mapa[sensor.getPosicao().x + x1][sensor.getPosicao().y+y1] = 99;
						break;
					
					case 4:
					case 5:
					case 100:
					case 200:
						mapa[sensor.getPosicao().x + x1][sensor.getPosicao().y+y1] += 3;
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	
	
	
	
	
	
}