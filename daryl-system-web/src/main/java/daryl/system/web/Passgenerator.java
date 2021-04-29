package daryl.system.web;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class Passgenerator {

	public static void main(String[] args) {
		Pbkdf2PasswordEncoder bCryptPasswordEncoder = new Pbkdf2PasswordEncoder("darylsystemproject");
        //El String que mandamos al metodo encode es el password que queremos encriptar.
		System.out.println(bCryptPasswordEncoder.encode("al3jandr0"));

	}

}
