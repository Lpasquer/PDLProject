package pdl.wiki;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;


public class SavePathTest {
	
	WikipediaMatrix wm;
	WikipediaMatrixInterface wmi;
	String backupPath;
	
	/**
	 * sauvegarde le chemin dans backupPath
	 */
	@BeforeAll
	public void saveBackupPath() {
		backupPath = wm.readSaveLocation();
		
	}
	
	/**
	 * Test de readSaveLocation avec une WikipediaMatrix et avec le fichier texte "save_path.txt" vide
	 * @throws FileNotFoundException
	 */
	@Test
	public void saveEmptyPath() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println("");
    	printwriter.close();
    	Assertions.assertTrue(wm.readSaveLocation().equals(System.getProperty("user.dir") + "/output"));
	}
	
	/**
	 * Test de readSaveLocation avec une WikipediaMatrix et avec le fichier texte "save_path.txt" paramétré
	 * @throws FileNotFoundException
	 */
	@Test
	public void saveParameterPath() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println(System.getProperty("user.home"));
    	printwriter.close();
    	Assertions.assertTrue(wm.readSaveLocation().equals(System.getProperty("user.home")));
	}
	
	/**
	 * Test de readSaveLocation avec une WikipediaMatrix et avec le fichier texte "save_path.txt" erroné
	 * @throws FileNotFoundException
	 */
	@Test
	public void saveWrongPath() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println("wrong path");
    	printwriter.close();
    	Assertions.assertTrue(wm.readSaveLocation().equals("Le chemin de sauvegarde n'est pas valide"));
	}
	
	/**
	 * Test de readSaveLocation avec une WikipediaMatrixInterface et avec le fichier texte "save_path.txt" vide
	 * @throws FileNotFoundException
	 */
	@Test
	public void saveEmptyPathInterface() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println("");
    	printwriter.close();
    	Assertions.assertTrue(wmi.readSaveLocation().equals(System.getProperty("user.dir") + "/output"));
	}
	
	/**
	 * Test de readSaveLocation avec une WikipediaMatrixInterface et avec le fichier texte "save_path.txt" paramétré
	 * @throws FileNotFoundException
	 */
	@Test
	public void saveParameterPathInterface() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println(System.getProperty("user.home"));
    	printwriter.close();
    	Assertions.assertTrue(wmi.readSaveLocation().equals(System.getProperty("user.home")));
	}
	
	/**
	 * Test de readSaveLocation avec une WikipediaMatrixInterface et avec le fichier texte "save_path.txt" erroné
	 * @throws FileNotFoundException
	 */
	@Test
	public void saveWrongPathInterface() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println("wrong path");
    	printwriter.close();
    	Assertions.assertTrue(wmi.readSaveLocation().equals("Le chemin de sauvegarde n'est pas valide"));
	}
	
	/**
	 * Test que le readSavelocation() des deux classes rendre bien la même chose
	 * @throws FileNotFoundException
	 */
	@Test
	public void twoClassPath() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println(System.getProperty("user.dir"));
    	printwriter.close();
    	Assertions.assertTrue(wmi.readSaveLocation().equals(wm.readSaveLocation()));
    	Assertions.assertTrue(wm.readSaveLocation().equals(System.getProperty("user.dir")));
    	Assertions.assertTrue(wmi.readSaveLocation().equals(System.getProperty("user.dir")));	
	}

	/**
	 * réinitialise le chemin de sauvegarde
	 * @throws FileNotFoundException
	 */
	@AfterAll
	public void resetBackupPath() throws FileNotFoundException {
		PrintWriter printwriter = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/save_path.txt"));
		printwriter.println(backupPath);
    	printwriter.close();
	}
}