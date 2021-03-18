package daryl.system.control.contizaciones.zeromq.control;

import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@Component
public class PruebaListener extends Thread{
	private String activo;
	public PruebaListener() {
	}
	
	@Override
	public void run() {
		try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:5559");
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Esperando los datos de cotizacion ");
            	// Block until a message is received
                byte[] reply = socket.recv(0);
                // Print the message
                String prediccionRecibida = new String(reply, ZMQ.CHARSET);
                System.out.println("Datos recibidos -> " + prediccionRecibida);
                
                // Enviamos la respuesta al cliente python
                String response = "Datos recibidos, gracias";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);


                
            }
        }catch (Exception e) {
        	e.printStackTrace();
			System.out.println("No se recibe la predicción del cliente python ");
		}finally {
		}
	}
	/*
	private void sendPrediction(String prediccion) {
		try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect(ConfigData.ZEROMQ_SERVER_URL_FROM_JAVA);
            //while (!Thread.currentThread().isInterrupted()) {
                // Send a message
                socket.send(prediccion.getBytes(ZMQ.CHARSET), 0);
                System.out.println("Prección envviada para el activo: " + this.activo);
                
                //Esperando la respuesta del servidor
                // Block until a message is received
                byte[] reply = socket.recv(0);
                //Print the message
                System.out.println("Received: [" + new String(reply, ZMQ.CHARSET) + "]");
            //}
        }catch (Exception e) {
        	e.printStackTrace();
			System.out.println("No se ha enviado la predicción del activo: " + this.activo);
		}finally {
		}
	}
	*/
	public static void main(String[] args) {
		new PruebaListener().start();
	}

}
