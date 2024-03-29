//Test

//+------------------------------------------------------------------+
//|                                                        Daryl.mq4 |
//|                             Copyright 2020, Daryl System Project |
//|                                      https://www.darylsystem.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, Daryl System Project."
#property link      "https://www.darylsystem.com"
#property version   "6.11"
#property strict

#include <mql4-http.mqh>
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+

int OnInit(){

      if(Symbol() == "AUDCAD") {
         ticker = "audcad";
         PipStep = 125;
      }
      if(Symbol() == "NDX") {
         ticker = "ndx";
         PipStep = 700;
      }
      if(Symbol() == "GDAXI") {
         ticker = "gdaxi";
         PipStep = 700;
      }
      if(Symbol() == "XAUUSD") {
         ticker = "xauusd";
         PipStep = 500;
      }
      EventSetTimer(10);
      return(INIT_SUCCEEDED);
}

void OnTimer(){
      
      double signal2WPR = iWPR(Symbol(), period, secretnumber2, 0);
      double calcUno =  iMA(Symbol(), PERIOD_M15, fastMaPeriod, shiftFastMaPeriod, MODE_EMA, PRICE_TYPICAL, 0);
      double calcDos = iMA(Symbol(), PERIOD_M15, slowMaPeriod, shiftSlowMaPeriod, MODE_SMA, PRICE_TYPICAL, 0);
   if(ActivarGraficos == true && IsTesting() == false){
      showText("M200", "M200: " + NormalizeDouble(calcDos, 5), clrAqua, (posInicio + (1 * separacion)));
      showText("M5", "M5: " + NormalizeDouble(calcUno, 5), clrAqua, (posInicio + (2 * separacion)));
      showText("Dif_Medias", "Dif_Medias: " + NormalizeDouble(MathAbs(calcUno - calcDos)/Point,0), clrLightGreen, (posInicio + (3 * separacion)));
      
      if(signal2WPR < -50){
         showText("WPR_H1", "WPR_H4: " +  NormalizeDouble(signal2WPR,2), clrLightGreen, (posInicio + (4 * separacion)));
      }
      if(signal2WPR > -50){
         showText("WPR_H1", "WPR_H4: " +  NormalizeDouble(signal2WPR,2), clrRed, (posInicio + (4 * separacion)));
      }
      //showText("RSI", "RSI_W1: " + NormalizeDouble(iRSI(Symbol(), PERIOD_W1, 9, PRICE_WEIGHTED, 0), 2), clrAqua, (posInicio + (5 * separacion)));
      showText("ProfitObj", "PROF_OBJ: " + NormalizeDouble( AccountBalance() * (MultiplicadorProfit * 0.01)  , 2), clrAqua, (posInicio + (6 * separacion)));
      double flotante = GetFlotante();
      if(flotante < 0){
         showText("Flotante", "FLOTANTE: " + NormalizeDouble(flotante, 2), clrRed, (posInicio + (7 * separacion)));
      }else{
         showText("Flotante", "FLOTANTE: " + NormalizeDouble(flotante, 2), clrGreen, (posInicio + (7 * separacion)));  
      }   
      //showText("SoloAlcista", "ALCISTA: " + OperarAlcista, clrRed, (posInicio + (8 * separacion)));
      //showText("SoloBajista", "BAJISTA: " + OperarBajista, clrRed, (posInicio + (9 * separacion)));   
      //showText("ACTIVE", "ACTIVE: " + NormalizeDouble(AccountFreeMargin() > AccountEquity() * 0.25, 2) , clrRed, (posInicio + (11 * separacion)));         
             
   }

}

//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason){
   EventKillTimer(); 
}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+


long timeW = -1;
int secretnumber2 = 9;
int period = PERIOD_H4;
extern double Lotes = 0.01;
int PipStep = 125;
//extern bool ActivarAumento = true;
//extern int OpsParaAumentoExtra = 3;
//extern double AumentoExtra = 0.02;
bool ActivarMultiplicador = false;
int Divisor = 1000;

extern bool ActivarGraficos = false;
bool ActivarCompras = true;
extern bool ActivarCierres = true;
int posInicio = 20;
int separacion = 20;

extern int SeparacionApertura = 350;
extern int SeparacionCierre = 250;

bool ActivarTrail = true;
extern bool ActivarCheckProfit = true;
extern double MultiplicadorProfit = 1.0;
bool DesactivarForce = true;

