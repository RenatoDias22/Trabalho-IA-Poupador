package algoritmo;

public class Ladrao extends ProgramaLadrao {
	
	static private int numeroLadrao;
	
    protected final int PARADO = 0;
    protected final int CIMA = 1;
    protected final int BAIXO = 2;
    protected final int DIREITA = 3;
    protected final int ESQUERDA = 4;
 
    protected final int VISAO_INDISPONIVEL = -2;
    protected final int VISAO_MUNDO_EXTERIOR = -1;
    protected final int VISAO_MAPA_VAZIA = 0;
    protected final int VISAO_PAREDE = 1;
    protected final int VISAO_BANCO = 3;
    protected final int VISAO_MOEDA = 4;
    protected final int VISAO_PASTILHA = 5;
 
    protected final int VISAO_POUPADOR[] = { 100, 110 };
    protected final int VISAO_LADRAO[] = { 200, 210, 220, 230 };
    
    protected int visaoIdentificao[];
	
    protected double[][] matrizMemoria = new double[30][30];
	
    protected boolean inicializouMemoria = false;
	
	public int acao() {
		
		if (!inicializouMemoria) {
			inicializarMatrizMemoria();
			inicializouMemoria = true;
		}
		
		matrizMemoria[(int)sensor.getPosicao().getX()][(int) sensor.getPosicao().getY()]++;
//		System.out.println("Posição x e y: " + sensor.getPosicao().getX() + " " + sensor.getPosicao().getY() );
		int andou = escolherCasaParaAndar();
		
		if(numeroLadrao%4==0) {
			imprimirMatrizMemoria();
			System.out.println("Posição x e y: " + sensor.getPosicao().getX() + " " + sensor.getPosicao().getY() );
			System.out.println("Valor matriz " + matrizMemoria[(int)sensor.getPosicao().getX()][(int) sensor.getPosicao().getY()]);
			
		}
		numeroLadrao++;
		return andou;
	}
	
	public int escolherCasaParaAndar() {
		
		visaoIdentificao = sensor.getVisaoIdentificacao();
		
		combinarVisaoComMapa();
		
		int x = (int) sensor.getPosicao().getX();
		int y = (int) sensor.getPosicao().getY();
		
		if(x-1 >= 0 && y-1 >= 0 && x+1 < 30 && y+1 < 30) {
			
			double andarCima = (double) (Math.random() * 10) - (double) matrizMemoria[x][y-1];
			double andarBaixo = (double) (Math.random() * 10) - (double) matrizMemoria[x][y+1];
			double andarEsquerda = (double) (Math.random() * 10) - (double) matrizMemoria[x-1][y];
			double andarDireita = (double) (Math.random() * 10) - (double) matrizMemoria[x+1][y];
			double parado = (double) (Math.random() * 10) - (double) matrizMemoria[x][y];
			
			double max = Math.max(andarCima,
	                Math.max(andarBaixo,
	                Math.max(andarEsquerda,
	                Math.max(andarDireita,parado))));
			
			if(numeroLadrao%4==0) {
				System.out.println("Valor maximo: "+ max);
				for(int i = 0; i < visaoIdentificao.length; i++) {
					System.out.println("Visão: "+ visaoIdentificao[i]);
				}
			}
			
			if (max == andarCima) {
				return CIMA;
			}
			if (max == andarBaixo) {
				return BAIXO;
			}
			if(max == andarDireita) {
				return DIREITA;
			}
			if(max == andarEsquerda) {
				return ESQUERDA;
			}
		}
		return PARADO;
		
	}
	
	public void combinarVisaoComMapa(){
        
        int x = (int) sensor.getPosicao().getX();
        int y = (int) sensor.getPosicao().getX();
 
        double v[] = new double[24];
         
        int iX;
        int iY;
        for (int i = 0; i < 5; i++) {
             iX = i +(x-2);
             iY = y - 2;
             plotarNoMapa(iX, iY, visaoIdentificao[i]); 
             v[i] = getCelula(iX,iY);
             iY = y - 1;
             plotarNoMapa(iX, iY, visaoIdentificao[i+5]); 
             v[i+5] = getCelula(iX,iY);
             iY = y + 1;
             plotarNoMapa(iX, iY, visaoIdentificao[i+14]); 
             v[i+14] = getCelula(iX,iY);
             iY = y + 2;
             plotarNoMapa(iX, iY, visaoIdentificao[i+19]); 
             v[i+19] = getCelula(iX,iY);
        }
        plotarNoMapa(x-2, y, visaoIdentificao[10]); 
        v[10] = getCelula(x-2,y);
        plotarNoMapa(x-1, y, visaoIdentificao[11]); 
        v[11] = getCelula(x-1,y);
        plotarNoMapa(x+1, y, visaoIdentificao[12]); 
        v[12] = getCelula(x+1,y);
        plotarNoMapa(x+2, y, visaoIdentificao[13]); 
        v[13] = getCelula(x+2,y);
         
//        return v;
    }
     
     
    public double getCelula(int x, int y){
        if(x < 30 && y < 30 && x >= 0 && y >= 0){
            return  matrizMemoria[x][y];
        }
        return 0;
    }
  
    public void plotarNoMapa(int x, int y, int valor){
        if(x < 30 && y < 30 && x >= 0 && y >= 0){
            if(valor != VISAO_INDISPONIVEL){
                if(valor == VISAO_MOEDA || 
                   valor == VISAO_PAREDE || 
                   valor ==  VISAO_PASTILHA || 
                   valor == VISAO_BANCO ){
                    matrizMemoria[y][x] = 9999999;
                }
            }
        }
    }

	
	public void inicializarMatrizMemoria() {
		for(int x = 0; x < this.matrizMemoria.length; x++) {
			for(int y= 0; y < this.matrizMemoria.length; y++) {
				this.matrizMemoria[x][y] = 0;
			}
		}
	}
	
	public void	imprimirMatrizMemoria() {
		
		for(int x = 0; x < this.matrizMemoria.length;x++) {
			for(int y= 0; y < this.matrizMemoria.length; y++) {
				System.out.print(" " + this.matrizMemoria[x][y]);
			}
			System.out.println(" ");
		}
	}

}