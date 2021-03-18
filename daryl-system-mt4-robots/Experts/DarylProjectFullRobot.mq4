//+------------------------------------------------------------------+
//|                                        DarylProjectFullRobot.mq4 |
//|                             Copyright 2020, Daryl System Project |
//|                                      https://www.darylsystem.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, Daryl System Project."
#property link      "http://www.darylsystem.com"
#property version   "1.40"
#property strict

#include <mql4-http.mqh>
#include <DarylProjectCommon.mqh>
#include <jason.mqh>
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+




int OnInit(){
   EventSetTimer(5);
   return(INIT_SUCCEEDED);
}

//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason){
   //---
   EventKillTimer();
}

//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+

void OnTick(){
   //---
}

//+------------------------------------------------------------------+
int Modo = 1;
double Lotes = 0.01;
double MaxLots = 0.10;
bool Parar = false;
int HoraInicio = 0;
int HoraFin = 24;

const string url = URL_BASE + "/mt/ordenes/all/";
void OnTimer(){
   bool onTime = Hour() >= HoraInicio && Hour() <= HoraFin;
   if(onTime == true){
      string str = httpGET(url+AccountInfoInteger(ACCOUNT_LOGIN));
      // Object
      CJAVal json;
      
      // Load in and deserialize the data
      json.Deserialize(str);
      //Print(str);
      
      string listaRobotsActivos = "Robots Activos: \n";
      for(int i = 0; i < json.Size(); i++){
         string Comment_Buy = json[i]["robot"].ToStr() + "_" + OP_BUY;
         string Comment_Sell = json[i]["robot"].ToStr() + "_" + OP_SELL;
         string tipoOrden = json[i]["tipoOrden"].ToStr();
         string symbol = json[i]["tipoActivo"].ToStr();
         if(json[i]["robot"].ToStr() != "" && json[i]["tipoOrden"].ToStr() != ""){
            bool robotActivo = CheckRobotActivo(json[i]["robot"].ToStr());
            
            if(ACTIVAR_TODOS == true) robotActivo = true;
            if(DESACTIVAR_TODOS == true) robotActivo = false;
            if(json[i]["robot"].ToStr() == "ARIMA_B_XAUUSD_60") robotActivo = false;
            
            evaluate(Lotes, onTime, Parar, tipoOrden , Comment_Buy, Comment_Sell, symbol, robotActivo );
            
            if(robotActivo){
               listaRobotsActivos += (json[i]["robot"].ToStr() + " -- " + tipoOrden + " \n");
            }
         }
         //Print(Comment_Buy, " ---- ", Comment_Sell, " ---- " , json[i]["tipoOrden"].ToStr());
      }
      
      Comment(listaRobotsActivos);

   }
}