double pObjetivoB = 0.0;
double pObjetivoS = 0.0;

int fastMaPeriod = 5;//5
int shiftFastMaPeriod = 0;

int slowMaPeriod = 200;//200
int shiftSlowMaPeriod = 0;



int rsiPeriod = 9;
int rsiHighLimit = 80;
int rsiLowLimit = 20;

int PipsAumentoPorOp = 0;

long timeD = -1;
double amplitudMedia = 0.0;


int WprVenta = 20;
int WprVentaCierre = 35;
int WprCompra = 80;
int WprCompraCierre = 65;

extern bool OperarAlcista = true;
extern bool OperarBajista = true;

bool stop = false;
bool compraRsi = false;
bool ventaRsi = false;
int timeAuxC = -1;
int timeAuxV = -1;

int volatilidad = -1;
long time15 = -1;


long timeM15 = -1;
long periodos = 0;
long limitePeriodos = 30;

int timeDC = -1;
string ticker = "";

long timeH1 = -1;

void OnTick(){

   int tradesB = CountTrades(MagicNumberBuys, OP_BUY);
   int tradesS = CountTrades(MagicNumberSells, OP_SELL);
   
   if(ActivarCierres == true){
      RefreshRates();
      double calcUnoC = iMA(Symbol(), PERIOD_M15, fastMaPeriod, shiftFastMaPeriod, MODE_EMA, PRICE_TYPICAL, 1);
      double calcDosC = iMA(Symbol(), PERIOD_M15, slowMaPeriod, shiftSlowMaPeriod, MODE_SMA, PRICE_TYPICAL, 1);
      double wpr = iWPR(Symbol(), period, secretnumber2, 0);
      
      
      //if(wpr != EMPTY_VALUE && wpr != 0 && calcDosC != 0 && calcUnoC != 0){
        
         if(tradesB > 0  && (wpr >= -65 /*|| MagicSignal(1) > 0 || MathAbs((calcUnoC) - (calcDosC)) <= SeparacionCierre * Point */)  ){
            CloseThisSymbolAll(MagicNumberBuys);
         }
         if(tradesS > 0  && (wpr <= -35 /*|| MagicSignal(1) < 0 || MathAbs((calcUnoC) - (calcDosC)) <= SeparacionCierre * Point*/ ) ){
            CloseThisSymbolAll(MagicNumberSells);
         }
      //}  
   }

   if(ActivarCheckProfit == true){
      if(tradesB > 0){
         CheckObjetivoB(MagicNumberBuys, OP_BUY);
      }
      if(tradesS > 0){
         CheckObjetivoS(MagicNumberSells, OP_SELL);             
      }        
   }


   if(timeW != iTime(Symbol(), PERIOD_M1, 0)){
      
      RefreshRates();

      double signal2WPR = iWPR(Symbol(), period, secretnumber2, 0);
      double calcUno =  iMA(Symbol(), PERIOD_M15, fastMaPeriod, shiftFastMaPeriod, MODE_EMA, PRICE_TYPICAL, 0);
      double calcDos = iMA(Symbol(), PERIOD_M15, slowMaPeriod, shiftSlowMaPeriod, MODE_SMA, PRICE_TYPICAL, 0);

      //string str = httpGET("http://trades.darylsystem.com:81/daryl/arima/"+ticker+"/60/"+AccountInfoInteger(ACCOUNT_LOGIN));
      //Print(str);

      //Comment("-"+str+"-");
      bool comprar = true 
                     && signal2WPR < -WprCompra                     
                     && iRSI(Symbol(), PERIOD_W1, rsiPeriod, PRICE_WEIGHTED, 0) > rsiLowLimit
                     && MathAbs((calcUno) - (calcDos)) >= (SeparacionApertura) * Point   
                     && OperarAlcista == true
                     ;
                      
      bool vender =  true 
                     && signal2WPR > -WprVenta                    
                     && iRSI(Symbol(), PERIOD_W1, rsiPeriod, PRICE_WEIGHTED, 0) < rsiHighLimit
                     && MathAbs((calcUno) - (calcDos)) >= (SeparacionApertura) * Point                 
                     && OperarBajista == true
                     ;
      //Print("Comprar -> " + comprar + " vender -> " + vender);               
      if((comprar == true /*&& str == "BUY"*/ && tradesB > 0 && timeH1 != iTime(Symbol(), PERIOD_H1, 0)) 
         || (comprar == true && tradesB == 0)){
      
         bool operar = true;
         if((DayOfWeek() == 5 && Hour() > 21 ) && DayOfWeek() == 0) operar = false;
         if(true){
            double lastBuyPrice = FindLastBuyPrice(MagicNumberBuys);
           
            if (((lastBuyPrice - Ask >= (PipStep + ( PipsAumentoPorOp * tradesB)) * Point) || (tradesB == 0) )){        
               double lot = GenerateLots();
               lot = lot + (0.01 * tradesB);
               if(AccountFreeMargin() > AccountEquity() * 0.25 && operar == true){
                  
                  int res = OrderSend(Symbol(), OP_BUY, lot, Ask, 0, 0, 0, IntegerToString(MagicNumberBuys), MagicNumberBuys, 0, 0);
                  if(res < 0){
                     Print("Operacion no se ha podido dar de alta");
                  }else{      
                  }
   
               }    

            }
   
         } 
         timeH1 = iTime(Symbol(), PERIOD_H1, 0);

      }
     
      if((vender == true /*&& str == "SELL"*/ && tradesS > 0 && timeH1 != iTime(Symbol(), PERIOD_H1, 0)) || (vender == true && tradesS == 0)){
         
         bool operar = true;
         if(((DayOfWeek() == 5 && Hour() > 21) ) && DayOfWeek() == 0) operar = false;
         if(true){
            double lastSellPrice = FindLastSellPrice(MagicNumberSells);
           
            if ((( Bid - lastSellPrice >= (PipStep + ( PipsAumentoPorOp * tradesS)) * Point) || tradesS == 0)){
               double lot = GenerateLots();
               lot = lot + (0.01 * tradesS);
               if(AccountFreeMargin() > AccountEquity() * 0.25 && operar == true){
                 
                  int res = OrderSend(Symbol(), OP_SELL, lot, Bid, 0, 0, 0, IntegerToString(MagicNumberSells), MagicNumberSells, 0, 0);
                  if(res < 0){
                     Print("Operacion no se ha podido dar de alta");
                  }else{                    
                  } 

               }  

            }
         } 
         timeH1 = iTime(Symbol(), PERIOD_H1, 0); 
      }
      timeW = iTime(Symbol(), PERIOD_M1, 0);
     

         
   }
   

}


