//+------------------------------------------------------------------+
//|                                                   TestZeroMQ.mq4 |
//|                        Copyright 2020, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"
#property strict

#include <Zmq/Zmq.mqh>

string symbols[] = { "XAUUSD", "GDAXI", "AUDCAD","NDX","EURUSD","XTIUSD"};
long timesH1[] = {-1, -1, -1, -1, -1, -1};
long timesH4[] = {-1, -1, -1, -1, -1, -1};
long timesD1[] = {-1, -1, -1, -1, -1, -1};
long timesW1[] = {-1, -1, -1, -1, -1, -1};
const string FILE_BASE = "Daryl//";

//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+

int OnInit(){
   EventSetTimer(10);
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason){
   EventKillTimer();
}

long time = -1;
void OnTimer(){
    //Comment("Guardando datos...");
    for(int i = 0; i < ArraySize(symbols); i++){
        handleH1(symbols[i], i, PERIOD_H1);
    }
    for(int i = 0; i < ArraySize(symbols); i++){
        handleH4(symbols[i], i, PERIOD_H4);
    }
    for(int i = 0; i < ArraySize(symbols); i++){
        handleD1(symbols[i], i, PERIOD_D1);
    }
    for(int i = 0; i < ArraySize(symbols); i++){
        handleW1(symbols[i], i, PERIOD_W1);
    }
    

}



//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void handleH1(string symbol, int posicion, int period){
	

	if(timesH1[posicion] != iTime(symbol, period, 0)){

		Context context;
		Socket requester(context,ZMQ_REQ);
		requester.connect("tcp://localhost:5559");

		double apertura = iOpen(symbol, period, 1);
		double maximo = iHigh(symbol, period, 1);
		double minimo = iLow(symbol, period, 1);
		double cierre = iClose(symbol, period, 1);
        double volumen = iVolume(symbol, period, 1);
         
        string time_yyyy_MM_dd = TimeToString( TimeCurrent(), TIME_DATE);
        string linea = symbol + "," + period + "," + time_yyyy_MM_dd + "," +
                        TimeToString( TimeCurrent(), TIME_SECONDS) + "," +
                        apertura + "," + 
                        maximo + "," + 
                        minimo + "," + 
                        cierre + "," + 
                        volumen;
         
		Print("Sending data -> ", linea);
		ZmqMsg message(linea);
		requester.send(message);
      
		//Sleep(5000);
      
		ZmqMsg reply;
		requester.recv(reply,true);

		Print("Received reply -> ",reply.getData());
		timesH1[posicion] = iTime(symbol, period, 0);

	} 

}

void handleH4(string symbol, int posicion, int period){

	if(timesH4[posicion] != iTime(symbol, period, 0)){


		Context context;
		Socket requester(context,ZMQ_REQ);
		requester.connect("tcp://localhost:5559");

		double apertura = iOpen(symbol, period, 1);
		double maximo = iHigh(symbol, period, 1);
		double minimo = iLow(symbol, period, 1);
		double cierre = iClose(symbol, period, 1);
        double volumen = iVolume(symbol, period, 1);
         
        string time_yyyy_MM_dd = TimeToString( TimeCurrent(), TIME_DATE);
        string linea = symbol + "," + period + "," + time_yyyy_MM_dd + "," +
                        TimeToString( TimeCurrent(), TIME_SECONDS) + "," +
                        apertura + "," + 
                        maximo + "," + 
                        minimo + "," + 
                        cierre + "," + 
                        volumen;
         
		Print("Sending data -> ", linea);
		ZmqMsg message(linea);
		requester.send(message);
      
		//Sleep(5000);
      
		ZmqMsg reply;
		requester.recv(reply,true);

		Print("Received reply -> ",reply.getData());

		timesH4[posicion] = iTime(symbol, period, 0);

   } 

}

void handleD1(string symbol, int posicion, int period){

   if(timesD1[posicion] != iTime(symbol, period, 0)){
		Context context;
		Socket requester(context,ZMQ_REQ);
		requester.connect("tcp://localhost:5559");

		double apertura = iOpen(symbol, period, 1);
		double maximo = iHigh(symbol, period, 1);
		double minimo = iLow(symbol, period, 1);
		double cierre = iClose(symbol, period, 1);
        double volumen = iVolume(symbol, period, 1);
         
        string time_yyyy_MM_dd = TimeToString( TimeCurrent(), TIME_DATE);
        string linea = symbol + "," + period + "," + time_yyyy_MM_dd + "," +
                        TimeToString( TimeCurrent(), TIME_SECONDS) + "," +
                        apertura + "," + 
                        maximo + "," + 
                        minimo + "," + 
                        cierre + "," + 
                        volumen;
         
		Print("Sending data -> ", linea);
		ZmqMsg message(linea);
		requester.send(message);
      
		//Sleep(5000);
      
		ZmqMsg reply;
		requester.recv(reply,true);

		Print("Received reply -> ",reply.getData());
		timesD1[posicion] = iTime(symbol, period, 0);

   } 
}
void handleW1(string symbol, int posicion, int period){

   if(timesW1[posicion] != iTime(symbol, period, 0)){
      		
		Context context;
		Socket requester(context,ZMQ_REQ);
		requester.connect("tcp://localhost:5559");

		double apertura = iOpen(symbol, period, 1);
		double maximo = iHigh(symbol, period, 1);
		double minimo = iLow(symbol, period, 1);
		double cierre = iClose(symbol, period, 1);
        double volumen = iVolume(symbol, period, 1);
         
        string time_yyyy_MM_dd = TimeToString( TimeCurrent(), TIME_DATE);
        string linea = symbol + "," + period + "," + time_yyyy_MM_dd + "," +
                        TimeToString( TimeCurrent(), TIME_SECONDS) + "," +
                        apertura + "," + 
                        maximo + "," + 
                        minimo + "," + 
                        cierre + "," + 
                        volumen;
         
		Print("Sending data -> ", linea);
		ZmqMsg message(linea);
		requester.send(message);
      
		//Sleep(5000);
      
		ZmqMsg reply;
		requester.recv(reply,true);

		Print("Received reply -> ",reply.getData());
		timesW1[posicion] = iTime(symbol, period, 0);

   } 

}
                 
void OnTick(){   
}
//+------------------------------------------------------------------+