bool CheckRobotActivo(string robot){

   bool seguir = false;
    
   if(robot == "RNA_XAUUSD_10080" && RNA_XAUUSD_10080 == true) seguir = true;
   if(robot == "RNA_XAUUSD_1440" && RNA_XAUUSD_1440 == true) seguir = true;
   if(robot == "RNA_XAUUSD_240" && RNA_XAUUSD_240 == true) seguir = true;
   if(robot == "RNA_XAUUSD_60" && RNA_XAUUSD_60 == true) seguir = true;
   if(robot == "RNA_NDX_10080" && RNA_NDX_10080 == true) seguir = true;
   if(robot == "RNA_NDX_1440" && RNA_NDX_1440 == true) seguir = true;
   if(robot == "RNA_NDX_240" && RNA_NDX_240 == true) seguir = true;
   if(robot == "RNA_NDX_60" && RNA_NDX_60 == true) seguir = true;
   if(robot == "RNA_GDAXI_10080" && RNA_GDAXI_10080 == true) seguir = true;
   if(robot == "RNA_GDAXI_1440" && RNA_GDAXI_1440 == true) seguir = true;
   if(robot == "RNA_GDAXI_240" && RNA_GDAXI_240 == true) seguir = true;
   if(robot == "RNA_GDAXI_60" && RNA_GDAXI_60 == true) seguir = true;
   if(robot == "RNA_AUDCAD_10080" && RNA_AUDCAD_10080 == true) seguir = true;
   if(robot == "RNA_AUDCAD_1440" && RNA_AUDCAD_1440 == true) seguir = true;
   if(robot == "RNA_AUDCAD_240" && RNA_AUDCAD_240 == true) seguir = true;
   if(robot == "RNA_AUDCAD_60" && RNA_AUDCAD_60 == true) seguir = true;
   
   if(robot == "ARIMA_C_EURUSD_10080" && ARIMA_C_EURUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_C_EURUSD_1440" && ARIMA_C_EURUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_C_EURUSD_240" && ARIMA_C_EURUSD_240 == true) seguir = true;
   if(robot == "ARIMA_C_EURUSD_60" && ARIMA_C_EURUSD_60 == true) seguir = true;
   if(robot == "ARIMA_B_EURUSD_10080" && ARIMA_B_EURUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_B_EURUSD_1440" && ARIMA_B_EURUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_B_EURUSD_240" && ARIMA_B_EURUSD_240 == true) seguir = true;
   if(robot == "ARIMA_B_EURUSD_60" && ARIMA_B_EURUSD_60 == true) seguir = true;
   if(robot == "ARIMA_EURUSD_10080" && ARIMA_EURUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_EURUSD_1440" && ARIMA_EURUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_EURUSD_240" && ARIMA_EURUSD_240 == true) seguir = true;
   if(robot == "ARIMA_EURUSD_60" && ARIMA_EURUSD_60 == true) seguir = true;


   if(robot == "ARIMA_C_GDAXI_10080" && ARIMA_C_GDAXI_10080 == true) seguir = true;
   if(robot == "ARIMA_C_GDAXI_1440" && ARIMA_C_GDAXI_1440 == true) seguir = true;
   if(robot == "ARIMA_C_GDAXI_240" && ARIMA_C_GDAXI_240 == true) seguir = true;
   if(robot == "ARIMA_C_GDAXI_60" && ARIMA_C_GDAXI_60 == true) seguir = true;
   if(robot == "ARIMA_B_GDAXI_10080" && ARIMA_B_GDAXI_10080 == true) seguir = true;
   if(robot == "ARIMA_B_GDAXI_1440" && ARIMA_B_GDAXI_1440 == true) seguir = true;
   if(robot == "ARIMA_B_GDAXI_240" && ARIMA_B_GDAXI_240 == true) seguir = true;
   if(robot == "ARIMA_B_GDAXI_60" && ARIMA_B_GDAXI_60 == true) seguir = true;   
   if(robot == "ARIMA_GDAXI_60" && ARIMA_GDAXI_60 == true) seguir = true;
   if(robot == "ARIMA_GDAXI_240" && ARIMA_GDAXI_240 == true) seguir = true;
   if(robot == "ARIMA_GDAXI_1440" && ARIMA_GDAXI_1440 == true) seguir = true;
   if(robot == "ARIMA_GDAXI_10080" && ARIMA_GDAXI_10080 == true) seguir = true;
   

   if(robot == "ARIMA_C_AUDCAD_60" && ARIMA_C_AUDCAD_60 == true) seguir = true;
   if(robot == "ARIMA_C_AUDCAD_240" && ARIMA_C_AUDCAD_240 == true) seguir = true;
   if(robot == "ARIMA_C_AUDCAD_1440" && ARIMA_C_AUDCAD_1440 == true) seguir = true;
   if(robot == "ARIMA_C_AUDCAD_10080" && ARIMA_C_AUDCAD_10080 == true) seguir = true;
   if(robot == "ARIMA_B_AUDCAD_60" && ARIMA_B_AUDCAD_60 == true) seguir = true;
   if(robot == "ARIMA_B_AUDCAD_240" && ARIMA_B_AUDCAD_240 == true) seguir = true;
   if(robot == "ARIMA_B_AUDCAD_1440" && ARIMA_B_AUDCAD_1440 == true) seguir = true;
   if(robot == "ARIMA_B_AUDCAD_10080" && ARIMA_B_AUDCAD_10080 == true) seguir = true;   
   if(robot == "ARIMA_AUDCAD_60" && ARIMA_AUDCAD_60 == true) seguir = true;
   if(robot == "ARIMA_AUDCAD_240" && ARIMA_AUDCAD_240 == true) seguir = true;
   if(robot == "ARIMA_AUDCAD_1440" && ARIMA_AUDCAD_1440 == true) seguir = true;
   if(robot == "ARIMA_AUDCAD_10080" && ARIMA_AUDCAD_10080 == true) seguir = true;

   if(robot == "ARIMA_C_WTI_60" && ARIMA_C_WTI_60 == true) seguir = true;
   if(robot == "ARIMA_C_WTI_240" && ARIMA_C_WTI_240 == true) seguir = true;
   if(robot == "ARIMA_C_WTI_1440" && ARIMA_C_WTI_1440 == true) seguir = true;

   if(robot == "ARIMA_C_XAUUSD_60" && ARIMA_C_XAUUSD_60 == true) seguir = true;
   if(robot == "ARIMA_C_XAUUSD_240" && ARIMA_C_XAUUSD_240 == true) seguir = true;
   if(robot == "ARIMA_C_XAUUSD_1440" && ARIMA_C_XAUUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_C_XAUUSD_10080" && ARIMA_C_XAUUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_B_XAUUSD_60" && ARIMA_B_XAUUSD_60 == true) seguir = true;
   if(robot == "ARIMA_B_XAUUSD_240" && ARIMA_B_XAUUSD_240 == true) seguir = true;
   if(robot == "ARIMA_B_XAUUSD_1440" && ARIMA_B_XAUUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_B_XAUUSD_10080" && ARIMA_B_XAUUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_XAUUSD_60" && ARIMA_XAUUSD_60 == true) seguir = true;
   if(robot == "ARIMA_XAUUSD_240" && ARIMA_XAUUSD_240 == true) seguir = true;
   if(robot == "ARIMA_XAUUSD_1440" && ARIMA_XAUUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_XAUUSD_10080" && ARIMA_XAUUSD_10080 == true) seguir = true;
   

   if(robot == "ARIMA_C_NDX_60" && ARIMA_C_NDX_60 == true) seguir = true;
   if(robot == "ARIMA_C_NDX_240" && ARIMA_C_NDX_240 == true) seguir = true;
   if(robot == "ARIMA_C_NDX_1440" && ARIMA_C_NDX_1440 == true) seguir = true;
   if(robot == "ARIMA_C_NDX_10080" && ARIMA_C_NDX_10080 == true) seguir = true;
   if(robot == "ARIMA_B_NDX_60" && ARIMA_B_NDX_60 == true) seguir = true;
   if(robot == "ARIMA_B_NDX_240" && ARIMA_B_NDX_240 == true) seguir = true;
   if(robot == "ARIMA_B_NDX_1440" && ARIMA_B_NDX_1440 == true) seguir = true;
   if(robot == "ARIMA_B_NDX_10080" && ARIMA_B_NDX_10080 == true) seguir = true;
   if(robot == "ARIMA_NDX_60" && ARIMA_NDX_60 == true) seguir = true;
   if(robot == "ARIMA_NDX_240" && ARIMA_NDX_240 == true) seguir = true;
   if(robot == "ARIMA_NDX_1440" && ARIMA_NDX_1440 == true) seguir = true;
   if(robot == "ARIMA_NDX_10080" && ARIMA_NDX_10080 == true) seguir = true;

   //========================================================================================
   
   if(robot == "ARIMA_I_C_EURUSD_10080" && ARIMA_I_C_EURUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_I_C_EURUSD_1440" && ARIMA_I_C_EURUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_C_EURUSD_240" && ARIMA_I_C_EURUSD_240 == true) seguir = true;
   if(robot == "ARIMA_I_C_EURUSD_60" && ARIMA_I_C_EURUSD_60 == true) seguir = true;
   if(robot == "ARIMA_I_B_EURUSD_10080" && ARIMA_I_B_EURUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_I_B_EURUSD_1440" && ARIMA_I_B_EURUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_B_EURUSD_240" && ARIMA_I_B_EURUSD_240 == true) seguir = true;
   if(robot == "ARIMA_I_B_EURUSD_60" && ARIMA_I_B_EURUSD_60 == true) seguir = true;
   if(robot == "ARIMA_I_EURUSD_10080" && ARIMA_I_EURUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_I_EURUSD_1440" && ARIMA_I_EURUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_EURUSD_240" && ARIMA_I_EURUSD_240 == true) seguir = true;
   if(robot == "ARIMA_I_EURUSD_60" && ARIMA_I_EURUSD_60 == true) seguir = true;


   if(robot == "ARIMA_I_C_GDAXI_10080" && ARIMA_I_C_GDAXI_10080 == true) seguir = true;
   if(robot == "ARIMA_I_C_GDAXI_1440" && ARIMA_I_C_GDAXI_1440 == true) seguir = true;
   if(robot == "ARIMA_I_C_GDAXI_240" && ARIMA_I_C_GDAXI_240 == true) seguir = true;
   if(robot == "ARIMA_I_C_GDAXI_60" && ARIMA_I_C_GDAXI_60 == true) seguir = true;
   if(robot == "ARIMA_I_B_GDAXI_10080" && ARIMA_I_B_GDAXI_10080 == true) seguir = true;
   if(robot == "ARIMA_I_B_GDAXI_1440" && ARIMA_I_B_GDAXI_1440 == true) seguir = true;
   if(robot == "ARIMA_I_B_GDAXI_240" && ARIMA_I_B_GDAXI_240 == true) seguir = true;
   if(robot == "ARIMA_I_B_GDAXI_60" && ARIMA_I_B_GDAXI_60 == true) seguir = true;   
   if(robot == "ARIMA_I_GDAXI_60" && ARIMA_I_GDAXI_60 == true) seguir = true;
   if(robot == "ARIMA_I_GDAXI_240" && ARIMA_I_GDAXI_240 == true) seguir = true;
   if(robot == "ARIMA_I_GDAXI_1440" && ARIMA_I_GDAXI_1440 == true) seguir = true;
   if(robot == "ARIMA_I_GDAXI_10080" && ARIMA_I_GDAXI_10080 == true) seguir = true;
   

   if(robot == "ARIMA_I_C_AUDCAD_60" && ARIMA_I_C_AUDCAD_60 == true) seguir = true;
   if(robot == "ARIMA_I_C_AUDCAD_240" && ARIMA_I_C_AUDCAD_240 == true) seguir = true;
   if(robot == "ARIMA_I_C_AUDCAD_1440" && ARIMA_I_C_AUDCAD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_C_AUDCAD_10080" && ARIMA_I_C_AUDCAD_10080 == true) seguir = true;
   if(robot == "ARIMA_I_B_AUDCAD_60" && ARIMA_I_B_AUDCAD_60 == true) seguir = true;
   if(robot == "ARIMA_I_B_AUDCAD_240" && ARIMA_I_B_AUDCAD_240 == true) seguir = true;
   if(robot == "ARIMA_I_B_AUDCAD_1440" && ARIMA_I_B_AUDCAD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_B_AUDCAD_10080" && ARIMA_I_B_AUDCAD_10080 == true) seguir = true;   
   if(robot == "ARIMA_I_AUDCAD_60" && ARIMA_I_AUDCAD_60 == true) seguir = true;
   if(robot == "ARIMA_I_AUDCAD_240" && ARIMA_I_AUDCAD_240 == true) seguir = true;
   if(robot == "ARIMA_I_AUDCAD_1440" && ARIMA_I_AUDCAD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_AUDCAD_10080" && ARIMA_I_AUDCAD_10080 == true) seguir = true;

   if(robot == "ARIMA_I_C_WTI_60" && ARIMA_I_C_WTI_60 == true) seguir = true;
   if(robot == "ARIMA_I_C_WTI_240" && ARIMA_I_C_WTI_240 == true) seguir = true;
   if(robot == "ARIMA_I_C_WTI_1440" && ARIMA_I_C_WTI_1440 == true) seguir = true;

   if(robot == "ARIMA_I_C_XAUUSD_60" && ARIMA_I_C_XAUUSD_60 == true) seguir = true;
   if(robot == "ARIMA_I_C_XAUUSD_240" && ARIMA_I_C_XAUUSD_240 == true) seguir = true;
   if(robot == "ARIMA_I_C_XAUUSD_1440" && ARIMA_I_C_XAUUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_C_XAUUSD_10080" && ARIMA_I_C_XAUUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_I_B_XAUUSD_60" && ARIMA_I_B_XAUUSD_60 == true) seguir = true;
   if(robot == "ARIMA_I_B_XAUUSD_240" && ARIMA_I_B_XAUUSD_240 == true) seguir = true;
   if(robot == "ARIMA_I_B_XAUUSD_1440" && ARIMA_I_B_XAUUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_B_XAUUSD_10080" && ARIMA_I_B_XAUUSD_10080 == true) seguir = true;
   if(robot == "ARIMA_I_XAUUSD_60" && ARIMA_I_XAUUSD_60 == true) seguir = true;
   if(robot == "ARIMA_I_XAUUSD_240" && ARIMA_I_XAUUSD_240 == true) seguir = true;
   if(robot == "ARIMA_I_XAUUSD_1440" && ARIMA_I_XAUUSD_1440 == true) seguir = true;
   if(robot == "ARIMA_I_XAUUSD_10080" && ARIMA_I_XAUUSD_10080 == true) seguir = true;
   

   if(robot == "ARIMA_I_C_NDX_60" && ARIMA_I_C_NDX_60 == true) seguir = true;
   if(robot == "ARIMA_I_C_NDX_240" && ARIMA_I_C_NDX_240 == true) seguir = true;
   if(robot == "ARIMA_I_C_NDX_1440" && ARIMA_I_C_NDX_1440 == true) seguir = true;
   if(robot == "ARIMA_I_C_NDX_10080" && ARIMA_I_C_NDX_10080 == true) seguir = true;
   if(robot == "ARIMA_I_B_NDX_60" && ARIMA_I_B_NDX_60 == true) seguir = true;
   if(robot == "ARIMA_I_B_NDX_240" && ARIMA_I_B_NDX_240 == true) seguir = true;
   if(robot == "ARIMA_I_B_NDX_1440" && ARIMA_I_B_NDX_1440 == true) seguir = true;
   if(robot == "ARIMA_I_B_NDX_10080" && ARIMA_I_B_NDX_10080 == true) seguir = true;
   if(robot == "ARIMA_I_NDX_60" && ARIMA_I_NDX_60 == true) seguir = true;
   if(robot == "ARIMA_I_NDX_240" && ARIMA_I_NDX_240 == true) seguir = true;
   if(robot == "ARIMA_I_NDX_1440" && ARIMA_I_NDX_1440 == true) seguir = true;
   if(robot == "ARIMA_I_NDX_10080" && ARIMA_I_NDX_10080 == true) seguir = true;



   return seguir;

}

//Estrategias
extern bool ACTIVAR_TODOS = false;
extern bool DESACTIVAR_TODOS = false;

extern bool ARIMA_AUDCAD_60 = false; 
extern bool ARIMA_AUDCAD_240 = false; 
extern bool ARIMA_AUDCAD_1440 = false; 
extern bool ARIMA_AUDCAD_10080 = false;
extern bool ARIMA_B_AUDCAD_60 = false; 
extern bool ARIMA_B_AUDCAD_240 = false; 
extern bool ARIMA_B_AUDCAD_1440 = false; 
extern bool ARIMA_B_AUDCAD_10080 = false;
extern bool ARIMA_C_AUDCAD_60 = false; 
extern bool ARIMA_C_AUDCAD_240 = false; 
extern bool ARIMA_C_AUDCAD_1440 = false; 
extern bool ARIMA_C_AUDCAD_10080 = false;
extern bool ARIMA_XAUUSD_60 = false;
extern bool ARIMA_XAUUSD_240 = false;
extern bool ARIMA_XAUUSD_1440 = false;
extern bool ARIMA_XAUUSD_10080 = false;
extern bool ARIMA_B_XAUUSD_60 = false;
extern bool ARIMA_B_XAUUSD_240 = false;
extern bool ARIMA_B_XAUUSD_1440 = false;
extern bool ARIMA_B_XAUUSD_10080 = false;
extern bool ARIMA_C_XAUUSD_60 = false;
extern bool ARIMA_C_XAUUSD_240 = false;
extern bool ARIMA_C_XAUUSD_1440 = false;
extern bool ARIMA_C_XAUUSD_10080 = false;

extern bool ARIMA_NDX_60 = false;
extern bool ARIMA_NDX_240 = false;
extern bool ARIMA_NDX_1440 = false;
extern bool ARIMA_NDX_10080 = false;
extern bool ARIMA_B_NDX_60 = false;
extern bool ARIMA_B_NDX_240 = false;
extern bool ARIMA_B_NDX_1440 = false;
extern bool ARIMA_B_NDX_10080 = false;
extern bool ARIMA_C_NDX_60 = false;
extern bool ARIMA_C_NDX_240 = false;
extern bool ARIMA_C_NDX_1440 = false;
extern bool ARIMA_C_NDX_10080 = false;

extern bool ARIMA_GDAXI_60 = false;
extern bool ARIMA_GDAXI_240 = false;
extern bool ARIMA_GDAXI_1440 = false;
extern bool ARIMA_GDAXI_10080 = false;
extern bool ARIMA_B_GDAXI_60 = false;
extern bool ARIMA_B_GDAXI_240 = false;
extern bool ARIMA_B_GDAXI_1440 = false;
extern bool ARIMA_B_GDAXI_10080 = false;
extern bool ARIMA_C_GDAXI_60 = false;
extern bool ARIMA_C_GDAXI_240 = false;
extern bool ARIMA_C_GDAXI_1440 = false;
extern bool ARIMA_C_GDAXI_10080 = false;


extern bool ARIMA_EURUSD_60 = false;
extern bool ARIMA_EURUSD_240 = false;
extern bool ARIMA_EURUSD_1440 = false;
extern bool ARIMA_EURUSD_10080 = false;
extern bool ARIMA_B_EURUSD_60 = false;
extern bool ARIMA_B_EURUSD_240 = false;
extern bool ARIMA_B_EURUSD_1440 = false;
extern bool ARIMA_B_EURUSD_10080 = false;
extern bool ARIMA_C_EURUSD_60 = false;
extern bool ARIMA_C_EURUSD_240 = false;
extern bool ARIMA_C_EURUSD_1440 = false;
extern bool ARIMA_C_EURUSD_10080 = false;

extern bool ARIMA_C_WTI_60 = false;
extern bool ARIMA_C_WTI_240 = false;
extern bool ARIMA_C_WTI_1440 = false;

extern bool RNA_AUDCAD_60 = false;
extern bool RNA_AUDCAD_240 = false;
extern bool RNA_AUDCAD_1440 = false;
extern bool RNA_AUDCAD_10080 = false;
extern bool RNA_GDAXI_60 = false;
extern bool RNA_GDAXI_240 = false;
extern bool RNA_GDAXI_1440 = false;
extern bool RNA_GDAXI_10080 = false;
extern bool RNA_NDX_60 = false;
extern bool RNA_NDX_240 = false;
extern bool RNA_NDX_1440 = false;
extern bool RNA_NDX_10080 = false;
extern bool RNA_XAUUSD_60 = false;
extern bool RNA_XAUUSD_240 = false;
extern bool RNA_XAUUSD_1440 = false;
extern bool RNA_XAUUSD_10080 = false;

extern bool ARIMA_I_AUDCAD_60 = false; 
extern bool ARIMA_I_AUDCAD_240 = false; 
extern bool ARIMA_I_AUDCAD_1440 = false; 
extern bool ARIMA_I_AUDCAD_10080 = false;
extern bool ARIMA_I_B_AUDCAD_60 = false; 
extern bool ARIMA_I_B_AUDCAD_240 = false; 
extern bool ARIMA_I_B_AUDCAD_1440 = false; 
extern bool ARIMA_I_B_AUDCAD_10080 = false;
extern bool ARIMA_I_C_AUDCAD_60 = false; 
extern bool ARIMA_I_C_AUDCAD_240 = false; 
extern bool ARIMA_I_C_AUDCAD_1440 = false; 
extern bool ARIMA_I_C_AUDCAD_10080 = false;
extern bool ARIMA_I_XAUUSD_60 = false;
extern bool ARIMA_I_XAUUSD_240 = false;
extern bool ARIMA_I_XAUUSD_1440 = false;
extern bool ARIMA_I_XAUUSD_10080 = false;
extern bool ARIMA_I_B_XAUUSD_60 = false;
extern bool ARIMA_I_B_XAUUSD_240 = false;
extern bool ARIMA_I_B_XAUUSD_1440 = false;
extern bool ARIMA_I_B_XAUUSD_10080 = false;
extern bool ARIMA_I_C_XAUUSD_60 = false;
extern bool ARIMA_I_C_XAUUSD_240 = false;
extern bool ARIMA_I_C_XAUUSD_1440 = false;
extern bool ARIMA_I_C_XAUUSD_10080 = false;

extern bool ARIMA_I_NDX_60 = false;
extern bool ARIMA_I_NDX_240 = false;
extern bool ARIMA_I_NDX_1440 = false;
extern bool ARIMA_I_NDX_10080 = false;
extern bool ARIMA_I_B_NDX_60 = false;
extern bool ARIMA_I_B_NDX_240 = false;
extern bool ARIMA_I_B_NDX_1440 = false;
extern bool ARIMA_I_B_NDX_10080 = false;
extern bool ARIMA_I_C_NDX_60 = false;
extern bool ARIMA_I_C_NDX_240 = false;
extern bool ARIMA_I_C_NDX_1440 = false;
extern bool ARIMA_I_C_NDX_10080 = false;

extern bool ARIMA_I_GDAXI_60 = false;
extern bool ARIMA_I_GDAXI_240 = false;
extern bool ARIMA_I_GDAXI_1440 = false;
extern bool ARIMA_I_GDAXI_10080 = false;
extern bool ARIMA_I_B_GDAXI_60 = false;
extern bool ARIMA_I_B_GDAXI_240 = false;
extern bool ARIMA_I_B_GDAXI_1440 = false;
extern bool ARIMA_I_B_GDAXI_10080 = false;
extern bool ARIMA_I_C_GDAXI_60 = false;
extern bool ARIMA_I_C_GDAXI_240 = false;
extern bool ARIMA_I_C_GDAXI_1440 = false;
extern bool ARIMA_I_C_GDAXI_10080 = false;


extern bool ARIMA_I_EURUSD_60 = false;
extern bool ARIMA_I_EURUSD_240 = false;
extern bool ARIMA_I_EURUSD_1440 = false;
extern bool ARIMA_I_EURUSD_10080 = false;
extern bool ARIMA_I_B_EURUSD_60 = false;
extern bool ARIMA_I_B_EURUSD_240 = false;
extern bool ARIMA_I_B_EURUSD_1440 = false;
extern bool ARIMA_I_B_EURUSD_10080 = false;
extern bool ARIMA_I_C_EURUSD_60 = false;
extern bool ARIMA_I_C_EURUSD_240 = false;
extern bool ARIMA_I_C_EURUSD_1440 = false;
extern bool ARIMA_I_C_EURUSD_10080 = false;

extern bool ARIMA_I_C_WTI_60 = false;
extern bool ARIMA_I_C_WTI_240 = false;
extern bool ARIMA_I_C_WTI_1440 = false;