void drawVerticalLine(int barsBack, color clr) {
   
   string lineName = "Line"+ IntegerToString(Time[barsBack]);
   
   if (ObjectFind(lineName) != 0) {
      ObjectCreate(lineName,OBJ_VLINE,0,Time[barsBack],0);
      ObjectSet(lineName,OBJPROP_COLOR, clr);
      ObjectSet(lineName,OBJPROP_WIDTH,1);
      ObjectSet(lineName,OBJPROP_STYLE,STYLE_DOT);
   }
   
}

void showText(string name, string texto, color clr, int yDistance){
   
   
   ObjectCreate(name, OBJ_LABEL, 0, 0, 0);
   ObjectSetText(name,texto, 15, "Verdana", clr);
   ObjectSet(name, OBJPROP_CORNER, 1);
   ObjectSet(name, OBJPROP_XDISTANCE, 20);
   ObjectSet(name, OBJPROP_YDISTANCE, yDistance);
}
//+------------------------------------------------------------------+

int CountTrades(int MagicNumber, int tipo) {
   //RefreshRates()
   int numTrades = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == tipo ){
            numTrades++;
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (numTrades);
}

int CountTradesSymbol() {
   //RefreshRates()
   int numTrades = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != Symbol()) continue;
         if (OrderSymbol() == Symbol()){
            numTrades++;
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (numTrades);
}

double CalcProfit() {
   //RefreshRates()
   double profit = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != Symbol()) continue;
         if (OrderSymbol() == Symbol()){
            profit += OrderProfit();
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (profit);
}

void CloseThisSymbolAll(int MagicNumber) {
   RefreshRates();
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
            if (OrderType() == OP_BUY){
               if(OrderClose(OrderTicket(), OrderLots(), Bid, 0.0, Blue) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }
            if (OrderType() == OP_SELL){
               if(!OrderClose(OrderTicket(), OrderLots(), Ask, 0.0, Red) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }  
         }
         //Sleep(1000);
      }  
   }
}

double FindLastBuyPrice(int MagicNumber) {
   RefreshRates();
   double price = 0.0;
   int ticket = 0;
   int ticketAux = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {

      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == OP_BUY) {
            ticket = OrderTicket();
            if (ticket > ticketAux) {
               price = OrderOpenPrice();
               ticketAux = ticket;
            }
         }
      }else{
         Print("Error al recuperar la operación: ", GetLastError());
      }  
   }
   return (price);
}

double FindLastSellPrice(int MagicNumber) {
   RefreshRates();
   double price = 0.0;
   int ticket = 0;
   int ticketAux = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == OP_SELL) {
            ticket = OrderTicket();
            if (ticket > ticketAux) {
               price = OrderOpenPrice();
               ticketAux = ticket;
            }
         }
      }else{
         Print("Error al recuperar la operación: ", GetLastError());
      }  
   }
   return (price);
}

int Multiplicador(){
   
   return AccountEquity()/Divisor;
   //return 1;
}

double GenerateLots(){
   
   double lot = Lotes;
   int multiplicador = Multiplicador();
   
   if(ActivarMultiplicador == true){
       lot = Lotes * Multiplicador();
   }
   if(lot < 0.01) lot = 0.01;
   return lot;
}

double array[];
void CargarArray(int periodo){
   ArrayResize(array, periodo);
   //ArrayFree(array);
   
   double total = 0.0;
   for(int i = 0; i < periodo; i++){
   
      double valor = iCustom(Symbol(), PERIOD_M15, "fxsecretsignal", 192, 0, i+1);
      //Print(valor);
      array[i] = valor;
   
   }
}


double MediaSecretSignal(int shift){
   double media = iMAOnArray(array, 0, 25, 0, MODE_EMA, shift);
   return media;
}

double Ciclo(){

   double valor = iCustom(Symbol(), PERIOD_M15, "SuperTrend", 0, 1);
   if(valor != EMPTY_VALUE && valor > 0){
      return 1.0;
   }else{
      return -1.0;
   }

}

void ActualizarObjetivosS(int MagicNumber, int tipo){

   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == tipo) {
            if(pObjetivoS != OrderTakeProfit()){
               bool ordenModificada = OrderModify(OrderTicket(), OrderOpenPrice(), /*OrderStopLoss()*/0, pObjetivoS, 0, Yellow);
               if(!ordenModificada){
                  Print("No se ha podido modificar la orden: " , OrderTicket(), " ", GetLastError());
               }
               RefreshRates();
            }  
         }
      }else{
         Print("Error al recuperar la operación: ", GetLastError());
      }  
   }


}

void ActualizarObjetivosB(int MagicNumber, int tipo){

   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == tipo) {
            if(pObjetivoB != OrderTakeProfit()){
               bool ordenModificada = OrderModify(OrderTicket(), OrderOpenPrice(), /*OrderStopLoss()*/0, pObjetivoB, 0, Yellow);
               if(!ordenModificada){
                  Print("No se ha podido modificar la orden: " , OrderTicket(), " ", GetLastError());
               }
               RefreshRates();
            }  
         }
      }else{
         Print("Error al recuperar la operación: ", GetLastError());
      }  
   }

}

double PrecioObjetivoB(int MagicNumber, int tipo){

   double precio = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == tipo) {
            precio = OrderOpenPrice();
         }
      }  
   }  
   return precio;
}

double PrecioObjetivoS(int MagicNumber, int tipo){

   double precio = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == tipo) {
            precio = OrderOpenPrice();
         }
      }  
   }  
   return precio;
}

void CheckObjetivoB(int MagicNumber, int tipo){

   
   double flotante = GetFlotante();
   double objetivo = AccountBalance() * 0.01 * MultiplicadorProfit;
   
   if(flotante > objetivo){
      CloseThisSymbolAll(MagicNumberBuys);
      timeDC = iTime(Symbol(), PERIOD_D1, 0);
   }

   
   
}

void CheckObjetivoS(int MagicNumber, int tipo){
 
   
   double flotante = GetFlotante();
   double objetivo = AccountBalance() * 0.01 * MultiplicadorProfit;
   
   if(flotante > objetivo){
      CloseThisSymbolAll(MagicNumberSells);
      timeDC = iTime(Symbol(), PERIOD_D1, 0);
   }

   
}
   
double GetFlotante(){
   
   
   double flotante = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         //if (OrderSymbol() != Symbol() || (OrderMagicNumber() != MagicNumberBuys && OrderMagicNumber() != MagicNumberSells)) continue;
         if (OrderSymbol() == Symbol() && (OrderMagicNumber() == MagicNumberBuys || OrderMagicNumber() == MagicNumberSells)) {
            flotante += OrderProfit();
         }
      }  
   }
   return flotante;
   
}



double AmplitudMedia(int periodos){

   double amplitudMedia = 0.0;
   double amplitudes[];
   ArrayResize(amplitudes, periodos);
   for(int i = 1; i < periodos; i++){
      
      double amplitud = MathAbs(iHigh(Symbol(), PERIOD_D1, i) - iLow(Symbol(), PERIOD_D1, i));
      amplitudes[i-1] = amplitud;
      
   }
   //Calcular la media
   double media = iMAOnArray(amplitudes, periodos, periodos, 0, MODE_SMA, 0);
   return media;
}

double AmplitudActual(){

   return MathAbs(iHigh(Symbol(), PERIOD_D1, 0) - iLow(Symbol(), PERIOD_D1, 0));
   
}

double PuntoMedio(int posicion){

   return iLow(Symbol(), PERIOD_D1, posicion) + MathAbs((iHigh(Symbol(), PERIOD_D1, posicion) - iLow(Symbol(), PERIOD_D1, posicion)) /2);
   
}

long VolumenTotal(int periodos){

   long volumen = 0 ;
   for(int i = 0; i < periodos; i++){
      volumen = volumen + iVolume(Symbol(), PERIOD_M1, i+1);
   
   }
   return volumen;

}

double DiferenciaVolumen(int periodoCorto, int periodoLargo){

   double volumenCorto = VolumenTotal(periodoCorto);
   double volumenLargo = VolumenTotal(periodoLargo);
   
   //Print(volumenCorto + "--" + volumenLargo);
   if(volumenLargo > 0){
      return volumenCorto/volumenLargo;
   }
   return 0;

}
bool CheckDiferenciaVolumnen(double limite, int periodoCorto, int periodoLargo){

   double difVolumen = DiferenciaVolumen(periodoCorto, periodoLargo);
   
   
   if(difVolumen >= limite) return true;
   else return false;

}

int difDias = 0;
bool CheckInterestRatesRBA(){

   bool operar = true;
   
   if(Year() == 2012){
      if(Month() == 12 && Day() >= 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 11 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 10 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 9 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 8 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 7 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 6 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 5 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 4 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 3 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 2 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;      
   }
   if(Year() == 2013){
      if(Month() == 12 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 11 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 10 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 9 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 8 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 7 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 6 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 5 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 4 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 3 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 2 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;     
   }
   if(Year() == 2014){
      if(Month() == 12 && Day() == 2 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 11 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 10 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 9 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 8 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 7 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 6 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 5 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 4 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 3 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 2 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;     
   }
   if(Year() == 2015){
      if(Month() == 12 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 11 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 10 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 9 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 8 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 7 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 6 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 5 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 4 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 3 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 2 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;   
   }
   if(Year() == 2016){
      if(Month() == 12 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 11 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 10 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 9 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 8 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 7 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 6 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 5 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 4 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 3 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 2 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;    
   }
   if(Year() == 2017){
      if(Month() == 12 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 11 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 10 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 9 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 8 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 7 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 6 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 5 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 4 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 3 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 2 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;      
   }   
   if(Year() == 2018){
      if(Month() == 12 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 11 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 10 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 9 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 8 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 7 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 6 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 5 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 4 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 3 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 2 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;   
   }
   if(Year() == 2019){
      if(Month() == 12 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 11 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 10 && Day() == 1 - difDias && Day() <= 1 + difDias) operar = false;
      if(Month() == 9 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 8 && Day() == 6 - difDias && Day() <= 6 + difDias) operar = false;
      if(Month() == 7 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 6 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      if(Month() == 5 && Day() == 7 - difDias && Day() <= 7 + difDias) operar = false;
      if(Month() == 4 && Day() == 2 - difDias && Day() <= 2 + difDias) operar = false;
      if(Month() == 3 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;
      if(Month() == 2 && Day() == 5 - difDias && Day() <= 5 + difDias) operar = false;     
   }   
   if(Year() == 2020){
      if(Month() == 3 && Day() == 3 - difDias && Day() <= 3 + difDias) operar = false;
      if(Month() == 2 && Day() == 4 - difDias && Day() <= 4 + difDias) operar = false;
      
   }
   if(DayOfWeek() == 5){
      //operar = false;
   }
      
   return operar;

}






extern int MagicNumberSells = 2222;
extern int MagicNumberBuys = 1111;




